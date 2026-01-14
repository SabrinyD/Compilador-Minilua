package br.com.minilua;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;

public class Main {
    public static void main(String[] args) {
        // CÓDIGO FONTE (Mude aqui para testar erros)
        String codigo = """
                int a;
                float b;
                string texto;
                
                a = 10;
                b = 5.5;
                texto = "Ola Mundo";
                
                -- Teste de Erro Semântico (Descomente para testar)
                -- int x;
                -- x = "Nao pode"; 
                
                if a > 5 then
                    print(texto);
                    b = b + 1.0;
                else
                    print("Menor");
                end
                
                while a > 0 do
                   print(a);
                   a = a - 1;
                end
                """;

        try {
            MiniLuaLexer lexer = new MiniLuaLexer(CharStreams.fromString(codigo));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            MiniLuaParser parser = new MiniLuaParser(tokens);

            // Gera a árvore sintática
            ParseTree tree = parser.programa();

            if (parser.getNumberOfSyntaxErrors() > 0) {
                System.out.println("Erro de Sintaxe detectado!");
                return;
            }

            // Análise Semântica
            System.out.println("--- Iniciando Análise Semântica ---");
            AnalisadorSemantico semantico = new AnalisadorSemantico();
            semantico.visit(tree);
            System.out.println("--- Compilação Finalizada ---");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}