import java.util.Scanner;
import visao.MenuAtores;
import visao.MenuEpisodio;
import visao.MenuRelacoesAtorSerie;
import visao.MenuSeries;

public class Principal {

    public static void main(String[] args) {
        Scanner console;
        try {

            console = new Scanner(System.in);
            int opcao;
            do {

                System.out.println("\n\nPUCFlix 1.0");
                System.out.println( "-----------");
                System.out.println("> Início");
                System.out.println("\n1 - Series");
                System.out.println("2 - Episodio");
                System.out.println("3 - Atores");
                System.out.println("4 - Incluir ator em série");
                System.out.println("0 - Sair");

                System.out.print("\nOpção: ");
                try {
                    opcao = Integer.valueOf(console.nextLine());
                } catch(NumberFormatException e) {
                    opcao = -1;
                }

                switch (opcao) {
                    case 1:
                        (new MenuSeries()).menu();
                        break;
                    case 2:
                        (new MenuEpisodio()).menu();
                        break;
                    case 3: 
                        (new MenuAtores()).exibirMenu();
                        break;
                    case 4: 
                        (new MenuRelacoesAtorSerie()).exibirMenu();
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Opção inválida!");
                        break;
                }

            } while (opcao != 0);

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
