package ru.spbau.mit.ast

import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.misc.Interval
import org.antlr.v4.runtime.tree.TerminalNode
import ru.spbau.mit.parser.LBaseVisitor
import ru.spbau.mit.parser.LParser
import ru.spbau.mit.parser.LParsingException

class AntlrLAstBuilder : LBaseVisitor<LAst.Node>() {
    fun buildAstFromContext(ctx: ParserRuleContext): LAst = LAst(visit(ctx))

    override fun visitFile(ctx: LParser.FileContext): LAst.Node {
        val procedures = ctx.procedure().map { visit(it) as LAst.Procedure}
        val block = visit(ctx.block()) as LAst.Block
        return LAst.File(procedures, block, intervalFor(ctx))
    }

    override fun visitProcedure(ctx: LParser.ProcedureContext): LAst.Node {
        val identifier = identifierFor(ctx.IDENTIFIER())
        val paramNames = visit(ctx.parameterNames()) as LAst.ParameterNames
        val body = visit(ctx.blockWithBraces()) as LAst.Block
        return LAst.Procedure(identifier, paramNames, body, intervalFor(ctx))
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

    override fun visitProcedureCall(ctx: LParser.ProcedureCallContext): LAst.Node {
        val identifier = identifierFor(ctx.IDENTIFIER())
        val arguments = visit(ctx.arguments()) as LAst.Arguments
        return LAst.ProcedureCall(identifier, arguments, intervalFor(ctx))
    }

    override fun visitArguments(ctx: LParser.ArgumentsContext): LAst.Node {
        val possibleExpressions = ctx.IDENTIFIER()
        val expressions = possibleExpressions?.map { identifierFor(it) }.orEmpty()
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

    override fun visitLorExpression(ctx: LParser.LorExpressionContext): LAst.Node =
        transformExpression(ctx)

    private fun transformExpression(ctx: ParserRuleContext): LAst.ExpressionImpl {
        var left = visit(ctx.children[0]) as LAst.ExpressionImpl
        var i = 1
        while (i < ctx.childCount) {
            val opParseTree = ctx.children[i++]
            val operator = LAst.Operator.getFor(opParseTree.text, opParseTree.sourceInterval)
            val right = visit(ctx.children[i++]) as LAst.ExpressionImpl
            left = LAst.BinaryExpression(
                left,
                operator,
                right,
                Interval(left.sourceInterval.a, right.sourceInterval.b))
        }
        return left
    }

    override fun visitLandExpression(ctx: LParser.LandExpressionContext): LAst.Node =
        transformExpression(ctx)

    override fun visitEquivalenceExpression(ctx: LParser.EquivalenceExpressionContext): LAst.Node =
        transformExpression(ctx)

    override fun visitRelationalExpression(ctx: LParser.RelationalExpressionContext): LAst.Node =
        transformExpression(ctx)

    override fun visitAdditiveExpression(ctx: LParser.AdditiveExpressionContext): LAst.Node =
        transformExpression(ctx)

    override fun visitMultiplicativeExpression(
        ctx: LParser.MultiplicativeExpressionContext
    ): LAst.Node = transformExpression(ctx)

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
        return visit(ctx.bracedExpression())
    }

    override fun aggregateResult(aggregate: LAst.Node?, nextResult: LAst.Node?): LAst.Node? =
        aggregate ?: nextResult

    companion object {
        private fun intervalFor(ctx: ParserRuleContext): Interval =
            Interval(ctx.start.startIndex, ctx.stop.stopIndex)

        private fun identifierFor(node: TerminalNode): LAst.Identifier =
            LAst.Identifier(node.text, Interval(node.symbol.startIndex, node.symbol.stopIndex))
    }
}