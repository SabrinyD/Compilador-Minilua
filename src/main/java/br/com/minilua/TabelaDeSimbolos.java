package br.com.minilua;

import java.util.HashMap;
import java.util.Map;

public class TabelaDeSimbolos {
    // Mapa: Nome da Variável -> Tipo (ex: "x" -> "int")
    private Map<String, String> tabela = new HashMap<>();

    // O famoso método "adSimb" sugerido nas fontes
    public boolean adicionar(String nome, String tipo) {
        if (tabela.containsKey(nome)) {
            return false; // Erro: já existe
        }
        tabela.put(nome, tipo);
        return true; // Sucesso
    }

    public boolean existe(String nome) {
        return tabela.containsKey(nome);
    }

    public String verificarTipo(String nome) {
        return tabela.get(nome);
    }
}