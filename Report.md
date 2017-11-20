# Experimental

1) Design of the tool
A ferramenta foi desenvolvida em Java apenas por preferência do grupo.
Decidimos usar um parser off the shelve chamado Gson, criado pela Google, que lê as slices de php fornecidas em formato JSON e cria objectos da classe JSONElement.
![alt text](https://github.com/Davidanil/project25/blob/master/php-vuln-finder/Untitled%20Diagram%20(2).png)
A estrura do programa foi dividida em :
App - tem a função main que inicia o processo da ferramenta. Esta classe chama as funções processPatternFile, que devolve uma lista de Pattern, createTreeNode, que gera uma àrvore de nós a partir da raiz program e printFinalState, que imprime na consola se slice era vulnerável, e caso não fosse, quais as funções de sanitizaço que ajudaram a limpar o programa.
Pattern - classe representativa dos padres lidos no ficheiro pattern.txt. Cria uma lista de todos os patterns para vir a ser usado pela App.
Analyser - classe que cria a àrvore de nós e executa uma análise estática na mesma. A análise é feita à medida que a àrvore é criada para tornar a pesquisa mais eficiente, aproveitando as chamadas recursivas utilizadas na criação da àrvore de nós.
Node - classe abstracta, com os atributos e funções comuns a todos os nós. Esta classe implementa a interface NodeInterface com o protótipo dos métodos comuns a todos os nós, getChildren e addChildren mas com implementações diferentes.


2) The main design options
3) Output of the tool for a few examples
4) Guarantees and limitations provided by the tool


Referências:
https://github.com/google/gson/blob/master/UserGuide.md#TOC-Array-Examples
https://github.com/glayzzle/php-parser/blob/master/docs/AST.md#bin




# Teórica



Limitações da Análise Estática:
1. A análise estática depende dos padrões criados para a analisar o código. Caso esses padrões estejam incompletos, a análise estática irá indicar falsos negativos.
1. Erros ou vulnerabilidades em runtime não são detectadas.


</br>

Como evitar falsos positivos:
1. Receber do programador se determinada vulnerabilidade é um falso positivo. Essa informação seria usada na criação de uma heurística para prevenir que o mesmo falso positivo apareça novamente no resultado da análise.  





Referencias
https://www.owasp.org/index.php/Static_Code_Analysis
https://www.cs.umd.edu/~pugh/BugWorkshop05/papers/34-chou.pdf
