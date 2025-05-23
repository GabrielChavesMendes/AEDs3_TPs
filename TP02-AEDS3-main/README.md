# TP02-AEDS3
TP 02 - Relação N:N

# Participantes:
 - Alessandra Faria Rodrigues
 - Débora Luiza de Paula Silva
 - Gabriel Chaves Mendes
 - Gabriela de Assis dos Reis

# O que o nosso trabalho faz?
 Analisamos os critérios e especificações do trabalho antes de inicia-lo e após essa analise, o grupo, sempre que possível, se reunia para analisar o código disponibilizado e começar as alterações com base no que a atividade pedia. Primeiro fizemos o CRUD de séries, alterando e corrigindo erros e logo após essa etapa focamos em implementar o CRUD de episódios com todas as suas restrições, utilizando os códigos disponibilizados e estruturas como Arvore B+ e Tabela Hash Extensível. Durante o projeto sempre nos atentamos nas restrições, como não permitir a inclusão de episódio sem série. No fim, construímos um projeto que cria um catálogo de séries com seus referentes episódios, podendo fazer inclusão, alteração, exclusão e listagem de séries e episódios. Além disso, foi adicionado a opção de CRUD dos atores nas séries, em uma relação N:N onde garante que seja possível ver quais séries o ator participou e os atores que participaram de uma série.

# Repositório no GitHub: https://github.com/GabrielChavesMendes/AEDs3_TPs.git

# Experiência do grupo:
1.As operações de inclusão, busca, alteração e exclusão de atores estão implementadas e funcionando corretamente? 
R: Sim, todas as operações do CRUD estão implementadas e funcionando corretamente.

2.O relacionamento entre séries e atores foi implementado com árvores B+ e funciona corretamente, assegurando a consistência entre as duas entidades?
R: Sim, para cada par de relacionamento foi implementado um par de ids, que funcionam perfeitamente e quando há uma exclusão de ator ele verifica se o ator esta vinculado a alguma serie, se sim, então não exclui.

3.É possível consultar quais são os atores de uma série?
R: Sim, é possível.

4.É possível consultar quais são as séries de um ator?
R: Sim, é possível.

5.A remoção de séries remove os seus vínculos de atores?
R: Sim, ao remover a serie ele remove o vículo com o ator;

6.A inclusão de um ator em uma série se limita aos atores existentes?
R: Sim, você só pode vincular um ator que exista.

7.A remoção de um ator checa se há alguma série vinculado a ele?
R: Sim, antes de excluir o ator é checado se há algum vinculo entre ele e a série.

8.O trabalho está funcionando corretamente?
R: Sim, o trabalho está funcionando corretamente.

9.O trabalho está completo?
R: Sim, o trabalho está completo.

10.O trabalho é original e não a cópia de um trabalho de outro grupo?
R: Sim, o trabalho é original.