package ru.spbau.mit.parser

/** Exception is thrown if Antlr could not parse the source file. */
class LParsingException(message: String?) : RuntimeException(message)