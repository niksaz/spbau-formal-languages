package ru.spbau.mit

import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.ParserRuleContext
import ru.spbau.mit.parser.FunLexer
import ru.spbau.mit.parser.FunParser
import ru.spbau.mit.parser.FunParsingException

fun buildAstFor(sourceCodePath: String): ParserRuleContext {
    val funLexer = FunLexer(CharStreams.fromFileName(sourceCodePath))
    val tokens = CommonTokenStream(funLexer)
    val funParser = FunParser(tokens)
    val fileContext = funParser.file()
    if (funParser.numberOfSyntaxErrors > 0) {
        throw FunParsingException()
    }
    return fileContext
}

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Pass the path to a Fun source file.")
        return
    }
    try {
        buildAstFor(args[0])
    } catch (e: FunParsingException) {
        System.err.println("The code will not be interpreted since parsing errors were met.")
    }
}