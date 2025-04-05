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

    public Serie[] readNome(String nome) throws Exception {//busca
        if(nome.length()==0)
            return new Serie[0];
        ArrayList<ParNameSerieID> ptis = indiceNome.read(new ParNameSerieID(nome, -1));
        if(ptis.size()>0) {
            Serie[] series = new Serie[ptis.size()];
            int j = 0;
            for(int i = 0; i < ptis.size(); i++){
                series[j] = read(ptis.get(i).getId());
                j++;
            }
            return series;
        }else 
            return new Serie[0];
    }

    @Override
    public boolean delete(int id) throws Exception {//delete por id
        Serie s = read(id);   // na superclasse
        if(s!=null) {
            if(super.delete(id))
                return indiceNome.delete(new ParNameSerieID(s.getNome(), id));
        }
        return false;
    }

    //delete por nome?

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
