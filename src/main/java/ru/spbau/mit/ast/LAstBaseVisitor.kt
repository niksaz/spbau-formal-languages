package ru.spbau.mit.ast

/** Base class for Visitors of [LAst]. */
interface LAstBaseVisitor<out T> {
    fun visit(node: LAst.Node): T = node.accept(this)

    fun visitFile(file: LAst.File): T

    fun visitProcedure(procedure: LAst.Procedure): T

    fun visitParameterNames(parameterNames: LAst.ParameterNames): T

    fun visitBlock(block: LAst.Block): T

    fun visitAssignment(assignment: LAst.Assignment): T

    fun visitReadCall(readCall: LAst.ReadCall): T

    fun visitWriteCall(writeCall: LAst.WriteCall): T

    fun visitProcedureCall(procedureCall: LAst.ProcedureCall): T

    fun visitArguments(arguments: LAst.Arguments): T

    fun visitWhileBlock(whileBlock: LAst.WhileBlock): T

    fun visitIfStatement(ifStatement: LAst.IfStatement): T

    fun visitBinaryExpression(binaryExpression: LAst.BinaryExpression): T

    fun visitBracedExpression(bracedExpression: LAst.BracedExpression): T

    fun visitIdentifier(identifier: LAst.Identifier): T

    fun visitNumber(number: LAst.Number): T

    fun visitOperator(operator: LAst.Operator): T
}