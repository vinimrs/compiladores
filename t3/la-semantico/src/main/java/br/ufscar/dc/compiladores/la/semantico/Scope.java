package br.ufscar.dc.compiladores.la.semantico;

import java.util.LinkedList;
import java.util.List;

public class Scope {

  private LinkedList<SymbolsTable> pilha;

  public Scope() {
    pilha = new LinkedList<>();
    criaEscopo();
  }

  // Cria um novo escopo
  public void criaEscopo() {
    pilha.push(new SymbolsTable());
  }

  // Pega o escopo em que se esta atualmente
  public SymbolsTable getEscopo() {
    return pilha.peek();
  }

  // Tira da pilha o escopo em que se esta
  public void dropEscopo() {
    pilha.pop();
  }

  // Pega a pilha com todas as SymbolsTables
  public List<SymbolsTable> getPilha() {
    return pilha;
  }

}