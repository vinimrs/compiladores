package br.ufscar.dc.compiladores.la.semantico;

import java.util.HashMap;
import java.util.Map;

public class SymbolsTable {
  public enum Tipos {
    INT, REAL, CADEIA, LOGICO, INVALIDO, TIPO, IDENT
  }

  class EntradaSymbolsTable {
    String nome;
    Tipos tipo;

    private EntradaSymbolsTable(String nome, Tipos tipo) {
      this.nome = nome;
      this.tipo = tipo;
    }
  }

  private final Map<String, EntradaSymbolsTable> tabela;

  public SymbolsTable() {
    this.tabela = new HashMap<>();
  }

  public void adicionar(String nome, Tipos tipo) {
    tabela.put(nome, new EntradaSymbolsTable(nome, tipo));
  }

  public boolean existe(String nome) {
    return tabela.containsKey(nome);
  }

  public Tipos verificar(String nome) {
    return tabela.get(nome).tipo;
  }
}