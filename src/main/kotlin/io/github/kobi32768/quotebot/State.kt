package io.github.kobi32768.quotebot

import com.github.ajalt.mordant.terminal.TextColors

enum class State(val color: TextColors) {
    SUCCESS(TextColors.green),
    FAILED(TextColors.yellow),
    FORBIDDEN(TextColors.yellow),
    EXCEPTION(TextColors.red),
    INVALID(TextColors.blue),
    INFORMATION(TextColors.blue),
    DEBUG(TextColors.magenta);

    val message = name.toLowerCase().capitalize()
}