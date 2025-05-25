package visao;

import entidades.Ator;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import aeds3.ArvoreBMais;
import modelo.ArquivoAtores;
import modelo.ElementoLista;
import modelo.ListaInvertida;
import modelo.ParIdIdAS;
import modelo.ParNameAtorID;
import modelo.StopWords;
import modelo.ParIdIdAS;

public class MenuAtores {

    private ListaInvertida listaInvertidaAtores;
    private static final String ARQ_DICIONARIO_ATORES = "dados/indice/dicionario_atores.db";
    private static final String ARQ_BLOCOS_ATORES = "dados/indice/blocos_atores.db";
    private static final int QTDE_REGISTROS_POR_BLOCO = 5;

    private ArquivoAtores arquivo;
    private Scanner scanner;
    ArvoreBMais<ParNameAtorID> arvore;
    ArvoreBMais<ParIdIdAS> arvore2;

    public MenuAtores() {
        try {
            arquivo = new ArquivoAtores();
            File d = new File("dados/Arvores");
            if(!d.exists()) { d.mkdir(); }
            
            File i = new File("dados/indice");
            if(!i.exists()) { i.mkdir(); }
            
            arvore = new ArvoreBMais<>(ParNameAtorID.class.getConstructor(), 5, "dados/Arvores/arvoreAtorNomeId.db");
            arvore2 = new ArvoreBMais<>(ParIdIdAS.class.getConstructor(), 5, "dados/Arvores/arvoreAtorSerie.db");
            
            // Inicializa a lista invertida para atores
            listaInvertidaAtores = new ListaInvertida(
                QTDE_REGISTROS_POR_BLOCO, 
                ARQ_DICIONARIO_ATORES, 
                ARQ_BLOCOS_ATORES
            );
        } catch (Exception e) {
            System.out.println("Erro ao inicializar o arquivo de atores: " + e.getMessage());
        }
        scanner = new Scanner(System.in);
    }

