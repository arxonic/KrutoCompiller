// Generated from C:/Users/HP/IdeaProjects/KursCompiller/src/com/company\KrutoParser.g4 by ANTLR 4.10.1
package com.company;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link KrutoParser}.
 */
public interface KrutoParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link KrutoParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(KrutoParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link KrutoParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(KrutoParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link KrutoParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(KrutoParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link KrutoParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(KrutoParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link KrutoParser#variableDeclarationPart}.
	 * @param ctx the parse tree
	 */
	void enterVariableDeclarationPart(KrutoParser.VariableDeclarationPartContext ctx);
	/**
	 * Exit a parse tree produced by {@link KrutoParser#variableDeclarationPart}.
	 * @param ctx the parse tree
	 */
	void exitVariableDeclarationPart(KrutoParser.VariableDeclarationPartContext ctx);
	/**
	 * Enter a parse tree produced by {@link KrutoParser#variableDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterVariableDeclaration(KrutoParser.VariableDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link KrutoParser#variableDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitVariableDeclaration(KrutoParser.VariableDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link KrutoParser#identifierList}.
	 * @param ctx the parse tree
	 */
	void enterIdentifierList(KrutoParser.IdentifierListContext ctx);
	/**
	 * Exit a parse tree produced by {@link KrutoParser#identifierList}.
	 * @param ctx the parse tree
	 */
	void exitIdentifierList(KrutoParser.IdentifierListContext ctx);
	/**
	 * Enter a parse tree produced by {@link KrutoParser#typeIdentifier}.
	 * @param ctx the parse tree
	 */
	void enterTypeIdentifier(KrutoParser.TypeIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link KrutoParser#typeIdentifier}.
	 * @param ctx the parse tree
	 */
	void exitTypeIdentifier(KrutoParser.TypeIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link KrutoParser#functionDeclarationPart}.
	 * @param ctx the parse tree
	 */
	void enterFunctionDeclarationPart(KrutoParser.FunctionDeclarationPartContext ctx);
	/**
	 * Exit a parse tree produced by {@link KrutoParser#functionDeclarationPart}.
	 * @param ctx the parse tree
	 */
	void exitFunctionDeclarationPart(KrutoParser.FunctionDeclarationPartContext ctx);
	/**
	 * Enter a parse tree produced by {@link KrutoParser#resultType}.
	 * @param ctx the parse tree
	 */
	void enterResultType(KrutoParser.ResultTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link KrutoParser#resultType}.
	 * @param ctx the parse tree
	 */
	void exitResultType(KrutoParser.ResultTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link KrutoParser#formalParameterList}.
	 * @param ctx the parse tree
	 */
	void enterFormalParameterList(KrutoParser.FormalParameterListContext ctx);
	/**
	 * Exit a parse tree produced by {@link KrutoParser#formalParameterList}.
	 * @param ctx the parse tree
	 */
	void exitFormalParameterList(KrutoParser.FormalParameterListContext ctx);
	/**
	 * Enter a parse tree produced by {@link KrutoParser#formalParameterSection}.
	 * @param ctx the parse tree
	 */
	void enterFormalParameterSection(KrutoParser.FormalParameterSectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KrutoParser#formalParameterSection}.
	 * @param ctx the parse tree
	 */
	void exitFormalParameterSection(KrutoParser.FormalParameterSectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link KrutoParser#compoundStatement}.
	 * @param ctx the parse tree
	 */
	void enterCompoundStatement(KrutoParser.CompoundStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link KrutoParser#compoundStatement}.
	 * @param ctx the parse tree
	 */
	void exitCompoundStatement(KrutoParser.CompoundStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link KrutoParser#statements}.
	 * @param ctx the parse tree
	 */
	void enterStatements(KrutoParser.StatementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link KrutoParser#statements}.
	 * @param ctx the parse tree
	 */
	void exitStatements(KrutoParser.StatementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link KrutoParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(KrutoParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link KrutoParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(KrutoParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link KrutoParser#specialStatement}.
	 * @param ctx the parse tree
	 */
	void enterSpecialStatement(KrutoParser.SpecialStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link KrutoParser#specialStatement}.
	 * @param ctx the parse tree
	 */
	void exitSpecialStatement(KrutoParser.SpecialStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link KrutoParser#printStatement}.
	 * @param ctx the parse tree
	 */
	void enterPrintStatement(KrutoParser.PrintStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link KrutoParser#printStatement}.
	 * @param ctx the parse tree
	 */
	void exitPrintStatement(KrutoParser.PrintStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link KrutoParser#returnStatement}.
	 * @param ctx the parse tree
	 */
	void enterReturnStatement(KrutoParser.ReturnStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link KrutoParser#returnStatement}.
	 * @param ctx the parse tree
	 */
	void exitReturnStatement(KrutoParser.ReturnStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link KrutoParser#breakStatement}.
	 * @param ctx the parse tree
	 */
	void enterBreakStatement(KrutoParser.BreakStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link KrutoParser#breakStatement}.
	 * @param ctx the parse tree
	 */
	void exitBreakStatement(KrutoParser.BreakStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link KrutoParser#continueStatement}.
	 * @param ctx the parse tree
	 */
	void enterContinueStatement(KrutoParser.ContinueStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link KrutoParser#continueStatement}.
	 * @param ctx the parse tree
	 */
	void exitContinueStatement(KrutoParser.ContinueStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link KrutoParser#structuredStatement}.
	 * @param ctx the parse tree
	 */
	void enterStructuredStatement(KrutoParser.StructuredStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link KrutoParser#structuredStatement}.
	 * @param ctx the parse tree
	 */
	void exitStructuredStatement(KrutoParser.StructuredStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link KrutoParser#ifStatement}.
	 * @param ctx the parse tree
	 */
	void enterIfStatement(KrutoParser.IfStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link KrutoParser#ifStatement}.
	 * @param ctx the parse tree
	 */
	void exitIfStatement(KrutoParser.IfStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link KrutoParser#whileStatement}.
	 * @param ctx the parse tree
	 */
	void enterWhileStatement(KrutoParser.WhileStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link KrutoParser#whileStatement}.
	 * @param ctx the parse tree
	 */
	void exitWhileStatement(KrutoParser.WhileStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link KrutoParser#simpleStatement}.
	 * @param ctx the parse tree
	 */
	void enterSimpleStatement(KrutoParser.SimpleStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link KrutoParser#simpleStatement}.
	 * @param ctx the parse tree
	 */
	void exitSimpleStatement(KrutoParser.SimpleStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link KrutoParser#emptyStatement_}.
	 * @param ctx the parse tree
	 */
	void enterEmptyStatement_(KrutoParser.EmptyStatement_Context ctx);
	/**
	 * Exit a parse tree produced by {@link KrutoParser#emptyStatement_}.
	 * @param ctx the parse tree
	 */
	void exitEmptyStatement_(KrutoParser.EmptyStatement_Context ctx);
	/**
	 * Enter a parse tree produced by {@link KrutoParser#assignmentStatement}.
	 * @param ctx the parse tree
	 */
	void enterAssignmentStatement(KrutoParser.AssignmentStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link KrutoParser#assignmentStatement}.
	 * @param ctx the parse tree
	 */
	void exitAssignmentStatement(KrutoParser.AssignmentStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link KrutoParser#variable}.
	 * @param ctx the parse tree
	 */
	void enterVariable(KrutoParser.VariableContext ctx);
	/**
	 * Exit a parse tree produced by {@link KrutoParser#variable}.
	 * @param ctx the parse tree
	 */
	void exitVariable(KrutoParser.VariableContext ctx);
	/**
	 * Enter a parse tree produced by {@link KrutoParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(KrutoParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KrutoParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(KrutoParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link KrutoParser#relationaloperator}.
	 * @param ctx the parse tree
	 */
	void enterRelationaloperator(KrutoParser.RelationaloperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link KrutoParser#relationaloperator}.
	 * @param ctx the parse tree
	 */
	void exitRelationaloperator(KrutoParser.RelationaloperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link KrutoParser#simpleExpression}.
	 * @param ctx the parse tree
	 */
	void enterSimpleExpression(KrutoParser.SimpleExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KrutoParser#simpleExpression}.
	 * @param ctx the parse tree
	 */
	void exitSimpleExpression(KrutoParser.SimpleExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link KrutoParser#additiveoperator}.
	 * @param ctx the parse tree
	 */
	void enterAdditiveoperator(KrutoParser.AdditiveoperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link KrutoParser#additiveoperator}.
	 * @param ctx the parse tree
	 */
	void exitAdditiveoperator(KrutoParser.AdditiveoperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link KrutoParser#term}.
	 * @param ctx the parse tree
	 */
	void enterTerm(KrutoParser.TermContext ctx);
	/**
	 * Exit a parse tree produced by {@link KrutoParser#term}.
	 * @param ctx the parse tree
	 */
	void exitTerm(KrutoParser.TermContext ctx);
	/**
	 * Enter a parse tree produced by {@link KrutoParser#multiplicativeoperator}.
	 * @param ctx the parse tree
	 */
	void enterMultiplicativeoperator(KrutoParser.MultiplicativeoperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link KrutoParser#multiplicativeoperator}.
	 * @param ctx the parse tree
	 */
	void exitMultiplicativeoperator(KrutoParser.MultiplicativeoperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link KrutoParser#signedFactor}.
	 * @param ctx the parse tree
	 */
	void enterSignedFactor(KrutoParser.SignedFactorContext ctx);
	/**
	 * Exit a parse tree produced by {@link KrutoParser#signedFactor}.
	 * @param ctx the parse tree
	 */
	void exitSignedFactor(KrutoParser.SignedFactorContext ctx);
	/**
	 * Enter a parse tree produced by {@link KrutoParser#factor}.
	 * @param ctx the parse tree
	 */
	void enterFactor(KrutoParser.FactorContext ctx);
	/**
	 * Exit a parse tree produced by {@link KrutoParser#factor}.
	 * @param ctx the parse tree
	 */
	void exitFactor(KrutoParser.FactorContext ctx);
	/**
	 * Enter a parse tree produced by {@link KrutoParser#unsignedNumber}.
	 * @param ctx the parse tree
	 */
	void enterUnsignedNumber(KrutoParser.UnsignedNumberContext ctx);
	/**
	 * Exit a parse tree produced by {@link KrutoParser#unsignedNumber}.
	 * @param ctx the parse tree
	 */
	void exitUnsignedNumber(KrutoParser.UnsignedNumberContext ctx);
	/**
	 * Enter a parse tree produced by {@link KrutoParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void enterFunctionCall(KrutoParser.FunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link KrutoParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void exitFunctionCall(KrutoParser.FunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link KrutoParser#expressionList}.
	 * @param ctx the parse tree
	 */
	void enterExpressionList(KrutoParser.ExpressionListContext ctx);
	/**
	 * Exit a parse tree produced by {@link KrutoParser#expressionList}.
	 * @param ctx the parse tree
	 */
	void exitExpressionList(KrutoParser.ExpressionListContext ctx);
}