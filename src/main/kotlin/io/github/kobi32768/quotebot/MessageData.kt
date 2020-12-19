package io.github.kobi32768.quotebot

import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class MessageData(
    var event: MessageReceivedEvent,
    val guild: Guild,
    val channel: TextChannel,
    val message: Message
)
