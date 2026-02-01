package br.com.minilua;

import java.util.HashMap;
import java.util.Map;

public class TabelaDeSimbolos {
    // Mapa para guardar Nome da Variável, Tipo da Variável
    private Map<String, String> tabela = new HashMap<>();

    // Adiciona uma variável na memória
    public void adicionar(String nome, String tipo) {
        tabela.put(nome, tipo);
    }

    // Verifica se a variável já existe
    public boolean contem(String nome) {
        return tabela.containsKey(nome);
    }

    // Retorna o tipo da variável
    public String getTipo(String nome) {
        return tabela.get(nome);
    }
}