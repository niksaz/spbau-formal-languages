package ru.spbau.mit.ast

import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.misc.Interval
import org.antlr.v4.runtime.tree.ParseTree
import org.antlr.v4.runtime.tree.TerminalNode
import ru.spbau.mit.parser.LBaseVisitor
import ru.spbau.mit.parser.LParser
import ru.spbau.mit.parser.LParsingException

class AntlrLAstBuilder : LBaseVisitor<LAst.Node>() {
    fun buildAstFromContext(ctx: ParserRuleContext): LAst = LAst(visit(ctx))

    override fun visitFile(ctx: LParser.FileContext): LAst.Node {
        val procedures = ctx.function().map { visit(it) as LAst.Function}
        val block = visit(ctx.block()) as LAst.Block
        return LAst.File(procedures, block, intervalFor(ctx))
    }

    override fun visitFunction(ctx: LParser.FunctionContext): LAst.Node {
        val identifier = identifierFor(ctx.IDENTIFIER())
        val paramNames = visit(ctx.parameterNames()) as LAst.ParameterNames
        val body = visit(ctx.blockWithBraces()) as LAst.Block
        return LAst.Function(identifier, paramNames, body, intervalFor(ctx))
    }

    override fun visitParameterNames(ctx: LParser.ParameterNamesContext): LAst.Node {
        val identifiers = ctx.IDENTIFIER()
        val params = identifiers?.map { identifierFor(it) }.orEmpty()
        return LAst.ParameterNames(params, intervalFor(ctx))
    }

    override fun visitBlock(ctx: LParser.BlockContext): LAst.Node {
        val statements = ctx.statement().map { visit(it) as LAst.Statement }
        return LAst.Block(statements.toList(), intervalFor(ctx))
    }

    override fun visitAssignment(ctx: LParser.AssignmentContext): LAst.Node {
        val identifier = identifierFor(ctx.IDENTIFIER())
        val expression = visit(ctx.expression()) as LAst.Expression
        return LAst.Assignment(identifier, expression, intervalFor(ctx))
    }

    override fun visitReadCall(ctx: LParser.ReadCallContext): LAst.Node {
        val identifier = identifierFor(ctx.IDENTIFIER())
        return LAst.ReadCall(identifier, intervalFor(ctx))
    }

    override fun visitWriteCall(ctx: LParser.WriteCallContext): LAst.Node =
        LAst.WriteCall(visit(ctx.expression()) as LAst.Expression, intervalFor(ctx))

    override fun visitReturnStatement(ctx: LParser.ReturnStatementContext): LAst.Node {
        val expression = visit(ctx.expression()) as LAst.Expression
        return LAst.ReturnStatement(expression, intervalFor(ctx))
    }

    override fun visitFunctionCall(ctx: LParser.FunctionCallContext): LAst.Node {
        val identifier = identifierFor(ctx.IDENTIFIER())
        val arguments = visit(ctx.arguments()) as LAst.Arguments
        return LAst.FunctionCall(identifier, arguments, intervalFor(ctx))
    }

    override fun visitArguments(ctx: LParser.ArgumentsContext): LAst.Node {
        val possibleExpressions = ctx.expression()
        val expressions = possibleExpressions?.map { visit(it) as LAst.Expression }.orEmpty()
        return LAst.Arguments(expressions, intervalFor(ctx))
    }

    override fun visitWhileBlock(ctx: LParser.WhileBlockContext): LAst.Node {
        val condition = visit(ctx.expression()) as LAst.Expression
        val body = visit(ctx.blockWithBraces()) as LAst.Block
        return LAst.WhileBlock(condition, body, intervalFor(ctx))
    }

    override fun visitIfStatement(ctx: LParser.IfStatementContext): LAst.Node {
        val condition = visit(ctx.expression()) as LAst.Expression
        val blocks = ctx.blockWithBraces().map { visit(it) }
        val body = blocks[0] as LAst.Block
        val elseBody = blocks.getOrNull(1) as LAst.Block?
        return LAst.IfStatement(condition, body, elseBody, intervalFor(ctx))
    }

