package entidades;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.time.LocalDate;

import aeds3.EntidadeArquivo;

public class Episodio implements EntidadeArquivo {

    private int id;
    private int idSerie; // Relacionamento 1:N, chave estrangeira
    private String nome;
    private int temporada;
    private LocalDate dataLancamento;
    private int duracao; // Em minutos

    public Episodio() {
        this.id = -1;
        this.idSerie = -1;
        this.nome = "";
        this.temporada = 0;
        this.dataLancamento = LocalDate.now();
        this.duracao = 0;
    }

    public Episodio(int idSerie, String nome, int temporada, LocalDate dataLancamento, int duracao) throws Exception {
        if (idSerie < 0) {
            throw new Exception("Episódio deve estar vinculado a uma série válida.");
        }
        this.id = -1;
        this.idSerie = idSerie;
        this.nome = nome;
        this.temporada = temporada;
        this.dataLancamento = dataLancamento;
        this.duracao = duracao;
    }

    // Getters e Setters
    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public int getIdSerie() {
        return idSerie;
    }

    public void setIdSerie(int idSerie) throws Exception {
        if (idSerie < 0) {
            throw new Exception("ID da série inválido.");
        }
        this.idSerie = idSerie;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getTemporada() {
        return temporada;
    }

    public void setTemporada(int temporada) {
        this.temporada = temporada;
    }

    public LocalDate getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(LocalDate dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    public int getDuracao() {
        return duracao;
    }

    public void setDuracao(int duracao) {
        this.duracao = duracao;
    }

    public byte[] toByteArray() throws Exception {//convertendo para array de bytes
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(id);
        dos.writeInt(idSerie);
        dos.writeUTF(nome);
        dos.writeInt(temporada);
        dos.writeInt((int) dataLancamento.toEpochDay());
        dos.writeInt(duracao);
        return baos.toByteArray();
    }

    public void fromByteArray(byte[] vb) throws Exception {//pegando de um array de bytes
        ByteArrayInputStream bais = new ByteArrayInputStream(vb);
        DataInputStream dis = new DataInputStream(bais);
        id = dis.readInt();
        idSerie = dis.readInt();
        nome = dis.readUTF();
        temporada = dis.readInt();
        dataLancamento = LocalDate.ofEpochDay(dis.readInt());
        duracao = dis.readInt();
    }
}
