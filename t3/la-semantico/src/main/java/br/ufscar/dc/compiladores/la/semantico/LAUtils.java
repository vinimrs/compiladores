package br.ufscar.dc.compiladores.la.semantico;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.Token;

import br.ufscar.dc.compiladores.la.semantico.LAParser;
import br.ufscar.dc.compiladores.la.semantico.LAParser.Exp_aritmeticaContext;
import br.ufscar.dc.compiladores.la.semantico.LAParser.ExpressaoContext;
import br.ufscar.dc.compiladores.la.semantico.LAParser.FatorContext;
import br.ufscar.dc.compiladores.la.semantico.LAParser.Fator_logicoContext;
import br.ufscar.dc.compiladores.la.semantico.LAParser.ParcelaContext;
import br.ufscar.dc.compiladores.la.semantico.LAParser.TermoContext;
import br.ufscar.dc.compiladores.la.semantico.LAParser.Termo_logicoContext;
import java.util.Iterator;

public class LAUtils {
  public static List<String> errosSemanticos = new ArrayList<>();

  // Adiciona um erro semântico à lista e informa a linha que foi encontrado, dado
  // um token e a mensagem como parâmetros
  public static void addSemanticError(Token t, String mensagem) {
    int linha = t.getLine();
    errosSemanticos.add(String.format("Linha %d: %s", linha, mensagem));
  }

  // Verifica o tipo de um símbolo presente em uma SymbolsTable de símbolos
  // considerando uma expressão
  public static SymbolsTable.Tipos verificarTipo(Scope escopos, LAParser.ExpressaoContext ctx) {
    SymbolsTable.Tipos ret = null;
    Iterator<Termo_logicoContext> iterator = ctx.termo_logico().iterator();
    while (iterator.hasNext()) {
      Termo_logicoContext ta = iterator.next();
      SymbolsTable.Tipos aux = verificarTipo(escopos, ta);
      if (ret == null) {
        ret = aux;
      } else if (ret != aux && aux != SymbolsTable.Tipos.INVALIDO) {
        ret = SymbolsTable.Tipos.INVALIDO;
      }
    }
    return ret;
  }

  // Verifica o tipo de um símbolo presente em uma SymbolsTable de símbolos
  // considerando um termo lógico
  public static SymbolsTable.Tipos verificarTipo(Scope escopos, LAParser.Termo_logicoContext ctx) {
    SymbolsTable.Tipos ret = null;
    Iterator<Fator_logicoContext> iterator = ctx.fator_logico().iterator();
    while (iterator.hasNext()) {
      Fator_logicoContext ta = iterator.next();
      SymbolsTable.Tipos aux = verificarTipo(escopos, ta);
      if (ret == null) {
        ret = aux;
      } else if (ret != aux && aux != SymbolsTable.Tipos.INVALIDO) {
        ret = SymbolsTable.Tipos.INVALIDO;
      }
    }
    return ret;
  }

  // Verifica o tipo de um símbolo presente em uma SymbolsTable de símbolos
  // considerando um fator lógico
  public static SymbolsTable.Tipos verificarTipo(Scope escopos, LAParser.Fator_logicoContext ctx) {
    return verificarTipo(escopos, ctx.parcela_logica());
  }

  // Verifica o tipo de um símbolo presente em uma SymbolsTable de símbolos
  // considerando uma parcela lógica
  public static SymbolsTable.Tipos verificarTipo(Scope escopos, LAParser.Parcela_logicaContext ctx) {
    SymbolsTable.Tipos ret = null;
    if (ctx.exp_relacional() != null) {
      ret = verificarTipo(escopos, ctx.exp_relacional());
    } else {
      ret = SymbolsTable.Tipos.LOGICO;
    }
    return ret;
  }

