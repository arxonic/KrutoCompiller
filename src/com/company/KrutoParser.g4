parser grammar KrutoParser;

options { tokenVocab=KrutoLexer; }

program: block '.';

block
   : (variableDeclarationPart | functionDeclarationPart)* compoundStatement
   ;

variableDeclarationPart
   : VAR variableDeclaration (';' variableDeclaration)* ';'
   ;
variableDeclaration
   : identifierList ':' typeIdentifier
   ;
identifierList
   : IDENTIFIER (',' IDENTIFIER)*
   ;

typeIdentifier
   : INT
   | FLOAT
   ;


functionDeclarationPart
   : FUNC resultType IDENTIFIER (formalParameterList)? block ';'
   ;
resultType
   : INT
   | FLOAT
   | VOID
   ;
formalParameterList
   : '(' formalParameterSection (';' formalParameterSection)* ')'
   ;
formalParameterSection
   : variableDeclaration
   ;


compoundStatement
   : '{' statements '}'
   ;

statements
   : statement (';' statement)*
   ;

statement
   : simpleStatement
   | structuredStatement
   | specialStatement
   ;

specialStatement
    : breakStatement
    | continueStatement
    | returnStatement
    | printStatement
    ;

printStatement
    : PRINT '(' variable ')'
    ;

returnStatement
    : RETURN variable
    ;

breakStatement
    : BREAK
    ;

continueStatement
    : CONTINUE
    ;

structuredStatement
   : compoundStatement
   | ifStatement
   | whileStatement
   ;

ifStatement
   : IF expression THEN statement
   ;

whileStatement
   : WHILE expression DO statement
   ;

simpleStatement
   : assignmentStatement
   | functionCall
   | emptyStatement_
   ;

emptyStatement_
   :
   ;

assignmentStatement
   : variable '=' expression
   ;

variable
   : IDENTIFIER
   ;

expression
   : simpleExpression (relationaloperator expression)?
   ;

relationaloperator
   : '=='
   | '!='
   | '<'
   | '<='
   | '>='
   | '>'
   ;

simpleExpression
   : term (additiveoperator simpleExpression)?
   ;

additiveoperator
   : '+'
   | '-'
   | '||'
   ;

term
   : signedFactor (multiplicativeoperator term)?
   ;

multiplicativeoperator
   : '*'
   | '/'
   | '&&'
   ;

signedFactor
   : ('+' | '-')? factor
   ;

factor
   : variable
   | '(' expression ')'
   | functionCall
   | unsignedNumber
   | '!' factor
   ;

unsignedNumber
   : DECIMAL_LITERAL
   | FLOAT_LITERAL
   ;

functionCall
   : IDENTIFIER '(' expressionList? ')'
   ;
expressionList
   : expression (',' expression)*
   ;