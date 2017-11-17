# A Fazer
* ~~Falta por o atributo __level__ a funcionar na classe __Node__.~~
* A função createTreeNode da __App__ está um bocado grande demais. Eu meti lá dentro dois ToDo, para se simplificar aquilo, inclusivé com um link de como começar.
* Criar classes abstractas menos generalistas que a __Node__, como por exemmplo a __Statement__ e __Expression__. Estas classes estão no readme do php-parser que gera as slices.
* Criar restantes classes para analisar qual programa php. Estas classes estão no readme do php-parser que gera as slices. A análise básica (vale 75% da parte prática) do nosso projecto pede que sejam criados os nós relacionados com assignments e expressions (function call inclusivé). Condições ou ciclos não são necessários para a análise básica, apenas para a avançada. Faltam as seguintes classes/nós:
   * __ConstRef__ - esta relacionado com strings entre plicas
   * __Encapsed__ - variaveis dentro de strings
   * __Number__ - nó terminal para numeros, sejam ints ou floats
   * ![#f03c15](https://placehold.it/15/f03c15/000000?text=+) `Analise Avancada` : __If__
   * ![#f03c15](https://placehold.it/15/f03c15/000000?text=+) `Analise Avancada` : __While__
   * ![#f03c15](https://placehold.it/15/f03c15/000000?text=+) `Analise Avancada` : __Block__  - para as condicoes e ciclos
   * Outros que não estejam nas slices exemplo...
* Completar as classes já feitas com os atributos necessários para a análise. Neste momento têm apenas as necessárias para criar a treeNode.
* Criar [todos os patterns](http://awap.sourceforge.net/support.html) necessários para encontrar todas as vulns em PHP.
* __Começar a implementar o Algoritmo__, ou seja, começar a detectar valores que estejam incluidos nos nossos patterns.

## Estrutura do projecto
* __App__ - tem o main que chama a função createTreeNode e printTreeNode também elas na classe __App__.
* __Pattern__ - classe que lê o ficheiro pattern.txt e cria uma lista de todos os patterns, para eventualmente vir a ser usado pela __App__.
* Pasta Node 
   * tem a classe abstracta __Node__ com os atributos e funções comuns a todos os nós.
   * tem a interface __NodeInterface__ com o protótipo dos métodos comuns a todos os nós mas com implementações diferentes.
   * todos os nós necessários para a splice1.txt e splice2.txt

## UML da coisa
![alt text](https://github.com/Davidanil/project25/blob/master/php-vuln-finder/Untitled%20Diagram%20(2).png)

## Ponto da situação
* Houve coisas que eu não percebi bem no código que já estava feito, como por exemplo o atributo vuln do Node, portanto não adicionei. A funçao crisscross deixei-a lá mas não estou a ver onde será usada.
* O programa está a ler o ficheiro pattern.txt e gera uma lista de patterns, mas esta ainda não é usada em lado nenhum.
* O programa lê as slice1 e slice2 na boa e gera as arvores correspondentes. No entanto, acho que faltam alguns atributos aos Nós no geral. Eu só criei aqueles que iria precisar como __kind__, __name__ para variables, __what__ e __arguments__ para call... Para ver todos os atributos basta ir à documentaço do php-parser.
* Todos os nós estão a herdar de Node. Para agora funfa mas idealmente iremos criar mais nós abtractos como Statement e Expression que herdam de Node, e os nossos nós herdaram directamente destes e não de node. OOP FWT ... not

## Algoritmo todo XPTO
* O objetivo é percorrer a àrvore o minimo numero de vezes.
* Para esta solução, é encontrado o output desejado à medida que se constroí a àrvore, portanto ela __idealmente__ será percorrida 1 vez.
* Existirão 4 componentes:
   * __vulnerable__ - boolean que indica se slice é vulnerável ou não.
   * __safeVars__ - lista com os nós das variáveis seguras (isto é, sem inputs do user, portanto que não contenham as entrypoints de nenhum pattern)
   * __sanitizationVars__ - lista com nós das variáveis com funções de sanitização das patterns.
   * __sinkVars__ - lista com nós das variáveis que foram usadas em sink functions.

1. À medida que parsing é feito, são adicionadas à lista __safeVars__ os nós das variaveis seguras, e à lista __sanitizationVars__  os nós das funções de sanitização.
1. Caso passe numa sink function:
   1. e argumento não esteja das lista __safeVars__ ou __sanitizationVars__, actualizar variável global __vulnerable__ para TRUE.
   1. Caso contrário, se argumento tiver na lista __sanitizationsVars__, guardar esse nó também na lista __sinkVars__.
1. O output do programa deve indicar se houve ou não vulnerabilidades detectadas na slice e, se __não__ houve, indicar as funções de sanitização que ajudaram a limpar o programa (__sinkVars__).

## Diagrama das listas usadas no Algoritmo
![alt text](https://github.com/Davidanil/project25/blob/master/php-vuln-finder/Untitled%20Diagram%20(3).png)

## Links úteis
* [PHP-PARSER](https://github.com/glayzzle/php-parser/blob/master/docs/AST.md) - o criador de slices
* [Gjson - github](https://github.com/google/gson/blob/master/UserGuide.md) - usado para fazer o parse das slices
* [Gjson - documentação](http://www.javadoc.io/doc/com.google.code.gson/gson/2.8.2) - documentaço estilo javadoc

