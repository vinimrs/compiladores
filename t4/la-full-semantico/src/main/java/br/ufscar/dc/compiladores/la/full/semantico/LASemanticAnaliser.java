package br.ufscar.dc.compiladores.la.full.semantico;

import br.ufscar.dc.compiladores.la.full.semantico.LAParser.CmdAtribuicaoContext;
import br.ufscar.dc.compiladores.la.full.semantico.LAParser.CmdRetorneContext;
import br.ufscar.dc.compiladores.la.full.semantico.LAParser.Declaracao_constanteContext;
import br.ufscar.dc.compiladores.la.full.semantico.LAParser.Declaracao_globalContext;
import br.ufscar.dc.compiladores.la.full.semantico.LAParser.Declaracao_tipoContext;
import br.ufscar.dc.compiladores.la.full.semantico.LAParser.Declaracao_variavelContext;
import br.ufscar.dc.compiladores.la.full.semantico.LAParser.IdentificadorContext;
import br.ufscar.dc.compiladores.la.full.semantico.LAParser.ParametroContext;
import br.ufscar.dc.compiladores.la.full.semantico.LAParser.Parcela_unarioContext;
import br.ufscar.dc.compiladores.la.full.semantico.LAParser.ProgramaContext;
import br.ufscar.dc.compiladores.la.full.semantico.LAParser.Tipo_basico_identContext;
import br.ufscar.dc.compiladores.la.full.semantico.LAParser.VariavelContext;
import br.ufscar.dc.compiladores.la.full.semantico.SymbolsTable.SymbolsTableEntry;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.tree.TerminalNode;

public class LASemanticAnaliser extends LABaseVisitor {

  Scope escoposAninhados = new Scope(SymbolsTable.Types.VOID);

  @Override
  public Object visitPrograma(ProgramaContext ctx) {
    return super.visitPrograma(ctx);
  }

  // método chamado quando o analisador encontra uma declaração de constante no
  // código fonte constante foi declarada anteriormente, adicionar a constante à
  // tabela
  // de símbolos gerar um erro semântico caso a constante já tenha sido declarada
  @Override
  public Object visitDeclaracao_constante(Declaracao_constanteContext ctx) {
    SymbolsTable atual = escoposAninhados.getScope();
    if (atual.exists(ctx.IDENT().getText())) {
      LAUtils.addSemanticError(ctx.start, "constante" + ctx.IDENT().getText() + " ja declarado anteriormente");
    } else {
      SymbolsTable.Types tipo = SymbolsTable.Types.INT;
      SymbolsTable.Types aux = LAUtils.getType(ctx.tipo_basico().getText());
      if (aux != null)
        tipo = aux;
      atual.add(ctx.IDENT().getText(), tipo, SymbolsTable.Structure.CONST);
    }
    return super.visitDeclaracao_constante(ctx);
  }

  // essa função é responsável por verificar se um novo tipo foi declarado
  // anteriormente, adicionar o novo tipo à tabela de símbolos, juntamente com
  // seus identificadores
  @Override
  public Object visitDeclaracao_tipo(Declaracao_tipoContext ctx) {
    SymbolsTable atual = escoposAninhados.getScope();
    if (atual.exists(ctx.IDENT().getText())) {
      LAUtils.addSemanticError(ctx.start,
          "tipo " + ctx.IDENT().getText() + " declarado duas vezes num mesmo escopo");
    } else {
      SymbolsTable.Types tipo = LAUtils.getType(ctx.tipo().getText());
      if (tipo != null)
        atual.add(ctx.IDENT().getText(), tipo, SymbolsTable.Structure.TIPO);
      else if (ctx.tipo().registro() != null) {
        ArrayList<SymbolsTable.SymbolsTableEntry> varReg = new ArrayList<>();
        Iterator<VariavelContext> varIterator = ctx.tipo().registro().variavel().iterator();
        while (varIterator.hasNext()) {
          VariavelContext va = varIterator.next();
          SymbolsTable.Types tipoReg = LAUtils.getType(va.tipo().getText());

          Iterator<IdentificadorContext> idIterator = va.identificador().iterator();
          while (idIterator.hasNext()) {
            IdentificadorContext id2 = idIterator.next();
            varReg.add(atual.new SymbolsTableEntry(id2.getText(), tipoReg, SymbolsTable.Structure.TIPO));
          }
        }
        if (atual.exists(ctx.IDENT().getText())) {
          LAUtils.addSemanticError(ctx.start,
              "identificador " + ctx.IDENT().getText() + " ja declarado anteriormente");
        } else {
          atual.add(ctx.IDENT().getText(), SymbolsTable.Types.REG, SymbolsTable.Structure.TIPO);
        }

        for (SymbolsTable.SymbolsTableEntry re : varReg) {
          String nameVar = ctx.IDENT().getText() + '.' + re.name;
          if (atual.exists(nameVar)) {
            LAUtils.addSemanticError(ctx.start, "identificador " + nameVar + " ja declarado anteriormente");
          } else {
            atual.add(re);
            atual.add(ctx.IDENT().getText(), re);
          }
        }
      }
      SymbolsTable.Types t = LAUtils.getType(ctx.tipo().getText());
      atual.add(ctx.IDENT().getText(), t, SymbolsTable.Structure.TIPO);
    }
    return super.visitDeclaracao_tipo(ctx);
  }

