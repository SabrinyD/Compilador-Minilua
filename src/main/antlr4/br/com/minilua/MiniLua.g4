grammar MiniLua;

// === ESTRUTURA PRINCIPAL ===
programa
    : comando* EOF
    ;

comando
    : declaracao ';'        # CmdDeclaracao
    | atribuicao ';'        # CmdAtribuicao
    | io_leitura ';'        # CmdLeitura
    | io_escrita ';'        # CmdEscrita
    | if_stmt               # CmdIf
    | while_stmt            # CmdWhile
    ;

// === DECLARAÇÕES ===
declaracao
    : tipo IDENTIFICADOR
    ;

tipo
    : 'int' | 'float' | 'string'
    ;

// === COMANDOS (ATRIBUIÇÃO E I/O) ===
atribuicao
    : IDENTIFICADOR '=' expressao
    ;

io_leitura
    : 'read' '(' IDENTIFICADOR ')'
    ;

io_escrita
    : 'print' '(' expressao ')'
    ;

// === CONTROLE DE FLUXO ===
if_stmt
    : 'if' expressao 'then' bloco ('else' bloco)? 'end'
    ;

while_stmt
    : 'while' expressao 'do' bloco 'end'
    ;

bloco
    : comando*
    ;

// === EXPRESSÕES (Precedência) ===
expressao
    : termo_logico ('or' termo_logico)*
    ;

termo_logico
    : expressao_relacional ('and' expressao_relacional)*
    ;

expressao_relacional
    : expressao_aditiva (op_relacional expressao_aditiva)?
    ;

op_relacional
    : '==' | '!=' | '<' | '>' | '<=' | '>='
    ;

expressao_aditiva
    : termo_multiplicativo (('+' | '-') termo_multiplicativo)*
    ;

termo_multiplicativo
    : fator (('*' | '/') fator)*
    ;

fator
   : '(' expressao ')'
    | '!' fator           // Negação
    | '-' fator           // Unário
    | IDENTIFICADOR
    | NUM_INT
    | NUM_FLOAT
    | STRING_LIT
    ;

// === LÉXICO ===
IF:'if'; THEN:'then'; ELSE:'else'; END:'end';
WHILE:'while'; DO:'do'; READ:'read'; PRINT:'print';
INT:'int'; FLOAT:'float'; STRING:'string';

IDENTIFICADOR : [a-zA-Z_] [a-zA-Z0-9_]* ;
NUM_INT       : [0-9]+ ;
NUM_FLOAT     : [0-9]+ '.' [0-9]+ ;
STRING_LIT    : '"' .*? '"' ;

COMENTARIO    : '--' .*? '\r'? '\n' -> skip ;
WS            : [ \t\r\n]+ -> skip ;
