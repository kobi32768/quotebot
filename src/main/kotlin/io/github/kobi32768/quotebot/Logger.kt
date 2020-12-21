package io.github.kobi32768.quotebot

import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder

class Logger {
//    fun printlog(message: String, state: State): String {
//        val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm:ss.ss")
//        val time      = LocalDateTime.now().format(formatter)
//
//        // idea: httpステータスコード 表示させるのいいかも
//        val stateMessage = state.message
//        val color        = state.color
//
//        println("[ $time ] [ ${color("%-11s")} ] $message".format(stateMessage))
//        return  "[ $time ] [ %-11s ] $message".format(stateMessage) // ANSI disabled
//    }

    fun printlog(message: String, state: State, data: MessageData? = null): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm:ss.ss")
        val time      = LocalDateTime.now().format(formatter)

        val stateMessage = state.message
        val color        = state.color

        val rawText: String
        val colored: String

        if (data == null) {
            rawText = "[ $time ] [ %-11s ] $message".format(stateMessage)
            colored = rawText.replace(stateMessage, color(stateMessage))
            println(colored)
        } else {
            val id64 = Utility().compress64(data.message.id)

            rawText = "[ $time ] [ %-11s ] %-9s - $message".format(stateMessage, id64)
            colored = rawText.replace(stateMessage, color(stateMessage))
            println(colored)
        }

        writeLogFile(rawText)
        return rawText
    }

    fun writeLogFile(text: String) {
        val filename = File("./log/latest.txt").readText()

        File("./log/$filename.txt")
            .appendText("$text\n")
    }

    fun makeLogFile() {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_hh-mm-ss")
        val time      = LocalDateTime.now().format(formatter)

        println(File("./").absolutePath)
        File("./log/$time.txt")
            .createNewFile()

        // overwrite
        File("./log/latest.txt")
            .writeText(time)
    }
}
