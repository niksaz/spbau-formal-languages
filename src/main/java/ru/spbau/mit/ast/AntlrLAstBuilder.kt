package ru.spbau.mit.ast

import org.antlr.v4.runtime.ParserRuleContext
import ru.spbau.mit.parser.LBaseVisitor
import ru.spbau.mit.parser.LParser
import ru.spbau.mit.parser.LParsingException

class AntlrLAstBuilder : LBaseVisitor<LAst.Node>() {
    fun buildAstFromContext(ctx: ParserRuleContext): LAst = LAst(visit(ctx))

    override fun visitFile(ctx: LParser.FileContext): LAst.Node {
        val procedures = ctx.procedure().map { visit(it) as LAst.Procedure}
        val block = visit(ctx.block()) as LAst.Block
        return LAst.File(procedures, block)
    }

    override fun visitProcedure(ctx: LParser.ProcedureContext): LAst.Node {
        val identifier = LAst.Identifier(ctx.IDENTIFIER().text)
        val paramNames = visit(ctx.parameterNames()) as LAst.ParameterNames
        val body = visit(ctx.blockWithBraces()) as LAst.Block
        return LAst.Procedure(identifier, paramNames, body)
    }

    override fun visitParameterNames(ctx: LParser.ParameterNamesContext): LAst.Node {
        val identifiers = ctx.IDENTIFIER()
        val params = identifiers?.map { LAst.Identifier(it.text) }?.toList() ?: listOf()
        return LAst.ParameterNames(params)
    }

    override fun visitBlock(ctx: LParser.BlockContext): LAst.Node {
        val statements = ctx.statement().map { visit(it) as LAst.Statement }
        return LAst.Block(statements.toList())
    }

    override fun visitAssignment(ctx: LParser.AssignmentContext): LAst.Node {
        val identifier = LAst.Identifier(ctx.IDENTIFIER().text)
        val expression = visit(ctx.expression()) as LAst.Expression
        return LAst.Assignment(identifier, expression)
    }

    override fun visitWriteCall(ctx: LParser.WriteCallContext): LAst.Node =
        LAst.WriteCall(visit(ctx.expression()) as LAst.Expression)

    override fun visitProcedureCall(ctx: LParser.ProcedureCallContext): LAst.Node {
        val identifier = LAst.Identifier(ctx.IDENTIFIER().text)
        val arguments = visit(ctx.arguments()) as LAst.Arguments
        return LAst.ProcedureCall(identifier, arguments)
    }

    override fun visitArguments(ctx: LParser.ArgumentsContext): LAst.Node {
        val possibleExpressions = ctx.IDENTIFIER()
        val expressions =
            possibleExpressions?.map { LAst.Identifier(it.text) }?.toList() ?: listOf()
        return LAst.Arguments(expressions)    }

    override fun visitWhileBlock(ctx: LParser.WhileBlockContext): LAst.Node {
        val condition = visit(ctx.expression()) as LAst.Expression
        val body = visit(ctx.blockWithBraces()) as LAst.Block
        return LAst.WhileBlock(condition, body)
    }

    override fun visitIfStatement(ctx: LParser.IfStatementContext): LAst.Node {
        val condition = visit(ctx.expression()) as LAst.Expression
        val blocks = ctx.blockWithBraces().map { visit(it) }
        val body = blocks[0] as LAst.Block
        val elseBody = blocks.getOrNull(1) as LAst.Block?
        return LAst.IfStatement(condition, body, elseBody)
    }

    override fun visitLorExpression(ctx: LParser.LorExpressionContext): LAst.Node =
        transformExpression(ctx)

    private fun transformExpression(ctx: ParserRuleContext): LAst.Expression {
        var left = visit(ctx.children[0]) as LAst.Expression
        var i = 1
        while (i < ctx.childCount) {
            val operator = LAst.Operator.getForSymbol(ctx.children[i++].text)
            val right = visit(ctx.children[i++]) as LAst.Expression
            left = LAst.BinaryExpression(left, operator, right)
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

    override fun visitAtomicExpression(ctx: LParser.AtomicExpressionContext): LAst.Node {
        val identifier = ctx.IDENTIFIER()
        if (identifier != null) {
            return LAst.Identifier(identifier.text)
        }
        val number = ctx.NUMBER()
        if (number != null) {
            val value = try {
                number.text.toInt()
            } catch (e: NumberFormatException) {
                throw LParsingException(e.message)
            }
            return LAst.Number(value)
        }
        return visit(ctx.expression())
    }

    override fun aggregateResult(aggregate: LAst.Node?, nextResult: LAst.Node?): LAst.Node? =
        aggregate ?: nextResult
}