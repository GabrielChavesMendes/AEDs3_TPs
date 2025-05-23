package visao;

import modelo.ArquivoRelacaoAtorSerie;
import modelo.ArquivoAtores;
import modelo.ArquivoSeries;
import modelo.ParIDAtorSerie;
import modelo.ParIDSerieAtor;

import entidades.Ator;
import entidades.Serie;

import modelo.ParIdIdAS;

import modelo.ParIdIdSA;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import aeds3.ArvoreBMais;
import aeds3.ParIdId;

public class MenuRelacoesAtorSerie {
    private Scanner scanner;
    private ArquivoRelacaoAtorSerie arquivoRelacoes;
    private ArquivoAtores arquivoAtores;
    private ArquivoSeries arquivoSeries;

    ArvoreBMais<ParIdIdAS> arvore;
    ArvoreBMais<ParIdIdSA> arvore2;
    Scanner console = new Scanner(System.in);
   
    public MenuRelacoesAtorSerie() {

        try {
            scanner = new Scanner(System.in);
            arquivoRelacoes = new ArquivoRelacaoAtorSerie();
            arquivoAtores = new ArquivoAtores();
            arquivoSeries = new ArquivoSeries();

            // arquivo da arvore ator serie
            File d = new File("dados/Arvores");
             if(!d.exists()){d.mkdir();}
             arvore=new ArvoreBMais<>(ParIdIdAS.class.getConstructor(),5,"dados/Arvores/arvoreAtorSerie.db");


            // arquivo da arvore serie ator
            arvore2=new ArvoreBMais<>(ParIdIdSA.class.getConstructor(),5,"dados/Arvores/arvoreSerieAtor.db");

        } catch (Exception e) {
            System.out.println("Erro ao inicializar o menu de relações: " + e.getMessage());
        }
         
       
       
        
    }

    public void exibirMenu() {

        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n==== Menu de Relações Ator-Série ====");
            System.out.println("1 - Adicionar relacionamento");
            System.out.println("2 - Listar séries por ator");
            System.out.println("3 - Listar atores por série");
            System.out.println("0 - Voltar");
            System.out.print("Escolha uma opção: ");
            opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {
                case 1 -> adicionarRelacionamento();
               // case 2 -> listarSeriesPorAtor();
               // case 3 -> listarAtoresPorSerie();
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

            arvore.create(new ParIdIdAS(idAtor, idSerie));
            arvore.print();

            arvore2.create(new ParIdIdSA(idSerie,idAtor));
            arvore.print();

            if (arquivoRelacoes.adicionarRelacionamento(idAtor, idSerie)) {
                
                System.out.println("Relacionamento adicionado com sucesso.");
            } else {
                System.out.println("Erro ao adicionar relacionamento.");
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
    //vai ser excluida essa opcao  
    /* 
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
    }*/

    /*
    private void listarSeriesPorAtor() {
        System.out.println("\nBUSCA");
        try {
            System.out.print("ID do ator: ");
            int idAtor = Integer.parseInt(scanner.nextLine());
            ArrayList<ParIdIdAS> lista = arvore.read(new ParIdIdAS(idAtor, -1));
            System.out.print("Resposta: ");
            for (int i = 0; i < lista.size(); i++)
              System.out.print(lista.get(i) + " ");
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
                } catch (Exception e) {
                    System.out.println("Erro: " + e.getMessage());
                }
            }
            }*/
    /* 
    private void listarAtoresPorSerie() {
        try {
            System.out.print("ID da série: ");
            int idSerie = Integer.parseInt(scanner.nextLine());
            ArrayList<ParIdIdAS> lista = arvore.read(new ParIdIdAS(idSerie, -1));
            System.out.print("Resposta: ");
            for (int i = 0; i < lista.size(); i++)
              System.out.print(lista.get(i) + " ");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }*/
}