  // Verifica o tipo de um símbolo presente em uma SymbolsTable de símbolos
  // considerando uma expressão relacional
  public static SymbolsTable.Tipos verificarTipo(Scope escopos, LAParser.Exp_relacionalContext ctx) {
    SymbolsTable.Tipos ret = null;
    if (ctx.op_relacional() != null) {
      Iterator<Exp_aritmeticaContext> iterator = ctx.exp_aritmetica().iterator();
      while (iterator.hasNext()) {
        Exp_aritmeticaContext ta = iterator.next();
        SymbolsTable.Tipos aux = verificarTipo(escopos, ta);
        Boolean auxNumeric = aux == SymbolsTable.Tipos.REAL || aux == SymbolsTable.Tipos.INT;
        Boolean retNumeric = ret == SymbolsTable.Tipos.REAL || ret == SymbolsTable.Tipos.INT;
        if (ret == null) {
          ret = aux;
        } else if (!(auxNumeric && retNumeric) && aux != ret) {
          ret = SymbolsTable.Tipos.INVALIDO;
        }
      }

      if (ret != SymbolsTable.Tipos.INVALIDO) {
        ret = SymbolsTable.Tipos.LOGICO;
      }
    } else {
      ret = verificarTipo(escopos, ctx.exp_aritmetica(0));
    }
    return ret;
  }

  // Verifica o tipo de um símbolo presente em uma SymbolsTable de símbolos
  // considerando uma expressão aritmética.
  public static SymbolsTable.Tipos verificarTipo(Scope escopos, LAParser.Exp_aritmeticaContext ctx) {
    SymbolsTable.Tipos ret = null;
    Iterator<TermoContext> iterator = ctx.termo().iterator();
    while (iterator.hasNext()) {
      TermoContext ta = iterator.next();
      SymbolsTable.Tipos aux = verificarTipo(escopos, ta);
      if (ret == null) {
        ret = aux;
      } else if (ret != aux && aux != SymbolsTable.Tipos.INVALIDO) {
        ret = SymbolsTable.Tipos.INVALIDO;
      }
    }
    return ret;
  }

  // Verifica o tipo de um símbolo presente em uma SymbolsTable de símbolos
  // considerando um termo.
  public static SymbolsTable.Tipos verificarTipo(Scope escopos, LAParser.TermoContext ctx) {
    SymbolsTable.Tipos ret = null;

    Iterator<FatorContext> iterator = ctx.fator().iterator();
    while (iterator.hasNext()) {
      FatorContext fa = iterator.next();
      SymbolsTable.Tipos aux = verificarTipo(escopos, fa);
      Boolean auxNumeric = aux == SymbolsTable.Tipos.REAL || aux == SymbolsTable.Tipos.INT;
      Boolean retNumeric = ret == SymbolsTable.Tipos.REAL || ret == SymbolsTable.Tipos.INT;
      if (ret == null) {
        ret = aux;
      } else if (!(auxNumeric && retNumeric) && aux != ret) {
        ret = SymbolsTable.Tipos.INVALIDO;
      }
    }
    return ret;
  }

  // Verifica o tipo de um símbolo presente em uma SymbolsTable de símbolos
  // considerando um fator.
  public static SymbolsTable.Tipos verificarTipo(Scope escopos, LAParser.FatorContext ctx) {
    SymbolsTable.Tipos ret = null;

    Iterator<ParcelaContext> iterator = ctx.parcela().iterator();
    while (iterator.hasNext()) {
      ParcelaContext fa = iterator.next();
      SymbolsTable.Tipos aux = verificarTipo(escopos, fa);
      if (ret == null) {
        ret = aux;
      } else if (ret != aux && aux != SymbolsTable.Tipos.INVALIDO) {
        ret = SymbolsTable.Tipos.INVALIDO;
      }
    }
    return ret;
  }

