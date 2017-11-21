package ru.spbau.mit.ast

import java.io.PrintStream

class LAstPrinter(private val printer: PrintStream = System.out) : LAstBaseVisitor<Unit> {
    private var indent = ""

    override fun visitFile(file: LAst.File) {
        printer.println("${indent}File ${file.sourceInterval}:")
        file.procedures.forEach { visit(it) }
        visit(file.block)
    }

    override fun visitProcedure(procedure: LAst.Procedure) {
        printer.println("${indent}Procedure ${procedure.sourceInterval}:")
        withIndentIncreased {
            visit(procedure.identifier)
            visit(procedure.paramNames)
            visit(procedure.body)
        }
    }

    override fun visitParameterNames(parameterNames: LAst.ParameterNames) {
        printer.println("${indent}ParameterNames ${parameterNames.sourceInterval}:")
        withIndentIncreased { parameterNames.params.forEach { visit(it) } }
    }

    override fun visitBlock(block: LAst.Block) {
        printer.println("${indent}Block ${block.sourceInterval}:")
        withIndentIncreased { block.statements.forEach { visit(it) } }
    }

    override fun visitAssignment(assignment: LAst.Assignment) {
        printer.println("${indent}Assignment ${assignment.sourceInterval}:")
        withIndentIncreased {
            visit(assignment.identifier)
            visit(assignment.expression)
        }
    }

    override fun visitWriteCall(writeCall: LAst.WriteCall) {
        printer.println("${indent}WriteCall ${writeCall.sourceInterval}:")
        withIndentIncreased { visit(writeCall.expression) }
    }

    override fun visitProcedureCall(procedureCall: LAst.ProcedureCall) {
        printer.println("${indent}ProcedureCall ${procedureCall.sourceInterval}:")
        withIndentIncreased {
            visit(procedureCall.identifier)
            visit(procedureCall.arguments)
        }
    }

    override fun visitArguments(arguments: LAst.Arguments) {
        printer.println("${indent}Arguments ${arguments.sourceInterval}:")
        withIndentIncreased { arguments.args.forEach { visit(it) } }
    }

    override fun visitWhileBlock(whileBlock: LAst.WhileBlock) {
        printer.println("${indent}WhileBlock ${whileBlock.sourceInterval}:")
        withIndentIncreased {
            visit(whileBlock.condition)
            visit(whileBlock.body)
        }
    }

    override fun visitIfStatement(ifStatement: LAst.IfStatement) {
        printer.println("${indent}IfStatement ${ifStatement.sourceInterval}:")
        withIndentIncreased {
            visit(ifStatement.condition)
            visit(ifStatement.body)
            if (ifStatement.elseBody != null) {
                visit(ifStatement.elseBody)
            }
        }
    }

    override fun visitBinaryExpression(binaryExpression: LAst.BinaryExpression) {
        printer.println("${indent}BinaryExpression ${binaryExpression.sourceInterval}:")
        withIndentIncreased {
            visit(binaryExpression.leftExpression)
            visit(binaryExpression.operator)
            visit(binaryExpression.rightExpression)
        }
    }

    override fun visitBracedExpression(bracedExpression: LAst.BracedExpression) {
        printer.println("${indent}BracedExpression ${bracedExpression.sourceInterval}:")
        withIndentIncreased { visit(bracedExpression.expression) }
    }

    override fun visitIdentifier(identifier: LAst.Identifier) {
        printer.println(
            "${indent}Identifier ${identifier.sourceInterval}: ${identifier.terminalNode.text}")
    }

    override fun visitNumber(number: LAst.Number) {
        printer.println("${indent}Number ${number.sourceInterval}: ${number.value}")
    }

    override fun visitOperator(operator: LAst.Operator) {
        printer.println("${indent}Operator ${operator.sourceInterval}: ${operator.symbol}")
    }

    private fun withIndentIncreased(inner: () -> Unit) {
        val oldIndent = indent
        indent += "  "
        inner()
        indent = oldIndent
    }
}