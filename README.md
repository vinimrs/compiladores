# Construção de Compiladores - 2024/1 UFSCar

## Requisitos

Para conseguir executar qualquer parte do compilador será necessário:

- Java openjdk version "17.0.10";
- Apache Maven 3.6.3;
- GCC;

Segue as respectivas descrições de cada parte da elaboração do compilador.

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

## T2 (Analisador Sintático)

Seguindo o trabalho T1 foi criado um analisador sintático para a Linguagem Algorítmica (LA), o qual deve analisar se as contruções do programa-fonte estão de acordo com a linguagem. Exemplo:

Entrada:

```
{ leitura de nome e idade com escrita de mensagem usando estes dados }


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
Linha 10: erro sintatico proximo a leia
Fim da compilacao
```

Para compilar e executar o analisador, será necessário navegar até a pasta `t2`, e executar o `script` de execução, no arquivo `run.sh`, por exemplo:

```
cd t2/ && ./run.sh
```

O analisador irá receber o programa de entrada a partir do arquivo `la-sintatico/programa.txt` e gerará a saída em `la-sintatico/saida.txt`.

## T3 (Analisador Semântico)

Seguindo o trabalho T2 foi criado um analisador semântico para a Linguagem Algorítmica (LA), o qual deve analisar se as contruções do programa-fonte estão de acordo com a linguagem. Exemplo:

Entrada:

```
{ leitura de nome e idade com escrita de mensagem usando estes dados }

algoritmo
	declare
		nome: literal
	declare
		idade: inteir

	{ leitura de nome e idade do teclado }
	leia(nome)
	leia(idades)

	{ saída da mensagem na tela }
	escreva(nome, " tem ", idade, " anos.")

fim_algoritmo
```

Saída:

```
Linha 7: tipo inteir nao declarado
Linha 11: identificador idades nao declarado
Fim da compilacao
```

Para compilar e executar o analisador, será necessário navegar até a pasta `t3`, e executar o `script` de execução, no arquivo `run.sh`, por exemplo:

```
cd t3/ && ./run.sh
```

O analisador irá receber o programa de entrada a partir do arquivo `la-semantico/programa.txt` e gerará a saída em `la-semantico/saida.txt`.
