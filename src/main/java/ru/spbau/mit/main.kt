package ru.spbau.mit

import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.ParserRuleContext
import ru.spbau.mit.parser.LLexer
import ru.spbau.mit.parser.LParser
import ru.spbau.mit.parser.LParsingException

fun buildAstFor(sourceCodePath: String): ParserRuleContext {
    val funLexer = LLexer(CharStreams.fromFileName(sourceCodePath))
    val tokens = CommonTokenStream(funLexer)
    val funParser = LParser(tokens)
    val fileContext = funParser.file()
    if (funParser.numberOfSyntaxErrors > 0) {
        throw LParsingException()
    }
    return fileContext
}

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Pass the path to an L source file.")
        return
    }
    try {
        buildAstFor(args[0])
    } catch (e: LParsingException) {
        System.err.println("The AST was not built since parsing errors were met.")
    }
}