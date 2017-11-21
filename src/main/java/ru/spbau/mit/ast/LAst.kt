package ru.spbau.mit.ast

data class LAst(val rootNode: Node) {
    interface Node {
        fun <T> accept(visitor: LAstBaseVisitor<T>): T
    }

    data class File(val procedures: List<Procedure>, val block: Block) : Node {
        override fun <T> accept(visitor: LAstBaseVisitor<T>): T =
            visitor.visitFile(this)
    }

    data class Procedure(
        val identifier: Identifier,
        val paramNames: ParameterNames,
        val body: Block
    ) : Node {
        override fun <T> accept(visitor: LAstBaseVisitor<T>): T =
            visitor.visitProcedure(this)
    }

    data class ParameterNames(val params: List<Identifier>) : Node {
        override fun <T> accept(visitor: LAstBaseVisitor<T>): T =
            visitor.visitParameterNames(this)
    }

    data class Block(val statements: List<Statement>) : Node {
        override fun <T> accept(visitor: LAstBaseVisitor<T>): T =
            visitor.visitBlock(this)
    }

    interface Statement : Node

    data class Assignment(val identifier: Identifier, val expression: Expression) : Statement {
        override fun <T> accept(visitor: LAstBaseVisitor<T>): T =
            visitor.visitAssignment(this)
    }

    data class WriteCall(val expression: Expression) : Statement {
        override fun <T> accept(visitor: LAstBaseVisitor<T>): T =
            visitor.visitWriteCall(this)
    }

    data class ProcedureCall(val identifier: Identifier, val arguments: Arguments) : Statement {
        override fun <T> accept(visitor: LAstBaseVisitor<T>): T =
            visitor.visitProcedureCall(this)
    }

    data class Arguments(val args: List<Identifier>) : Node {
        override fun <T> accept(visitor: LAstBaseVisitor<T>): T =
            visitor.visitArguments(this)
    }

    data class WhileBlock(val condition: Expression, val body: Block) : Statement {
        override fun <T> accept(visitor: LAstBaseVisitor<T>): T =
            visitor.visitWhileBlock(this)
    }

    data class IfStatement(
        val condition: Expression,
        val body: Block,
        val elseBody: Block?
    ) : Statement {
        override fun <T> accept(visitor: LAstBaseVisitor<T>): T =
            visitor.visitIfStatement(this)
    }

    interface Expression : Node

    data class BinaryExpression(
        val leftExpression: Expression,
        val operator: Operator,
        val rightExpression: Expression
    ) : Expression {
        override fun <T> accept(visitor: LAstBaseVisitor<T>): T =
            visitor.visitBinaryExpression(this)
    }

    data class Identifier(val name: String) : Expression {
        override fun <T> accept(visitor: LAstBaseVisitor<T>): T =
            visitor.visitIdentifier(this)
    }

    data class Number(val value: Int) : Expression {
        override fun <T> accept(visitor: LAstBaseVisitor<T>): T =
            visitor.visitNumber(this)
    }

    enum class Operator(val symbol: String) : Node {
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
        LAND("&&");

        override fun <T> accept(visitor: LAstBaseVisitor<T>): T =
            visitor.visitOperator(this)

        companion object {
            fun getForSymbol(symbol: String): Operator =
                values().firstOrNull { it.symbol == symbol }!!
        }
    }
}