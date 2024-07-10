package br.ufscar.dc.compiladores.la.semantico;

import java.util.HashMap;
import java.util.Map;

// Classe que representa uma tabela de símbolos
public class SymbolsTable {
  public enum Tipos {
    INT, REAL, CADEIA, LOGICO, INVALIDO, TIPO, IDENT
  }

  class SymbolsTableEntry {
    String nome;
    Tipos tipo;

    private SymbolsTableEntry(String nome, Tipos tipo) {
      this.nome = nome;
      this.tipo = tipo;
    }
  }

  private final Map<String, SymbolsTableEntry> tabela;

  public SymbolsTable() {
    this.tabela = new HashMap<>();
  }

  public void add(String nome, Tipos tipo) {
    tabela.put(nome, new SymbolsTableEntry(nome, tipo));
  }

  public boolean exists(String nome) {
    return tabela.containsKey(nome);
  }

  public Tipos verify(String nome) {
    return tabela.get(nome).tipo;
  }
}