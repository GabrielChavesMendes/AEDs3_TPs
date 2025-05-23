package visao;
import entidades.Episodio;
import entidades.Serie;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import modelo.ArquivoEpisodios;
import modelo.ArquivoSeries;

public class MenuEpisodio {

    ArquivoEpisodios arqEpisodio;
    private static Scanner console = new Scanner(System.in);

    public MenuEpisodio() throws Exception {
        arqEpisodio = new ArquivoEpisodios();
    }

    public void menu() throws Exception {
        int opcao;
        do {
            System.out.println("\n\nPUCFlix 1.0");
            System.out.println("-----------");
            System.out.println("> Início > Episódios");
            System.out.println("\n1 - Incluir");
            System.out.println("2 - Buscar");
            System.out.println("3 - Alterar");
            System.out.println("4 - Excluir");
            System.out.println("5 - Listar por Série");  // Nova opção
            System.out.println("0 - Voltar ao menu anterior");
    
            System.out.print("\nOpção: ");
            try {
                opcao = Integer.valueOf(console.nextLine());
            } catch(NumberFormatException e) {
                opcao = -1;
            }
    
            switch (opcao) {
                case 1:
                    incluirEpisodio();
                    break;
                case 2:
                    buscarEpisodio();
                    break;
                case 3:
                    alterarEpisodio();
                    break;
                case 4:
                    excluirEpisodio();
                    break;
                case 5:  // Novo case
                    listarEpisodiosdeSerie();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida!");
                    break;
            }
        } while (opcao != 0);
    }

