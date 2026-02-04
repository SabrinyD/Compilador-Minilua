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

    // --- VISITANTES (Lógica de Tradução) ---

    // 1. Atribuição (x = 10 + 2)
    @Override
    public String visitCmdAtrib(MiniLuaParser.CmdAtribContext ctx) {
        String variavel = ctx.IDENTIFICADOR().getText();
        String expr = visit(ctx.expr()); // Visita a expressão e pega onde o resultado está (ex: t1)

        emit(variavel + " = " + expr);
        return null;
    }

    // 2. Operações Aritméticas (+, -, *, /)
    @Override
    public String visitExprAritmetica(MiniLuaParser.ExprAritmeticaContext ctx) {
        String op1 = visit(ctx.expr(0)); // Pega o operando da esquerda (pode ser var, numero ou outra temp)
        String op2 = visit(ctx.expr(1)); // Pega o operando da direita
        String operador = ctx.op.getText();

        String temp = newTemp(); // Cria t1, t2...
        emit(temp + " = " + op1 + " " + operador + " " + op2);

        return temp; // Retorna o nome da temporária para quem chamou
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
    
}
