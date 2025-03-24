package modelo;
import aeds3.*;
import entidades.Serie;
import java.util.ArrayList;

public class ArquivoSeries extends Arquivo<Serie> {

    Arquivo<Serie> arqSerie;
    ArvoreBMais<ParNameSerieID> indiceNome;

    public ArquivoSeries() throws Exception {
        super("series", Serie.class.getConstructor());
        indiceNome = new ArvoreBMais<>(
            ParNameSerieID.class.getConstructor(), 
            5, 
            "./dados/"+nomeEntidade+"/indiceNome.db");
    }

    @Override
    public int create(Serie s) throws Exception {
        int id = super.create(s);
        indiceNome.create(new ParNameSerieID(s.getNome(), id));
        return id;
    }

    public Serie[] readNome(String nome) throws Exception {
        if(nome == null || nome.isEmpty())
            return new Serie[0];
            
        ArrayList<ParNameSerieID> ptis = indiceNome.read(new ParNameSerieID(nome, -1));
        if(ptis != null && !ptis.isEmpty()) {
            Serie[] series = new Serie[ptis.size()];
            for(int i = 0; i < ptis.size(); i++) {
                series[i] = read(ptis.get(i).getId());
            }
            return series;
        }
        return new Serie[0];
    }

    @Override
    public boolean delete(int id) throws Exception {
        Serie s = read(id);
        if(s != null) {
            // Primeiro remove do índice
            boolean removedFromIndex = indiceNome.delete(new ParNameSerieID(s.getNome(), id));
            // Depois remove do arquivo principal
            boolean removedFromFile = super.delete(id);
            return removedFromIndex && removedFromFile;
        }
        return false;
    }

    public boolean delete(Serie serie) throws Exception {
        if(serie != null) {
            return delete(serie.getID());
        }
        return false;
    }

    public int deletePorNome(String nome) throws Exception {
        if(nome == null || nome.isEmpty())
            return 0;
            
        Serie[] series = readNome(nome);
        int count = 0;
        for(Serie s : series) {
            if(delete(s.getID())) {
                count++;
            }
        }
        return count;
    }

    @Override
    public boolean update(Serie novaSerie) throws Exception {
        Serie s = read(novaSerie.getID());    // na superclasse
        if(s!=null) {
            if(super.update(novaSerie)) {
                if(!s.getNome().equals(novaSerie.getNome())) {
                    indiceNome.delete(new ParNameSerieID(s.getNome(), s.getID()));
                    indiceNome.create(new ParNameSerieID(novaSerie.getNome(), novaSerie.getID()));
                }
                return true;
            }
        }
        return false;
    }


}
