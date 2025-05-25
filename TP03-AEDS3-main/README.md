# TP03-AEDS3
TP 03 - Índice Invertido 

# Participantes:
 - Alessandra Faria Rodrigues
 - Débora Luiza de Paula Silva
 - Gabriel Chaves Mendes
 - Gabriela de Assis dos Reis

# O que o nosso trabalho faz?
Este projeto tem como objetivo implementar um sistema de busca eficiente para séries, episódios e atores utilizando índices invertidos e o cálculo do valor TFxIDF (Term Frequency × Inverse Document Frequency). 
Após uma análise inicial, começamos a explorar o código base já existente do trabalho anterior (TP2), entendendo como as entidades estavam organizadas e de que forma poderíamos integrar o novo mecanismo de busca. Inicialmente, nos dedicamos à construção da estrutura de índice invertido, que exigiu o pré-processamento dos textos (títulos e nomes), com a remoção de stop words, normalização das palavras (retirada de acentos e conversão para minúsculas) e cálculo da frequência dos termos. A partir disso, implementamos as listas invertidas para mapear cada termo às entidades correspondentes, registrando também a frequência relativa (TF) dos termos. 
Com as listas invertidas funcionando, partimos para a implementação da lógica de busca. Para isso, desenvolvemos o cálculo dinâmico do IDF (inverso da frequência dos documentos) com base no total de registros e na quantidade de entidades que contêm cada termo. Em seguida, aplicamos o cálculo do TFxIDF para gerar uma pontuação de relevância, que serviu de base para ordenarmos os resultados da busca. As buscas foram integradas às três entidades do sistema: séries, episódios e atores. Em cada uma delas, garantimos que a consulta passasse pelo índice invertido, substituindo as formas de busca tradicionais por um sistema mais eficiente e inteligente.

# Repositório no GitHub: https://github.com/GabrielChavesMendes/AEDs3_TPs.git

# Experiência do grupo:
1. O índice invertido com os termos dos títulos das séries foi criado usando a classe ListaInvertida?
R: Sim, as palavras das séries foi criado usando Lista Invertida 

2. O índice invertido com os termos dos títulos dos episódios foi criado usando a classe ListaInvertida?
R:Sim, os termos dos títulos dos episódios foi criado usando Lista Invertida

3. O índice invertido com os termos dos nomes dos atores foi criado usando a classe ListaInvertida?
R: Sim, atores foi criado usando Lista Invertida

4. É possível buscar séries por palavras usando o índice invertido?
R: Sim, é possível fazer a busca de séries por palavras usando o índice invertido

5. É possível buscar episódios por palavras usando o índice invertido?
R: Sim, é possível fazer a busca de episódios por palavras usando o índice invertido

6. É possível buscar atores por palavras usando o índice invertido?
R: Sim, é possível fazer a busca de atores por palavras usando o índice invertido

7. O trabalho está completo?
R: Sim, o trabalho está completo

8. O trabalho é original e não a cópia de um trabalho de um colega?
R: O trabalho é totalmente original 
