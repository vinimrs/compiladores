package br.ufscar.dc.compiladores.la.sintatico;

import java.io.FileWriter;
import java.io.IOException;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

public class Principal {

  public static void main(String[] args) {

    try {
      // Cria um fluxo de caracteres a partir do arquivo de origem (args[0])
      CharStream input = CharStreams.fromFileName(args[0]);

      // Inicializa o lexer com o fluxo de caracteres
      LALexer lexer = new LALexer(input);

      // Cria um fluxo de tokens a partir do lexer
      CommonTokenStream tokens = new CommonTokenStream(lexer);

      // Inicializa o parser com o fluxo de tokens
      LAParser parser = new LAParser(tokens);

      // Abre o arquivo de destino para escrita (args[1])
      FileWriter writer = new FileWriter(args[1]);

      // Configura um listener personalizado para lidar com erros de análise
      MyCustomErrorListener errorListener = new MyCustomErrorListener(writer);
      parser.removeErrorListeners();
      parser.addErrorListener(errorListener);

      // Inicia o processo de análise
      parser.programa();

      // Fecha o arquivo de destino
      writer.close();
    } catch (IOException ex) {
      // TODO: handle exception
    }
  }
}