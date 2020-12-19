package io.github.kobi32768.quotebot

class DiscordLink {
    private fun extractLinks(text: String): List<String> {
        return Regex("""https://discord.com/channels/\d{18}/\d{18}/\d{18}""")
            .findAll(text)
            .map { it.value }
            .toList()
    }

    fun extractIDs(text: String): List<String> {
        return extractLinks(text)
            .joinToString(separator = "")
            .replace("https://discord.com/channels", "")
            .split("/")
            .drop(1)
    }

    fun areValidLinks(text: String): List<Boolean> {
        val list  = mutableListOf<Boolean>()
        val links = extractLinks(text)

        for (i in links.indices) {
            // 85: length of Discord Link
            val index = text.indexOf(links[i], i * 85)
            when (index) {
                0    -> list.add(true) // prev char doesn't exist
                else -> list.add(text[index-1] != '\\')
            }
        }
        return list
    }
}
