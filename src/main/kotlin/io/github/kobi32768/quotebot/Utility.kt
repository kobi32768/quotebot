package io.github.kobi32768.quotebot

import net.dv8tion.jda.api.events.message.MessageReceivedEvent

fun List<String>.isContainOr(vararg substring: String): Boolean {
    return substring.any { this.contains(it) }
}

fun MessageReceivedEvent.isForce(): Boolean {
    return this.message.contentDisplay.split(' ').isContainOr("-f", "--force")
}

fun printlnf(text: String, vararg args: String) {
    println(text.format(args))
}

fun compress64(id: String): String {
    val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
    var id10 = id.toLong()
    return buildString {
        while (id10 / 64 != 0L) {
            append(chars[(id10 % 64).toInt()])
            id10 /= 64
        }
    }
}
