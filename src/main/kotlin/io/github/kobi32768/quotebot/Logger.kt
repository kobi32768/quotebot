package io.github.kobi32768.quotebot

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Logger {
    fun printlog(message: String, state: State): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm:ss.ss")
        val time      = LocalDateTime.now().format(formatter)

        // idea: httpステータスコード 表示させるのいいかも
        val stateMessage = state.message
        val color        = state.color

        println("[ $time ] [ ${color("%-11s")} ] $message".format(stateMessage))
        return  "[ $time ] [ %-11s ] $message"            .format(stateMessage) // ANSI disabled
    }

    fun printlogMsg(message: String, state: State, data: MessageData): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm:ss.ss")
        val time      = LocalDateTime.now().format(formatter)
        val util = Utility()
        val id64 = util.compress64(data.message.id)

        val stateMessage = state.message
        val color        = state.color

        println("[ $time ] [ ${color("%-11s")} ] %-9s - $message".format(stateMessage, id64))
        return  "[ $time ] [ %-11s ] %-9s - $message"            .format(stateMessage, id64) // ANSI disabled
    }

    fun writeLogFile() {

    }

    fun makeLogFile() {

    }
}
