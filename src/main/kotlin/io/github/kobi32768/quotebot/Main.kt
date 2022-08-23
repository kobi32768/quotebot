package io.github.kobi32768.quotebot

import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.requests.GatewayIntent
import java.util.*
import javax.security.auth.login.LoginException
import java.lang.NullPointerException
import java.util.concurrent.ExecutionException

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
        if (event.author.isBot) return

        val prefix = "https://discord.com/channels/"
        val content = event.message.contentDisplay.lowercase(Locale.getDefault())
            .replace("https://discordapp.com/channels/", prefix) // old to new
            .replace("https://ptb.discord.com/channels/", prefix) // ptb to general

        // Command
        if (content.startsWith("!quote")) {
            val commands = content.split(' ')
            val version = this.javaClass.classLoader.getResourceAsStream("version.txt")!!
                .reader()
                .readText()
                .trim()

            if (commands.isContainOr("-v", "--version")) {
                event.sendMessage("**Version: ** $version")
                printlog("Displayed version ($version)", State.INFORMATION)
            }
        }

        // Quote
        if (!content.isContain(prefix)) return

        val ids = content.extractIDs()
        for (i in ids.indices step 3) {
            if (!content.areValidLinks()[i / 3] && !event.isForce()) {
                printlog("Invalid Link", State.INVALID)
                continue
            }

            // Pre-define
            val quotedGuild: Guild
            val quotedChannel: TextChannel
            val quotedMessage: Message

            try {
                quotedGuild = event.jda.getGuildById(ids[i])!!
                quotedChannel = quotedGuild.getTextChannelById(ids[i + 1])!!
                quotedMessage = quotedChannel.retrieveMessageById(ids[i + 2]).submit().get()
            } catch (ex: NullPointerException) {
                printlog("Null Pointer Exception", State.EXCEPTION)
                event.sendErrorMessage(Error.NOT_EXIST); return
            } catch (ex: ExecutionException) {
                printlog("Execution Exception", State.EXCEPTION)
                event.sendErrorMessage(Error.NOT_EXIST_MSG); return
            } catch (ex: InsufficientPermissionException) {
                printlog("Insufficient Permission Exception", State.EXCEPTION)
                event.sendErrorMessage(Error.CANNOT_REF); return
            }

            val quotedData = MessageData(event, quotedGuild, quotedChannel, quotedMessage)

            if (quotedData.isSameChannel()) {
                sendRegularEmbedMessage(quotedData)
                printlog("Successfully referenced", State.SUCCESS, false, quotedData)
            } else if (quotedChannel.isNSFW) {
                if (event.isForce()) {
                    quotedData.callForceQuote()
                } else {
                    printlog("Quote from NSFW channel", State.FORBIDDEN)
                    event.sendErrorMessage(Error.NSFW)
                }
            } else if (!quotedData.isEveryoneViewable()) {
                if (event.isForce()) {
                    quotedData.callForceQuote()
                } else {
                    event.sendErrorMessage(Error.FORBIDDEN)
                    printlog("@everyone doesn't have permission", State.FORBIDDEN, false, quotedData)
                }
            } else if (quotedData.isSameGuild()) {
                sendRegularEmbedMessage(quotedData)
                printlog("Successfully referenced", State.SUCCESS, false, quotedData)
            } else {
                if (event.isForce()) {
                    quotedData.callForceQuote()
                } else {
                    event.sendErrorMessage(Error.CROSS_GUILD)
                    printlog("Cross-Guild", State.FORBIDDEN, false, quotedData)
                }
            }
        }
    }
}
