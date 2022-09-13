package io.github.kobi32768.quotebot

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.ChannelType
import net.dv8tion.jda.api.entities.ICategorizableChannel
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.ThreadChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import java.awt.Color
import java.util.concurrent.ExecutionException

fun MessageReceivedEvent.sendMessage(message: String) {
    this.channel.sendMessage(message).queue()
}

fun MessageReceivedEvent.sendErrorMessage(error: Error) {
    val text = "**${error.title}**\n" + error.description
    this.sendMessage(text)
}

fun MessageData.isSameGuild(): Boolean {
    return this.event.channelType != ChannelType.PRIVATE && this.guild == this.event.guild
}

fun MessageData.isSameChannel(): Boolean {
    return this.channel.type == this.event.channelType && this.channel == this.event.channel
}

fun MessageData.callForceQuote() {
    val guild = this.guild
    val user = this.event.author

    guild.findMembers { it.user == user }
        .onSuccess { members -> forceQuote(this, members) }
}

fun forceQuote(data: MessageData, foundMembers: List<Member>) {
    val member = foundMembers.singleOrNull()
    val channel = data.channel

    if (member != null) {
        if (member.hasPermission(channel, Permission.MANAGE_CHANNEL) ||
            member.hasPermission(channel, Permission.MESSAGE_MANAGE) ||
            member.hasPermission(Permission.ADMINISTRATOR)
        ) {
            sendRegularEmbedMessage(data)
            printlog("Successfully referenced", State.SUCCESS, true, data)
            return
        }
    }

    data.event.sendErrorMessage(Error.FORCE_FAILED)
    printlog("Need more permissions to force quoting.", State.FAILED, false, data)
}

fun createEmbedTitle(data: MessageData): String {
    val guild = data.guild
    val thread = if (data.channel is ThreadChannel) {
        data.channel
    } else {
        null
    }
    val channel = (thread?.parentChannel ?: data.channel) as ICategorizableChannel
    val category = channel.parentCategory

    var title = "from: "
    if (category != null) title += "${category.name} / "
    title += channel.name
    if (thread != null) title += " / ${thread.name}"
    title += " (${guild.name})"

    return title
}

fun sendRegularEmbedMessage(data: MessageData) {
    val message = data.message
    val event = data.event
    val member = try {
        data.guild.retrieveMember(data.message.author).submit().get()
    } catch (ex: ExecutionException) { // Member has already left
        null
    }

    val embed = EmbedBuilder()
        .setTitle(createEmbedTitle(data))
        .setDescription(message.contentDisplay)
        .setTimestamp(message.timeCreated)
        .setColor(Color(238, 150, 181))

    embed.setAuthor(
        member?.effectiveName ?: message.author.name,
        null,
        message.author.effectiveAvatarUrl
    )

    event.channel.sendMessageEmbeds(embed.build()).queue()
}
