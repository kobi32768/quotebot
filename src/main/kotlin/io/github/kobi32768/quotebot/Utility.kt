package io.github.kobi32768.quotebot

import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class Utility {
    fun isContain(text: String, substring: String): Boolean {
        return text.indexOf(substring) >= 0
    }

    fun isContainOr(text: String, vararg substring: String): Boolean {
        // 1度でもisContain()でtrueが出たらtrueを返す
        // foreachのようなもの
        return substring.indices.any { i: Int ->
            isContain(text, substring[i])
        }
    }

    fun isContainOr(list: List<String>, vararg substring: String): Boolean {
        return substring.indices.any { i: Int ->
            list.contains(substring[i])
        }
    }

    fun isForce(event: MessageReceivedEvent): Boolean {
        return isContainOr(
            event.message.contentDisplay.split(' '),
            "-f", "--force")
    }

    fun printlnf(text: String, vararg args: String) {
        println(text.format(args))
    }

    fun compress64(id: String): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
        var id10  = id.toLong()
        var id64  = ""

        while (id10 / 64 != 0L) {
            id64 = chars[(id10 % 64).toInt()] + id64
            id10 /= 64
        }

        return id64
    }

    fun beginWithLowerCase(text: String): String {
        return when (text.length) {
            0 -> ""
            1 -> text.toLowerCase()
            else -> text[0].toLowerCase() + text.substring(1)
        }
    }

    fun beginWithUpperCase(text: String): String {
        return when (text.length) {
            0 -> ""
            1 -> text.toUpperCase()
            else -> text[0].toUpperCase() + text.substring(1)
        }
    }

    fun toCamelCase(text: String): String {
        return text
            .split(' ')
            .map { beginWithUpperCase(text) }
            .joinToString("")
    }
}
