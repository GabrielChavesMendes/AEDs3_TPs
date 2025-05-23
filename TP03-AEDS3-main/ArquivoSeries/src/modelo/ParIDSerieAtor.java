package modelo;

import aeds3.RegistroArvoreBMais;

import java.io.*;

public class ParIDSerieAtor implements RegistroArvoreBMais<ParIDSerieAtor> {

    private int idSerie;
    private int idAtor;

    public ParIDSerieAtor() {
        this(-1, -1);
    }

    public ParIDSerieAtor(int idSerie, int idAtor) {
        this.idSerie = idSerie;
        this.idAtor = idAtor;
    }

    public int getIdSerie() {
        return idSerie;
    }

    public int getIdAtor() {
        return idAtor;
    }

    @Override
    public ParIDSerieAtor clone() {
        return new ParIDSerieAtor(idSerie, idAtor);
    }

    @Override
    public int compareTo(ParIDSerieAtor outro) {
        if (this.idSerie != outro.idSerie)
            return this.idSerie - outro.idSerie;
        return this.idAtor - outro.idAtor;
    }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(idSerie);
        dos.writeInt(idAtor);
        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] bytes) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        DataInputStream dis = new DataInputStream(bais);
        this.idSerie = dis.readInt();
        this.idAtor = dis.readInt();
    }

    @Override
    public short size() {
        return 8;
    }
}
