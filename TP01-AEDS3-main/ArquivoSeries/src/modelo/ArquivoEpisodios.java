package modelo;
import aeds3.*;
import entidades.Episodio;
import java.util.ArrayList;

public class ArquivoEpisodios extends Arquivo<Episodio> {

    private ArvoreBMais<ParNameEpisodioID> indiceNome;
    private ArvoreBMais<ParIdId> indiceSerie;

    public ArquivoEpisodios() throws Exception {
        super("episodios", Episodio.class.getConstructor());
        indiceNome = new ArvoreBMais<>(
            ParNameEpisodioID.class.getConstructor(), 
            5, 
            "./dados/" + nomeEntidade + "/indiceNome.db"
        );
    }

    @Override
    public int create(Episodio e) throws Exception {
        int id = super.create(e);
        indiceNome.create(new ParNameEpisodioID(e.getNome(), id));
        indiceSerie.create(new ParIdId(e.getIdSerie(), id)); 
        return id;
    }

    public Episodio[] readNome(String nome) throws Exception {
        if (nome.length() == 0)
            return new Episodio[0];  
        
        ArrayList<ParNameEpisodioID> ptis = indiceNome.read(new ParNameEpisodioID(nome, -1));
        if (ptis.size() > 0) {
            Episodio[] episodios = new Episodio[ptis.size()];
            int j = 0;
            for (int i = 0; i < ptis.size(); i++) {
                episodios[j] = read(ptis.get(i).getId());
                j++;
            }
            return episodios;
        } else {
            return new Episodio[0];  
        }
    }

    @Override
    public boolean delete(int id) throws Exception {
        Episodio e = read(id);
        if (e != null) {
            if (super.delete(id)) {
                indiceNome.delete(new ParNameEpisodioID(e.getNome(), id));
                indiceSerie.delete(new ParIdId(e.getIdSerie(), id)); // Remove do índice de série
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean update(Episodio novoEpisodio) throws Exception {
        Episodio e = read(novoEpisodio.getID());
        if (e != null) {
            String nomeAntigo = e.getNome();
            String nomeNovo = novoEpisodio.getNome();
            int idSerieAntiga = e.getIdSerie();
            int idSerieNova = novoEpisodio.getIdSerie();
            int id = novoEpisodio.getID();
            
            if (super.update(novoEpisodio)) {
                // Atualiza índice por nome
                if (!nomeAntigo.equals(nomeNovo)) {
                    indiceNome.delete(new ParNameEpisodioID(nomeAntigo, id));
                    indiceNome.create(new ParNameEpisodioID(nomeNovo, id));
                }
                // Atualiza índice por série
                if (idSerieAntiga != idSerieNova) {
                    indiceSerie.delete(new ParIdId(idSerieAntiga, id));
                    indiceSerie.create(new ParIdId(idSerieNova, id));
                }
                return true;
            }
        }
        return false;
    }

    public Episodio[] readPorSerie(int idSerie) throws Exception {
        ArrayList<Episodio> episodios = new ArrayList<>();
        
        // Busca no índice usando ParIdId (idSerie como id1, qualquer valor como id2)
        ArrayList<ParIdId> pares = indiceSerie.read(new ParIdId(idSerie));
        
        for (ParIdId par : pares) {
            Episodio ep = read(par.getId2()); // id2 contém o ID do episódio
            if (ep != null) {
                episodios.add(ep);
            }
        }
        
        return episodios.toArray(new Episodio[0]);
    }
}
