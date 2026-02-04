# Documentação dos Testes – MiniLua

Este arquivo descreve os testes automatizados implementados para a linguagem **MiniLua**.
Os testes têm como objetivo validar tanto a **sintaxe** (gramática) quanto a **semântica**
(regras de tipos e escopo) da linguagem.

Os testes foram implementados utilizando **JUnit** em conjunto com o **ANTLR 4**.

---

## Estrutura Geral dos Testes

A classe de testes principal é `MiniLuaTest`, responsável por executar:

- Testes sintáticos (parser / gramática)
- Testes semânticos (analisador semântico)

Todos os testes operam sobre códigos MiniLua fornecidos como `String`.

---

## Método Auxiliar de Parser

Para evitar repetição de código, foi criado um método auxiliar responsável
por inicializar o lexer e o parser do ANTLR:

```java
private MiniLuaParser getParser(String codigo) {
    MiniLuaLexer lexer = new MiniLuaLexer(CharStreams.fromString(codigo));
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    return new MiniLuaParser(tokens);
}
```

Esse método:

1. Converte o código-fonte em um fluxo de caracteres
2. Realiza a análise léxica (tokens)
3. Inicializa o parser com os tokens gerados

---

## Testes Sintáticos (Gramática)

Os testes sintáticos verificam se o código está de acordo com as regras
definidas na gramática da linguagem MiniLua.

### Declaração de Variável Válida

```java
int x;
```

Esse teste verifica que uma declaração válida não gera erros sintáticos.

---

### Atribuição com Erro de Sintaxe

```java
x = 10
```

Nesse caso, o ponto e vírgula está ausente.
O teste garante que o parser detecta corretamente esse erro.

---

### Estrutura Condicional Completa (`if / then / else / end`)

```java
if x > 0 then
    y = 2;
else
    y = 3;
end
```

Esse teste valida o correto reconhecimento de estruturas condicionais completas.

---

## Testes Semânticos

Os testes semânticos são responsáveis por verificar regras que não podem ser
capturadas apenas pela gramática, como:

- Uso de variáveis não declaradas
- Compatibilidade de tipos
- Execução correta de códigos válidos

Para isso, é utilizado o `AnalisadorSemantico`, que percorre a árvore sintática
gerada pelo parser.

---

### Uso de Variável Não Declarada

```java
x = 10;
```

Esse teste verifica se o analisador semântico detecta o uso de uma variável
que não foi previamente declarada.

---

### Atribuição com Tipos Incompatíveis

```java
int x;
x = 5.5;
```

Nesse caso, um valor do tipo `float` é atribuído a uma variável `int`.
O teste garante que o erro semântico seja corretamente identificado.

---

### Execução Semântica Válida

```java
int x;
x = 10;
float y;
y = 5.5;
```

Esse teste garante que um código semanticamente correto não gere mensagens de erro.

---

## Considerações Finais

Os testes automatizados garantem que:

- A gramática da linguagem está corretamente definida
- O analisador semântico aplica as regras de escopo e tipos
- Erros são detectados e reportados de forma consistente
