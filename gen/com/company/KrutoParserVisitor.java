// Generated from C:/Users/HP/IdeaProjects/KursCompiller/src/com/company\KrutoParser.g4 by ANTLR 4.10.1
package com.company;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link KrutoParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface KrutoParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link KrutoParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(KrutoParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link KrutoParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(KrutoParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link KrutoParser#variableDeclarationPart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableDeclarationPart(KrutoParser.VariableDeclarationPartContext ctx);
	/**
	 * Visit a parse tree produced by {@link KrutoParser#variableDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableDeclaration(KrutoParser.VariableDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link KrutoParser#identifierList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifierList(KrutoParser.IdentifierListContext ctx);
	/**
	 * Visit a parse tree produced by {@link KrutoParser#typeIdentifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeIdentifier(KrutoParser.TypeIdentifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link KrutoParser#functionDeclarationPart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionDeclarationPart(KrutoParser.FunctionDeclarationPartContext ctx);
	/**
	 * Visit a parse tree produced by {@link KrutoParser#resultType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResultType(KrutoParser.ResultTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link KrutoParser#formalParameterList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFormalParameterList(KrutoParser.FormalParameterListContext ctx);
	/**
	 * Visit a parse tree produced by {@link KrutoParser#formalParameterSection}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFormalParameterSection(KrutoParser.FormalParameterSectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link KrutoParser#compoundStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompoundStatement(KrutoParser.CompoundStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link KrutoParser#statements}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatements(KrutoParser.StatementsContext ctx);
	/**
	 * Visit a parse tree produced by {@link KrutoParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(KrutoParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link KrutoParser#specialStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpecialStatement(KrutoParser.SpecialStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link KrutoParser#printStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrintStatement(KrutoParser.PrintStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link KrutoParser#returnStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnStatement(KrutoParser.ReturnStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link KrutoParser#breakStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBreakStatement(KrutoParser.BreakStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link KrutoParser#continueStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContinueStatement(KrutoParser.ContinueStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link KrutoParser#structuredStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStructuredStatement(KrutoParser.StructuredStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link KrutoParser#ifStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStatement(KrutoParser.IfStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link KrutoParser#whileStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileStatement(KrutoParser.WhileStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link KrutoParser#simpleStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleStatement(KrutoParser.SimpleStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link KrutoParser#emptyStatement_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEmptyStatement_(KrutoParser.EmptyStatement_Context ctx);
	/**
	 * Visit a parse tree produced by {@link KrutoParser#assignmentStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignmentStatement(KrutoParser.AssignmentStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link KrutoParser#variable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariable(KrutoParser.VariableContext ctx);
	/**
	 * Visit a parse tree produced by {@link KrutoParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(KrutoParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link KrutoParser#relationaloperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelationaloperator(KrutoParser.RelationaloperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link KrutoParser#simpleExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleExpression(KrutoParser.SimpleExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link KrutoParser#additiveoperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdditiveoperator(KrutoParser.AdditiveoperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link KrutoParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTerm(KrutoParser.TermContext ctx);
	/**
	 * Visit a parse tree produced by {@link KrutoParser#multiplicativeoperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiplicativeoperator(KrutoParser.MultiplicativeoperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link KrutoParser#signedFactor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSignedFactor(KrutoParser.SignedFactorContext ctx);
	/**
	 * Visit a parse tree produced by {@link KrutoParser#factor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFactor(KrutoParser.FactorContext ctx);
	/**
	 * Visit a parse tree produced by {@link KrutoParser#unsignedNumber}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnsignedNumber(KrutoParser.UnsignedNumberContext ctx);
	/**
	 * Visit a parse tree produced by {@link KrutoParser#functionCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionCall(KrutoParser.FunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link KrutoParser#expressionList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionList(KrutoParser.ExpressionListContext ctx);
}