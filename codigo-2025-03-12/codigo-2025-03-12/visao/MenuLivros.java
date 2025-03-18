package visao;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import entidades.Serie;
import modelo.ArquivoSerie;

public class MenuLivros {
    
    ArquivoSerie arqSeries;
    private static Scanner console = new Scanner(System.in);

    public MenuLivros() throws Exception {
        arqSeries = new ArquivoSerie();
    }

    public void menu() {

        int opcao;
        do {

            System.out.println("\n\nPUCBook 1.0");
            System.out.println( "-----------");
            System.out.println("> Início > Livros");
            System.out.println("\n1 - Buscar por ISBN");
            System.out.println("2 - Buscar por Título");
            System.out.println("3 - Incluir");
            System.out.println("4 - Alterar");
            System.out.println("5 - Excluir");
            System.out.println("0 - Voltar");

            System.out.print("\nOpção: ");
            try {
                opcao = Integer.valueOf(console.nextLine());
            } catch(NumberFormatException e) {
                opcao = -1;
            }

            switch (opcao) {
                case 1:
                    buscarNome();
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
   

    public void buscarNome() {
        System.out.println("\nBusca de livro por título");
        System.out.print("\nTítulo: ");
        String nome = console.nextLine();  // Lê o título digitado pelo usuário

        if(nome.isEmpty())
            return; 

        try {
            Serie[] series = arqSeries.readNome(nome);  // Chama o método de leitura da classe Arquivo
            if (series.length>0) {
                int n=1;
                for(Serie l : series) {
                    System.out.println((n++)+": "+l.getNome());
                }
                System.out.print("Escolha o livro: ");
                int o;
                do { 
                    try {
                        o = Integer.valueOf(console.nextLine());
                    } catch(NumberFormatException e) {
                        o = -1;
                    }
                    if(o<=0 || o>n-1)
                        System.out.println("Escolha um número entre 1 e "+(n-1));
                }while(o<=0 || o>n-1);
                mostrarSerie(series[o-1]);  // Exibe os detalhes do livro encontrado
            } else {
                System.out.println("Nenhum livro encontrado.");
            }
        } catch(Exception e) {
            System.out.println("Erro do sistema. Não foi possível buscar os livros!");
            e.printStackTrace();
        }
    }      


    public void incluirSerie() {
        System.out.println("\nInclusão de livro");
        String nome = "";
        String sinopse = "";
        String streaming = "";
        LocalDate dataLancamento = null;
        boolean dadosCorretos = false;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        dadosCorretos = false;
        do {
            System.out.print("ISBN (13 dígitos sem pontos ou traço. Deixe vazio para cancelar.): ");
            nome = console.nextLine();
            if(nome.length()==0)
                return;            
            if(!Serie.isValidNome(nome))
                dadosCorretos = true;
            else
                System.err.println("O ISBN deve ter exatamente 13 dígitos.");
        } while(!dadosCorretos);

        dadosCorretos = false;
        do {
            System.out.print("Título (min. de 4 letras): ");
            nome = console.nextLine();
            if(nome.length()>=4)
                dadosCorretos = true;
            else
                System.err.println("O título do livro deve ter no mínimo 4 caracteres.");
        } while(!dadosCorretos);

        dadosCorretos = false;
        do {
            System.out.print("Data de lançamento (DD/MM/AAAA): ");
            String dataStr = console.nextLine();
            try {
                dataLancamento = LocalDate.parse(dataStr, formatter);
                dadosCorretos = true;
            } catch (Exception e) {
                System.err.println("Data inválida! Use o formato DD/MM/AAAA.");
            }
        } while(!dadosCorretos);

        System.out.print("\nConfirma a inclusão da livro? (S/N) ");
        char resp = console.nextLine().charAt(0);
        if(resp=='S' || resp=='s') {
            try {
                Serie c = new Serie(nome, sinopse, dataLancamento, streaming);
                arqSeries.create(c);
                System.out.println("Serie incluído com sucesso.");
            } catch(Exception e) {
                System.out.println("Erro do sistema. Não foi possível incluir o livro!");
            }
        }
    }

    public void alterarSerie() {
        System.out.println("\nAlteração de livro");
        String nome;
        boolean dadosCorretos;

        dadosCorretos = false;
        do {
            System.out.print("\nNome: ");
            nome = console.nextLine();  // Lê o ISBN digitado pelo usuário

            if(nome.isEmpty())
                return; 

            // Validação do ISBN (13 dígitos e composto apenas por números)
            if (Serie.isValidNome(nome)) 
                dadosCorretos = true;  // ISBN válido
            else 
                System.out.println("Nome inválido.");
        } while (!dadosCorretos);


        try {
            // Tenta ler o livro com o ID fornecido
            Serie serie = arqSeries.readISBN(nome);
            if (serie != null) {
                mostrarSerie(serie);  // Exibe os dados do livro para confirmação

                // Alteração de ISBN
                String novoNome;
                dadosCorretos = false;
                do {
                    System.out.print("Novo nome (deixe em branco para manter o anterior): ");
                    novoNome = console.nextLine();
                    if(!novoNome.isEmpty()) {
                        if(Serie.isValidNome(novoNome)) {
                            serie.setNome(novoNome);  // Atualiza o ISBN se fornecido
                            dadosCorretos = true;
                        } else 
                            System.err.println("O nome deve ter mais de 2 letras.");
                    } else 
                        dadosCorretos = true;
                } while(!dadosCorretos);

                // Alteração de titulo
                String novoTitulo;
                dadosCorretos = false;
                do {
                    System.out.print("Novo título (deixe em branco para manter o anterior): ");
                    novoTitulo = console.nextLine();
                    if(!novoTitulo.isEmpty()) {
                        if(novoTitulo.length()>=4) {
                            serie.setNome(novoTitulo);  // Atualiza o título se fornecido
                            dadosCorretos = true;
                        } else
                            System.err.println("O título do livro deve ter no mínimo 4 caracteres.");
                    } else
                        dadosCorretos = true;
                } while(!dadosCorretos);

                // Alteração de autor
                String novoAutor;
                dadosCorretos = false;
                do {
                    System.out.print("Novo autor (deixe em branco para manter o anterior): ");
                    novoAutor = console.nextLine();
                    if(!novoAutor.isEmpty()) {
                        if(novoAutor.length()>=4) {
                            serie.setAutor(novoAutor);  // Atualiza o título se fornecido
                            dadosCorretos = true;
                        } else
                            System.err.println("O nome do autor deve ter no mínimo 4 caracteres.");
                    } else
                        dadosCorretos = true;
                } while(!dadosCorretos);

                // Alteração da edição
                String novaEdicao;
                dadosCorretos = false;
                do {
                    System.out.print("Nova edição (deixe em branco para manter a anterior): ");
                    novaEdicao = console.nextLine();
                    if(!novaEdicao.isEmpty()) {
                        try {
                            int edicao = Integer.parseInt(novaEdicao);
                            if(edicao>0 && edicao<128) {
                                serie.setEdicao((byte)edicao);  // Atualiza a edição, se fornecida
                                dadosCorretos = true;
                            } else
                                 System.err.println("A edição deve ser um número entre 1 e 127.");
                        } catch(NumberFormatException e) {
                            System.err.println("Número de edição inválido! Por favor, insira um número válido.");
                        }
                    } else
                        dadosCorretos = true;
                } while(!dadosCorretos);

                // Alteração de preço
                String novoPreco;
                dadosCorretos = false;
                do {
                    System.out.print("Novo preço (deixe em branco para manter o anterior): ");
                    novoPreco = console.nextLine();
                    if(!novoPreco.isEmpty()) {
                        try {
                            serie.setPreco(Float.parseFloat(novoPreco));  // Atualiza o preço, se fornecido
                            dadosCorretos = true;
                        } catch(NumberFormatException e) {
                            System.err.println("Preço inválido! Por favor, insira um número válido.");
                        }
                    } else
                        dadosCorretos = true;
                } while(!dadosCorretos);


                // Alteração de data de lançamento
                String novaData;
                dadosCorretos = false;
                do {
                    System.out.print("Nova data de lançamento (DD/MM/AAAA) (deixe em branco para manter a anterior): ");
                    novaData = console.nextLine();
                    if (!novaData.isEmpty()) {
                        try {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                            serie.setDataLancamento(LocalDate.parse(novaData, formatter));  // Atualiza a data de lançamento se fornecida
                        } catch (Exception e) {
                            System.err.println("Data inválida. Valor mantido.");
                        }
                    } else
                        dadosCorretos = true;
                } while(!dadosCorretos);

                // Confirmação da alteração
                System.out.print("\nConfirma as alterações? (S/N) ");
                char resp = console.next().charAt(0);
                if (resp == 'S' || resp == 's') {
                    // Salva as alterações no arquivo
                    boolean alterado = arqLivros.update(serie);
                    if (alterado) {
                        System.out.println("Livro alterado com sucesso.");
                    } else {
                        System.out.println("Erro ao alterar o livro.");
                    }
                } else {
                    System.out.println("Alterações canceladas.");
                }
                 console.nextLine(); // Limpar o buffer 
            } else {
                System.out.println("Livro não encontrado.");
            }
        } catch (Exception e) {
            System.out.println("Erro do sistema. Não foi possível alterar o livro!");
            e.printStackTrace();
        }
        
    }


    public void excluirSerie() {
        System.out.println("\nExclusão de livro");
        String isbn;
        boolean dadosCorretos = false;

        do {
            System.out.print("\nISBN (13 dígitos): ");
            isbn = console.nextLine();  // Lê o ISBN digitado pelo usuário

            if(isbn.isEmpty())
                return; 

            // Validação do ISBN (13 dígitos e composto apenas por números)
            if (Serie.isValidNome(isbn)) 
                dadosCorretos = true;  // ISBN válido
            else 
                System.out.println("ISBN inválido. O ISBN deve conter exatamente 13 dígitos numéricos, sem pontos e traços.");
        } while (!dadosCorretos);

        try {
            // Tenta ler o livro com o ID fornecido
            Serie serie = arqSeries.readISBN(isbn);
            if (serie != null) {
                System.out.println("Livro encontrado:");
                mostrarSerie(serie);  // Exibe os dados do livro para confirmação

                System.out.print("\nConfirma a exclusão do livro? (S/N) ");
                char resp = console.next().charAt(0);  // Lê a resposta do usuário

                if (resp == 'S' || resp == 's') {
                    boolean excluido = arqSeries.delete(isbn);  // Chama o método de exclusão no arquivo
                    if (excluido) {
                        System.out.println("Livro excluído com sucesso.");
                    } else {
                        System.out.println("Erro ao excluir o livro.");
                    }
                    
                } else {
                    System.out.println("Exclusão cancelada.");
                }
                console.nextLine(); // Limpar o buffer 
            } else {
                System.out.println("Livro não encontrado.");
            }
        } catch (Exception e) {
            System.out.println("Erro do sistema. Não foi possível excluir o livro!");
            e.printStackTrace();
        }
    }

    public void mostrarSerie(Serie serie) {
        if (serie != null) {
            System.out.println("----------------------");
            System.out.printf("Nome......: %s%n", serie.getNome());
            System.out.printf(".....: %s%n", serie.getSinopse());
            System.out.printf("Streaming....: %s%n", serie.getStreaming());
            System.out.printf("Lançamento: %s%n", serie.getDataLancamento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            System.out.println("----------------------");
        }
    }

    /*public void povoar() throws Exception {
        arqLivros.create(new Livro("9788595159907", "Algoritmos: Teoria e Prática", "Thomas H. Cormen", 4, LocalDate.of(2024,2,6), 416.52F));
        arqLivros.create(new Livro("9788575225639", "Entendendo Algoritmos", "Aditya Y. Bhargava", 1, LocalDate.of(2017, 4, 24),  51.30F));
        arqLivros.create(new Livro("9788573933758", "Estruturas de Dados e Algoritmos em Java", "Lafore", 2, LocalDate.of(2005,1,1), 119.42F));
        arqLivros.create(new Livro("9788543004792", "Java: Como programar", "Paul Deitel", 10, LocalDate.of(2016,6,24), 365.25F));
        arqLivros.create(new Livro("9788575228418", "Python Para Análise de Dados", "Wes McKinney", 3, LocalDate.of(2023, 3, 16), 122.55F));
        arqLivros.create(new Livro("9788550804620", "Java Efetivo: As melhores práticas para a plataforma Java", "Joshua Bloch", 3, LocalDate.of(2019,5,28), 96.98F));
        arqLivros.create(new Livro("9788522128143", "Algoritmos e Lógica de Programação", "Marco A. Furlano de Souza", 3, LocalDate.of(2019,1,10), 65.75F));
    }*/

}
