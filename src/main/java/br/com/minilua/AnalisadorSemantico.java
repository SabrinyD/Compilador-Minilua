package br.com.minilua;

// Importa classes geradas pelo ANTLR e o utilitário de árvore
import org.antlr.v4.runtime.tree.TerminalNode;

// Classe BaseVisitor
public class AnalisadorSemantico extends MiniLuaBaseVisitor<String> { //retorna tipo do dado

    private TabelaDeSimbolos tabela = new TabelaDeSimbolos();

    // 1. DECLARAÇÃO DE VARIÁVEIS (Ex: int x;)
    @Override
    public String visitCmdDecl(MiniLuaParser.CmdDeclContext ctx) {
        // Pega o tipo e o nome
        String tipo = ctx.tipo().getText();
        String nome = ctx.IDENTIFICADOR().getText();

        // Regra: Não pode declarar a mesma variável duas vezes
        if (tabela.contem(nome)) {
            System.out.println("Erro Semântico: Variável '" + nome + "' já existe.");
        } else {
            tabela.adicionar(nome, tipo);
            //Mensagem para confirmar declaração da variável
            System.out.println("Declaração da variável '" + nome + "' do tipo " + tipo);
        }
        return null;
    }

    // 2. ATRIBUIÇÃO (Ex: x = 10;)
    @Override
    public String visitCmdAtrib(MiniLuaParser.CmdAtribContext ctx) {
        // Pega o nome da variável
        String nome = ctx.IDENTIFICADOR().getText();

        // Regra: A variável precisa existir
        if (!tabela.contem(nome)) {
            System.out.println("Erro Semântico (Linha " + ctx.start.getLine() + "): A variável '" + nome + "' não foi declarada.");            return null; // Para aqui para não causar mais erros
        }

        // Regra: O tipo do valor deve ser igual ao tipo da variável
        String tipoVariavel = tabela.getTipo(nome);

        // Calcula o tipo do valor que é atribuído
        String tipoExpressao = visit(ctx.expr());

        // Se houve erro na expressão, para
        if ("erro".equals(tipoExpressao)) return null;

        // Verifica a compatibilidade
        if (tipoExpressao != null && !tipoVariavel.equals(tipoExpressao)) {
            // Float aceitar int (caso especial)
            if (tipoVariavel.equals("float") && tipoExpressao.equals("int")) {
                // Mensagem para confirmar atribuição válida
                System.out.println("Atribuição válida (Coerção int->float): " + nome + " = " + tipoExpressao);
                return null;
            }

            System.out.println("Erro Semântico (Linha " + ctx.start.getLine() + "): Atribuição incompatível. A variável '" + nome +
                    "' é " + tipoVariavel + ", mas recebeu " + tipoExpressao + ".");
        } else {
            // Mensagem para confirmar que a atribuição é válida:
            System.out.println("Atribuição válida: " + nome + " (" + tipoVariavel + ") = " + tipoExpressao);
        }

        return null;
    }

    // 3. EXPRESSÕES (Para descobrir os tipos)
    // Quando encontra um número/texto
    @Override
    public String visitExprLiteral(MiniLuaParser.ExprLiteralContext ctx) {
        if (ctx.literal().NUM_INT() != null) return "int";
        if (ctx.literal().NUM_FLOAT() != null) return "float";
        if (ctx.literal().STRING_LIT() != null) return "string";
        return null;
    }

    // Quando encontra uma variável no meio da conta (ex: y + 2)
    @Override
    public String visitExprId(MiniLuaParser.ExprIdContext ctx) {
        String nome = ctx.IDENTIFICADOR().getText();
        if (!tabela.contem(nome)) {
            System.out.println("Erro Semântico (Linha " + ctx.start.getLine() + "): Variável '" + nome + "' usada sem ser declarada.");            return "erro";
        }
        return tabela.getTipo(nome); // Retorna o tipo da variável
    }

    // Quando encontra operações matemáticas
    @Override
    public String visitExprAritmetica(MiniLuaParser.ExprAritmeticaContext ctx) {
        // Verifica os dois lados da operação
        String esquerda = visit(ctx.expr(0));
        String direita = visit(ctx.expr(1));

        // Regra: Não pode somar texto com número
        if ("string".equals(esquerda) || "string".equals(direita)) {
            System.out.println("Erro Semântico: Não é possível fazer contas com Strings.");            return "erro";
        }

        // Se um dos dois for float, o resultado é float. Se ambos são int, o resultado int.
        if ("float".equals(esquerda) || "float".equals(direita)) {
            return "float";
        }
        return "int";
    }
}