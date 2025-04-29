package modelo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import aeds3.*;

public class ParIntIntAtorSerie implements RegistroArvoreBMais<ParIntIntAtorSerie> {

    private int idAtor;   // ID do Ator
    private int idSerie;  // ID da Série
    private short TAMANHO = 8;

    public ParIntIntAtorSerie() {
        this(-1, -1);
    }

    public ParIntIntAtorSerie(int idAtor) {
        this(idAtor, -1);
    }

    public ParIntIntAtorSerie(int idAtor, int idSerie) {
        try {
            this.idAtor = idAtor;   
            this.idSerie = idSerie; 
        } catch (Exception ec) {
            ec.printStackTrace();
        }
    }

    @Override
    public ParIntIntAtorSerie clone() {
        return new ParIntIntAtorSerie(this.idAtor, this.idSerie);
    }

    public short size() {
        return this.TAMANHO;
    }

    public int compareTo(ParIntIntAtorSerie a) {
        if (this.idAtor != a.idAtor)
            return this.idAtor - a.idAtor;
        else
            return this.idSerie == -1 ? 0 : this.idSerie - a.idSerie;
    }

    public String toString() {
        return String.format("%3d", this.idAtor) + ";" + String.format("%-3d", this.idSerie);
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.idAtor);
        dos.writeInt(this.idSerie);
        return baos.toByteArray();
    }

    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        this.idAtor = dis.readInt();
        this.idSerie = dis.readInt();
    }
}
