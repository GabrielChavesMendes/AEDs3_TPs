package visao;

import entidades.Ator;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

import aeds3.ArvoreBMais;
import modelo.ArquivoAtores;
import modelo.ParIdIdAS;
import modelo.ParNameAtorID;
import modelo.ParIdIdAS;

public class MenuAtores {

    private ArquivoAtores arquivo;
    private Scanner scanner;
    ArvoreBMais<ParNameAtorID> arvore;
    ArvoreBMais<ParIdIdAS> arvore2;
    public MenuAtores() {
        try {
            arquivo = new ArquivoAtores();
            File d = new File("dados/Arvores");
            if(!d.exists()){d.mkdir();}
            arvore=new ArvoreBMais<>(ParNameAtorID.class.getConstructor(),5,"dados/Arvores/arvoreAtorNomeId.db");
            arvore2 = new ArvoreBMais<>(ParIdIdAS.class.getConstructor(), 5, "dados/Arvores/arvoreAtorSerie.db");

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
            System.out.println("3 - Atualizar ator");
            System.out.println("4 - Remover ator");
            System.out.println("5 - Listar todos os atores");
             System.out.println("6 - Listar a serie de cada ator");
            System.out.println("0 - Voltar");
            System.out.print("Escolha uma opção: ");

            opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {
                case 1 -> cadastrar();
                case 2 -> buscarPorNome();
                case 3 -> atualizar();
                case 4 -> remover();
                case 5 -> listarTodos();
                case 6 -> listarSeriesPorAtor();
                case 0 -> System.out.println("Retornando...");
                default -> System.out.println("Opção inválida.");
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
            System.out.println("Ator cadastrado com ID: " + id);
            
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar ator: " + e.getMessage());
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
            
            
            System.out.println("Digite o nome do ator:");
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

            ator.setNome(nome);
            ator.setNacionalidade(nacionalidade);
            ator.setDataNascimento(dataNascimento);

            boolean sucesso = arquivo.update(ator);
            if (sucesso) {
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
            System.out.print("Nome: ");
            String nome = scanner.nextLine();
            /*arvore.delete(new ParNameAtorID(nome, id));
            arvore.print();*/
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