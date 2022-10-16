lexer grammar KrutoLexer;

WHILE:              'while';
DO:                 'do';
BREAK:              'break';
CONTINUE:           'continue';

IF:                 'if';
THEN:               'then';

VAR:                'var';
INT:                'int';
FLOAT:              'float';

FUNC:               'func';
VOID:               'void';
RETURN:             'return';

PRINT:              'print';

// Separators
LPAREN:             '(';
RPAREN:             ')';
LBRACE:             '{';
RBRACE:             '}';
LBRACK:             '[';
RBRACK:             ']';
SEMI:               ';';
COLON:              ':';
COMMA:              ',';
DOT:                '.';

// Operators
ASSIGN:             '=';
GT:                 '>';
LT:                 '<';
BANG:               '!';
EQUAL:              '==';
LE:                 '<=';
GE:                 '>=';
NOTEQUAL:           '!=';
AND:                '&&';
OR:                 '||';
ADD:                '+';
SUB:                '-';
MUL:                '*';
DIV:                '/';
CARET:              '^';

// Literals
DECIMAL_LITERAL:    ('0' | [1-9] (Digits? | '_'+ Digits)) [lL]?;
FLOAT_LITERAL:      (Digits '.' Digits? | '.' Digits) ExponentPart? [fFdD]?
             |       Digits (ExponentPart [fFdD]? | [fFdD])
             ;

IDENTIFIER:         Letter LetterOrDigit*;

fragment ExponentPart
    : [eE] [+-]? Digits
    ;

fragment Digits
    : [0-9]+
    ;

fragment LetterOrDigit
    : Letter
    | [0-9]
    ;

fragment Letter
    : [a-zA-Z] [a-zA-Z]*
    ;

WS:                 [ \t\r\n]+      -> skip;
COMMENT:            '!#' .*? '#!'   -> skip;