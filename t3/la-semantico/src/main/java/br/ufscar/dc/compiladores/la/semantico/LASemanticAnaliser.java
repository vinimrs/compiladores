package br.ufscar.dc.compiladores.la.semantico;

import br.ufscar.dc.compiladores.la.semantico.LAParser.CmdAtribuicaoContext;
import br.ufscar.dc.compiladores.la.semantico.LAParser.Declaracao_constanteContext;
import br.ufscar.dc.compiladores.la.semantico.LAParser.Declaracao_globalContext;
import br.ufscar.dc.compiladores.la.semantico.LAParser.Declaracao_variavelContext;
import br.ufscar.dc.compiladores.la.semantico.LAParser.IdentificadorContext;
import br.ufscar.dc.compiladores.la.semantico.LAParser.Tipo_basico_identContext;

import java.util.Iterator;

public class LASemanticAnaliser extends LABaseVisitor {

  // Cria os escopos como variavel global
  Scope escoposAninhados = new Scope();

  // Verifica se a função principal tem um retorno, se não, emite erro.
  @Override
  public Object visitCorpo(LAParser.CorpoContext ctx) {
    Iterator<LAParser.CmdContext> iterator = ctx.cmd().iterator();
    // Cria o escopo da função principal
    while (iterator.hasNext()) {
      LAParser.CmdContext cmd = iterator.next();
      if (cmd.cmdRetorne() != null) {
        LAUtils.addSemanticError(cmd.getStart(), "comando retorne nao permitido nesse escopo");
      }
    }

    return super.visitCorpo(ctx);
  }

  // Verifica se a contante ja existe, se não existe, ele adiciona a contante e
  // seu tipo à SymbolsTable
  @Override
  public Object visitDeclaracao_constante(Declaracao_constanteContext ctx) {
    SymbolsTable atual = escoposAninhados.getScope();
    if (atual.exists(ctx.IDENT().getText())) {
      LAUtils.addSemanticError(ctx.start, "constante" + ctx.IDENT().getText()
          + " ja declarado anteriormente");
    } else {
      SymbolsTable.Tipos tipo = SymbolsTable.Tipos.INT;
      switch (ctx.tipo_basico().getText()) {
        case "logico":
          tipo = SymbolsTable.Tipos.LOGICO;
          break;
        case "literal":
          tipo = SymbolsTable.Tipos.CADEIA;
          break;
        case "real":
          tipo = SymbolsTable.Tipos.REAL;
          break;
        case "inteiro":
          tipo = SymbolsTable.Tipos.INT;
          break;

      }
      atual.add(ctx.IDENT().getText(), tipo);
    }

    return super.visitDeclaracao_constante(ctx);
  }

  // Verifica se a variavel ja existe, se não existe, ele adiciona a variavel
  // e seu tipo à SymbolsTable
  @Override
  public Object visitDeclaracao_variavel(Declaracao_variavelContext ctx) {
    SymbolsTable atual = escoposAninhados.getScope();
    Iterator<IdentificadorContext> iterator = ctx.variavel().identificador().iterator();

    while (iterator.hasNext()) {
      IdentificadorContext id = iterator.next();
      if (atual.exists(id.getText())) {
        LAUtils.addSemanticError(id.start, "identificador " + id.getText()
            + " ja declarado anteriormente");
      } else {
        SymbolsTable.Tipos tipo = SymbolsTable.Tipos.INT;
        switch (ctx.variavel().tipo().getText()) {
          case "literal":
            tipo = SymbolsTable.Tipos.CADEIA;
            break;
          case "inteiro":
            tipo = SymbolsTable.Tipos.INT;
            break;
          case "real":
            tipo = SymbolsTable.Tipos.REAL;
            break;
          case "logico":
            tipo = SymbolsTable.Tipos.LOGICO;
            break;
        }
        atual.add(id.getText(), tipo);
      }
    }
    return super.visitDeclaracao_variavel(ctx);
  }

  // Verifica se uma atribuição é válida e verifica se a expressão da atribuição
  // tem o mesmo tipo que a variável ou é compatível com ela, se não for, emite
  // erro.
  @Override
  public Object visitDeclaracao_global(Declaracao_globalContext ctx) {
    SymbolsTable atual = escoposAninhados.getScope();
    if (atual.exists(ctx.IDENT().getText())) {
      LAUtils.addSemanticError(ctx.start, ctx.IDENT().getText()
          + " ja declarado anteriormente");
    } else {
      atual.add(ctx.IDENT().getText(), SymbolsTable.Tipos.TIPO);
    }
    return super.visitDeclaracao_global(ctx);
  }

  // Valida se o tipo básico identificado é válido e verifica se o identificador
  // foi declarado em algum escopo anterior, se sim, emite erro.
  public Object visitTipo_basico_ident(Tipo_basico_identContext ctx) {
    if (ctx.IDENT() != null) {
      Iterator<SymbolsTable> iterator = escoposAninhados.getPilha().iterator();
      boolean found = false;
      while (iterator.hasNext()) {
        SymbolsTable escopo = iterator.next();
        if (escopo.exists(ctx.IDENT().getText())) {
          found = true;
          break;
        }
      }

      if (!found) {
        LAUtils.addSemanticError(ctx.start,
            "tipo " + ctx.IDENT().getText() + " nao declarado");
      }
    }
    return super.visitTipo_basico_ident(ctx);
  }

  // Valida se a variavel foi declarada em algum escopo ligado anterior, se não,
  // emite erro.
  public Object visitIdentificador(IdentificadorContext ctx) {
    Iterator<SymbolsTable> iterator = escoposAninhados.getPilha().iterator();
    boolean IdentDec = false;

    while (iterator.hasNext()) {
      SymbolsTable escoposAninhados = iterator.next();
      if (escoposAninhados.exists(ctx.IDENT(0).getText())) {
        IdentDec = true;
        break;
      }
    }

    if (!IdentDec) {
      LAUtils.addSemanticError(ctx.start,
          "identificador " + ctx.IDENT(0).getText() + " nao declarado");
    }

    return super.visitIdentificador(ctx);
  }

  // Valida se a atribuição é válida e verifica se a expressão da atribuição
  // tem o mesmo tipo que a variável ou é compatível com ela, se não for, emite
  // erro.
  @Override
  public Object visitCmdAtribuicao(CmdAtribuicaoContext ctx) {
    SymbolsTable.Tipos Exptipo = LAUtils.verificarTipo(escoposAninhados, ctx.expressao());
    boolean erro = false;
    String var = ctx.identificador().getText();
    if (Exptipo != SymbolsTable.Tipos.INVALIDO) {
      Iterator<SymbolsTable> iterator = escoposAninhados.getPilha().iterator();
      while (iterator.hasNext()) {
        SymbolsTable escopo = iterator.next();
        if (escopo.exists(var)) {
          SymbolsTable.Tipos tipoVariavel = LAUtils.verificarTipo(escoposAninhados, var);
          Boolean varNumeric = tipoVariavel == SymbolsTable.Tipos.REAL
              || tipoVariavel == SymbolsTable.Tipos.INT;
          Boolean expNumeric = Exptipo == SymbolsTable.Tipos.REAL || Exptipo == SymbolsTable.Tipos.INT;
          if (!(varNumeric && expNumeric) && tipoVariavel != Exptipo
              && Exptipo != SymbolsTable.Tipos.INVALIDO) {
            erro = true;
            break;
          }
        }
      }
    } else {
      erro = true;
    }

    if (erro)
      LAUtils.addSemanticError(ctx.identificador().start,
          "atribuicao nao compativel para " + var);

    return super.visitCmdAtribuicao(ctx);
  }

}