package br.com.minilua;

// --- Importações do ANTLR ---
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;

// --- Importação para erros de arquivo ---
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            String arquivo = "teste_stress.txt";
            CharStream cs = CharStreams.fromFileName(arquivo);

            // 1. Instancia o Lexer
            MiniLuaLexer lexer = new MiniLuaLexer(cs);

            // 2. Loop para imprimir Token por Token 
            Token t = null;
            System.out.println("=== INICIANDO ANALISE LÉXICA ===");

            while ((t = lexer.nextToken()).getType() != Token.EOF) {

                // Pega o nome o nome simbólico do token (Ex: INT, IDENTIFICADOR)
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
            e.printStackTrace();
        }
    }
}
