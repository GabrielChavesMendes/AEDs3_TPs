package modelo;

import aeds3.RegistroArvoreBMais;

import java.io.*;

public class ParIDAtorSerie implements RegistroArvoreBMais<ParIDAtorSerie> {

    private int idAtor;
    private int idSerie;

    public ParIDAtorSerie() {
        this(-1, -1);
    }

    public ParIDAtorSerie(int idAtor, int idSerie) {
        this.idAtor = idAtor;
        this.idSerie = idSerie;
    }

    public int getIdAtor() {
        return idAtor;
    }

    public int getIdSerie() {
        return idSerie;
    }

    @Override
    public ParIDAtorSerie clone() {
        return new ParIDAtorSerie(idAtor, idSerie);
    }

    @Override
    public int compareTo(ParIDAtorSerie outro) {
        if (this.idAtor != outro.idAtor)
            return this.idAtor - outro.idAtor;
        return this.idSerie - outro.idSerie;
    }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(idAtor);
        dos.writeInt(idSerie);
        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] bytes) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        DataInputStream dis = new DataInputStream(bais);
        this.idAtor = dis.readInt();
        this.idSerie = dis.readInt();
    }

    @Override
    public short size() {
        return 8; // 2 inteiros de 4 bytes
    }
}