  // Verifica o tipo de um símbolo presente em uma SymbolsTable de símbolos
  // considerando uma parcela.
  public static SymbolsTable.Tipos verificarTipo(Scope escopos, LAParser.ParcelaContext ctx) {
    SymbolsTable.Tipos ret = SymbolsTable.Tipos.INVALIDO;

    if (ctx.parcela_nao_unario() != null) {
      ret = verificarTipo(escopos, ctx.parcela_nao_unario());
    } else {
      ret = verificarTipo(escopos, ctx.parcela_unario());
    }
    return ret;
  }

  // Verifica o tipo de um símbolo presente em uma SymbolsTable de símbolos
  // considerando uma parcela não unária.
  public static SymbolsTable.Tipos verificarTipo(Scope escopos, LAParser.Parcela_nao_unarioContext ctx) {
    if (ctx.identificador() != null) {
      return verificarTipo(escopos, ctx.identificador());
    }
    return SymbolsTable.Tipos.CADEIA;
  }

  // Verifica o tipo de um símbolo presente em uma SymbolsTable de símbolos
  // considerando um identificador.
  public static SymbolsTable.Tipos verificarTipo(Scope escopos, LAParser.IdentificadorContext ctx) {
    String nomeVar = "";
    SymbolsTable.Tipos ret = SymbolsTable.Tipos.INVALIDO;
    for (int i = 0; i < ctx.IDENT().size(); i++) {
      nomeVar += ctx.IDENT(i).getText();
      if (i != ctx.IDENT().size() - 1) {
        nomeVar += ".";
      }
    }
    Iterator<SymbolsTable> iterator = escopos.getPilha().iterator();
    while (iterator.hasNext()) {
      SymbolsTable SymbolsTable = iterator.next();
      if (SymbolsTable.exists(nomeVar)) {
        ret = verificarTipo(escopos, nomeVar);
      }
    }
    System.out.println(nomeVar);
    return ret;
  }

  // Verifica o tipo de um símbolo presente em uma SymbolsTable de símbolos
  // considerando uma parcela unária.
  public static SymbolsTable.Tipos verificarTipo(Scope escopos, LAParser.Parcela_unarioContext ctx) {
    if (ctx.NUM_INT() != null) {
      return SymbolsTable.Tipos.INT;
    }
    if (ctx.NUM_REAL() != null) {
      return SymbolsTable.Tipos.REAL;
    }
    if (ctx.identificador() != null) {
      return verificarTipo(escopos, ctx.identificador());
    }
    if (ctx.IDENT() != null) {
      SymbolsTable.Tipos ret = null;
      ret = verificarTipo(escopos, ctx.IDENT().getText());
      Iterator<ExpressaoContext> iterator = ctx.expressao().iterator();
      while (iterator.hasNext()) {
        ExpressaoContext fa = iterator.next();
        SymbolsTable.Tipos aux = verificarTipo(escopos, fa);
        if (ret == null) {
          ret = aux;
        } else if (ret != aux && aux != SymbolsTable.Tipos.INVALIDO) {
          ret = SymbolsTable.Tipos.INVALIDO;
        }
      }
      return ret;
    } else {
      SymbolsTable.Tipos ret = null;
      Iterator<ExpressaoContext> iterator = ctx.expressao().iterator();
      while (iterator.hasNext()) {
        ExpressaoContext fa = iterator.next();
        SymbolsTable.Tipos aux = verificarTipo(escopos, fa);
        if (ret == null) {
          ret = aux;
        } else if (ret != aux && aux != SymbolsTable.Tipos.INVALIDO) {
          ret = SymbolsTable.Tipos.INVALIDO;
        }
      }
      return ret;
    }
  }

  // Verifica o tipo de um símbolo presente em uma SymbolsTable de símbolos
  // considerando uma string.
  public static SymbolsTable.Tipos verificarTipo(Scope escopos, String nomeVar) {
    SymbolsTable.Tipos type = null;
    Iterator<SymbolsTable> iterator = escopos.getPilha().iterator();
    while (iterator.hasNext()) {
      SymbolsTable SymbolsTable = iterator.next();
      type = SymbolsTable.verify(nomeVar);
    }
    return type;
  }
}