package io.github.kobi32768.quotebot

import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class Utility {
    fun isContain(text: String, substring: String): Boolean {
        return text.indexOf(substring) >= 0
    }

    fun isContainOr(text: String, vararg substring: String): Boolean {
        // 1度でもisContain()でtrueが出たらtrueを返す
        // foreachのようなもの
        return substring.indices.any {
            i: Int -> isContain(text, substring[i])
        }
    }

    fun sendMessage(event: MessageReceivedEvent, message: String) {
        event.channel.sendMessage(message).queue()
    }
}
