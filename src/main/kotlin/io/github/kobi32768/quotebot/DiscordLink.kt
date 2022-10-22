package io.github.kobi32768.quotebot

private val linkRegexWithValidCheck = Regex("""(?<!<)https://discord\.com/channels/\d{6,19}/\d{6,19}/\d{6,19}(?!>)""")
private val linkRegexWithoutValidCheck = Regex("""https://discord\.com/channels/\d{6,19}/\d{6,19}/\d{6,19}""")

private fun String.extractLinks(force: Boolean): Sequence<String> {
    return (if (force) linkRegexWithoutValidCheck else linkRegexWithValidCheck)
        .findAll(this)
        .map { it.value }
}

fun String.extractIDs(force: Boolean): Sequence<MessageLink> = this.extractLinks(force)
    .map { link ->
        val (guildId, channelId, messageId) = link.removePrefix("https://discord.com/channels/").split('/')
        MessageLink(guildId.toLong(), channelId.toLong(), messageId.toLong())
    }

data class MessageLink(
    val guildId: Long,
    val channelId: Long,
    val messageId: Long,
)
