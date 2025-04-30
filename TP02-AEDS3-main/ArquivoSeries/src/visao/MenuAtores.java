package visao;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import entidades.Ator;
import modelo.ArquivoAtores;

public class MenuAtores {

    private ArquivoAtores arqAtores;
    private static Scanner console = new Scanner(System.in);

    public MenuAtores() throws Exception {
        arqAtores = new ArquivoAtores();
    }

    public void menu() {
        int opcao;
        do {
            System.out.println("\n\nPUCFlix 1.0");
            System.out.println("-----------");
            System.out.println("> Início > Atores");
            System.out.println("\n1 - Incluir");
            System.out.println("2 - Excluir");
            System.out.println("0 - Voltar ao menu anterior");

            System.out.print("\nOpção: ");
            try {
                opcao = Integer.parseInt(console.nextLine());
            } catch (NumberFormatException e) {
                opcao = -1;
            }

            switch (opcao) {
                case 1:
                    incluirAtor();
                    break;
                case 2:
                    excluirAtor();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }

    private void incluirAtor() {
        System.out.println("\nInclusão de ator");
        String nome, nacionalidade;
        LocalDate dataNascimento = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        System.out.print("Nome: ");
        nome = console.nextLine();

        System.out.print("Nacionalidade: ");
        nacionalidade = console.nextLine();

        boolean dataValida = false;
        do {
            System.out.print("Data de nascimento (DD/MM/AAAA): ");
            String dataStr = console.nextLine();
            try {
                dataNascimento = LocalDate.parse(dataStr, formatter);
                dataValida = true;
            } catch (Exception e) {
                System.err.println("Data inválida! Use o formato DD/MM/AAAA.");
            }
        } while (!dataValida);

        System.out.print("\nConfirma a inclusão do ator? (S/N) ");
        char resp = console.nextLine().charAt(0);
        if (resp == 'S' || resp == 's') {
            try {
                Ator ator = new Ator(-1, nome, nacionalidade, dataNascimento);
                arqAtores.create(ator);
                System.out.println("Ator incluído com sucesso.");
            } catch (Exception e) {
                System.out.println("Erro do sistema. Não foi possível incluir o ator!");
                e.printStackTrace();
            }
        }
    }

    private void excluirAtor() {
        System.out.println("\nExclusão de ator");
        System.out.print("ID do ator a ser excluído: ");
        int id;
        try {
            id = Integer.parseInt(console.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("ID inválido.");
            return;
        }

        System.out.print("Confirma a exclusão do ator com ID " + id + "? (S/N) ");
        char resp = console.nextLine().charAt(0);
        if (resp == 'S' || resp == 's') {
            try {
                if (arqAtores.delete(id)) {
                    System.out.println("Ator excluído com sucesso.");
                } else {
                    System.out.println("Ator não encontrado.");
                }
            } catch (Exception e) {
                System.out.println("Erro do sistema. Não foi possível excluir o ator.");
                e.printStackTrace();
            }
        } else {
            System.out.println("Operação cancelada.");
        }
    }
}
