package br.com.minilua;

// --- Importações do ANTLR ---
import java.io.IOException; // Define o tipo de dado para o fluxo de entrada

import org.antlr.v4.runtime.CharStream; // Ferramenta que lê o arquivo de texto e transforma em CharStram
import org.antlr.v4.runtime.CharStreams; // Armazena os tokens gerados pelo Lexer
import org.antlr.v4.runtime.CommonTokenStream; // Guarda o Tipo, Texto e Linha
import org.antlr.v4.runtime.Token; // Árvore sintática (AST) gerada
import org.antlr.v4.runtime.tree.ParseTree; // Manipulação e extração do texto da árvore
import org.antlr.v4.runtime.tree.Trees;

public class Main {
    public static void main(String[] args) {
        // Define o arquivo que será lido
        String arquivo = "teste_sucesso.txt";

        try {
            // Transforma o conteúdo do arquivo em um fluxo de caracteres
            CharStream cs = CharStreams.fromFileName(arquivo);

            // Instancia o Lexer
            MiniLuaLexer lexer = new MiniLuaLexer(cs);

            // Buffer de tokens
            CommonTokenStream tokens = new CommonTokenStream(lexer);

            // ETAPA 0: ANÁLISE LÉXICA
            System.out.println("=== INICIANDO ANALISE LÉXICA ===");
            // Carrega tudo na memória para imprimir
            tokens.fill();

            for (Token t : tokens.getTokens()) {
                // Ignora o token de fim de arquivo (EOF) na impressão visual
                if (t.getType() != Token.EOF) {

                    // Pega o nome simbólico do token (Ex: INT, IDENTIFICADOR)
                    String nomeToken = MiniLuaLexer.VOCABULARY.getSymbolicName(t.getType());
                    // Se não tiver nome (for null), pega o próprio símbolo
                    if (nomeToken == null) {
                        nomeToken = MiniLuaLexer.VOCABULARY.getLiteralName(t.getType());
                    }

                    // Imprime no formato: <Tipo, "Valor", Linha, Coluna>
                    System.out.println("Token: <" + nomeToken +
                            ", Valor: \"" + t.getText() + "\"" +
                            ", Linha: " + t.getLine() +
                            ", Coluna: " + t.getCharPositionInLine() + ">");
                }
            }

            System.out.println("=== FIM DA LEITURA ===");

            // ETAPA 1: ANÁLISE SINTÁTICA
            System.out.println("=== ANÁLISE SINTÁTICA (Gerando AST) ===");

            MiniLuaParser parser = new MiniLuaParser(tokens); //analisador gramatical

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
            System.out.println(""); // Pula uma linha visual

            // ETAPA 2: ANÁLISE SEMÂNTICA
            System.out.println("=== ANÁLISE SEMÂNTICA (Verificando Tipos) ===");

            //roda verificador
            AnalisadorSemantico semantico = new AnalisadorSemantico();
            semantico.visit(tree); // Roda verificador

            System.out.println("=== FIM DA COMPILAÇÃO ===");

            // ETAPA 3: GERAÇÃO DE CÓDIGO INTERMEDIÁRIO (NOVO!)
            System.out.println("=== GERAÇÃO DE CÓDIGO INTERMEDIÁRIO (TAC) ===");

            GeradorCodigoIntermediario gerador = new GeradorCodigoIntermediario();
            gerador.visit(tree);

            System.out.println("--- CÓDIGO DE TRÊS ENDEREÇOS GERADO ---");
            System.out.println(gerador.getCodigo());
            System.out.println("---------------------------------------");

            System.out.println("=== SUCESSO! ===");

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