  // verifica se os identificadores de tipos personalizados (identificadores de
  // tipo) utilizados no código foram previamente declarados.
  // Se um identificador de tipo não tiver sido declarado, um erro semântico é
  // gerado
  @Override
  public Object visitTipo_basico_ident(Tipo_basico_identContext ctx) {
    if (ctx.IDENT() != null) {
      boolean exists = false;
      for (SymbolsTable escopo : escoposAninhados.getStack()) {
        if (escopo.exists(ctx.IDENT().getText())) {
          exists = true;
        }
      }
      if (!exists) {
        LAUtils.addSemanticError(ctx.start, "tipo " + ctx.IDENT().getText() + " nao declarado");
      }
    }
    return super.visitTipo_basico_ident(ctx);
  }

  // verificar e adicionar as declarações globais (funções ou procedimentos)
  // àtabela de símbolos,
  // tratando corretamente os parâmetros e gerando erros semânticos quando
  // necessário
  @Override
  public Object visitDeclaracao_global(Declaracao_globalContext ctx) {
    SymbolsTable atual = escoposAninhados.getScope();
    Object ret;
    if (atual.exists(ctx.IDENT().getText())) {
      LAUtils.addSemanticError(ctx.start, ctx.IDENT().getText() + " ja declarado anteriormente");
      ret = super.visitDeclaracao_global(ctx);
    } else {
      SymbolsTable.Types returnTypeFunc = SymbolsTable.Types.VOID;
      if (ctx.getText().startsWith("funcao")) {
        returnTypeFunc = LAUtils.getType(ctx.tipo_complexo().getText());
        atual.add(ctx.IDENT().getText(), returnTypeFunc, SymbolsTable.Structure.FUNC);
      } else {
        returnTypeFunc = SymbolsTable.Types.VOID;
        atual.add(ctx.IDENT().getText(), returnTypeFunc, SymbolsTable.Structure.PROC);
      }
      escoposAninhados.createScope(returnTypeFunc);
      SymbolsTable escopoAntigo = atual;
      atual = escoposAninhados.getScope();
      if (ctx.parametros() != null) {

        for (ParametroContext p : ctx.parametros().parametro()) {
          for (IdentificadorContext id : p.identificador()) {
            String nomeId = "";
            int i = 0;
            for (TerminalNode ident : id.IDENT()) {
              if (i++ > 0)
                nomeId += ".";
              nomeId += ident.getText();
            }
            if (atual.exists(nomeId)) {
              LAUtils.addSemanticError(id.start, "identificador " + nomeId + " ja declarado anteriormente");
            } else {
              SymbolsTable.Types tipo = LAUtils.getType(p.tipo_complexo().getText());
              // Se o tipo for nulo, o parâmetro não tem tipo simples
              if (tipo != null) {
                SymbolsTableEntry in = atual.new SymbolsTableEntry(nomeId, tipo,
                    SymbolsTable.Structure.VAR);
                atual.add(in);
                escopoAntigo.add(ctx.IDENT().getText(), in);
              } else {
                TerminalNode identTipo = p.tipo_complexo().tipo_basico_ident() != null
                    && p.tipo_complexo().tipo_basico_ident().IDENT() != null
                        ? p.tipo_complexo().tipo_basico_ident().IDENT()
                        : null;
                if (identTipo != null) {
                  ArrayList<SymbolsTable.SymbolsTableEntry> regVars = null;
                  boolean found = false;
                  for (SymbolsTable t : escoposAninhados.getStack()) {
                    if (!found) {
                      if (t.exists(identTipo.getText())) {
                        regVars = t.getTypeProperties(identTipo.getText());
                        found = true;
                      }
                    }
                  }
                  if (atual.exists(nomeId)) {
                    LAUtils.addSemanticError(id.start, "identificador " + nomeId + " ja declarado anteriormente");
                  } else {
                    SymbolsTableEntry in = atual.new SymbolsTableEntry(nomeId,
                        SymbolsTable.Types.REG, SymbolsTable.Structure.VAR);
                    atual.add(in);
                    escopoAntigo.add(ctx.IDENT().getText(), in);
                    for (SymbolsTable.SymbolsTableEntry s : regVars) {
                      atual.add(nomeId + "." + s.name, s.tipo, SymbolsTable.Structure.VAR);
                    }
                  }
                }
              }
            }
          }
        }
      }
      ret = super.visitDeclaracao_global(ctx);
      escoposAninhados.dropScope();
    }
    return ret;
  }

