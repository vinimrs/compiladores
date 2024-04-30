package br.ufscar.dc.compiladores.la.sintatico;

import java.io.FileWriter;
import java.io.IOException;
import java.util.BitSet;

import org.antlr.v4.runtime.ANTLRErrorListener; // cuidado para importar a versão 4
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token; // Vamos também precisar de Token
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;

public class MyCustomErrorListener implements ANTLRErrorListener {
  private FileWriter file;
  private boolean errou;

  public MyCustomErrorListener(FileWriter file) {
    this.file = file;
    this.errou = false;
  }

  @Override
  public void reportAmbiguity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, boolean exact,
      BitSet ambigAlts, ATNConfigSet configs) {
  }

  @Override
  public void reportAttemptingFullContext(Parser recognizer, DFA dfa, int startIndex, int stopIndex,
      BitSet conflictingAlts, ATNConfigSet configs) {
  }

  @Override
  public void reportContextSensitivity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, int prediction,
      ATNConfigSet configs) {
  }

  /**
   * Método chamado quando ocorre um erro de sintaxe durante a análise do código.
   *
   * @param recognizer         O reconhecedor responsável pela análise.
   * @param offendingSymbol    O símbolo que causou o erro.
   * @param line               O número da linha onde ocorreu o erro.
   * @param charPositionInLine A posição do caractere na linha onde ocorreu o
   *                           erro.
   * @param msg                A mensagem de erro.
   * @param e                  A exceção que causou o erro.
   */
  @Override
  public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine,
      String msg, RecognitionException e) {
    // Verifica se um erro já foi identificado anteriormente
    if (errou)
      return;

    // Converte o símbolo ofensivo para um Token
    Token token = (Token) offendingSymbol;

    // Formata a mensagem de erro
    String errorMessage = formatErrorMessage(token);

    try {
      // Escreve a mensagem de erro no arquivo de saída
      file.write(errorMessage);

      // Escreve uma mensagem indicando o fim da compilação
      file.write("Fim da compilacao\n");

      // Marca que um erro foi identificado
      errou = true;
    } catch (IOException ex) {
      // TODO: Lidar com a exceção de E/S
    }
  }

  /**
   * Formata a mensagem de erro com base no tipo de token.
   *
   * @param token O token que causou o erro.
   * @return A mensagem de erro formatada.
   */
  private String formatErrorMessage(Token token) {
    // Obtém o nome do token a partir de seu tipo
    String displayName = LALexer.VOCABULARY.getDisplayName(token.getType());

    // Formata a mensagem de erro com base no tipo de token
    switch (displayName) {
      case "ERRO":
        return "Linha " + token.getLine() + ": " + token.getText() + " - simbolo nao identificado\n";
      case "COMENTARIO_NAO_FECHADO":
        return "Linha " + token.getLine() + ": comentario nao fechado\n";
      case "CADEIA_NAO_FECHADA":
        return "Linha " + token.getLine() + ": cadeia literal nao fechada\n";
      case "EOF":
        return "Linha " + token.getLine() + ": erro sintatico proximo a EOF\n";
      default:
        return "Linha " + token.getLine() + ": erro sintatico proximo a " + token.getText() + "\n";
    }
  }

}