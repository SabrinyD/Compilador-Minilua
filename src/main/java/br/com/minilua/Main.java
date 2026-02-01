package br.com.minilua;

// --- Importações do ANTLR ---
import org.antlr.v4.runtime.CharStream; // Fluxo de caracteres
import org.antlr.v4.runtime.CharStreams; // Lê o arquivo de texto e converte para CharStream
import org.antlr.v4.runtime.CommonTokenStream; // Armazena os tokens gerados pelo Lexer
import org.antlr.v4.runtime.Token; // Guarda o Tipo, Texto e Linha
import org.antlr.v4.runtime.tree.ParseTree; // Árvore Sintática (AST) gerada
import org.antlr.v4.runtime.tree.Trees; // Manipulação e extração do texto da árvore

// --- Importação do JAVA ---
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        String arquivo = "teste_sucesso.txt";

        try {

            // Lê o arquivo uma única vez
            CharStream cs = CharStreams.fromFileName(arquivo);
            MiniLuaLexer lexer = new MiniLuaLexer(cs);
            CommonTokenStream tokens = new CommonTokenStream(lexer); // Buffer de tokens

            // ETAPA 1: ANÁLISE LÉXICA
            System.out.println("=== 1. ANÁLISE LÉXICA (Tokens Gerados) ===");

            // Carrega na memória para imprimir
            tokens.fill();

            for (Token t : tokens.getTokens()) {
                // Ignora o EOF na impressão
                if (t.getType() != Token.EOF) {

                    String nomeToken = MiniLuaLexer.VOCABULARY.getSymbolicName(t.getType());
                    if (nomeToken == null) {
                        nomeToken = MiniLuaLexer.VOCABULARY.getLiteralName(t.getType());
                    }

                    System.out.println("Token: <" + nomeToken +
                            ", Valor: \"" + t.getText() + "\"" +
                            ", Linha: " + t.getLine() +
                            ", Coluna: " + t.getCharPositionInLine() + ">");
                }
            }
            System.out.println("=== FIM DA ANÁLISE LÉXICA ===");
            System.out.println("");

            // ETAPA 2: ANÁLISE SINTÁTICA
            System.out.println("=== 2. ANÁLISE SINTÁTICA (Gerando AST) ===");

            // Passa os tokens carregados para o parser
            MiniLuaParser parser = new MiniLuaParser(tokens);

            // Gera a árvore
            ParseTree tree = parser.programa();

            // Se houver erros de sintaxe, para
            if (parser.getNumberOfSyntaxErrors() > 0) {
                System.out.println("Erro Sintático detectado. Compilação abortada.");
                return;
            }

            // Se não houve erro, imprime a árvore
            System.out.println("--- Árvore Sintática Gerada ---");
            imprimirArvore(tree, parser, "");
            System.out.println("-------------------------------");
            System.out.println("Sintaxe OK!");
            System.out.println("");

            // ETAPA 3: ANÁLISE SEMÂNTICA
            System.out.println("=== 3. ANÁLISE SEMÂNTICA (Verificando Tipos) ===");

            AnalisadorSemantico semantico = new AnalisadorSemantico();
            semantico.visit(tree); //roda verificador

            System.out.println("=== FIM DA COMPILAÇÃO ===");

        } catch (IOException e) {
            System.out.println("Erro ao ler arquivo: " + e.getMessage());
        }
    }

    // --- Função auxiliar para imprimir a árvore ---
    private static void imprimirArvore(ParseTree tree, MiniLuaParser parser, String indent) {
        String nodeText = Trees.getNodeText(tree, parser);
        System.out.println(indent + nodeText);

        for (int i = 0; i < tree.getChildCount(); i++) {
            imprimirArvore(tree.getChild(i), parser, indent + "|-- ");
        }
    }
}