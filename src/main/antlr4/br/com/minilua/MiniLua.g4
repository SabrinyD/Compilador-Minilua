grammar MiniLua;

// --- PARSER (Sintático) ---

//Pega os tokens identificados e verifica se faz sentido
programa : comandos EOF;

comandos : comando*;

comando : tipo IDENTIFICADOR ';'                        # CmdDecl
        | IDENTIFICADOR '=' expr ';'                    # CmdAtrib
        | IF expr THEN comandos (ELSE comandos)? END    # CmdIf
        | PRINT '(' expr ')' ';'                        # CmdPrint
        ;

expr : '(' expr ')'                                  # ExprParenteses
     | '-' expr                                      # ExprMenosUnario
     | expr op=('*'|'/') expr                        # ExprAritmetica
     | expr op=('+'|'-') expr                        # ExprAritmetica
     | expr op=('>'|'<'|'>='|'<='|'=='|'!=') expr    # ExprRelacional
     | IDENTIFICADOR                                 # ExprId
     | literal                                       # ExprLiteral
     ;

tipo : 'int' | 'float' | 'string';

literal : NUM_INT | NUM_FLOAT | STRING_LIT;

// --- LEXER (Léxico) ---

INT     : 'int';
FLOAT   : 'float';
STRING  : 'string';
IF      : 'if';
THEN    : 'then';
ELSE    : 'else';
END     : 'end';
PRINT   : 'print';

IDENTIFICADOR : [a-zA-Z_][a-zA-Z0-9_]*;
NUM_INT       : [0-9]+;
NUM_FLOAT     : [0-9]+ '.' [0-9]+;
STRING_LIT    : '"' .*? '"';

WS          : [ \t\r\n]+ -> skip;
COMENTARIO  : '--' ~[\r\n]* -> skip;