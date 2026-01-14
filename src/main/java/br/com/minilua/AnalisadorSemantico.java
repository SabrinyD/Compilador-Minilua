package br.com.minilua;

public class AnalisadorSemantico extends MiniLuaBaseVisitor<Void> {

    private TabelaDeSimbolos tabela = new TabelaDeSimbolos();

    // === REGRA 1: DECLARAÇÃO ===
    @Override
    public Void visitDeclaracao(MiniLuaParser.DeclaracaoContext ctx) {
        String nomeVar = ctx.IDENTIFICADOR().getText();
        String tipoVar = ctx.tipo().getText();

        // 1. Verifica duplicidade (Escopo Global único)
        if (tabela.existe(nomeVar)) {
            System.err.println("Erro Semântico (Linha " + ctx.start.getLine() + "): Variável '" + nomeVar + "' já declarada.");
        } else {
            // 2. Guarda na memória (Ação adSimb)
            tabela.adicionar(nomeVar, tipoVar);
        }
        return super.visitDeclaracao(ctx);
    }

    // === REGRA 2: ATRIBUIÇÃO E CHECAGEM DE TIPOS ===
    @Override
    public Void visitAtribuicao(MiniLuaParser.AtribuicaoContext ctx) {
        String nomeVar = ctx.IDENTIFICADOR().getText();

        // 1. Verifica se a variável existe
        if (!tabela.existe(nomeVar)) {
            System.err.println("Erro Semântico (Linha " + ctx.start.getLine() + "): Variável '" + nomeVar + "' não foi declarada.");
            return null;
        }

        String tipoVariavel = tabela.verificarTipo(nomeVar);

        // 2. Infere o tipo da expressão que está sendo atribuída
        String tipoExpressao = verificarTipoExpressao(ctx.expressao());

        // 3. Verifica compatibilidade (Regra de Ouro)
        if (!tiposCompativeis(tipoVariavel, tipoExpressao)) {
            System.err.println("Erro Semântico (Linha " + ctx.start.getLine() + "): " +
                    "Tentando atribuir " + tipoExpressao + " para variável '" + nomeVar + "' (" + tipoVariavel + ").");
        }

        return super.visitAtribuicao(ctx);
    }

    // === REGRA 3: USO DE VARIÁVEL EM EXPRESSÕES ===
    // Precisamos garantir que variáveis usadas em contas (ex: y = x + 1) existem
    @Override
    public Void visitFator(MiniLuaParser.FatorContext ctx) {
        if (ctx.IDENTIFICADOR() != null) {
            String nomeVar = ctx.IDENTIFICADOR().getText();
            if (!tabela.existe(nomeVar)) {
                System.err.println("Erro Semântico (Linha " + ctx.start.getLine() + "): Variável '" + nomeVar + "' usada sem declarar.");
            }
        }
        return super.visitFator(ctx);
    }

    // --- MÉTODOS AUXILIARES ---

    // Lógica simplificada para descobrir o tipo de uma expressão
    private String verificarTipoExpressao(MiniLuaParser.ExpressaoContext ctx) {
        String texto = ctx.getText();

        // Se tem aspas, é string
        if (texto.contains("\"")) return "string";

        // Se tem ponto, ou operações de divisão, assumimos float
        if (texto.contains(".") || texto.contains("/")) return "float";

        // Se tem true/false ou operadores lógicos/relacionais
        if (texto.contains("true") || texto.contains("false") ||
                texto.contains(">") || texto.contains("==")) return "boolean"; // (interno)

        // Caso contrário, assumimos int (simplificação para o trabalho)
        return "int";
    }

    private boolean tiposCompativeis(String tipoVar, String tipoExpr) {
        // Regra simples: Tipos iguais são compatíveis
        if (tipoVar.equals(tipoExpr)) return true;

        // Regra de coerção: Float aceita Int
        if (tipoVar.equals("float") && tipoExpr.equals("int")) return true;

        return false;
    }
}