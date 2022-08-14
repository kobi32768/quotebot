package io.github.kobi32768.quotebot

private fun String.extractLinks(): List<String> {
    return Regex("""https://discord\.com/channels/\d{6,19}/\d{6,19}/\d{6,19}""").findAll(this).map { it.value }.toList()
}

fun String.extractIDs(): List<String> {
    return this.extractLinks().joinToString(separator = "").replace("https://discord.com/channels", "").split("/")
        .drop(1)
}

fun String.areValidLinks(): List<Boolean> {
    val list = mutableListOf<Boolean>()
    val links = this.extractLinks()

    for (i in links.indices) {
        // 85: length of Discord Link
        val index = this.indexOf(links[i], i * 85)
        when (index) {
            0 -> list.add(true) // prev char doesn't exist
            else -> list.add(this[index - 1] != '\\')
        }
    }
    return list
}