  // por verifica se um identificado é válido, ou seja, se ele foi previamente
  // declarado em algum dos escopos presentes na pilha de escopos.
  // Se um identificador não tiver sido declarado, um erro semântico é gerado
  @Override
  public Object visitIdentificador(IdentificadorContext ctx) {
    String Var = "";
    int i = 0;
    for (TerminalNode id : ctx.IDENT()) {
      if (i++ > 0)
        Var += ".";
      Var += id.getText();
    }
    boolean erro = true;
    for (SymbolsTable escopo : escoposAninhados.getStack()) {
      if (escopo.exists(Var)) {
        erro = false;
      }
    }
    if (erro)
      LAUtils.addSemanticError(ctx.start, "identificador " + Var + " nao declarado");
    return super.visitIdentificador(ctx);
  }

  // verifica e add as declarações de variáveis à tabela de símbolos,
  // tratando corretamente os diferentes tipos de variáveis,
  // incluindo variáveis com membros de tipos registro e também add erros
  // semânticos quando identificadores duplicados são encontrados
  @Override
  public Object visitDeclaracao_variavel(Declaracao_variavelContext ctx) {
    SymbolsTable atual = escoposAninhados.getScope();
    for (IdentificadorContext id : ctx.variavel().identificador()) {
      String nomeId = "";
      int i = 0;
      for (TerminalNode ident : id.IDENT()) {
        if (i++ > 0)
          nomeId += ".";
        nomeId += ident.getText();
      }
      if (atual.exists(nomeId)) {
        LAUtils.addSemanticError(id.start, "identificador " + nomeId + " ja declarado anteriormente");
      } else {
        SymbolsTable.Types tipo = LAUtils.getType(ctx.variavel().tipo().getText());
        if (tipo != null)
          atual.add(nomeId, tipo, SymbolsTable.Structure.VAR);
        else {
          TerminalNode identTipo = ctx.variavel().tipo() != null
              && ctx.variavel().tipo().tipo_complexo() != null
              && ctx.variavel().tipo().tipo_complexo().tipo_basico_ident() != null
              && ctx.variavel().tipo().tipo_complexo().tipo_basico_ident().IDENT() != null
                  ? ctx.variavel().tipo().tipo_complexo().tipo_basico_ident().IDENT()
                  : null;
          if (identTipo != null) {
            ArrayList<SymbolsTable.SymbolsTableEntry> regVars = null;
            boolean found = false;
            for (SymbolsTable t : escoposAninhados.getStack()) {
              if (!found) {
                if (t.exists(identTipo.getText())) {
                  regVars = t.getTypeProperties(identTipo.getText());
                  found = true;
                }
              }
            }
            if (atual.exists(nomeId)) {
              LAUtils.addSemanticError(id.start, "identificador " + nomeId
                  + " ja declarado anteriormente");
            } else {
              atual.add(nomeId, SymbolsTable.Types.REG, SymbolsTable.Structure.VAR);
              for (SymbolsTable.SymbolsTableEntry s : regVars) {
                atual.add(nomeId + "." + s.name, s.tipo, SymbolsTable.Structure.VAR);
              }
            }
          } else if (ctx.variavel().tipo().registro() != null) {
            ArrayList<SymbolsTable.SymbolsTableEntry> varReg = new ArrayList<>();
            for (VariavelContext va : ctx.variavel().tipo().registro().variavel()) {
              SymbolsTable.Types tipoReg = LAUtils.getType(va.tipo().getText());
              for (IdentificadorContext id2 : va.identificador()) {
                varReg.add(atual.new SymbolsTableEntry(id2.getText(), tipoReg, SymbolsTable.Structure.VAR));
              }
            }
            atual.add(nomeId, SymbolsTable.Types.REG, SymbolsTable.Structure.VAR);

            for (SymbolsTable.SymbolsTableEntry re : varReg) {
              String nameVar = nomeId + '.' + re.name;
              if (atual.exists(nameVar)) {
                LAUtils.addSemanticError(id.start, "identificador " + nameVar + " ja declarado anteriormente");
              } else {
                atual.add(re);
                atual.add(nameVar, re.tipo, SymbolsTable.Structure.VAR);
              }
            }
          } else {
            atual.add(id.getText(), SymbolsTable.Types.INT, SymbolsTable.Structure.VAR);
          }
        }
      }
    }
    return super.visitDeclaracao_variavel(ctx);
  }

