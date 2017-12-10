package ru.spbau.mit

import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import ru.spbau.mit.ast.AntlrLAstBuilder
import ru.spbau.mit.ast.LAst
import ru.spbau.mit.ast.LAstPrinter
import ru.spbau.mit.parser.LLexer
import ru.spbau.mit.parser.LParser
import ru.spbau.mit.parser.LParsingException
import java.io.PrintStream

fun buildAstFor(sourceCodePath: String): LAst {
    val funLexer = LLexer(CharStreams.fromFileName(sourceCodePath))
    val tokens = CommonTokenStream(funLexer)
    val funParser = LParser(tokens)
    val fileContext = funParser.file()
    if (funParser.numberOfSyntaxErrors > 0) {
        throw LParsingException("Antlr met error during parsing")
    }
    val astBuilder = AntlrLAstBuilder()
    return astBuilder.buildAstFromContext(fileContext)
}

fun parseAndPrintAst(sourceCodePath: String, printStream: PrintStream = System.out) {
    val ast = buildAstFor(sourceCodePath)
    val printer = LAstPrinter(printStream)
    printer.visit(ast.rootNode)
}

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Pass the path to an L source file.")
        return
    }
    try {
        parseAndPrintAst(args[0])
    } catch (e: LParsingException) {
        if (e.message != null) {
            System.err.println(e.message)
        }
        System.err.println("The AST was not built since parsing errors were met.")
    }
}