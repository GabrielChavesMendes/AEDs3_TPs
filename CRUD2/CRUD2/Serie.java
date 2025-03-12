import java.time.LocalDate;

import aed3.Registro;

import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

public class Serie implements Registro {

    public int id;
    public String nome;
    public String sinopse;
    public String streaming;
    public LocalDate lancamento;
    public int idCategoria;

    public Serie() {
        this(-1, "", "", "", LocalDate.now());
    }
    public Serie(String n, String c, String streaming, LocalDate d) {
        this(-1, n, c, streaming, d);
    }

    public Serie(int i, String n, String c, String streaming, LocalDate d) {
            this.id = i;
            this.nome = n;
            this.sinopse = c;
            this.streaming = streaming;
        this.lancamento = d;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getCpf() {
        return sinopse;
    }

    public String toString() {
        return "\nID........: " + this.id +
               "\nNome......: " + this.nome +
               "\nSinopse.......: " + this.sinopse +
               "\nStreaming...: " + this.streaming +
               "\nLançamento: " + this.lancamento;
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.id);
        dos.writeUTF(this.nome);
        dos.write(this.sinopse.getBytes());
        dos.writeUTF(this.streaming);
        dos.writeInt((int) this.lancamento.toEpochDay());
        return baos.toByteArray();
    }


    public void fromByteArray(byte[] b) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);

        byte[] cpf = new byte[11];
        this.id = dis.readInt();
        this.nome = dis.readUTF();
        dis.read(cpf);
        this.sinopse = new String(cpf);
        this.streaming = dis.readUTF();
        this.lancamento = LocalDate.ofEpochDay(dis.readInt());
    }
}
