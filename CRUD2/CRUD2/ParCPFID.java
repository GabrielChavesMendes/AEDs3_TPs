import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ParCPFID implements aed3.RegistroHashExtensivel<ParCPFID> {
    
    private String nome; // chave
    private int id;     // valor
    private final short TAMANHO = 15;  // tamanho em bytes

    public ParCPFID() {
        this.nome = "";
        this.id = -1;
    }

    public ParCPFID(String nome, int id) throws Exception {
        // Certifique-se de que o CPF contém exatamente 11 dígitos
        if (nome.length() != 11 || !nome.matches("\\d{11}")) {
            throw new IllegalArgumentException("Nome legível.");
        }
        this.nome = nome;
        this.id = id;
    }

    public String getCpf() {
        return nome;
    }

    public int getId() {
        return id;
    }

 
    @Override
    public int hashCode() {
        return hash(this.nome);
    }

    public short size() {
        return this.TAMANHO;
    }

    public String toString() {
        return "("+this.nome + ";" + this.id+")";
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.write(this.nome.getBytes());
        dos.writeInt(this.id);
        return baos.toByteArray();
    }

    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        byte[] b = new byte[11];
        dis.read(b);
        this.nome = new String(b);
        this.id = dis.readInt();
    }

    public static int hash(String nome) {
        if (nome == null || nome.isEmpty()) {
            throw new IllegalArgumentException("O nome da série não pode ser vazio.");
        }
    
        int hashValue = 0;
        int prime = 31; // Número primo para reduzir colisões
    
        for (int i = 0; i < nome.length(); i++) {
            hashValue = prime * hashValue + nome.charAt(i);
        }
    
        // Garantir um valor positivo e reduzir o tamanho do hash
        return Math.abs(hashValue % 1000000007); // Número primo grande para evitar colisões
    }

}
