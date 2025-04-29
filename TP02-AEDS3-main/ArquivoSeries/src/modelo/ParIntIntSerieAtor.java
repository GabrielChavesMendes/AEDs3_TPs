package modelo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import aeds3.*;
public class ParIntIntSerieAtor implements RegistroArvoreBMais<ParIntIntSerieAtor> {

    private int idSerie;  
    private int idAtor; 
    private short TAMANHO = 8;

    public ParIntIntSerieAtor() {
        this(-1, -1);
    }

    public ParIntIntSerieAtor(int idSerie) {
        this(idSerie, -1);
    }

    public ParIntIntSerieAtor(int idSerie, int idAtor) {
        try {
            this.idSerie = idSerie;  
            this.idAtor = idAtor;    
        } catch (Exception ec) {
            ec.printStackTrace();
        }
    }

    @Override
    public ParIntIntSerieAtor clone() {
        return new ParIntIntSerieAtor(this.idSerie, this.idAtor);
    }

    public short size() {
        return this.TAMANHO;
    }

    public int compareTo(ParIntIntSerieAtor a) {
        if (this.idSerie != a.idSerie)
            return this.idSerie - a.idSerie;
        else
            return this.idAtor == -1 ? 0 : this.idAtor - a.idAtor;
    }

    public String toString() {
        return String.format("%3d", this.idSerie) + ";" + String.format("%-3d", this.idAtor);
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.idSerie);
        dos.writeInt(this.idAtor);
        return baos.toByteArray();
    }

    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        this.idSerie = dis.readInt();
        this.idAtor = dis.readInt();
    }
}