    public void exibirMenu() {
        int opcao = -1;

        while (opcao != 0) {
            System.out.println("\n----- MENU DE ATORES -----");
            System.out.println("1 - Cadastrar ator");
            System.out.println("2 - Buscar ator por nome");
            System.out.println("3 - Buscar ator por termos"); 
            System.out.println("4 - Atualizar ator");
            System.out.println("5 - Remover ator");
            System.out.println("6 - Listar todos os atores");
            System.out.println("7 - Listar a serie de cada ator");
            System.out.println("0 - Voltar");
            System.out.print("Escolha uma opção: ");

            opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {
                case 1 -> cadastrar();
                case 2 -> buscarPorNome();
                case 3 -> buscarPorTermos(); 
                case 4 -> atualizar();
                case 5 -> remover();
                case 6 -> listarTodos();
                case 7 -> listarSeriesPorAtor();
                case 0 -> System.out.println("Retornando...");
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    private void indexarTermosAtor(Ator ator) throws Exception {
        // Indexa o nome do ator
        String texto = ator.getNome().toLowerCase();
        String[] palavras = texto.split("[^a-zA-ZáéíóúÁÉÍÓÚâêîôÂÊÎÔãõÃÕçÇ]+");
        
        for (String palavra : palavras) {
            palavra = palavra.trim();
            if (!palavra.isEmpty() && palavra.length() > 2 && !StopWords.isStopWord(palavra)) {
                ElementoLista elemento = new ElementoLista(ator.getID(), 1.0f);
                listaInvertidaAtores.create(palavra, elemento);
            }
        }
        
        // Indexa a nacionalidade (opcional)
        texto = ator.getNacionalidade().toLowerCase();
        palavras = texto.split("[^a-zA-ZáéíóúÁÉÍÓÚâêîôÂÊÎÔãõÃÕçÇ]+");
        
        for (String palavra : palavras) {
            palavra = palavra.trim();
            if (!palavra.isEmpty() && palavra.length() > 2 && !StopWords.isStopWord(palavra)) {
                ElementoLista elemento = new ElementoLista(ator.getID(), 0.7f); // Peso menor para nacionalidade
                listaInvertidaAtores.create(palavra, elemento);
            }
        }
    }

    private void cadastrar() {
        try {
            System.out.print("Nome: ");
            String nome = scanner.nextLine();
            System.out.print("Nacionalidade: ");
            String nacionalidade = scanner.nextLine();
            System.out.print("Data de nascimento (AAAA-MM-DD): ");
            LocalDate dataNascimento = LocalDate.parse(scanner.nextLine());

            Ator ator = new Ator(-1, nome, nacionalidade, dataNascimento);
            int id = arquivo.create(ator);
            ator.setID(id); // Garante que o ID está definido
            indexarTermosAtor(ator); // Adiciona esta linha
            System.out.println("Ator cadastrado com ID: " + id);
            
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar ator: " + e.getMessage());
        }
    }

    private void buscarPorTermos() {
        System.out.println("\nBusca de atores por termos");
        System.out.print("\nDigite os termos de busca (nome ou nacionalidade): ");
        String termos = scanner.nextLine().toLowerCase();
        
        if(termos.isEmpty()) {
            return;
        }
        
        try {
            String[] palavras = termos.split("[^a-zA-ZáéíóúÁÉÍÓÚâêîôÂÊÎÔãõÃÕçÇ]+");
            Set<Integer> idsEncontrados = new HashSet<>();
            boolean primeiroTermo = true;
            
            for (String palavra : palavras) {
                palavra = palavra.trim();
                if (palavra.isEmpty() || palavra.length() < 2) continue;
                
                ElementoLista[] elementos = listaInvertidaAtores.read(palavra);
                if (elementos != null && elementos.length > 0) {
                    Set<Integer> idsTermo = new HashSet<>();
                    for (ElementoLista elemento : elementos) {
                        idsTermo.add(elemento.getId());
                    }
                    
                    if (primeiroTermo) {
                        idsEncontrados.addAll(idsTermo);
                        primeiroTermo = false;
                    } else {
                        // Interseção para busca AND
                        idsEncontrados.retainAll(idsTermo);
                    }
                } else {
                    // Se um termo não for encontrado, limpa os resultados
                    idsEncontrados.clear();
                    break;
                }
            }
            
            // Mostrar resultados
            if (idsEncontrados.isEmpty()) {
                System.out.println("Nenhum ator encontrado com esses termos.");
            } else {
                System.out.println("\nAtores encontrados (" + idsEncontrados.size() + "):");
                List<Ator> atoresEncontrados = new ArrayList<>();
                
                for (int id : idsEncontrados) {
                    Ator a = arquivo.read(id);
                    if (a != null) {
                        atoresEncontrados.add(a);
                    }
                }
                
                // Ordenar por nome do ator
                atoresEncontrados.sort(Comparator.comparing(Ator::getNome));
                
                int contador = 1;
                for (Ator a : atoresEncontrados) {
                    System.out.println(contador++ + " - " + a.getNome() + 
                                    " (" + a.getNacionalidade() + ")");
                }
                
                // Opção para ver detalhes
                if (!atoresEncontrados.isEmpty()) {
                    System.out.print("\nDigite o número do ator para ver detalhes (0 para voltar): ");
                    try {
                        int opcao = Integer.parseInt(scanner.nextLine());
                        if (opcao > 0 && opcao <= atoresEncontrados.size()) {
                            System.out.println(atoresEncontrados.get(opcao-1));
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Opção inválida.");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao realizar a busca.");
            e.printStackTrace();
        }
    }

    private void buscarPorNome() {
        try {
            System.out.print("Nome a buscar: ");
            String nome = scanner.nextLine();
            Ator[] atores = arquivo.readNome(nome);
            for (int i = 0; i < atores.length; i++ ){
                System.out.println(atores[i]);
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar ator: " + e.getMessage());
        }
    }

    private void atualizar() {
        try {
            System.out.print("ID do ator a atualizar: ");
            int id = Integer.parseInt(scanner.nextLine());
            
            Ator ator = arquivo.read(id);

            if (ator == null) {
                System.out.println("Ator não encontrado.");
                return;
            }

            System.out.println(ator);
            System.out.print("Novo nome: ");
            String nome = scanner.nextLine();
            System.out.print("Nova nacionalidade: ");
            String nacionalidade = scanner.nextLine();
            System.out.print("Nova data de nascimento (AAAA-MM-DD): ");
            LocalDate dataNascimento = LocalDate.parse(scanner.nextLine());

            // Remove índices antigos
            listaInvertidaAtores.delete(Integer.toString(ator.getID()), ator.getID());
            
            ator.setNome(nome);
            ator.setNacionalidade(nacionalidade);
            ator.setDataNascimento(dataNascimento);

            boolean sucesso = arquivo.update(ator);
            if (sucesso) {
                // Reindexa com os novos dados
                indexarTermosAtor(ator);
                System.out.println("Ator atualizado com sucesso.");
            } else {
                System.out.println("Falha na atualização.");
            }

        } catch (Exception e) {
            System.out.println("Erro ao atualizar ator: " + e.getMessage());
        }
    }

    private void remover() {
        try {
            System.out.print("ID do ator a remover: ");
            int id = Integer.parseInt(scanner.nextLine());
            
            // Remove os índices primeiro
            listaInvertidaAtores.delete(Integer.toString(id), id);
            
            boolean sucesso = arquivo.delete(id);
            if (sucesso) {
                System.out.println("Ator removido com sucesso.");
            } else {
                System.out.println("Ator não encontrado.");
            }

        } catch (Exception e) {
            System.out.println("Erro ao remover ator: " + e.getMessage());
        }
    }

    private void listarTodos() {
    try {
        System.out.println("\n--- Lista de Todos os Atores ---");
        ArrayList<ParNameAtorID> pares = arvore.read(null); // read(null) retorna todos os registros

        if (pares == null || pares.isEmpty()) {
            System.out.println("Nenhum ator encontrado.");
            return;
        }

        ArrayList<Integer> idsUnicos = new ArrayList<>();
        for (ParNameAtorID par : pares) {
            if (!idsUnicos.contains(par.getId())) {
                idsUnicos.add(par.getId());
            }
        }

        for (int id : idsUnicos) {
            Ator ator = arquivo.read(id);
            if (ator != null && ator.getNome() != null && !ator.getNome().isEmpty()) {
                System.out.println(ator);
            }
        }

    } catch (Exception e) {
        System.out.println("Erro ao listar todos os atores: " + e.getMessage());
    }
}
private void listarSeriesPorAtor() {
        System.out.println("\nBUSCA");
        try {
            System.out.print("ID do ator: ");
            int idAtor = Integer.parseInt(scanner.nextLine());
            ArrayList<ParIdIdAS> lista = arvore2.read(new ParIdIdAS(idAtor, -1));
            System.out.print("Resposta: ");
            for (int i = 0; i < lista.size(); i++)
              System.out.print(lista.get(i) + " ");
            /*
            int[] series = arquivoRelacoes.buscarSeriesPorAtor(idAtor);
            if (series.length == 0) {
                System.out.println("Nenhuma série encontrada para este ator.");
                return;
            } 
            System.out.println("Séries desse ator:");
            for (int idSerie : series) {
                Serie s = arquivoSeries.read(idSerie);
                if (s != null)
                    System.out.println(idSerie + " - " + s.getNome());
            }*/
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }


}