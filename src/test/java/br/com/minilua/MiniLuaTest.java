package br.com.minilua;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Essa classe MiniLuaTest testa duas coisas principais:
 * - Sintaxe (gramática) -> se o código está bem formado
 * - Semântica -> se o código faz sentido (variáveis declaradas, tipos compatíveis etc.)
 * 
 * Ela valida o lexer, o parser e o analisador semântico da linguagem MiniLua.
 *
 * @author isabelle
 */
public class MiniLuaTest {

    // Método auxiliar para facilitar a execução do parser nos testes
    private MiniLuaParser getParser(String codigo) {
        MiniLuaLexer lexer = new MiniLuaLexer(CharStreams.fromString(codigo));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        return new MiniLuaParser(tokens);
    }

    // --- TESTES SINTÁTICOS (Gramática) ---

    @Test
    public void testDeclaracaoVariavelValida() {
        String codigo = "int x;";
        MiniLuaParser parser = getParser(codigo);
        parser.programa(); // Executa a regra inicial

        // Verifica se não houve erros de sintaxe (falta de ';', etc)
        Assertions.assertEquals(0, parser.getNumberOfSyntaxErrors(), "Não deve haver erros sintáticos.");
    }

    @Test
    public void testAtribuicaoInvalidaSintaxe() {
        String codigo = "x = 10"; // Faltou ponto e vírgula
        MiniLuaParser parser = getParser(codigo);
        parser.programa();

        // Deve ter pelo menos 1 erro de sintaxe
        Assertions.assertTrue(parser.getNumberOfSyntaxErrors() > 0, "Deve detectar a falta de ponto e vírgula.");
    }

    @Test
    public void testEstruturaIfCompleta() {
        String codigo = "if x > 0 then y = 2; else y = 3; end";
        MiniLuaParser parser = getParser(codigo);
        parser.programa();
        Assertions.assertEquals(0, parser.getNumberOfSyntaxErrors());
    }

    // --- TESTES SEMÂNTICOS (Tipos e Regras) ---

    @Test
    public void testSemanticoUsoVariavelNaoDeclarada() {
        String codigo = "x = 10;"; // x não foi declarado
        
        // Configura para capturar o que seria impresso no console
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Executa Parser e Semântico
        MiniLuaParser parser = getParser(codigo);
        ParseTree tree = parser.programa();
        AnalisadorSemantico semantico = new AnalisadorSemantico();
        semantico.visit(tree);

        // Verifica se a mensagem de erro esperada apareceu na saída
        // Baseado na mensagem do seu AnalisadorSemantico.java: "A variável 'x' não foi declarada"
        Assertions.assertTrue(outContent.toString().contains("não foi declarada"), 
            "Deve acusar erro de variável não declarada.");
    }

    @Test
    public void testSemanticoTiposIncompativeis() {
        String codigo = "int x; x = 5.5;"; // Atribuindo float em int
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        MiniLuaParser parser = getParser(codigo);
        ParseTree tree = parser.programa();
        new AnalisadorSemantico().visit(tree);

        // Tenta "Pegar" do log a string  "Atribuição incompatível" que foi lançada na linha [58] da classe AnalisadorSemantico.java
        Assertions.assertTrue(outContent.toString().contains("Atribuição incompatível"), 
            "Deve acusar erro de tipos incompatíveis.");
    }
    
    @Test
    public void testSemanticoSucesso() {
        String codigo = "int x; x = 10; float y; y = 5.5;";
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        MiniLuaParser parser = getParser(codigo);
        ParseTree tree = parser.programa();
        new AnalisadorSemantico().visit(tree);

        // Se deu certo, não deve ter a palavra "Erro" na saída
        Assertions.assertFalse(outContent.toString().contains("Erro"), 
            "Código válido não deve gerar mensagens de erro.");
    }
}