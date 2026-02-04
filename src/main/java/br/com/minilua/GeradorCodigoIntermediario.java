/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package br.com.minilua;

/**
 *
 * @author eric
 */
public class GeradorCodigoIntermediario extends MiniLuaBaseVisitor<String> {

    // StringBuilder para acumular o código gerado
    private StringBuilder codigo = new StringBuilder();

    // Contadores para variáveis temporárias (t1, t2...) e labels (L1, L2...)
    private int tempCount = 0;
    private int labelCount = 0;

    // --- Métodos Utilitários ---

    // Gera uma nova variável temporária (ex: t1, t2)
    private String newTemp() {
        tempCount++;
        return "t" + tempCount;
    }

    // Gera um novo Label para desvios (ex: L1, L2)
    private String newLabel() {
        labelCount++;
        return "L" + labelCount;
    }

    // Adiciona uma linha ao código final
    private void emit(String instrucao) {
        codigo.append(instrucao + "\n");
    }

    // Retorna o código completo gerado
    public String getCodigo() {
        return codigo.toString();
    }

    // --- VISITANTES ---

    // 0. Declaração (int a;)
    // Necessário para o compilador não quebrar quando encontrar "int x;"
    @Override
    public String visitCmdDecl(MiniLuaParser.CmdDeclContext ctx) {
        return null;
    }

    // 1. Atribuição (x = 10 + 2)
    @Override
    public String visitCmdAtrib(MiniLuaParser.CmdAtribContext ctx) {
        String variavel = ctx.IDENTIFICADOR().getText();
        String expr = visit(ctx.expr());
        emit(variavel + " = " + expr);
        return null;
    }

    // 2. Operações Aritméticas (+, -, *, /)
    @Override
    public String visitExprAritmetica(MiniLuaParser.ExprAritmeticaContext ctx) {
        String op1 = visit(ctx.expr(0));
        String op2 = visit(ctx.expr(1));
        String operador = ctx.op.getText();

        String temp = newTemp();
        emit(temp + " = " + op1 + " " + operador + " " + op2);
        return temp;
    }

    // 3. Operações Relacionais (>, <, ==)
    @Override
    public String visitExprRelacional(MiniLuaParser.ExprRelacionalContext ctx) {
        String op1 = visit(ctx.expr(0));
        String op2 = visit(ctx.expr(1));
        String operador = ctx.op.getText();

        String temp = newTemp();
        emit(temp + " = " + op1 + " " + operador + " " + op2);
        return temp;
    }

    // 4. Parênteses
    @Override
    public String visitExprParenteses(MiniLuaParser.ExprParentesesContext ctx) {
        return visit(ctx.expr());
    }

    // 5. Menos Unário (-5)
    @Override
    public String visitExprMenosUnario(MiniLuaParser.ExprMenosUnarioContext ctx) {
        String expr = visit(ctx.expr());
        String temp = newTemp();
        emit(temp + " = -" + expr);
        return temp;
    }

    // 6. IF / ELSE
    @Override
    public String visitCmdIf(MiniLuaParser.CmdIfContext ctx) {
        String condicao = visit(ctx.expr());

        String labelFalse = newLabel();
        String labelEnd = newLabel();

        // Se a condição for falsa, pula para o ELSE
        emit("ifFalse " + condicao + " goto " + labelFalse);

        // Bloco THEN
        visit(ctx.comandos(0));
        emit("goto " + labelEnd); // Pula o ELSE

        // Bloco ELSE
        emit(labelFalse + ":");
        if (ctx.comandos(1) != null) {
            visit(ctx.comandos(1));
        }

        emit(labelEnd + ":");
        return null;
    }

    // 7. Print
    @Override
    public String visitCmdPrint(MiniLuaParser.CmdPrintContext ctx) {
        String expr = visit(ctx.expr());
        emit("param " + expr);
        emit("call print, 1");
        return null;
    }

    // 8. Fatores básicos
    @Override
    public String visitExprId(MiniLuaParser.ExprIdContext ctx) {
        return ctx.IDENTIFICADOR().getText();
    }

    @Override
    public String visitExprLiteral(MiniLuaParser.ExprLiteralContext ctx) {
        return ctx.getText();
    }
}
