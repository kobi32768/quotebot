package io.github.kobi32768.quotebot

import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.GuildMessageChannel
import net.dv8tion.jda.api.entities.NewsChannel
import net.dv8tion.jda.api.entities.StandardGuildMessageChannel
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.entities.ThreadChannel
import net.dv8tion.jda.api.entities.VoiceChannel
import net.dv8tion.jda.api.entities.channel.attribute.IAgeRestrictedChannel

fun Guild.getQuotableChannelById(id: String): GuildMessageChannel? {
    return this.getTextChannelById(id)
        ?: this.getVoiceChannelById(id)
        ?: this.getThreadChannelById(id)
        ?: this.getNewsChannelById(id)
}

fun GuildMessageChannel.isNSFW(): Boolean {
    return when (this) {
        is TextChannel, is VoiceChannel, is NewsChannel -> {
            (this as IAgeRestrictedChannel).isNSFW
        }
        is ThreadChannel -> {
            (this.parentChannel as StandardGuildMessageChannel).isNSFW
        }
        else -> {
            false
        }
    }
}