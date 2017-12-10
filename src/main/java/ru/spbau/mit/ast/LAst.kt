package ru.spbau.mit.ast

import org.antlr.v4.runtime.misc.Interval
import org.antlr.v4.runtime.tree.TerminalNode

data class LAst(val rootNode: Node) {
    interface Node {
        fun <T> accept(visitor: LAstBaseVisitor<T>): T
    }

    abstract class NodeImpl(open val sourceInterval: Interval) : Node

    data class File(
        val procedures: List<Procedure>,
        val block: Block,
        override val sourceInterval: Interval
    ) : NodeImpl(sourceInterval) {
        override fun <T> accept(visitor: LAstBaseVisitor<T>): T =
            visitor.visitFile(this)
    }

    data class Procedure(
        val identifier: Identifier,
        val paramNames: ParameterNames,
        val body: Block,
        override val sourceInterval: Interval
    ) : NodeImpl(sourceInterval) {
        override fun <T> accept(visitor: LAstBaseVisitor<T>): T =
            visitor.visitProcedure(this)
    }

    data class ParameterNames(
        val params: List<Identifier>,
        override val sourceInterval: Interval
    ) : NodeImpl(sourceInterval) {
        override fun <T> accept(visitor: LAstBaseVisitor<T>): T =
            visitor.visitParameterNames(this)
    }

    data class Block(
        val statements: List<Statement>,
        override val sourceInterval: Interval
    ) : NodeImpl(sourceInterval) {
        override fun <T> accept(visitor: LAstBaseVisitor<T>): T =
            visitor.visitBlock(this)
    }

    interface Statement : Node

    abstract class StatementImpl(
        override val sourceInterval: Interval
    ) : NodeImpl(sourceInterval), Statement

    data class Assignment(
        val identifier: Identifier, val expression: Expression,
        override val sourceInterval: Interval
    ) : StatementImpl(sourceInterval) {
        override fun <T> accept(visitor: LAstBaseVisitor<T>): T =
            visitor.visitAssignment(this)
    }

    data class ReadCall(
        val argument: Identifier,
        override val sourceInterval: Interval
    ) : StatementImpl(sourceInterval) {
        override fun <T> accept(visitor: LAstBaseVisitor<T>): T =
            visitor.visitReadCall(this)
    }

    data class WriteCall(
        val expression: Expression,
        override val sourceInterval: Interval
    ) : StatementImpl(sourceInterval) {
        override fun <T> accept(visitor: LAstBaseVisitor<T>): T =
            visitor.visitWriteCall(this)
    }

    data class ProcedureCall(
        val identifier: Identifier,
        val arguments: Arguments,
        override val sourceInterval: Interval
    ) : StatementImpl(sourceInterval) {
        override fun <T> accept(visitor: LAstBaseVisitor<T>): T =
            visitor.visitProcedureCall(this)
    }

    data class Arguments(
        val args: List<Identifier>,
        override val sourceInterval: Interval
    ) : StatementImpl(sourceInterval) {
        override fun <T> accept(visitor: LAstBaseVisitor<T>): T =
            visitor.visitArguments(this)
    }

    data class WhileBlock(
        val condition: Expression,
        val body: Block,
        override val sourceInterval: Interval
    ) : StatementImpl(sourceInterval) {
        override fun <T> accept(visitor: LAstBaseVisitor<T>): T =
            visitor.visitWhileBlock(this)
    }

    data class IfStatement(
        val condition: Expression,
        val body: Block,
        val elseBody: Block?,
        override val sourceInterval: Interval
    ) : StatementImpl(sourceInterval) {
        override fun <T> accept(visitor: LAstBaseVisitor<T>): T =
            visitor.visitIfStatement(this)
    }

    interface Expression : Node

    abstract class ExpressionImpl(
        override val sourceInterval: Interval
    ) : NodeImpl(sourceInterval), Expression

    data class BinaryExpression(
        val leftExpression: Expression,
        val operator: Operator,
        val rightExpression: Expression,
        override val sourceInterval: Interval
    ) : ExpressionImpl(sourceInterval) {
        override fun <T> accept(visitor: LAstBaseVisitor<T>): T =
            visitor.visitBinaryExpression(this)
    }

    data class BracedExpression(
        val expression: Expression,
        override val sourceInterval: Interval
    ) : ExpressionImpl(sourceInterval) {
        override fun <T> accept(visitor: LAstBaseVisitor<T>): T =
            visitor.visitBracedExpression(this)
    }

    data class Identifier(
        val name: String,
        override val sourceInterval: Interval
    ) : ExpressionImpl(sourceInterval) {
        override fun <T> accept(visitor: LAstBaseVisitor<T>): T =
            visitor.visitIdentifier(this)
    }

    data class Number(
        val value: Int,
        override val sourceInterval: Interval
    ) : ExpressionImpl(sourceInterval) {
        override fun <T> accept(visitor: LAstBaseVisitor<T>): T =
            visitor.visitNumber(this)
    }

    data class Operator(
        val symbol: OperatorSymbol,
        override val sourceInterval: Interval
    ) : NodeImpl(sourceInterval) {
        override fun <T> accept(visitor: LAstBaseVisitor<T>): T =
            visitor.visitOperator(this)

        companion object {
            fun getFor(symbol: String, sourceInterval: Interval): Operator =
                Operator(
                    OperatorSymbol.values().firstOrNull { it.symbol == symbol }!!,
                    sourceInterval)
        }

        enum class OperatorSymbol(val symbol: String)  {
            MULTIPLY("*"),
            DIVIDE("/"),
            MODULUS("%"),
            PLUS("+"),
            MINUS("-"),
            GT(">"),
            LT("<"),
            GTE(">="),
            LTE("<="),
            EQ("=="),
            NQ("!="),
            LOR("||"),
            LAND("&&")
        }
    }
}