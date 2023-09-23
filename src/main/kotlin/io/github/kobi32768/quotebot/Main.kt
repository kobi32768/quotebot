package io.github.kobi32768.quotebot

import kotlinx.coroutines.future.await
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.GuildMessageChannel
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.exceptions.ErrorResponseException
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.requests.GatewayIntent
import java.util.*
import java.util.concurrent.ExecutionException
import javax.security.auth.login.LoginException

fun main() {
    QuoteBot().start()
}

class QuoteBot : ListenerAdapter() {
    fun start() {
        try {
            val token = System.getenv("DISCORD_TOKEN")
            JDABuilder.createDefault(token).addEventListeners(this)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .build()
        } catch (ex: LoginException) {
            ex.printStackTrace()
        } catch (ex: InterruptedException) {
            ex.printStackTrace()
        }

        makeLogFile()
        printlog("Quote Bot started", State.INFORMATION)
    }

    override fun onMessageReceived(event: MessageReceivedEvent) {
        startCoroutine {
            try {
                onMessageReceivedSuspendable(event)
            } catch (t: Throwable) {
                printlog("unhandled exception: $t", State.EXCEPTION)
                t.printStackTrace()
            }
        }
    }

    suspend fun onMessageReceivedSuspendable(event: MessageReceivedEvent) {
        if (event.author.isBot) return

        val prefix = "https://discord.com/channels/"
        val content = event.message.contentDisplay.lowercase(Locale.getDefault())
            // Discord was at https://discordapp.com.
            // see https://support.discord.com/hc/articles/360042987951-Discordapp-com-is-now-Discord-com
            .replace("https://discordapp.com/channels/", prefix) // old to new
            // prerelease versions
            // see https://support.discord.com/hc/articles/360035675191-Discord-Testing-Clients
            .replace("https://ptb.discord.com/channels/", prefix)
            .replace("https://canary.discord.com/channels/", prefix)

        // Command
        if (content.startsWith("!quote")) {
            val commands = content.split(' ')

            if (commands.containsAny("-v", "--version")) {
                event.sendMessage("**Version: ** ${VersionHolder.version}")
                printlog("Displayed version (${VersionHolder.version})", State.INFORMATION)
            }
        }

        // Quote
        if (prefix !in content) return

        for ((guildId, channelId, messageId) in content.extractIDs(event.isForce())) {

            // Pre-define
            val quotedGuild: Guild
            val quotedChannel: GuildMessageChannel
            val quotedMessage: Message

            try {
                quotedGuild = event.jda.getGuildById(guildId)!!
                quotedChannel = quotedGuild.getQuotableChannelById(channelId])!!
                quotedMessage = quotedChannel.retrieveMessageById(messageId).submit().await()
            } catch (ex: NullPointerException) {
                printlog("Null Pointer Exception", State.EXCEPTION)
                event.sendErrorMessage(Error.NOT_EXIST); return
            } catch (ex: ErrorResponseException) {
                // see https://discord.com/developers/docs/topics/opcodes-and-status-codes#json-json-error-codes
                if (ex.errorCode == 10008) {
                    printlog("Message Not Exists error response", State.EXCEPTION)
                    event.sendErrorMessage(Error.NOT_EXIST_MSG); return
                } else {
                    printlog("Unexpected Error Response: $ex", State.EXCEPTION)
                    event.sendErrorMessage(Error.UNEXPECTED_ERROR); return
                }
            } catch (ex: InsufficientPermissionException) {
                printlog("Insufficient Permission Exception", State.EXCEPTION)
                event.sendErrorMessage(Error.CANNOT_REF); return
            }

            val quotedData = MessageData(event, quotedGuild, quotedChannel, quotedMessage)

            if (event.isForce()) {
                quotedData.callForceQuote()
            } else {
                if (quotedData.isSameChannel()) {
                    sendRegularEmbedMessage(quotedData)
                    printlog("Successfully referenced", State.SUCCESS, false, quotedData)
                } else if (quotedChannel.isNSFW()) {
                    printlog("Quote from NSFW channel", State.FORBIDDEN)
                    event.sendErrorMessage(Error.NSFW)
                } else if (!quotedData.isEveryoneViewable()) {
                    event.sendErrorMessage(Error.FORBIDDEN)
                    printlog("@everyone doesn't have permission", State.FORBIDDEN, false, quotedData)
                } else if (quotedData.isSameGuild()) {
                    sendRegularEmbedMessage(quotedData)
                    printlog("Successfully referenced", State.SUCCESS, false, quotedData)
                } else {
                    event.sendErrorMessage(Error.CROSS_GUILD)
                    printlog("Cross-Guild", State.FORBIDDEN, false, quotedData)
                }
            }
        }
    }

    /**
     * The class holds version of this bot. use an individual object for lazy init.
     */
    private object VersionHolder {
        val version = this.javaClass.classLoader.getResourceAsStream("version.txt")!!
            .reader()
            .readText()
            .trim()
    }
}
