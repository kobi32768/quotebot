package io.github.kobi32768.quotebot

import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.GuildChannel
import net.dv8tion.jda.api.entities.GuildMessageChannel
import net.dv8tion.jda.api.entities.ThreadChannel
import net.dv8tion.jda.api.entities.channel.attribute.IAgeRestrictedChannel

fun Guild.getQuotableChannelById(id: Long): GuildMessageChannel? {
    return this.getTextChannelById(id)
        ?: this.getVoiceChannelById(id)
        ?: this.getThreadChannelById(id)
        ?: this.getNewsChannelById(id)
}

tailrec fun GuildChannel.isNSFW(): Boolean = when (this) {
    is IAgeRestrictedChannel -> this.isNSFW
    is ThreadChannel -> this.parentChannel.isNSFW()
    else -> false
}
