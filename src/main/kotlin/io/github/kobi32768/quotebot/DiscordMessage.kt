package io.github.kobi32768.quotebot

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.ChannelType
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import java.awt.Color

fun MessageReceivedEvent.sendMessage(message: String) {
    this.channel.sendMessage(message).queue()
}

fun MessageReceivedEvent.sendErrorMessage(error: Error) {
    val text = "**${error.title}**\n" +
            error.description
    this.sendMessage(text)
}

fun MessageData.isSameGuild(): Boolean {
    return this.guild == this.event.guild
}

fun MessageData.isSameChannel(): Boolean {
    return this.channel == this.event.channel
}

fun MessageData.isForceQuotable(): Boolean {
    val channel = this.channel
    val member = this.event.member!!

    return member.hasPermission(channel, Permission.MANAGE_CHANNEL) || member.hasPermission(channel, Permission.MESSAGE_MANAGE) || member.hasPermission(channel, Permission.ADMINISTRATOR)
}

fun sendRegularEmbedMessage(data: MessageData) {
    val guild = data.guild
    val channel = data.channel
    val message = data.message
    val event = data.event

    val embed = EmbedBuilder()
        .setTitle("from: ${channel.name} (${guild.name})")
        .setDescription(message.contentDisplay)
        .setTimestamp(message.timeCreated)
        .setColor(Color(238, 150, 181))

    // If get message from IDs, API don't get member
    if (data.isSameGuild() && message.isFromType(ChannelType.TEXT)) {
        embed.setAuthor(
            message.getEffectiveName(),
            null,
            message.author.effectiveAvatarUrl)
    }

    event.channel.sendMessageEmbeds(embed.build())
        .queue()
}

private fun Message.getEffectiveName(): String {
    return if (this.member == null)
        this.author.name
    else
        this.member!!.effectiveName
}
