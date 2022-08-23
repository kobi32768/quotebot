package io.github.kobi32768.quotebot

private val linkRegexWithValidCheck = Regex("""(?<!\\)https://discord\.com/channels/\d{6,19}/\d{6,19}/\d{6,19}""")
private val linkRegexWithoutValidCheck = Regex("""https://discord\.com/channels/\d{6,19}/\d{6,19}/\d{6,19}""")

private fun String.extractLinks(force: Boolean): List<String> {
    return (if (force) linkRegexWithoutValidCheck else linkRegexWithValidCheck)
        .findAll(this)
        .map { it.value }
        .toList()
}

fun String.extractIDs(force: Boolean): List<String> {
    return this.extractLinks(force)
        .joinToString(separator = "")
        .replace("https://discord.com/channels", "")
        .split("/")
        .drop(1)
}
