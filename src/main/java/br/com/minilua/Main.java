package br.com.minilua;

// --- Importações do ANTLR ---
import org.antlr.v4.runtime.CharStream; // Define o tipo de dado para o fluxo de entrada
import org.antlr.v4.runtime.CharStreams; // Ferramenta que lê o arquivo de texto e transforma em CharStram
import org.antlr.v4.runtime.Token; // Guarda o Tipo, Texto e Linha

// --- Importação do JAVA para tratamento de erros ---
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            // Define o arquivo que será lido
            String arquivo = "teste_stress.txt";
            // Transforma o conteúdo do arquivo em um fluxo de caracteres
            CharStream cs = CharStreams.fromFileName(arquivo);

            // Instancia o Lexer
            MiniLuaLexer lexer = new MiniLuaLexer(cs);

            // Loop para imprimir Token por Token
            Token t = null;
            System.out.println("=== INICIANDO ANALISE LÉXICA ===");

            while ((t = lexer.nextToken()).getType() != Token.EOF) {

                // Pega o nome simbólico do token (Ex: INT, IDENTIFICADOR)
                String nomeToken = MiniLuaLexer.VOCABULARY.getSymbolicName(t.getType());
                // Se não tiver nome (for null), pega o próprio símbolo
                if (nomeToken == null) {
                    nomeToken = MiniLuaLexer.VOCABULARY.getLiteralName(t.getType());
                }

                // Imprime no formato: <Tipo, "Valor", Linha>
                System.out.println("Token: <" + nomeToken + ", \"" + t.getText() + "\", Linha: " + t.getLine() + ">");
            }

            System.out.println("=== FIM DA LEITURA ===");

        } catch (IOException e) {
            // Se o arquivo não existir, imprime erro
            e.printStackTrace();
        }
    }
}