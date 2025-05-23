package modelo;

import aeds3.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.Normalizer;
import java.util.regex.Pattern;

public class ParNameAtorID implements RegistroArvoreBMais<ParNameAtorID> {

    private String nome;
    private int id;
    private short TAMANHO = 24;
    private short TAMANHO_NOME = 20;

    public ParNameAtorID() throws Exception {
        this("", -1);
    }

    public ParNameAtorID(String nome) throws Exception {
        this(nome, -1);
    }

    public ParNameAtorID(String nome, int id) throws Exception {
        this.nome = nome;
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public int getId() {
        return id;
    }

    @Override
    public ParNameAtorID clone() {
        try {
            return new ParNameAtorID(this.nome, this.id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public short size() {
        return this.TAMANHO;
    }

    @Override
    public int compareTo(ParNameAtorID outro) {
        String str1 = transforma(this.nome);
        String str2 = transforma(outro.nome);

        if (str2.length() > str1.length())
            str2 = str2.substring(0, str1.length());

        if (str1.compareTo(str2) == 0) {
            if (this.id == -1)
                return 0;
            else
                return this.id - outro.id;
        } else {
            return str1.compareTo(str2);
        }
    }

    @Override
    public String toString() {
        return this.nome + ";" + String.format("%-3d", this.id);
    }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        byte[] vb = new byte[TAMANHO_NOME];
        byte[] vbNome = this.nome.getBytes();
        int i = 0;
        while (i < vbNome.length && i < TAMANHO_NOME) {
            vb[i] = vbNome[i];
            i++;
        }
        while (i < TAMANHO_NOME) {
            vb[i] = ' ';
            i++;
        }
        dos.write(vb);
        dos.writeInt(this.id);
        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        byte[] vb = new byte[TAMANHO_NOME];
        dis.read(vb);
        this.nome = (new String(vb)).trim();
        this.id = dis.readInt();
    }

    public static String transforma(String str) {
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("").toLowerCase();
    }
}