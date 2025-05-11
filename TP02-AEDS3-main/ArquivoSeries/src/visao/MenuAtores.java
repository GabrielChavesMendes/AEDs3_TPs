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

public class MenuAtores {

    private ArquivoAtores arquivo;
    private Scanner scanner;
    ArvoreBMais<ParNameAtorID> arvore;
    public MenuAtores() {
        try {
            arquivo = new ArquivoAtores();
            File d = new File("dados/Arvores");
            if(!d.exists()){d.mkdir();}
            arvore=new ArvoreBMais<>(ParNameAtorID.class.getConstructor(),5,"dados/Arvores/arvoreAtorNomeId.db");
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
            System.out.println("0 - Voltar");
            System.out.print("Escolha uma opção: ");

            opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {
                case 1 -> cadastrar();
                case 2 -> buscarPorNome();
                //case 3 -> atualizar();
                case 4 -> remover();
                //case 5 -> listarTodos();
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
            arvore.create(new ParNameAtorID(nome, id));
            arvore.print();
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
            //busca por nome na arvore
           ArrayList<ParNameAtorID> lista = arvore.read(new ParNameAtorID(nome, -1));
            System.out.print("Resposta: ");
            for (int i = 0; i < lista.size(); i++)
              System.out.print(lista.get(i) + " ");

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

            exibirAtor(ator);

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
            arvore.delete(new ParNameAtorID(nome, id));
            arvore.print();
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

    

    private void exibirAtor(Ator ator) {
        /*System.out.println("--------------------------");
        System.out.println("ID: " + ator.getID());
        System.out.println("Nome: " + ator.getNome());
        System.out.println("Nacionalidade: " + ator.getNacionalidade());
        System.out.println("Data de nascimento: " + ator.getDataNascimento());
        ArrayList<ParNameAtorID> lista = arvore.read(null);
            for (int i = 0; i < lista.size(); i++){
                System.out.print(lista.get(i) + " ");
            }
                */ 
    }
}