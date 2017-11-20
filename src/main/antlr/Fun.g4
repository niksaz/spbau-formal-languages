grammar Fun;

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
    : assignment
    | writeCall
    | procedureCall
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
    : multiplicativeExpression
    ;

multiplicativeExpression
    : additiveExpression (op = (MULTIPLY | DIVIDE | MODULUS) multiplicativeExpression)*
    ;

additiveExpression
    : relationalExpression (op = (PLUS | MINUS) additiveExpression)*
    ;

relationalExpression
    : equivalenceExpression (op = (GT | LT | GTE | LTE) relationalExpression)*
    ;

equivalenceExpression
    : landExpression (op = (EQ | NQ) equivalenceExpression)*
    ;

landExpression
    : lorExpression (op = LAND landExpression)*
    ;

lorExpression
    : atomicExpression (op = LOR lorExpression)*
    ;

atomicExpression
    : IDENTIFIER
    | NUMBER
    | L_BRACE expression R_BRACE
    ;

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