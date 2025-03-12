import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class MenuSerie {
    
    ArquivoSerie arqSerie;
    private static Scanner console = new Scanner(System.in);

    public MenuSerie() throws Exception {
        arqSerie = new ArquivoSerie();
    }

    public void menu() {

        int opcao;
        do {

            System.out.println("\n\nAEDsIII");
            System.out.println("-------");
            System.out.println("> Início > Clientes");
            System.out.println("\n1 - Buscar");
            System.out.println("2 - Incluir");
            System.out.println("3 - Alterar");
            System.out.println("4 - Excluir");
            System.out.println("0 - Voltar");

            System.out.print("\nOpção: ");
            try {
                opcao = Integer.valueOf(console.nextLine());
            } catch(NumberFormatException e) {
                opcao = -1;
            }

            switch (opcao) {
                case 1:
                    buscarSerie();
                    break;
                case 2:
                    incluirSerie();
                    break;
                case 3:
                    alterarSerie();
                    break;
                case 4:
                    excluirSerie();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida!");
                    break;
            }

        } while (opcao != 0);
    }


    public void buscarSerie() {
        System.out.println("\nBusca de filmes");
        String nome;
    
        do {
            System.out.print("\nNome: ");
            nome = console.nextLine().trim();  // Lê e remove espaços extras do nome
    
            if (nome.isEmpty()) {
                System.out.println("Nome inválido. Digite um nome válido.");
            }
        } while (nome.isEmpty());
    
        try {
            Serie serie = arqSerie.read(nome);  // Chama o método de leitura da classe Arquivo
            if (serie != null) {
                mostraSerie(serie);  // Exibe os detalhes da série encontrada
            } else {
                System.out.println("Série não encontrada.");
            }
        } catch (Exception e) {
            System.out.println("Erro do sistema. Não foi possível buscar a série!");
            e.printStackTrace();
        }
    }    


    public void incluirSerie() {
        System.out.println("\nInclusão de serie");
        String nome = "";
        LocalDate dataLancamento = null;
        String sinopse = "";
        String streaming = "";
        boolean dadosCorretos = false;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        do {
            System.out.print("\nNome (min. de 4 letras ou vazio para cancelar): ");
            nome = console.nextLine();
            if(nome.length()==0)
                return;
            if(nome.length()<4)
                System.err.println("O nome do cliente deve ter no mínimo 4 caracteres.");
        } while(nome.length()<4);

        do {
            System.out.print("Sinopse (máximo de 200 caracteres, sem pontos ou traços): ");
            sinopse = console.nextLine();
            if (sinopse.length() > 200) 
                System.err.println("A sinopse deve ter no máximo 200 caracteres.");
        } while (sinopse.length() > 200);

        do {
            System.out.print("\nStreaming (min. de 4 letras ou vazio para cancelar): ");
            streaming = console.nextLine();
            if(streaming.length()==0)
                return;
            if(streaming.length()<4)
                System.err.println("A streaming deve ter no mínimo 4 caracteres.");
        } while(nome.length()<4);

        do {
            System.out.print("Data de lan (DD/MM/AAAA): ");
            String dataStr = console.nextLine();
            dadosCorretos = false;
            try {
                dataLancamento = LocalDate.parse(dataStr, formatter);
                dadosCorretos = true;
            } catch (Exception e) {
                System.err.println("Data inválida! Use o formato DD/MM/AAAA.");
            }
        } while(!dadosCorretos);

        System.out.print("\nConfirma a inclusão da cliente? (S/N) ");
        char resp = console.nextLine().charAt(0);
        if(resp=='S' || resp=='s') {
            try {
                Serie c = new Serie(nome, sinopse, streaming, dataLancamento);
                arqSerie.create(c);
                System.out.println("Cliente incluído com sucesso.");
            } catch(Exception e) {
                System.out.println("Erro do sistema. Não foi possível incluir o cliente!");
            }
        }
    }

    public void alterarSerie() {
        System.out.println("\nAlteração de cliente");
        String nome;
        boolean nomeValido = false;

        do {
            System.out.print("\nNome da série: ");
            nome = console.nextLine();  // Lê o CPF digitado pelo usuário

            if(nome.isEmpty())
                return; 

            // Validação do CPF (11 dígitos e composto apenas por números)
            if (nome.length() == 11 && nome.matches("\\d{11}")) {
                nomeValido = true;  // CPF válido
            } else {
                System.out.println("Nome inválido.");
            }
        } while (!nomeValido);


        try {
            // Tenta ler o cliente com o ID fornecido
            Serie serie = arqSerie.read(nome);
            if (serie != null) {
                System.out.println("Cliente encontrado:");
                mostraSerie(serie);  // Exibe os dados do cliente para confirmação

                // Alteração de nome
                System.out.print("\nNovo nome (deixe em branco para manter o anterior): ");
                String novoNome = console.nextLine();
                if (!novoNome.isEmpty()) {
                    serie.nome = novoNome;  // Atualiza o nome se fornecido
                }

                // Alteração de CPF
                System.out.print("Novo CPF (deixe em branco para manter o anterior): ");
                String novoSinopse = console.nextLine();
                if (!novoSinopse.isEmpty()) {
                    serie.sinopse = novoSinopse;  // Atualiza o CPF se fornecido
                }

                // Alteração de salário
                System.out.print("Novo salário (deixe em branco para manter o anterior): ");
                String novoStreamingStr = console.nextLine();
                if (!novoStreamingStr.isEmpty()) {
                    serie.streaming = novoStreamingStr;
                }

                // Alteração de data de nascimento
                System.out.print("Nova data de nascimento (DD/MM/AAAA) (deixe em branco para manter a anterior): ");
                String novaDataStr = console.nextLine();
                if (!novaDataStr.isEmpty()) {
                    try {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        serie.lancamento = LocalDate.parse(novaDataStr, formatter);  // Atualiza a data de nascimento se fornecida
                    } catch (Exception e) {
                        System.err.println("Data inválida. Valor mantido.");
                    }
                }

                // Confirmação da alteração
                System.out.print("\nConfirma as alterações? (S/N) ");
                char resp = console.next().charAt(0);
                if (resp == 'S' || resp == 's') {
                    // Salva as alterações no arquivo
                    boolean alterado = arqSerie.update(serie);
                    if (alterado) {
                        System.out.println("Cliente alterado com sucesso.");
                    } else {
                        System.out.println("Erro ao alterar o cliente.");
                    }
                } else {
                    System.out.println("Alterações canceladas.");
                }
            } else {
                System.out.println("Cliente não encontrado.");
            }
        } catch (Exception e) {
            System.out.println("Erro do sistema. Não foi possível alterar o cliente!");
            e.printStackTrace();
        }
        
    }


    public void excluirSerie() {
        System.out.println("\nExclusão de cliente");
        String nome;
        boolean nomeValido = false;

        do {
            System.out.print("\nNome da série: ");
            nome = console.nextLine();  // Lê o CPF digitado pelo usuário

            if(nome.isEmpty())
                return; 

            // Validação do CPF (11 dígitos e composto apenas por números)
            if (nome.length() == 11 && nome.matches("\\d{11}")) {
                nomeValido = true;  // CPF válido
            } else {
                System.out.println("Nome inválido.");
            }
        } while (!nomeValido);

        try {
            // Tenta ler o cliente com o ID fornecido
            Serie serie = arqSerie.read(nome);
            if (serie != null) {
                System.out.println("Série encontrado:");
                mostraSerie(serie);  // Exibe os dados do cliente para confirmação

                System.out.print("\nConfirma a exclusão do cliente? (S/N) ");
                char resp = console.next().charAt(0);  // Lê a resposta do usuário

                if (resp == 'S' || resp == 's') {
                    boolean excluido = arqSerie.delete(nome);  // Chama o método de exclusão no arquivo
                    if (excluido) {
                        System.out.println("Cliente excluído com sucesso.");
                    } else {
                        System.out.println("Erro ao excluir o cliente.");
                    }
                } else {
                    System.out.println("Exclusão cancelada.");
                }
            } else {
                System.out.println("Cliente não encontrado.");
            }
        } catch (Exception e) {
            System.out.println("Erro do sistema. Não foi possível excluir o cliente!");
            e.printStackTrace();
        }
    }


    public void mostraSerie(Serie serie) {
    if (serie != null) {
        System.out.println("\nDetalhes da serie:");
        System.out.println("----------------------");
        System.out.printf("Nome......: %s%n", serie.nome);
        System.out.printf("Sinopse.......: %s%n", serie.sinopse);
        System.out.printf("Streaming...: R$ %.2f%n", serie.streaming);
        System.out.printf("Lançamento: %s%n", serie.lancamento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        System.out.println("----------------------");
    }
}
}
