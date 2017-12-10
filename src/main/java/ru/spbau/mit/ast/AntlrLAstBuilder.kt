package ru.spbau.mit.ast

import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.misc.Interval
import ru.spbau.mit.parser.LBaseVisitor
import ru.spbau.mit.parser.LParser
import ru.spbau.mit.parser.LParsingException

class AntlrLAstBuilder : LBaseVisitor<LAst.Node>() {
    fun buildAstFromContext(ctx: ParserRuleContext): LAst = LAst(visit(ctx))

    override fun visitFile(ctx: LParser.FileContext): LAst.Node {
        val procedures = ctx.procedure().map { visit(it) as LAst.Procedure}
        val block = visit(ctx.block()) as LAst.Block
        return LAst.File(procedures, block, ctx.sourceInterval)
    }

    override fun visitProcedure(ctx: LParser.ProcedureContext): LAst.Node {
        val identifier = LAst.Identifier(ctx.IDENTIFIER(), ctx.IDENTIFIER().sourceInterval)
        val paramNames = visit(ctx.parameterNames()) as LAst.ParameterNames
        val body = visit(ctx.blockWithBraces()) as LAst.Block
        return LAst.Procedure(identifier, paramNames, body, ctx.sourceInterval)
    }

    override fun visitParameterNames(ctx: LParser.ParameterNamesContext): LAst.Node {
        val identifiers = ctx.IDENTIFIER()
        val params =
            identifiers?.map { LAst.Identifier(it, it.sourceInterval) }?.toList() ?: listOf()
        return LAst.ParameterNames(params, ctx.sourceInterval)
    }

    override fun visitBlock(ctx: LParser.BlockContext): LAst.Node {
        val statements = ctx.statement().map { visit(it) as LAst.Statement }
        return LAst.Block(statements.toList(), ctx.sourceInterval)
    }

    override fun visitAssignment(ctx: LParser.AssignmentContext): LAst.Node {
        val identifier = LAst.Identifier(ctx.IDENTIFIER(), ctx.IDENTIFIER().sourceInterval)
        val expression = visit(ctx.expression()) as LAst.Expression
        return LAst.Assignment(identifier, expression, ctx.sourceInterval)
    }

    override fun visitReadCall(ctx: LParser.ReadCallContext): LAst.Node {
        val identifier = LAst.Identifier(ctx.IDENTIFIER(), ctx.IDENTIFIER().sourceInterval)
        return LAst.ReadCall(identifier, ctx.sourceInterval)
    }

    override fun visitWriteCall(ctx: LParser.WriteCallContext): LAst.Node =
        LAst.WriteCall(visit(ctx.expression()) as LAst.Expression, ctx.sourceInterval)

    override fun visitProcedureCall(ctx: LParser.ProcedureCallContext): LAst.Node {
        val identifier = LAst.Identifier(ctx.IDENTIFIER(), ctx.IDENTIFIER().sourceInterval)
        val arguments = visit(ctx.arguments()) as LAst.Arguments
        return LAst.ProcedureCall(identifier, arguments, ctx.sourceInterval)
    }

    override fun visitArguments(ctx: LParser.ArgumentsContext): LAst.Node {
        val possibleExpressions = ctx.IDENTIFIER()
        val expressions =
            possibleExpressions?.map {
                LAst.Identifier(it, it.sourceInterval)
            }?.toList() ?: listOf()
        return LAst.Arguments(expressions, ctx.sourceInterval)
    }

    override fun visitWhileBlock(ctx: LParser.WhileBlockContext): LAst.Node {
        val condition = visit(ctx.expression()) as LAst.Expression
        val body = visit(ctx.blockWithBraces()) as LAst.Block
        return LAst.WhileBlock(condition, body, ctx.sourceInterval)
    }

    override fun visitIfStatement(ctx: LParser.IfStatementContext): LAst.Node {
        val condition = visit(ctx.expression()) as LAst.Expression
        val blocks = ctx.blockWithBraces().map { visit(it) }
        val body = blocks[0] as LAst.Block
        val elseBody = blocks.getOrNull(1) as LAst.Block?
        return LAst.IfStatement(condition, body, elseBody, ctx.sourceInterval)
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
        LAst.BracedExpression(visit(ctx.expression()) as LAst.Expression, ctx.sourceInterval)

    override fun visitAtomicExpression(ctx: LParser.AtomicExpressionContext): LAst.Node {
        val identifier = ctx.IDENTIFIER()
        if (identifier != null) {
            return LAst.Identifier(identifier, ctx.sourceInterval)
        }
        val number = ctx.NUMBER()
        if (number != null) {
            val value = try {
                number.text.toInt()
            } catch (e: NumberFormatException) {
                throw LParsingException(e.message)
            }
            return LAst.Number(value, ctx.sourceInterval)
        }
        return visit(ctx.bracedExpression())
    }

    override fun aggregateResult(aggregate: LAst.Node?, nextResult: LAst.Node?): LAst.Node? =
        aggregate ?: nextResult
}