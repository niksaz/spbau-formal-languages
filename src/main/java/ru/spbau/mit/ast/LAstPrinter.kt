package ru.spbau.mit.ast

import java.io.PrintStream

class LAstPrinter(val printer: PrintStream = System.out) : LAstBaseVisitor<Unit> {
    private var indent = ""

    override fun visitFile(file: LAst.File) {
        printer.println("${indent}File:")
        file.procedures.forEach { visit(it) }
        visit(file.block)
    }

    override fun visitProcedure(procedure: LAst.Procedure) {
        printer.println("${indent}Procedure:")
        withIndentIncreased {
            visit(procedure.identifier)
            visit(procedure.paramNames)
            visit(procedure.body)
        }
    }

    override fun visitParameterNames(parameterNames: LAst.ParameterNames) {
        printer.println("${indent}ParameterNames:")
        withIndentIncreased { parameterNames.params.forEach { visit(it) } }
    }

    override fun visitBlock(block: LAst.Block) {
        printer.println("${indent}Block:")
        withIndentIncreased { block.statements.forEach { visit(it) } }
    }

    override fun visitAssignment(assignment: LAst.Assignment) {
        printer.println("${indent}Assignment:")
        withIndentIncreased {
            visit(assignment.identifier)
            visit(assignment.expression)
        }
    }

    override fun visitWriteCall(writeCall: LAst.WriteCall) {
        printer.println("${indent}WriteCall:")
        withIndentIncreased { visit(writeCall.expression) }
    }

    override fun visitProcedureCall(procedureCall: LAst.ProcedureCall) {
        printer.println("${indent}ProcedureCall:")
        withIndentIncreased {
            visit(procedureCall.identifier)
            visit(procedureCall.arguments)
        }
    }

    override fun visitArguments(arguments: LAst.Arguments) {
        printer.println("${indent}Arguments:")
        withIndentIncreased { arguments.args.forEach { visit(it) } }
    }

    override fun visitWhileBlock(whileBlock: LAst.WhileBlock) {
        printer.println("${indent}WhileBlock:")
        withIndentIncreased {
            visit(whileBlock.condition)
            visit(whileBlock.body)
        }
    }

    override fun visitIfStatement(ifStatement: LAst.IfStatement) {
        printer.println("${indent}IfStatement:")
        withIndentIncreased {
            visit(ifStatement.condition)
            visit(ifStatement.body)
            if (ifStatement.elseBody != null) {
                visit(ifStatement.elseBody)
            }
        }
    }

    override fun visitBinaryExpression(binaryExpression: LAst.BinaryExpression) {
        printer.println("${indent}BinaryExpression:")
        withIndentIncreased {
            visit(binaryExpression.leftExpression)
            visit(binaryExpression.operator)
            visit(binaryExpression.rightExpression)
        }
    }

    override fun visitIdentifier(identifier: LAst.Identifier) {
        printer.println("${indent}Identifier ${identifier.name}")
    }

    override fun visitNumber(number: LAst.Number) {
        printer.println("${indent}Number ${number.value}")
    }

    override fun visitOperator(operator: LAst.Operator) {
        printer.println("${indent}Operator ${operator.symbol}")
    }

    private fun withIndentIncreased(inner: () -> Unit) {
        val oldIndent = indent
        indent += "  "
        inner()
        indent = oldIndent
    }
}