package br.ufscar.dc.compiladores.la.full.semantico;

import java.util.LinkedList;
import java.util.List;

// Classe que representa um escopo de símbolos e seus métodos
public class Scope {

  private LinkedList<SymbolsTable> pilha;

  public Scope(SymbolsTable.Types returnType) {
    pilha = new LinkedList<>();
    createScope(returnType);
  }

  public void createScope(SymbolsTable.Types returnType) {
    pilha.push(new SymbolsTable(returnType));
  }

  public SymbolsTable getScope() {
    return pilha.peek();
  }

  public void dropScope() {
    pilha.pop();
  }

  public List<SymbolsTable> getStack() {
    return pilha;
  }

}