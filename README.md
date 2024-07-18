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

## T4

Como segunda parte do T3, melhoramos o analisador semântico para a Linguagem Algorítmica (LA), o qual deve detectar mais erros:

1. Identificador (variável, constante, procedimento, função, tipo) já declarado anteriormente no escopo, mas agora envolvendo também ponteiros, registros, funções;
2. Identificador (variável, constante, procedimento, função) não declarado, mas agora envolvendo também ponteiros, registros, funções;
3. Incompatibilidade entre argumentos e parâmetros formais (número, ordem e tipo) na chamada de um procedimento ou uma função;
4. Atribuição não compatível com o tipo declarado, agora envolvendo ponteiros e registros;
5. Uso do comando 'retorne' em um escopo não permitido.

Um exemplo de erros agora detectados:

```
{ exemplificacao de sub-rotinas na forma de funcao e seu uso }

funcao menorInteiro(valor1: inteiro, valor2: inteiro): inteiro
{ retorna o menor entre valor1 e valor2; se iguais retorna um deles }

      se valor1 < valor2 entao
           retorne valor1
      senao
           retorne valor2
      fim_se
fim_funcao

funcao menorReal(valor1: real, valor2: real): real
{ retorna o menor entre valor1 e valor2; se iguais, retorna um deles }

      se valor1 < valor2 entao
           retorne valor1
      senao
           retorne valor2
      fim_se
fim_funcao

funcao modulo(valor: real): real
{ retorna o valor absoluto do valor }

      se valor < 0 entao
           valor <- -valor
      fim_se
      retorne valor
fim_funcao

{ parte principal }
algoritmo
      declare
           primeiroInt, segundoInt: inteiro
		declare
           primeiroReal, segundoReal: real

      { entrada de dados }
      leia(primeiroInt, segundoInt, primeiroReal, segundoReal)

      { algumas saidas e manipulacoes }
      escreva("O menor inteiro entre", primeiroInt, "e",
                segundoInt, "eh", menorInteiro(primeiroInt))

      se menorReal(primeiroReal, segundoInt) <> primeiroReal entao
           escreva(segundoReal, "eh menor que", primeiroReal)
      fim_se

      se modulo(primeiroReal) = primeiroReal e primeiroReal <> 0 entao
           escreva("O valor", primeiroReal, "eh positivo")
      senao
           escreva("O valor", primeiroReal, "nao eh positivo")
      fim_se

      escreva("Considerando-se o modulo, tem-se que o menor entre",
                primeiroReal, "e", segundoReal, "eh",
                menorReal(modulo(primeiroReal), modulo(segundoReal)))
fim_algoritmo
```

Saída:

```
Linha 44: incompatibilidade de parametros na chamada de menorInteiro
Linha 46: incompatibilidade de parametros na chamada de menorReal
Fim da compilacao
```

Para compilar e executar o analisador, será necessário navegar até a pasta `t4`, e executar o `script` de execução, no arquivo `run.sh`, por exemplo:

```
cd t4/ && ./run.sh
```

O analisador irá receber o programa de entrada a partir do arquivo `la-full-semantico/programa.txt` e gerará a saída em `la-full-semantico/saida.txt`.
