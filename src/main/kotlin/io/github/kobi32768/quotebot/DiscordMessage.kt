package io.github.kobi32768.quotebot

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.ChannelType
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
        
        val embed = EmbedBuilder()
            .setTitle("from: ${channel.name} / ${guild.name}")
            .setDescription(message.contentDisplay)
            .setTimestamp(message.timeCreated)
            .setColor(Color(238, 150, 181))

        // If get message from IDs, API don't get member
        if (isSameGuild(data) && message.isFromType(ChannelType.TEXT)) {
            embed.setAuthor(
                getName(data),
                null,
                message.author.effectiveAvatarUrl)
        }

        event.channel
            .sendMessage(embed.build())
            .queue()
    }

    private fun getName(data: MessageData): String {
        val message = data.message
        if (message.member == null)
            return message.author.name
        else
            return message.member!!.effectiveName
    }
}