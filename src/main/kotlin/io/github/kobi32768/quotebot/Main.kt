package io.github.kobi32768.quotebot

import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException
import net.dv8tion.jda.api.hooks.ListenerAdapter
import javax.security.auth.login.LoginException
import java.lang.NullPointerException
import java.util.concurrent.ExecutionException

fun main() {
    QuoteBot().start()
}

class QuoteBot : ListenerAdapter() {
    fun start() {
        try {
            JDABuilder
                .createDefault(
                    this.javaClass.classLoader
                        .getResourceAsStream("token.txt")!!
                        .reader()
                        .readText())
                .addEventListeners(this)
                .build()
        } catch (ex: LoginException) {
            ex.printStackTrace()
        } catch (ex: InterruptedException) {
            ex.printStackTrace()
        }

        val log = Logger()
        log.makeLogFile()
        log.printlog("Quote Bot started", State.INFORMATION)
    }

    override fun onMessageReceived(event: MessageReceivedEvent) {
        if (event.author.isBot) return

        val log  = Logger()
        val util = Utility()
        val link = DiscordLink()
        val role = DiscordRole()
        val msg  = DiscordMessage()

        val prefix = "https://discord.com/channels/"
        val content = event.message.contentDisplay
            .toLowerCase()
            .replace("https://discordapp.com/channels/", prefix) // old to new

        // Command
        if (content.startsWith("!quote")) {
            val commands = content.split(' ')
            val version = this.javaClass.classLoader
                .getResourceAsStream("version.txt")!!
                .reader()
                .readText()

            if (util.isContainOr(commands, "-h", "--help")) {
                msg.sendMessage("help", event)
                log.printlog("Displayed help", State.INFORMATION)
            }

            if (util.isContainOr(commands, "-v", "--version")) {
                msg.sendMessage("**Version: ** $version", event)
                log.printlog("Displayed version", State.INFORMATION)
            }

            // todo: MSG表示後またはコマンド引数として入力できるようにする
            if (util.isContainOr(commands, "-l", "--language")) {
                msg.languageChanger(event)
                log.printlog("Displayed language changer", State.INFORMATION)
            }
        }

        // Quote
        if (!util.isContain(content, prefix)) return

        val ids = link.extractIDs(content)
        for (i in ids.indices step 3) {
            if (!link.areValidLinks(content)[i/3] && !util.isForce(event)) {
                log.printlog("Invalid Link", State.INVALID)
                continue
            }

            // Pre define
            val quotedGuild  : Guild
            val quotedChannel: TextChannel
            val quotedMessage: Message

            try {
                quotedGuild   = event.jda.getGuildById     (ids[i])!!
                quotedChannel = quotedGuild.getTextChannelById   (ids[i + 1])!!
                quotedMessage = quotedChannel.retrieveMessageById(ids[i + 2]).submit().get()
            }
            catch (ex: NullPointerException) {
                log.printlog("Null Pointer Exception", State.EXCEPTION)
                msg.sendErrorMessage(Error.NOT_EXIST, event); return
            }
            catch (ex: ExecutionException) {
                log.printlog("Execution Exception", State.EXCEPTION)
                msg.sendErrorMessage(Error.NOT_EXIST_MSG, event); return
            }
            catch (ex: InsufficientPermissionException) {
                log.printlog("Insufficient Permission Exception", State.EXCEPTION)
                msg.sendErrorMessage(Error.CANNOT_REF, event); return
            }

            val quotedData = MessageData(event, quotedGuild, quotedChannel, quotedMessage)

            role.isEveryoneViewable(quotedData)

            if (quotedChannel.isNSFW) {
                log.printlog("Quote from NSFW channel", State.FORBIDDEN)
                msg.sendErrorMessage(Error.NSFW, event)
            }
            else if (!role.isEveryoneViewable(quotedData)) {
                if (util.isForce(event)) {
                    msg.sendRegularEmbedMessage(quotedData)
                    log.printlog("Use Force", State.INFORMATION, quotedData)
                } else {
                    msg.sendErrorMessage(Error.FORBIDDEN, event)
                    log.printlog("Non-force", State.FORBIDDEN, quotedData)
                }
            }
            else if (!msg.isSameGuild(quotedData)) {
                if (util.isForce(event)) {
                    msg.sendRegularEmbedMessage(quotedData)
                    log.printlog("Use Force", State.INFORMATION, quotedData)
                } else {
                    msg.sendErrorMessage(Error.CROSS_GUILD, event)
                    log.printlog("Cross-Guild", State.FORBIDDEN, quotedData)
                }
            } else {
                msg.sendRegularEmbedMessage(quotedData)
            }
        }
    }
}
