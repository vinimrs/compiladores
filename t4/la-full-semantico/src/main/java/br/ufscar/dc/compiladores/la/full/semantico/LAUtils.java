package br.ufscar.dc.compiladores.la.full.semantico;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.Token;
import br.ufscar.dc.compiladores.la.full.semantico.LAParser.Exp_aritmeticaContext;
import br.ufscar.dc.compiladores.la.full.semantico.LAParser.ExpressaoContext;
import br.ufscar.dc.compiladores.la.full.semantico.LAParser.FatorContext;
import br.ufscar.dc.compiladores.la.full.semantico.LAParser.Fator_logicoContext;
import br.ufscar.dc.compiladores.la.full.semantico.LAParser.ParcelaContext;
import br.ufscar.dc.compiladores.la.full.semantico.LAParser.TermoContext;
import br.ufscar.dc.compiladores.la.full.semantico.LAParser.Termo_logicoContext;
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
  public static SymbolsTable.Types verifyType(Scope escopos, LAParser.ExpressaoContext ctx) {
    SymbolsTable.Types ret = null;
    Iterator<Termo_logicoContext> iterator = ctx.termo_logico().iterator();
    while (iterator.hasNext()) {
      Termo_logicoContext ta = iterator.next();
      SymbolsTable.Types aux = verifyType(escopos, ta);
      if (ret == null) {
        ret = aux;
      } else if (ret != aux && aux != SymbolsTable.Types.INVALIDO) {
        ret = SymbolsTable.Types.INVALIDO;
      }
    }
    return ret;
  }

  // Verifica o tipo de um símbolo presente em uma SymbolsTable de símbolos
  // considerando um termo lógico
  public static SymbolsTable.Types verifyType(Scope escopos, LAParser.Termo_logicoContext ctx) {
    SymbolsTable.Types ret = null;
    Iterator<Fator_logicoContext> iterator = ctx.fator_logico().iterator();
    while (iterator.hasNext()) {
      Fator_logicoContext ta = iterator.next();
      SymbolsTable.Types aux = verifyType(escopos, ta);
      if (ret == null) {
        ret = aux;
      } else if (ret != aux && aux != SymbolsTable.Types.INVALIDO) {
        ret = SymbolsTable.Types.INVALIDO;
      }
    }
    return ret;
  }

  // Verifica o tipo de um símbolo presente em uma SymbolsTable de símbolos
  // considerando um fator lógico
  public static SymbolsTable.Types verifyType(Scope escopos, LAParser.Fator_logicoContext ctx) {
    return verifyType(escopos, ctx.parcela_logica());
  }

  // Verifica o tipo de um símbolo presente em uma SymbolsTable de símbolos
  // considerando uma parcela lógica
  public static SymbolsTable.Types verifyType(Scope escopos, LAParser.Parcela_logicaContext ctx) {
    SymbolsTable.Types ret = null;
    if (ctx.exp_relacional() != null) {
      ret = verifyType(escopos, ctx.exp_relacional());
    } else {
      ret = SymbolsTable.Types.LOGICO;
    }
    return ret;
  }

  // Verifica o tipo de um símbolo presente em uma SymbolsTable de símbolos
  // considerando uma expressão relacional
  public static SymbolsTable.Types verifyType(Scope escopos, LAParser.Exp_relacionalContext ctx) {
    SymbolsTable.Types ret = null;
    if (ctx.op_relacional() != null) {
      Iterator<Exp_aritmeticaContext> iterator = ctx.exp_aritmetica().iterator();
      while (iterator.hasNext()) {
        Exp_aritmeticaContext ta = iterator.next();
        SymbolsTable.Types aux = verifyType(escopos, ta);
        Boolean auxNumeric = aux == SymbolsTable.Types.REAL || aux == SymbolsTable.Types.INT;
        Boolean retNumeric = ret == SymbolsTable.Types.REAL || ret == SymbolsTable.Types.INT;
        if (ret == null) {
          ret = aux;
        } else if (!(auxNumeric && retNumeric) && aux != ret) {
          ret = SymbolsTable.Types.INVALIDO;
        }
      }

      if (ret != SymbolsTable.Types.INVALIDO) {
        ret = SymbolsTable.Types.LOGICO;
      }

    } else {
      ret = verifyType(escopos, ctx.exp_aritmetica(0));
    }
    return ret;
  }

  // Verifica o tipo de um símbolo presente em uma SymbolsTable de símbolos
  // considerando uma expressão aritmética.
  public static SymbolsTable.Types verifyType(Scope escopos, LAParser.Exp_aritmeticaContext ctx) {
    SymbolsTable.Types ret = null;
    Iterator<TermoContext> iterator = ctx.termo().iterator();
    while (iterator.hasNext()) {
      TermoContext ta = iterator.next();
      SymbolsTable.Types aux = verifyType(escopos, ta);
      if (ret == null) {
        ret = aux;
      } else if (ret != aux && aux != SymbolsTable.Types.INVALIDO) {
        ret = SymbolsTable.Types.INVALIDO;
      }
    }
    return ret;
  }

  // Verifica o tipo de um símbolo presente em uma SymbolsTable de símbolos
  // considerando um termo.
  public static SymbolsTable.Types verifyType(Scope escopos, LAParser.TermoContext ctx) {
    SymbolsTable.Types ret = null;

    Iterator<FatorContext> iterator = ctx.fator().iterator();
    while (iterator.hasNext()) {
      FatorContext fa = iterator.next();
      SymbolsTable.Types aux = verifyType(escopos, fa);
      Boolean auxNumeric = aux == SymbolsTable.Types.REAL || aux == SymbolsTable.Types.INT;
      Boolean retNumeric = ret == SymbolsTable.Types.REAL || ret == SymbolsTable.Types.INT;
      if (ret == null) {
        ret = aux;
      } else if (!(auxNumeric && retNumeric) && aux != ret) {
        ret = SymbolsTable.Types.INVALIDO;
      }
    }
    return ret;
  }

  // Verifica o tipo de um símbolo presente em uma SymbolsTable de símbolos
  // considerando um fator.
  public static SymbolsTable.Types verifyType(Scope escopos, LAParser.FatorContext ctx) {
    SymbolsTable.Types ret = null;

    Iterator<ParcelaContext> iterator = ctx.parcela().iterator();
    while (iterator.hasNext()) {
      ParcelaContext fa = iterator.next();
      SymbolsTable.Types aux = verifyType(escopos, fa);
      if (ret == null) {
        ret = aux;
      } else if (ret != aux && aux != SymbolsTable.Types.INVALIDO) {
        ret = SymbolsTable.Types.INVALIDO;
      }
    }
    return ret;
  }

  // Verifica o tipo de um símbolo presente em uma SymbolsTable de símbolos
  // considerando uma parcela.
  public static SymbolsTable.Types verifyType(Scope escopos, LAParser.ParcelaContext ctx) {
    SymbolsTable.Types ret = SymbolsTable.Types.INVALIDO;

    if (ctx.parcela_nao_unario() != null) {
      ret = verifyType(escopos, ctx.parcela_nao_unario());
    } else {
      ret = verifyType(escopos, ctx.parcela_unario());
    }
    return ret;
  }

  // Verifica o tipo de um símbolo presente em uma SymbolsTable de símbolos
  // considerando uma parcela não unária.
  public static SymbolsTable.Types verifyType(Scope escopos, LAParser.Parcela_nao_unarioContext ctx) {
    if (ctx.identificador() != null) {
      return verifyType(escopos, ctx.identificador());
    }
    return SymbolsTable.Types.CADEIA;
  }

  // Verifica o tipo de um símbolo presente em uma SymbolsTable de símbolos
  // considerando um identificador.
  public static SymbolsTable.Types verifyType(Scope escopos, LAParser.IdentificadorContext ctx) {
    String nomeVar = "";
    SymbolsTable.Types ret = SymbolsTable.Types.INVALIDO;
    for (int i = 0; i < ctx.IDENT().size(); i++) {
      nomeVar += ctx.IDENT(i).getText();
      if (i != ctx.IDENT().size() - 1) {
        nomeVar += ".";
      }
    }
    Iterator<SymbolsTable> iterator = escopos.getStack().iterator();
    while (iterator.hasNext()) {
      SymbolsTable SymbolsTable = iterator.next();
      if (SymbolsTable.exists(nomeVar)) {
        ret = verifyType(escopos, nomeVar);
      }
    }
    return ret;
  }

  // Verifica o tipo de um símbolo presente em uma SymbolsTable de símbolos
  // considerando uma parcela unária.
  public static SymbolsTable.Types verifyType(Scope escopos, LAParser.Parcela_unarioContext ctx) {
    if (ctx.NUM_INT() != null) {
      return SymbolsTable.Types.INT;
    }
    if (ctx.NUM_REAL() != null) {
      return SymbolsTable.Types.REAL;
    }
    if (ctx.identificador() != null) {
      return verifyType(escopos, ctx.identificador());
    }
    if (ctx.IDENT() != null) {
      SymbolsTable.Types ret = null;
      ret = verifyType(escopos, ctx.IDENT().getText());
      // Se o símbolo for uma função, retorna tipo do retorno da função
      if (verifyStructure(escopos, ctx.IDENT().getText()) == SymbolsTable.Structure.FUNC) {
        return ret;
      }
      Iterator<ExpressaoContext> iterator = ctx.expressao().iterator();
      while (iterator.hasNext()) {
        ExpressaoContext fa = iterator.next();
        SymbolsTable.Types aux = verifyType(escopos, fa);
        if (ret == SymbolsTable.Types.REAL && aux == SymbolsTable.Types.REG) {
          break;
        }
        if (ret == null) {
          ret = aux;
        } else if (ret != aux && aux != SymbolsTable.Types.INVALIDO) {
          ret = SymbolsTable.Types.INVALIDO;
        }
      }
      return ret;
    } else {
      SymbolsTable.Types ret = null;
      Iterator<ExpressaoContext> iterator = ctx.expressao().iterator();
      while (iterator.hasNext()) {
        ExpressaoContext fa = iterator.next();
        SymbolsTable.Types aux = verifyType(escopos, fa);
        if (ret == null) {
          ret = aux;
        } else if (ret != aux && aux != SymbolsTable.Types.INVALIDO) {
          ret = SymbolsTable.Types.INVALIDO;
        }
      }
      return ret;
    }
  }

  // Verifica o tipo de um símbolo presente em uma SymbolsTable de símbolos
  // considerando uma string.
  public static SymbolsTable.Types verifyType(Scope escopos, String nomeVar) {
    SymbolsTable.Types type = null;
    Iterator<SymbolsTable> iterator = escopos.getStack().iterator();
    while (iterator.hasNext()) {
      SymbolsTable SymbolsTable = iterator.next();
      type = SymbolsTable.verify(nomeVar);
    }
    return type;
  }

  public static SymbolsTable.Structure verifyStructure(Scope escopos, String nomeVar) {
    SymbolsTable.Structure type = null;
    Iterator<SymbolsTable> iterator = escopos.getStack().iterator();
    while (iterator.hasNext()) {
      SymbolsTable SymbolsTable = iterator.next();
      type = SymbolsTable.getStructure(nomeVar);
    }
    return type;
  }

  // transforma a string em um tipo da tabela de simbolos
  public static SymbolsTable.Types getType(String val) {
    SymbolsTable.Types tipo = null;
    switch (val) {
      case "real":
        tipo = SymbolsTable.Types.REAL;
        break;
      case "inteiro":
        tipo = SymbolsTable.Types.INT;
        break;
      case "logico":
        tipo = SymbolsTable.Types.LOGICO;
        break;
      case "literal":
        tipo = SymbolsTable.Types.CADEIA;
        break;
      default:
        break;
    }
    return tipo;
  }
}