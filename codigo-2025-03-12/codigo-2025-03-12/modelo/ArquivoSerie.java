package modelo;
import entidades.Serie;

import java.util.ArrayList;

import aeds3.*;

public class ArquivoSerie extends Arquivo<Serie> {

    Arquivo<Serie> arqSerie;
    ArvoreBMais<ParNameSerieID> indiceNome;

    public ArquivoSerie() throws Exception {
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
        if(nome.length()==0)
            return null;
        ArrayList<ParNameSerieID> ptis = indiceNome.read(new ParNameSerieID(nome, -1));
        if(ptis.size()>0) {
            Serie[] series = new Serie[ptis.size()];
            int i=0;
            for(ParNameSerieID pti: ptis) 
                series[i++] = read(pti.getId());
            return series;
        }
        else 
            return null;
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