package modelo;

import aeds3.ArvoreBMais;
import java.io.File;
import java.util.ArrayList;

public class ArquivoRelacaoAtorSerie {

    private ArvoreBMais<ParIDAtorSerie> arvoreAtorSerie;
    private ArvoreBMais<ParIDSerieAtor> arvoreSerieAtor; 



    public ArquivoRelacaoAtorSerie() throws Exception {

        
    File pasta = new File("./dados/relacoes/atorSerie.db");
       // Cria o diretório, se ainda não existir
    File diretorio = new File("./dados/relacoes");
    if (!diretorio.exists()) {
        diretorio.mkdirs();
    }

    // Cria os arquivos .db se não existirem
    File arquivoAtorSerie = new File("./dados/relacoes/atorSerie.db");
    if (!arquivoAtorSerie.exists()) {
        arquivoAtorSerie.createNewFile(); // Cria o arquivo
    }

    File arquivoSerieAtor = new File("./dados/relacoes/serieAtor.db");
    if (!arquivoSerieAtor.exists()) {
        arquivoSerieAtor.createNewFile();
    }
        
        this.arvoreAtorSerie = new ArvoreBMais<>(ParIDAtorSerie.class.getConstructor(), 4, "./dados/relacoes/atorSerie.db");
        this.arvoreSerieAtor = new ArvoreBMais<>(ParIDSerieAtor.class.getConstructor(), 4, "./dados/relacoes/serieAtor.db");
    }

    public boolean adicionarRelacionamento(int idAtor, int idSerie) throws Exception {
        arvoreAtorSerie.create(new ParIDAtorSerie(idAtor, idSerie));
        return arvoreSerieAtor.create(new ParIDSerieAtor(idSerie, idAtor));
    }

    public boolean removerRelacionamento(int idAtor, int idSerie) throws Exception {
        boolean ok1 = arvoreAtorSerie.delete(new ParIDAtorSerie(idAtor, idSerie));
        boolean ok2 = arvoreSerieAtor.delete(new ParIDSerieAtor(idSerie, idAtor));
        return ok1 && ok2;
    }

    public int[] buscarSeriesPorAtor(int idAtor) throws Exception {
        ArrayList<ParIDAtorSerie> lista = arvoreAtorSerie.read(new ParIDAtorSerie(idAtor, -1));
        return lista.stream().mapToInt(ParIDAtorSerie::getIdSerie).toArray();
    }

    public int[] buscarAtoresPorSerie(int idSerie) throws Exception {
        ArrayList<ParIDSerieAtor> lista = arvoreSerieAtor.read(new ParIDSerieAtor(idSerie, -1));
        return lista.stream().mapToInt(ParIDSerieAtor::getIdAtor).toArray();
    }
}
