package modelo;
import aeds3.*;
import entidades.Episodio;
import java.util.ArrayList;
import java.util.List;

public class ArquivoEpisodios extends Arquivo<Episodio> {

    private ArvoreBMais<ParNameEpisodioID> indiceNome;
    private ArvoreBMais<ParIdId> indiceSerieEpisodio; // Índice Série → Episódio
    private ArvoreBMais<ParIdId> indiceTemporadaEpisodio; // Índice Temporada → Episódio

    public ArquivoEpisodios() throws Exception {
        super("episodios", Episodio.class.getConstructor());
        indiceNome = new ArvoreBMais<>(
            ParNameEpisodioID.class.getConstructor(), 
            5, 
            "./dados/" + nomeEntidade + "/indiceNome.db"
        );
        indiceSerieEpisodio = new ArvoreBMais<>(
            ParIdId.class.getConstructor(),
            5,
            "./dados/" + nomeEntidade + "/indiceSerieEpisodio.db"
        );
        indiceTemporadaEpisodio = new ArvoreBMais<>(
            ParIdId.class.getConstructor(),
            5,
            "./dados/" + nomeEntidade + "/indiceTemporadaEpisodio.db"
        );
    }

    @Override
    public int create(Episodio episodio) throws Exception {
        int id = super.create(episodio);
        episodio.setID(id); // Garante que o ID está atualizado
        
        // Índice por nome
        indiceNome.create(new ParNameEpisodioID(episodio.getNome(), id));
        
        // Índice por série
        indiceSerieEpisodio.create(new ParIdId(episodio.getIdSerie(), id));
        
        // Índice por temporada (usando ID da série + número da temporada como chave composta)
        int chaveTemporada = gerarChaveTemporada(episodio.getIdSerie(), episodio.getTemporada());
        indiceTemporadaEpisodio.create(new ParIdId(chaveTemporada, id));
        
        return id;
    }

    private int gerarChaveTemporada(int idSerie, int numTemporada) {
        // Combina ID da série e número da temporada em uma única chave
        return (idSerie << 16) | (numTemporada & 0xFFFF);
    }

    // Busca por nome (retorna todos os episódios com esse nome)
    public List<Episodio> readNome(String nome) throws Exception {
        List<Episodio> episodios = new ArrayList<>();
        for (ParNameEpisodioID par : indiceNome.read(new ParNameEpisodioID(nome, -1))) {
            episodios.add(read(par.getId()));
        }
        return episodios;
    }

    // Busca todos os episódios de uma série
    public List<Episodio> readPorSerie(int idSerie) throws Exception {
        List<Episodio> episodios = new ArrayList<>();
        for (ParIdId par : indiceSerieEpisodio.read(new ParIdId(idSerie, -1))) {
            episodios.add(read(par.getId2()));
        }
        return episodios;
    }

    public ArrayList<ParIdId> getAllEpisodesFromSerie(int id1) throws Exception {
        ArrayList<ParIdId> lista = new ArrayList<>();
        ParIdId pii = new ParIdId(id1, -1);
        lista = indiceSerieEpisodio.read(pii);

        return lista;
    }

    // Busca todos os episódios de uma temporada específica
    public List<Episodio> readPorTemporada(int idSerie, int numTemporada) throws Exception {
        List<Episodio> episodios = new ArrayList<>();
        int chaveTemporada = gerarChaveTemporada(idSerie, numTemporada);
        for (ParIdId par : indiceTemporadaEpisodio.read(new ParIdId(chaveTemporada, -1))) {
            episodios.add(read(par.getId2()));
        }
        return episodios;
    }

    @Override
    public boolean delete(int idEpisodio) throws Exception {
        Episodio episodio = read(idEpisodio);
        if (episodio != null && super.delete(idEpisodio)) {
            // Remove dos índices
            indiceNome.delete(new ParNameEpisodioID(episodio.getNome(), idEpisodio));
            indiceSerieEpisodio.delete(new ParIdId(episodio.getIdSerie(), idEpisodio));
            
            int chaveTemporada = gerarChaveTemporada(episodio.getIdSerie(), episodio.getTemporada());
            indiceTemporadaEpisodio.delete(new ParIdId(chaveTemporada, idEpisodio));
            
            return true;
        }
        return false;
    }

    @Override
    public boolean update(Episodio novoEpisodio) throws Exception {
        Episodio antigoEpisodio = read(novoEpisodio.getID());
        if (antigoEpisodio != null && super.update(novoEpisodio)) {
            // Atualiza índice de nome se necessário
            if (!antigoEpisodio.getNome().equals(novoEpisodio.getNome())) {
                indiceNome.delete(new ParNameEpisodioID(antigoEpisodio.getNome(), novoEpisodio.getID()));
                indiceNome.create(new ParNameEpisodioID(novoEpisodio.getNome(), novoEpisodio.getID()));
            }
            
            // Atualiza índices de série e temporada se necessário
            if (antigoEpisodio.getIdSerie() != novoEpisodio.getIdSerie() || 
                antigoEpisodio.getTemporada() != novoEpisodio.getTemporada()) {
                
                // Remove dos índices antigos
                indiceSerieEpisodio.delete(new ParIdId(antigoEpisodio.getIdSerie(), novoEpisodio.getID()));
                int chaveTemporadaAntiga = gerarChaveTemporada(antigoEpisodio.getIdSerie(), antigoEpisodio.getTemporada());
                indiceTemporadaEpisodio.delete(new ParIdId(chaveTemporadaAntiga, novoEpisodio.getID()));
                
                // Adiciona aos novos índices
                indiceSerieEpisodio.create(new ParIdId(novoEpisodio.getIdSerie(), novoEpisodio.getID()));
                int chaveTemporadaNova = gerarChaveTemporada(novoEpisodio.getIdSerie(), novoEpisodio.getTemporada());
                indiceTemporadaEpisodio.create(new ParIdId(chaveTemporadaNova, novoEpisodio.getID()));
            }
            return true;
        }
        return false;
    }
}