package io.github.kobi32768.quotebot

import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import javax.security.auth.login.LoginException
import java.io.File

fun main() {
    QuoteBot().start()
}

class QuoteBot : ListenerAdapter() {
    fun start() {
        try {
            JDABuilder
                .createDefault(
                    File("./src/main/resources/token.txt").readText())
                .addEventListeners(this)
                .build()
        }
        catch (ex: LoginException) {
            ex.printStackTrace()
        }
        catch (ex: InterruptedException) {
            ex.printStackTrace()
        }
    }

    override fun onMessageReceived(event: MessageReceivedEvent) {
        if (event.author.isBot) return

        val util = Utility()

        val resourcePath = "./src/main/resources"
        val prefix = "https://discord.com/channels/"
        val content = event.message.contentDisplay
            .toLowerCase()
            .replace("https://discordapp.com/channels/", prefix) // old to new

        if (content.startsWith("!quote")) {
            if (util.isContainOr(content, "-v", "--version")) {
                val version = File("$resourcePath/version.txt").readText()
                util.sendMessage(event, "**Version: ** $version")
            }
        }
    }
}
