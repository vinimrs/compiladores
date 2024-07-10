package br.ufscar.dc.compiladores.la.semantico;

import java.util.LinkedList;
import java.util.List;

// Classe que representa um escopo de símbolos e seus métodos
public class Scope {

  private LinkedList<SymbolsTable> pilha;

  public Scope() {
    pilha = new LinkedList<>();
    criaEscopo();
  }

  public void criaEscopo() {
    pilha.push(new SymbolsTable());
  }

  public SymbolsTable getEscopo() {
    return pilha.peek();
  }

  public void dropEscopo() {
    pilha.pop();
  }

  public List<SymbolsTable> getPilha() {
    return pilha;
  }

}