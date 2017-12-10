package ru.spbau.mit.parser

/** Exception is thrown if some error is met during parsing the source file. */
class LParsingException(message: String?) : RuntimeException(message)