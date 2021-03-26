package io.github.kobi32768.quotebot

import net.dv8tion.jda.api.events.message.MessageReceivedEvent

fun String.isContain(substring: String): Boolean {
    return this.indexOf(substring) >= 0
}

fun String.isContainOr(vararg substring: String): Boolean {
    // foreach もどき
    return substring.indices.any { i: Int ->
        this.isContain(substring[i])
    }
}

fun List<String>.isContainOr(vararg substring: String): Boolean {
    return substring.indices.any { i: Int ->
        this.contains(substring[i])
    }
}

fun MessageReceivedEvent.isForce(): Boolean {
    return this.message.contentDisplay
        .split(' ')
        .isContainOr("-f", "--force")
}

fun printlnf(text: String, vararg args: String) {
    println(text.format(args))
}

fun compress64(id: String): String {
    val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
    var id10 = id.toLong()
    var id64 = ""

    while (id10 / 64 != 0L) {
        id64 = chars[(id10 % 64).toInt()] + id64
        id10 /= 64
    }

    return id64
}
