package entidades;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.time.LocalDate;
import java.util.Set;

import aeds3.EntidadeArquivo;

public class Serie implements EntidadeArquivo {

    private int id;
    private String nome;
    private String sinopse;
    private LocalDate dataLancamento;
    private String streaming;

    private static final Set<String> STREAMING_SERVICES = Set.of(
        "Netflix", "Prime", "Max", "Disney", "HBO", "AppleTV", "Hulu", "Paramount", "StarPlus", "Crunchyroll"
    );

    public Serie() throws Exception {
        this(-1, "", "", LocalDate.now(), "");
    }

    public Serie(String nome, String sinopse, LocalDate dataLancamento, String streaming) throws Exception {
        this(-1, nome, sinopse, dataLancamento, streaming);
    }

    public Serie(int id, String nome, String sinopse, LocalDate dataLancamento, String streaming) throws Exception {
        this.id = id;
        this.nome = nome;
        this.sinopse = sinopse;
        this.dataLancamento = dataLancamento;
        this.streaming = streaming;

        if (!isValidStreaming(streaming)) {
            throw new Exception("Streaming inválido");
        }
    }

    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getSinopse() {
        return sinopse;
    }
    
    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }
    
    public LocalDate getDataLancamento() {
        return dataLancamento;
    }
    
    public void setDataLancamento(LocalDate dataLancamento) {
        this.dataLancamento = dataLancamento;
    }
    
    public String getStreaming() {
        return streaming;
    }
    
    public void setStreaming(String streaming) throws Exception {
        if (!isValidStreaming(streaming)) {
            throw new Exception("Streaming inválido");
        }
        this.streaming = streaming;
    }
    
    public byte[] toByteArray() throws Exception {//convertendo para array de bytes
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(id);
        dos.writeUTF(nome);
        dos.writeUTF(sinopse);
        dos.writeInt((int)dataLancamento.toEpochDay());
        dos.writeUTF(streaming);
        return baos.toByteArray();
    }
    
    public void fromByteArray(byte[] vb) throws Exception {//pegando de um array de bytes
        ByteArrayInputStream bais = new ByteArrayInputStream(vb);
        DataInputStream dis = new DataInputStream(bais);
        id = dis.readInt();
        nome = dis.readUTF();
        sinopse = dis.readUTF();
        dataLancamento = LocalDate.ofEpochDay(dis.readInt());
        streaming = dis.readUTF();
    }
    
    public static boolean isValidStreaming(String streaming) {
        if (!STREAMING_SERVICES.contains(streaming)) {
            return false;
        }
        return true;
    }

    @Override
    public void setID(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setID'");
    }

    @Override
    public int getID() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getID'");
    }

    public static boolean isValidNome(String nome2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isValidNome'");
    }
}