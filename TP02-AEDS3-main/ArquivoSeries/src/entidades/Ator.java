package entidades;
import aeds3.EntidadeArquivo;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.time.LocalDate;


public class Ator implements EntidadeArquivo {
    private int id;
    private int idSerie;
    private String nome;
    private String nacionalidade;
    private LocalDate dataNascimento;

    public Ator(int id, String nome, String nacionalidade, LocalDate dataNascimento) {
        this.id = id;
        this.nome = nome;
        this.nacionalidade = nacionalidade;
        this.dataNascimento = dataNascimento;
        
    }

    public Ator() {
        this.id = -1;
        this.idSerie = -1;
        this.nome = "";
        this.nacionalidade = "";
        this.dataNascimento = LocalDate.now();
    }
    

    //Construtor alternativo (caso queira criar sem data de nascimento ou genero)
    public Ator(int id, String nome) {
        this.id = id;
        this.nome = nome;
        this.nacionalidade = "";
        this.dataNascimento = null;
        
    }

    
    @Override
    public int getID() {
        return id; // Retorna o ID do ator
    }

    @Override
    public void setID(int id) {
        this.id = id; // Define o ID do ator
    }

    public String getNome() {
        return nome;
    }

    public String getNacionalidade() {
        return nacionalidade;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }


    
    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setNacionalidade(String nacionalidade) {
        this.nacionalidade = nacionalidade;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

   

    public byte[] toByteArray() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(id);
        dos.writeInt(idSerie);
        dos.writeUTF(nome);
        dos.writeUTF(nacionalidade);
        dos.writeInt((int) dataNascimento.toEpochDay()); 
        return baos.toByteArray();
    }
    
    public void fromByteArray(byte[] vb) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(vb);
        DataInputStream dis = new DataInputStream(bais);
        id = dis.readInt();
        idSerie = dis.readInt();
        nome = dis.readUTF();
        nacionalidade = dis.readUTF();
        dataNascimento = LocalDate.ofEpochDay(dis.readInt());
         
    }
    @Override
    public String toString() {
         return "ID: " + id +
           ", ID da Série: " + idSerie +
           ", Nome: " + nome +
           ", Nacionalidade: " + nacionalidade +
           ", Nascimento: " + dataNascimento;
    }
}
