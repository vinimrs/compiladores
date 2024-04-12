package br.ufscar.dc.compiladores.la.lexico;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;

public class Principal {

    public static void main(String[] args) {
        // Verifica se os argumentos necessários foram fornecidos
        if (args.length < 2) {
            System.out.println("Forneca o arquivo de entrada e o arquivo de saída.");
            return;
        }

        // Escreve os tokens no arquivo de saída
        try {
            StringBuilder output = new StringBuilder();
            CharStream cs = CharStreams.fromFileName(args[0]);
            LALexer lex = new LALexer(cs);

            Token t = null;
            while ((t = lex.nextToken()).getType() != Token.EOF) {
                String tokenStr = "<" + "'" + t.getText() + "'" + "," + LALexer.VOCABULARY.getDisplayName(t.getType()) + ">";

                // Se o token for um erro, para a execução
                if (t.getType() == LALexer.ERR) {
                    tokenStr = "Linha " + t.getLine() + ": " + t.getText() + " - simbolo nao identificado";
                    output.append(tokenStr).append(System.lineSeparator());
                    break;
                }

                // Se o comentario nao tiver fechado gera erro de comentário não fechado
                if (t.getType() == LALexer.COMENTARIO_NAO_FECHADO) {
                    tokenStr = "Linha " + t.getLine() + ": comentario nao fechado";
                    output.append(tokenStr).append(System.lineSeparator());
                    break;
                }

                // Se a string nao tiver fechada gera erro de string nao fechada
                if (t.getType() == LALexer.CADEIA_NAO_FECHADA) {
                    tokenStr = "Linha " + t.getLine() + ": cadeia literal nao fechada";
                    output.append(tokenStr).append(System.lineSeparator());
                    break;
                }

                output.append(tokenStr).append(System.lineSeparator());
            }

            Files.write(Paths.get(args[1]), output.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException ex) {
            System.err.println("Erro ao abrir os arquivos: " + ex.getMessage());
        }
    }
}