  // verifica se o comando retorne é permitido no escopo atual. Se o escopo for um
  // procedimento
  // (com tipo de retorno VOID), o comando retorne não é permitido e um erro
  // semântico é gerado
  @Override
  public Object visitCmdRetorne(CmdRetorneContext ctx) {
    if (escoposAninhados.getScope().returnType == SymbolsTable.Types.VOID) {
      LAUtils.addSemanticError(ctx.start, "comando retorne nao permitido nesse escopo");
    }
    return super.visitCmdRetorne(ctx);
  }

  // verifica a compatibilidade da atribuição entre a expressão do lado direito e
  // a variável do lado esquerdo.
  // Se a atribuição não for compatível (por exemplo, tentando atribuir um valor
  // real a uma variável inteira), um erro semântico é gerado.
  @Override
  public Object visitCmdAtribuicao(CmdAtribuicaoContext ctx) {
    SymbolsTable.Types tipoExpressao = LAUtils.verifyType(escoposAninhados, ctx.expressao());
    boolean error = false;
    String pointerChar = ctx.getText().charAt(0) == '^' ? "^" : "";
    String Var = "";
    int i = 0;
    for (TerminalNode id : ctx.identificador().IDENT()) {
      if (i++ > 0)
        Var += ".";
      Var += id.getText();
    }
    if (tipoExpressao != SymbolsTable.Types.INVALIDO) {
      boolean found = false;
      for (SymbolsTable escopo : escoposAninhados.getStack()) {
        if (escopo.exists(Var) && !found) {
          found = true;
          SymbolsTable.Types tipoVariavel = LAUtils.verifyType(escoposAninhados, Var);
          Boolean varNumeric = tipoVariavel == SymbolsTable.Types.REAL || tipoVariavel == SymbolsTable.Types.INT;
          Boolean expNumeric = tipoExpressao == SymbolsTable.Types.REAL || tipoExpressao == SymbolsTable.Types.INT;
          if (!(varNumeric && expNumeric) && tipoVariavel != tipoExpressao
              && tipoExpressao != SymbolsTable.Types.INVALIDO) {
            error = true;
          }
        }
      }
    } else {
      error = true;
    }

    if (error) {
      Var = ctx.identificador().getText();
      LAUtils.addSemanticError(ctx.identificador().start, "atribuicao nao compativel para " + pointerChar + Var);
    }
    return super.visitCmdAtribuicao(ctx);
  }

  // responsável por verificar se a chamada de função ou procedimento possui os
  // parâmetros corretos
  // e compatíveis com os declarados na declaração da função ou procedimento. Se
  // houver incompatibilidade nos parâmetros, um erro semântico é gerado
  @Override
  public Object visitParcela_unario(Parcela_unarioContext ctx) {
    SymbolsTable atual = escoposAninhados.getScope();
    if (ctx.IDENT() != null) {
      String name = ctx.IDENT().getText();

      if (atual.exists(ctx.IDENT().getText())) {
        List<SymbolsTableEntry> params = atual.getTypeProperties(name);
        boolean error = false;
        if (params.size() != ctx.expressao().size()) {
          error = true;
        } else {
          for (int i = 0; i < params.size(); i++) {
            if (params.get(i).tipo != LAUtils.verifyType(escoposAninhados, ctx.expressao().get(i))) {
              error = true;
            }
          }
        }
        if (error) {
          LAUtils.addSemanticError(ctx.start, "incompatibilidade de parametros na chamada de " + name);
        }
      }
    }
    return super.visitParcela_unario(ctx);
  }
}