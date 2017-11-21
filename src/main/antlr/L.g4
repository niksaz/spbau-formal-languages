grammar L;

file
    : (procedure)* block
    ;

procedure
    : PROCEDURE IDENTIFIER L_BRACE parameterNames R_BRACE blockWithBraces
    ;

parameterNames
    : (IDENTIFIER (COMMA IDENTIFIER)*)?
    ;

block
    : (statement)*
    ;

blockWithBraces
    : L_CURLY_BRACE block R_CURLY_BRACE
    ;

statement
    : assignment SEMICOLON
    | writeCall SEMICOLON
    | procedureCall SEMICOLON
    | whileBlock
    | ifStatement
    ;

assignment
    : IDENTIFIER ASSIGN expression
    ;

writeCall
    : WRITE L_BRACE expression R_BRACE
    ;

procedureCall
    : IDENTIFIER L_BRACE arguments R_BRACE
    ;

arguments
    : (IDENTIFIER (COMMA IDENTIFIER)*)?
    ;

whileBlock
    : WHILE L_BRACE expression R_BRACE blockWithBraces
    ;

ifStatement
    : IF L_BRACE expression R_BRACE THEN blockWithBraces (ELSE blockWithBraces)?
    ;

expression
    : lorExpression
    ;

lorExpression
    : landExpression (LOR landExpression)*
    ;

landExpression
    : equivalenceExpression (op = LAND equivalenceExpression)*
    ;

equivalenceExpression
    : relationalExpression (op = (EQ | NQ) relationalExpression)*
    ;

relationalExpression
    : additiveExpression (op = (GT | LT | GTE | LTE) additiveExpression)*
    ;

additiveExpression
    : multiplicativeExpression (op = (PLUS | MINUS) multiplicativeExpression)*
    ;

multiplicativeExpression
    : atomicExpression (op = (MULTIPLY | DIVIDE | MODULUS) atomicExpression)*
    ;

bracedExpression
    : L_BRACE expression R_BRACE
    ;

atomicExpression
    : bracedExpression
    | IDENTIFIER
    | NUMBER
    ;

SEMICOLON : ';';

L_BRACE : '(';
R_BRACE : ')';
L_CURLY_BRACE : '{';
R_CURLY_BRACE : '}';

ASSIGN : ':=';
COMMA : ',';

PLUS : '+';
MINUS : '-';

MULTIPLY : '*';
DIVIDE : '/';
MODULUS : '%';

EQ : '==';
NQ : '!=';

GT : '>';
LT : '<';
GTE : '>=';
LTE : '<=';

LOR : '||';
LAND : '&&';

NUMBER
    : ([1-9] [0-9]*)
    | '0'
    ;

PROCEDURE : 'proc';
WHILE : 'while';
IF : 'if';
THEN : 'then';
ELSE : 'else';
WRITE : 'write';

IDENTIFIER
    : ALPHA_UNDERSCORE (ALPHA_NUM_UNDERSCORE)*
    ;

ALPHA_UNDERSCORE
    : [a-z]
    | [A-Z]
    | '_'
    ;

ALPHA_NUM_UNDERSCORE
    : ALPHA_UNDERSCORE
    | [0-9]
    ;

COMMENT
    : '//' ~[\r\n]* -> skip
    ;

WS
    : (' ' | '\t' | '\r'| '\n') -> skip
    ;