package modelo;

import aeds3.*;
import entidades.Ator;
import java.io.File;
import java.util.ArrayList;

public class ArquivoAtores extends Arquivo<Ator> {

    ArvoreBMais<ParNameAtorID> indiceNome;

    public ArquivoAtores() throws Exception {
        // Inicializa o arquivo base de atores
        super("atores", Ator.class.getConstructor());


        // Garante que a pasta de dados exista
        File pasta = new File("./dados/atores");
        if (!pasta.exists()) {
            pasta.mkdirs();
        }
        
        // Inicializa o índice B+ para busca por nome
        indiceNome = new ArvoreBMais<>(
            ParNameAtorID.class.getConstructor(),
            5,
            "./dados/atores/indiceNome.db"
        );
    }

    @Override
    public int create(Ator ator) throws Exception {
        int id = super.create(ator);
        indiceNome.create(new ParNameAtorID(ator.getNome(), id));
        return id;
    }

    public Ator[] readNome(String nome) throws Exception {
        if (nome.length() == 0)
            return new Ator[0];

        ArrayList<ParNameAtorID> lista = indiceNome.read(new ParNameAtorID(nome, -1));
        if (lista.size() > 0) {
            Ator[] atores = new Ator[lista.size()];
            int i = 0;
            for (ParNameAtorID p : lista) {
                atores[i++] = read(p.getId());
            }
            return atores;
        } else {
            return new Ator[0];
        }
    }

    @Override
    public boolean delete(int id) throws Exception {
        Ator ator = read(id);
        if (ator != null) {
            if (super.delete(id)) {
                return indiceNome.delete(new ParNameAtorID(ator.getNome(), id));
            }
        }
        return false;
    }

    @Override
    public boolean update(Ator novoAtor) throws Exception {
        Ator antigoAtor = read(novoAtor.getID());
        if (antigoAtor != null) {
            if (super.update(novoAtor)) {
                if (!antigoAtor.getNome().equals(novoAtor.getNome())) {
                    indiceNome.delete(new ParNameAtorID(antigoAtor.getNome(), antigoAtor.getID()));
                    indiceNome.create(new ParNameAtorID(novoAtor.getNome(), novoAtor.getID()));
                }
                return true;
            }
        }
        return false;
    }
    


}
