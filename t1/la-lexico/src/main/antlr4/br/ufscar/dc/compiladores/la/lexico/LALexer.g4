lexer grammar LALexer;

ALGORITMO : 'algoritmo';
DECLARE : 'declare';
REAL: 'real';
LITERAL : 'literal';
INTEIRO : 'inteiro';
LEIA : 'leia';
ESCREVA : 'escreva';
FIM_ALGORITMO : 'fim_algoritmo';

TIPO: 'tipo';
VAR: 'var';
CONSTANTE: 'constante';
REGISTRO: 'registro';
FIM_REGISTRO: 'fim_registro';

FALSO: 'falso';
VERDADEIRO: 'verdadeiro';

OU: 'ou';
E: 'e';
NAO: 'nao';
LOGICO: 'logico';

// Loops e condicionais
SE: 'se';
FIM_SE: 'fim_se';
SENAO: 'senao';
ENQUANTO: 'enquanto';
FIM_ENQUANTO: 'fim_enquanto';
PARA: 'para';
FIM_PARA: 'fim_para';
ENTAO: 'entao';
FACA: 'faca';
ATE: 'ate';
PROCEDIMENTO: 'procedimento';
FIM_PROCEDIMENTO: 'fim_procedimento';
FUNCAO: 'funcao';
FIM_FUNCAO: 'fim_funcao';
RETORNE: 'retorne';
CASO: 'caso';
FIM_CASO: 'fim_caso';
SEJA: 'seja';

PONTO_PONTO: '..';
PONTO: '.';

CADEIA_NAO_FECHADA : '"' ( ~["\r\n] | '""' )*;
CADEIA : '"' ( ~["\r\n] | '""' )* '"';
WS : [ \t\r\n]+ -> skip;
COMENTARIO : '{' ~('\n'|'\r'|'}')* '}' -> skip;
COMENTARIO_NAO_FECHADO : '{' ~('}')*;


DOIS_PONTOS: ':';
ABREPAR: '(';
FECHAPAR: ')';
VIRGULA: ',';
ABRECOL: '[';
FECHACOL: ']';

DIFERENTE: '<>';
MAIOR_IGUAL: '>=';
MENOR_IGUAL: '<=';
ATRIBUICAO: '<-';
IGUAL: '=';
MAIS: '+';
MENOS: '-';
MULTIPLICACAO: '*';
DIVISAO: '/';
MAIOR: '>';
MENOR: '<';
MOD: '%';
PONTEIRO: '^';
ENDERECO: '&';

NUM_INT	: ('0'..'9')+;
NUM_REAL : ('0'..'9')+ ('.' ('0'..'9')+)?;

IDENT : [a-zA-Z_][a-zA-Z_0-9]*;



ERR : . ;
