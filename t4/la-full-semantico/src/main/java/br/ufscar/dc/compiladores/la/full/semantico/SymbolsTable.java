package br.ufscar.dc.compiladores.la.full.semantico;

import java.util.ArrayList;
import java.util.HashMap;

// Classe que representa uma tabela de símbolos
public class SymbolsTable {
  public SymbolsTable.Types returnType;

  public enum Types {
    INT, REAL, CADEIA, LOGICO, INVALIDO, REG, VOID
  }

  public enum Structure {
    VAR, CONST, PROC, FUNC, TIPO
  }

  class SymbolsTableEntry {
    String name;
    Types tipo;
    Structure structure;

    public SymbolsTableEntry(String name, Types tipo, Structure structure) {
      this.name = name;
      this.tipo = tipo;
      this.structure = structure;
    }
  }

  private HashMap<String, SymbolsTableEntry> Mtabela;
  private HashMap<String, ArrayList<SymbolsTableEntry>> Ttabela;

  public SymbolsTable(SymbolsTable.Types returnType) {
    Mtabela = new HashMap<>();
    Ttabela = new HashMap<>();
    this.returnType = returnType;
  }

  public void add(String name, Types tipo, Structure structure) {
    SymbolsTableEntry input = new SymbolsTableEntry(name, tipo, structure);
    Mtabela.put(name, input);
  }

  public void add(SymbolsTableEntry input) {
    Mtabela.put(input.name, input);

  }

  public void add(String tipoName, SymbolsTableEntry input) {
    if (Ttabela.containsKey(tipoName)) {
      Ttabela.get(tipoName).add(input);
    } else {
      ArrayList<SymbolsTableEntry> list = new ArrayList<>();
      list.add(input);
      Ttabela.put(tipoName, list);
    }
  }

  public Types verify(String name) {
    if (Mtabela.containsKey(name))
      return Mtabela.get(name).tipo;
    else
      return null;
  }

  public Structure getStructure(String name) {
    return Mtabela.get(name).structure;
  }

  public boolean exists(String name) {
    return Mtabela.containsKey(name);
  }

  public ArrayList<SymbolsTableEntry> getTypeProperties(String name) {
    return Ttabela.get(name);
  }
}