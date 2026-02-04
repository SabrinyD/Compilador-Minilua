# Compilador MiniLua

Este projeto consiste no desenvolvimento de um compilador para a linguagem **MiniLua**, um subconjunto funcional da linguagem Lua, desenvolvido como requisito para a disciplina de Compiladores.

## Sobre a Linguagem

A **MiniLua** é uma linguagem acadêmica baseada em algoritmos estruturados básicos, suportando tipos primitivos, expressões aritméticas e estruturas de controle de fluxo.

### Escopo do Compilador:

- **Tipos de Dados:** Inteiros (`int`), Ponto Flutuante (`float`) e Texto (`string`).

- **Entrada e Saída:** Comandos para leitura (`read`) e escrita (`print`) de dados.

- **Estruturas de Controle:** Desvio condicional (`if/then/else/end`) e repetição (pelo menos `while` ou `for`).

- **Expressões:** Suporte completo a operações aritméticas, relacionais e lógicas com respeito à precedência de operadores.

## Tecnologias Utilizadas

- **Linguagem de Implementação:** Java 17.
- **Gerador de Parser/Lexer:** ANTLR 4.13.1.
- **Gerenciamento de Dependências:** Maven.
- **Controle de Versão:** Git/GitHub.

## Estrutura do Projeto

A estrutura segue o padrão Maven, com as gramáticas localizadas na pasta de recursos do ANTLR:

- `src/main/antlr4/br/com/minilua/MiniLua.g4`: Definição formal da gramática (Léxico e Sintático).
- `src/main/java/br/com/minilua/`: Código fonte da implementação do compilador, incluindo analisadores e geradores de código.
- `pom.xml`: Configurações de build e dependências do projeto.

## Como Executar

1. **Pré-requisitos:** Certifique-se de ter o JDK 17 e o Maven instalados.
2. **Compilação:** No diretório raiz, execute:

```bash
mvn clean compile

```

_Este comando gerará automaticamente as classes do ANTLR com base no arquivo `.g4_`.
3. **Execução:** (Adicione aqui o comando principal de execução da sua classe `Main.java`).

## Cronograma de Entregas

- [x] **Entrega 1:** Gramática da Linguagem (BNF/EBNF).

- [x] **Entrega 2:** Analisador Léxico (Scanner) com testes unitários.

- [x] **Entrega 3:** Analisador Sintático e Semântico (Parser) com árvore AST.

- [x] **Entrega 4:** Gerador de Código Intermediário (Código de Três Endereços).

- [ ] **Entrega 5:** Ajustes Finais e Apresentação.

## Integrantes

- Éric Santos de Jesus
- Isabelle Saahirah Ribeiro de Lima
- Sabriny Cavalcante Dantas

---

Este projeto é estritamente acadêmico e segue as diretrizes de Engenharia de Software focadas em código limpo, modularização e testes unitários.
