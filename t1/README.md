
## T1 (Analisador Léxico)

Utilizando a biblioteca ANTLR, foi criado um analisador léxico para a Linguagem Algorítmica (LA), o qual deve gerar uma lista de tokens identificados. Exemplo:

Entrada:
```
{ leitura de nome e idade com escrita de mensagem usando estes dados }
algoritmo
	declare
		nome: literal
	declare
		idade: inteiro

	{ leitura de nome e idade do teclado }
	leia(nome)
	leia(idade)

	{ saída da mensagem na tela }
	escreva(nome, " tem ", idade, " anos.")
fim_algoritmo
```

Saída:
```
<'algoritmo','algoritmo'>
<'declare','declare'>
<'nome',IDENT>
<':',':'>
<'literal','literal'>
<'declare','declare'>
<'idade',IDENT>
<':',':'>
<'inteiro','inteiro'>
<'leia','leia'>
<'(','('>
<'nome',IDENT>
<')',')'>
<'leia','leia'>
<'(','('>
<'idade',IDENT>
<')',')'>
<'escreva','escreva'>
<'(','('>
<'nome',IDENT>
<',',','>
<'" tem "',CADEIA>
<',',','>
<'idade',IDENT>
<',',','>
<'" anos."',CADEIA>
<')',')'>
<'fim_algoritmo','fim_algoritmo'>
```

Para compilar e executar o analisador, será necessário navegar até a pasta `t1`, e executar o `script` de execução, no arquivo `run.sh`, por exemplo:

```
cd t1/ && ./run.sh
```

O programa irá receber o programa de entrada a partir do arquivo `la-lexico/programa.txt` e gerará a saída em `la-lexico/saida.txt`.


Feito por:

Eduardo Henrique Spinelli 800220  

Matheus Bessa 801839

Vinicius Matheus Romualdo 801258 https://github.com/vinimrs
