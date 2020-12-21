package io.github.kobi32768.quotebot

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import java.awt.Color

class DiscordMessage {
    fun sendMessage(message: String, event: MessageReceivedEvent) {
        event.channel.sendMessage(message).queue()
    }

    fun sendErrorMessage(error: Error,
                         event: MessageReceivedEvent) {
        val text ="**${error.title}**\n" +
                error.description
        sendMessage(text, event)
    }

    fun isSameGuild(data: MessageData): Boolean {
        return data.guild == data.event.guild
    }

    fun sendRegularEmbedMessage(data: MessageData) {
        val guild = data.guild
        val channel = data.channel
        val message = data.message
        val event = data.event

        val log = Logger()

        val embed = EmbedBuilder()
            .setTitle("from: ${channel.name} / ${guild.name}")
            .setDescription(message.contentDisplay)
            .setTimestamp(message.timeCreated)
            .setColor(Color(238, 150, 181))

        // When quote from other guild:
        // * Can't get Member
        if (isSameGuild(data)) {
            embed.setAuthor(
                message.member!!.effectiveName, null,
                message.author.effectiveAvatarUrl)
        }

        event.channel.sendMessage(embed.build()).queue()
        log.printlog("Successfully referenced", State.SUCCESS, data)
    }

    fun languageChanger(event: MessageReceivedEvent) {
        sendMessage("Select number\n```" +
                "1. ja_JP: 日本語\n" +
                "2. en_US: English```", event)
    }
}