    public void incluirEpisodio() throws Exception {
        System.out.println("\nInclusão de episódio");
        String nome = "";
        int temporada = 1;
        LocalDate dataLancamento = null;
        LocalTime duracao = null;
        int idSerie = -1;
        boolean dadosCorretos = false;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        dadosCorretos = false;
        do {
            System.out.print("Nome (min. de 4 letras): ");
            nome = console.nextLine();
            if(nome.length()>=4)
                dadosCorretos = true;
            else
                System.err.println("O nome do epísodio deve ter no mínimo 4 caracteres.");
        } while(!dadosCorretos);

        dadosCorretos = false;
        do {
            System.out.print("Temporada: ");
            String tempStr = console.nextLine();
            try {
                temporada = Integer.parseInt(tempStr);
                if (temporada > 0)
                    dadosCorretos = true;
                else
                    System.err.println("As temporadas começam a partir da primeira.");
            } catch (NumberFormatException e) {
                System.err.println("Número inválido.");
            }
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

        dadosCorretos = false;
        do {
            System.out.print("Duração do episodio (HH:MM:SS): ");
            String duraStr = console.nextLine();
            try {
                duracao = LocalTime.parse(duraStr, timeFormatter);
                dadosCorretos = true;
            } catch (Exception e) {
                System.err.println("Duração inválida! Use o formato HH:MM:SS.");
            }
        } while(!dadosCorretos);

        // Solicitar nome da série e buscar no arquivo
        ArquivoSeries arqSeries = new ArquivoSeries();
        System.out.print("Nome da Série: ");
        String nomeSerie = console.nextLine();
        
        try {
            Serie[] seriesEncontradas = arqSeries.readNome(nomeSerie);
            
            if (seriesEncontradas == null || seriesEncontradas.length == 0) {
                System.out.println("Nenhuma série encontrada. Episódio não será incluído.");
                return; // Encerra sem incluir
            }
            
            // Mostrar séries encontradas
            System.out.println("\nSéries encontradas:");
            for (int i = 0; i < seriesEncontradas.length; i++) {
                System.out.println((i+1) + " - " + seriesEncontradas[i].getNome());
            }
            
            // Solicitar seleção
            System.out.print("Escolha a série correta (1-" + seriesEncontradas.length + "): ");
            String escolhaStr = console.nextLine();
            try {
                int escolha = Integer.parseInt(escolhaStr);
                if (escolha >= 1 && escolha <= seriesEncontradas.length) {
                    idSerie = seriesEncontradas[escolha-1].getID();
                } else {
                    System.out.println("Opção inválida. Episódio não será incluído.");
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("Número inválido. Episódio não será incluído.");
                return;
            }
            
            // Confirmar inclusão
            System.out.print("\nConfirma a inclusão do episódio? (S/N) ");
            char resp = console.nextLine().charAt(0);
            if(resp == 'S' || resp == 's') {
                Episodio c = new Episodio(idSerie, nome, temporada, dataLancamento, duracao);
                arqEpisodio.create(c);
                System.out.println("Episódio incluído com sucesso.");
            } else {
                System.out.println("Operação cancelada.");
            }
            
        } catch (Exception e) {
            System.out.println("Erro ao buscar séries. Episódio não será incluído.");
            e.printStackTrace();
        }
    }

    public void buscarEpisodio() {
        System.out.println("\nBusca de episódio por nome");
        System.out.print("\nNome: ");
        String nome = console.nextLine(); 
        if(nome.isEmpty())
            return; 

        try {
            List<Episodio> episodio = arqEpisodio.readNome(nome);  
            if (episodio.size()>0) {
                int n=1;
                for(Episodio s : episodio) {
                    System.out.println((n++));
                }
                System.out.print("Escolha o episódio: ");
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
                mostrarEpisodio(episodio.get(o-1));
            } else {
                System.out.println("Nenhuma episódio encontrado.");
            }
        } catch(Exception e) {
            System.out.println("Erro do sistema. Não foi possível buscar os episódio!");
            e.printStackTrace();
        }
    }

    public void listarEpisodiosdeSerie() throws Exception {
    System.out.println("\nListagem de Episódios por Série");
    
    // Primeiro, pedir ao usuário para selecionar a série
    ArquivoSeries arqSeries = new ArquivoSeries();
    System.out.print("Digite o nome da série: ");
    String nomeSerie = console.nextLine();
    
    if(nomeSerie.isEmpty()) {
        System.out.println("Nome vazio. Operação cancelada.");
        return;
    }
    
    try {
        // Buscar séries com o nome informado
        Serie[] seriesEncontradas = arqSeries.readNome(nomeSerie);
        
        if(seriesEncontradas == null || seriesEncontradas.length == 0) {
            System.out.println("Nenhuma série encontrada com esse nome.");
            return;
        }
        
        // Se encontrou mais de uma série com o mesmo nome
        if(seriesEncontradas.length > 1) {
            System.out.println("\nForam encontradas várias séries:");
            for(int i = 0; i < seriesEncontradas.length; i++) {
                System.out.println((i+1) + " - " + seriesEncontradas[i].getNome() + 
                                 " (ID: " + seriesEncontradas[i].getID() + ")");
            }
            
            System.out.print("\nSelecione a série desejada (1-" + seriesEncontradas.length + "): ");
            int escolha;
            try {
                escolha = Integer.parseInt(console.nextLine());
                if(escolha < 1 || escolha > seriesEncontradas.length) {
                    System.out.println("Opção inválida. Operação cancelada.");
                    return;
                }
            } catch(NumberFormatException e) {
                System.out.println("Número inválido. Operação cancelada.");
                return;
            }
            
            listarEpisodiosDaSerie(seriesEncontradas[escolha-1]);
        } else {
            // Se encontrou apenas uma série
            listarEpisodiosDaSerie(seriesEncontradas[0]);
        }
        
    } catch(Exception e) {
        System.out.println("Erro ao buscar séries: " + e.getMessage());
        e.printStackTrace();
    }
}

    private void listarEpisodiosDaSerie(Serie serie) throws Exception {
        System.out.println("\nEpisódios da Série: " + serie.getNome());
        System.out.println("--------------------------------");
        
        try {
            // Usar o método readPorSerie que implementamos anteriormente
            List<Episodio> episodios = arqEpisodio.readPorSerie(serie.getID());
            
            if(episodios.isEmpty()) {
                System.out.println("Nenhum episódio encontrado para esta série.");
                return;
            }
            
            // Agrupar episódios por temporada
            Map<Integer, List<Episodio>> episodiosPorTemporada = episodios.stream()
                .collect(Collectors.groupingBy(Episodio::getTemporada));
            
            // Ordenar as temporadas
            List<Integer> temporadasOrdenadas = new ArrayList<>(episodiosPorTemporada.keySet());
            Collections.sort(temporadasOrdenadas);
            
            // Exibir episódios por temporada
            for(int temporada : temporadasOrdenadas) {
                System.out.println("\nTemporada " + temporada + ":");
                System.out.println("----------------");
                
                List<Episodio> epsTemporada = episodiosPorTemporada.get(temporada);
                
                // Ordenar episódios por data de lançamento
                epsTemporada.sort(Comparator.comparing(Episodio::getDataLancamento));
                
                // Exibir cada episódio
                for(Episodio ep : epsTemporada) {
                    System.out.printf("• %s (Lançamento: %s | Duração: %s)%n",
                        ep.getNome(),
                        ep.getDataLancamento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        ep.getDuracao().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                }
            }
            
            System.out.println("\nTotal de episódios: " + episodios.size());
            
        } catch(Exception e) {
            System.out.println("Erro ao listar episódios: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void mostrarEpisodio(Episodio episodio) {
        if (episodio != null) {
            try {
                ArquivoSeries arqSeries = new ArquivoSeries();
                Serie serie = arqSeries.read(episodio.getIdSerie());
                
                System.out.println("\n----------------------");
                System.out.printf("Nome do Episódio......: %s%n", episodio.getNome());
                System.out.printf("Temporada............: %s%n", episodio.getTemporada());
                System.out.printf("Data de Lançamento...: %s%n", 
                    episodio.getDataLancamento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                System.out.printf("Duração.............: %s%n", 
                    episodio.getDuracao().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                
                // Mostrar informações da série
                if (serie != null) {
                    System.out.println("\nSérie associada:");
                    System.out.printf("ID da Série.........: %d%n", serie.getID());
                    System.out.printf("Nome da Série.......: %s%n", serie.getNome());
                } else {
                    System.out.println("\nSérie associada: Não encontrada");
                }
                System.out.println("----------------------\n");
                
            } catch (Exception e) {
                System.out.println("\nErro ao buscar informações da série:");
                e.printStackTrace();
                
                System.out.println("\n----------------------");
                System.out.printf("Nome do Episódio......: %s%n", episodio.getNome());
                System.out.printf("Temporada............: %s%n", episodio.getTemporada());
                System.out.printf("Data de Lançamento...: %s%n", 
                    episodio.getDataLancamento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                System.out.printf("Duração.............: %s%n", 
                    episodio.getDuracao().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                System.out.printf("ID da Série.........: %d%n", episodio.getIdSerie());
                System.out.println("----------------------\n");
            }
        }
    }

    public void alterarEpisodio() {
        System.out.println("\nAlteração do episódio");
        System.out.print("\nNome do episódio a ser alterada: ");
        String nome = console.nextLine();  
        if(nome.isEmpty()) {
            System.out.println("Nome vazio ou inválido!");
            return;
        }
    
        try {
            List<Episodio> episodio = arqEpisodio.readNome(nome); 
            if (episodio.size() > 0) {//verifica se algum nome foi encontrado no arquivo
                int n = 1;
                for (Episodio s : episodio) {
                    System.out.println((n++) + ": " + s);
                }
    
                System.out.print("Escolha a série para alterar: ");
                int o;
                do { 
                    try {
                        o = Integer.valueOf(console.nextLine());
                    } catch (NumberFormatException e) {
                        o = -1;
                    }
                    if(o <= 0 || o > n-1)
                        System.out.println("Escolha um número entre 1 e " + (n-1));
                } while(o <= 0 || o > n-1);
    
                Episodio serieSelecionada = episodio.get(o-1);
                System.out.println("Alterando a série: " + serieSelecionada);
    
                String novoNome = "";
                int novaTemporada = 1;
                LocalDate novaDataLancamento = null;
                LocalTime novaDuracao = null;
    
                System.out.print("Novo nome (deixe em branco para manter): ");
                novoNome = console.nextLine();
                if (!novoNome.isEmpty()) {
                    serieSelecionada.setNome(novoNome);
                }
    
                System.out.print("Nova temporada (deixe em branco para manter): ");
                String tempStr = console.nextLine();
                if (!tempStr.isEmpty()) {
                    try {
                        novaTemporada = Integer.parseInt(tempStr);
                        serieSelecionada.setTemporada(novaTemporada);
                    } catch (NumberFormatException e) {
                        System.out.println("Temporada inválida. Manterá a anterior.");
                    }
                }
    
                System.out.print("Nova data de lançamento (DD/MM/AAAA) ou deixe em branco para manter: ");
                String dataStr = console.nextLine();
                if (!dataStr.isEmpty()) {
                    try {
                        novaDataLancamento = LocalDate.parse(dataStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                        serieSelecionada.setDataLancamento(novaDataLancamento);
                    } catch (Exception e) {
                        System.err.println("Data inválida! Manterá a data anterior.");
                    }
                }
    
                System.out.print("Nova duração da séria (HH:MM:SS) ou deixe em branco para manter: ");
                String duraStr = console.nextLine();
                if (!duraStr.isEmpty()) {
                    try {
                        novaDuracao = LocalTime.parse(duraStr, DateTimeFormatter.ofPattern("HH:mm:ss"));
                        serieSelecionada.setDuracao(novaDuracao);
                    } catch (Exception e) {
                        System.err.println("Data inválida! Manterá a data anterior.");
                    }
                }
    
                System.out.print("Novo ID da série (deixe em branco para manter): ");
                String idStr = console.nextLine();
                if (!idStr.isEmpty()) {
                    try {
                        int novoIdSerie = Integer.parseInt(idStr);
                        serieSelecionada.setIdSerie(novoIdSerie);
                    } catch (NumberFormatException e) {
                        System.out.println("ID inválido. Manterá o anterior.");
                    }
                }

                System.out.print("\nConfirma a alteração da série? (S/N) ");
                char resp = console.nextLine().charAt(0);
                if (resp == 'S' || resp == 's') {
                    try {
                        arqEpisodio.update(serieSelecionada); 
                        System.out.println("Série alterada com sucesso.");
                    } catch (Exception e) {
                        System.out.println("Erro do sistema. Não foi possível alterar a série!");
                    }
                }
            } else {
                System.out.println("Nenhuma série encontrada com esse nome.");
            }
        } catch (Exception e) {
            System.out.println("Erro do sistema. Não foi possível buscar a série!");
            e.printStackTrace();
        }
    }

    public void excluirEpisodio() {
        System.out.println("\nExclusão de série");
        System.out.print("\nNome da série a ser excluída: ");
        String nome = console.nextLine();  
        if (nome.isEmpty()) {
            System.out.println("Nome vazio ou inválido!");
            return;
        }
        
        try {
            List<Episodio> episodio = arqEpisodio.readNome(nome);
            if (episodio != null && episodio.size() > 0) {
                int n = 1;
                for (Episodio s : episodio) {
                    System.out.println((n++) + ": " + s);
                }
                
                System.out.print("Escolha a série para excluir: ");
                int o;
                do { 
                    try {
                        o = Integer.valueOf(console.nextLine());
                    } catch (NumberFormatException e) {
                        o = -1;
                    }
                    if (o <= 0 || o > n - 1)
                        System.out.println("Escolha um número entre 1 e " + (n - 1));
                } while (o <= 0 || o > n - 1);
                
                Episodio serieSelecionada = episodio.get(o-1);
                System.out.print("\nConfirma a exclusão da série? (S/N) ");
                char resp = console.nextLine().charAt(0);
                if (resp == 'S' || resp == 's') {
                    try {
                        if (arqEpisodio.delete(serieSelecionada.getID())) {
                            System.out.println("Série excluída com sucesso.");
                        } else {
                            System.out.println("Erro ao excluir a série.");
                        }
                    } catch (Exception e) {
                        System.out.println("Erro do sistema. Não foi possível excluir a série!");
                    }
                }
            } else {
                System.out.println("Nenhuma série encontrada com esse nome.");
            }
        } catch (Exception e) {
            System.out.println("Erro do sistema. Não foi possível buscar a série!");
            e.printStackTrace();
        }
    }

}
