package visao;

import modelo.ArquivoRelacaoAtorSerie;
import modelo.ArquivoAtores;
import modelo.ArquivoSeries;
import entidades.Ator;
import entidades.Serie;
import java.util.Scanner;

public class MenuRelacoesAtorSerie {
    private Scanner scanner;
    private ArquivoRelacaoAtorSerie arquivoRelacoes;
    private ArquivoAtores arquivoAtores;
    private ArquivoSeries arquivoSeries;

    public MenuRelacoesAtorSerie() {
        try {
            scanner = new Scanner(System.in);
            arquivoRelacoes = new ArquivoRelacaoAtorSerie();
            arquivoAtores = new ArquivoAtores();
            arquivoSeries = new ArquivoSeries();
        } catch (Exception e) {
            System.out.println("Erro ao inicializar o menu de relações: " + e.getMessage());
        }
    }

    public void exibirMenu() {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n==== Menu de Relações Ator-Série ====");
            System.out.println("1 - Adicionar relacionamento");
            System.out.println("2 - Remover relacionamento");
            System.out.println("3 - Listar séries por ator");
            System.out.println("4 - Listar atores por série");
            System.out.println("0 - Voltar");
            System.out.print("Escolha uma opção: ");
            opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {
                case 1 -> adicionarRelacionamento();
                case 2 -> removerRelacionamento();
                case 3 -> listarSeriesPorAtor();
                case 4 -> listarAtoresPorSerie();
                case 0 -> System.out.println("Voltando ao menu principal.");
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    private void adicionarRelacionamento() {
        try {
            System.out.print("ID do ator: ");
            int idAtor = Integer.parseInt(scanner.nextLine());
            System.out.print("ID da série: ");
            int idSerie = Integer.parseInt(scanner.nextLine());

            if (arquivoRelacoes.adicionarRelacionamento(idAtor, idSerie))
                System.out.println("Relacionamento adicionado com sucesso.");
            else
                System.out.println("Erro ao adicionar relacionamento.");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void removerRelacionamento() {
        try {
            System.out.print("ID do ator: ");
            int idAtor = Integer.parseInt(scanner.nextLine());
            System.out.print("ID da série: ");
            int idSerie = Integer.parseInt(scanner.nextLine());

            if (arquivoRelacoes.removerRelacionamento(idAtor, idSerie))
                System.out.println("Relacionamento removido com sucesso.");
            else
                System.out.println("Relacionamento não encontrado.");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void listarSeriesPorAtor() {
        try {
            System.out.print("ID do ator: ");
            int idAtor = Integer.parseInt(scanner.nextLine());
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
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void listarAtoresPorSerie() {
        try {
            System.out.print("ID da série: ");
            int idSerie = Integer.parseInt(scanner.nextLine());
            int[] atores = arquivoRelacoes.buscarAtoresPorSerie(idSerie);
            if (atores.length == 0) {
                System.out.println("Nenhum ator encontrado para esta série.");
                return;
            }
            System.out.println("Atores dessa série:");
            for (int idAtor : atores) {
                Ator a = arquivoAtores.read(idAtor);
                if (a != null)
                    System.out.println(idAtor + " - " + a.getNome());
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}
