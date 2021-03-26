package io.github.kobi32768.quotebot

import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Logger {
    fun printlog(message: String, state: State, useForce: Boolean = false, data: MessageData? = null): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm:ss.ss")
        val time      = LocalDateTime.now().format(formatter)

        val stateMessage = state.message
        val color        = state.color

        var rawText: String
        if (data == null) {
            rawText = "[ $time ] [ %-11s ] $message".format(stateMessage)
        } else {
            val id64 = Utility().compress64(data.message.id)
            rawText = "[ $time ] [ %-11s ] %-9s - $message".format(stateMessage, id64)
        }

        if (useForce) {
            rawText += " (Forced)"
        }

        val colored = rawText.replaceFirst(stateMessage, color(stateMessage))

        println(colored)
        writeLogFile(rawText)

        return rawText
    }

    fun printlog(error: Error, state: State, useForce: Boolean = false, data: MessageData? = null): String {
        return printlog(error.description, state, useForce, data)
    }

    private fun writeLogFile(text: String) {
        val filename = File("log/latest.txt").readText()

        File("log/$filename.txt")
            .appendText("$text\n")
    }

    fun makeLogFile() {
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss")
        val time      = LocalDateTime.now().format(formatter)

        printlog("Log file created: $time", State.INFORMATION)
        File("log/$time.txt")
            .createNewFile()

        // overwrite
        File("log/latest.txt")
            .writeText(time)
    }
}
