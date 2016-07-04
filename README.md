# 2016-so-projeto
Projeto da disciplina SO


Grupo:
-----

*[André Pessoni]()

*[Catharina Bermal]()

*[Jessica Temporal](https://github.com/jtemporal)

*[João Paulo Peres]()


Descrição do Projeto
-----

Implementar em Java um sistema que simula parte do módulo de organização de
arquivo de um sistema de gerenciamento de arquivos. Em particular, será
simulado a alocação e liberação de espaço livre (blocos) do disco de forma
simplificada.
Políticas as serem implementadas:
- Vetor de Bits;
- Lista Interligada;

Uso
-----
inicialização:

```
git clone https://github.com/jtemporal/2016-so-projeto/
cd 2016-so-projeto
javac *.java
```
Para instanciar as classes de acordo com as políticas, em seu terminal rode:

java FileOrganizationModuleSimulator [nome da política] [nome do arquivo de teste]

```
# para vetor de bits
java FileOrganizationModuleSimulator vetor teste.txt
# para lista interligada
java FileOrganizationModuleSimulator lista teste.txt
```