    private fun transformExpression(
        exprs: List<ParserRuleContext>, ops: List<TerminalNode>): LAst.ExpressionImpl {
        var left = visit(exprs[0]) as LAst.ExpressionImpl
        exprs.drop(1).zip(ops).forEach { (expr, op) ->
            val operator = operatorFor(op)
            val right = visit(expr) as LAst.ExpressionImpl
            left = LAst.BinaryExpression(
                left, operator, right, Interval(left.sourceInterval.a, right.sourceInterval.b))
        }
        return left
    }

    override fun visitLorExpression(ctx: LParser.LorExpressionContext): LAst.Node =
        transformExpression(ctx.landExpression(), ctx.LOR())

    override fun visitLandExpression(ctx: LParser.LandExpressionContext): LAst.Node =
        transformExpression(ctx.equivalenceExpression(), ctx.LAND())

    override fun visitEquivalenceExpression(ctx: LParser.EquivalenceExpressionContext): LAst.Node =
        transformExpression(ctx.relationalExpression(), joinTokenLists(ctx.NQ(), ctx.EQ()))

    override fun visitRelationalExpression(ctx: LParser.RelationalExpressionContext): LAst.Node =
        transformExpression(
            ctx.additiveExpression(),
            joinTokenLists(ctx.GT(), ctx.GTE(), ctx.LT(), ctx.LTE()))

    override fun visitAdditiveExpression(ctx: LParser.AdditiveExpressionContext): LAst.Node =
        transformExpression(
            ctx.multiplicativeExpression(),
            joinTokenLists(ctx.MINUS(), ctx.PLUS()))

    override fun visitMultiplicativeExpression(
        ctx: LParser.MultiplicativeExpressionContext
    ): LAst.Node =
        transformExpression(
            ctx.atomicExpression(),
            joinTokenLists(ctx.MULTIPLY(), ctx.DIVIDE(), ctx.MODULUS()))

    override fun visitBracedExpression(ctx: LParser.BracedExpressionContext): LAst.Node =
        LAst.BracedExpression(visit(ctx.expression()) as LAst.Expression, intervalFor(ctx))

    override fun visitAtomicExpression(ctx: LParser.AtomicExpressionContext): LAst.Node {
        val identifierToken = ctx.IDENTIFIER()
        if (identifierToken != null) {
            return identifierFor(identifierToken)
        }
        val numberToken = ctx.NUMBER()
        if (numberToken != null) {
            val value = try {
                numberToken.text.toInt()
            } catch (e: NumberFormatException) {
                throw LParsingException(e.message)
            }
            return LAst.Number(value, intervalFor(ctx))
        }
        val otherAtomicExpressions = listOf<ParseTree?>(
            ctx.functionCall(),
            ctx.bracedExpression()
        )
        val atomicExpression = otherAtomicExpressions.find { it != null }
        return visit(atomicExpression!!)
    }

    override fun aggregateResult(aggregate: LAst.Node?, nextResult: LAst.Node?): LAst.Node? =
        aggregate ?: nextResult

    companion object {
        private fun intervalFor(ctx: ParserRuleContext): Interval =
            Interval(ctx.start.startIndex, ctx.stop.stopIndex)

        private fun identifierFor(node: TerminalNode): LAst.Identifier =
            LAst.Identifier(node.text, Interval(node.symbol.startIndex, node.symbol.stopIndex))

        private fun operatorFor(node: TerminalNode): LAst.Operator =
            LAst.Operator.getFor(node.text, Interval(node.symbol.startIndex, node.symbol.stopIndex))

        private fun joinTokenLists(vararg tokenLists: List<TerminalNode>): List<TerminalNode> {
            val result = mutableListOf<TerminalNode>()
            tokenLists.forEach { result.addAll(it) }
            result.sortBy { it -> it.symbol.startIndex }
            return result.toList()
        }
    }
}