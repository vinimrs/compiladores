lexer grammar LALexer;

ALGORITMO : 'algoritmo';
DECLARE : 'declare';
LITERAL : 'literal';
INTEIRO : 'inteiro';
REAL: 'real';
LEIA : 'leia';
ESCREVA : 'escreva';
LOGICO: 'logico';
E: 'e';
OU: 'ou';
NAO: 'nao';
SE: 'se';
SENAO: 'senao';
FIM_SE:'fim_se';
ENQUANTO: 'enquanto';
ENTAO: 'entao';

REGISTRO:'registro';
CONSTANTE: 'constante';
VAR: 'var';
FIM_REGISTRO: 'fim_registro';
PROCEDIMENTO: 'procedimento';
FIM_PROCEDIMENTO: 'fim_procedimento';
FUNCAO: 'funcao';
RETORNE: 'retorne';
FIM_FUNCAO: 'fim_funcao';
PARA: 'para';
ATE: 'ate';
FACA: 'faca';
FIM_PARA: 'fim_para';



FIM_ALGORITMO : 'fim_algoritmo';
IDENT : [a-zA-Z_][a-zA-Z_0-9]*;
CADEIA : '"' ( ~["\r\n] | '""' )* '"';
WS : [ \t\r\n]+ -> skip;
COMMENT : '{' .*? '}' -> skip;
DOIS_PONTOS: ':';
NUM_INT	: ('+'|'-')?('0'..'9')+;
NUM_REAL	: ('+'|'-')?('0'..'9')+ ('.' ('0'..'9')+)?;
TIPO: 'tipo';
INSTANTE: 'instante';

ABREPAR: '(';
FECHAPAR: ')';
VIRGULA: ',';
ABRE_CHAVE: '[';
FECHA_CHAVE: ']';

BARRA: '/';
ADICAO: '+';
MULTIPLICACAO: '*';
SUBTRACAO: '-';
IGUAL: '=';

SETA: '<-';
MENOR_QUE: '<';
MENOR_IGUAL: '<=';
MAIOR_QUE: '>';
MAIOR_IGUAL: '>=';

// Lexer error handling
ERR : . ;
