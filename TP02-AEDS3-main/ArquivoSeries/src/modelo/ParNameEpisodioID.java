package modelo;

import aeds3.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.regex.Pattern;

public class ParNameEpisodioID implements RegistroArvoreBMais<ParNameEpisodioID> {

    private String nome;
    private int id;
    private short TAMANHO = 24;
    private short TAMANHO_NOME = 20;

    public ParNameEpisodioID() throws Exception {
        this("", -1);
    }

    public ParNameEpisodioID(String n) throws Exception {
        this(n, -1);
    }

    public ParNameEpisodioID(String t, int i) throws Exception {
        if (!t.isEmpty()) {
            byte[] vb = t.getBytes(StandardCharsets.UTF_8);
        }
        this.nome = t;
        this.id = i;
    }

    public String getNome() {
        return nome;
    }

    public int getId() {
        return id;
    }

    @Override
    public ParNameEpisodioID clone() {
        try {
            return new ParNameEpisodioID(this.nome, this.id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public short size() {
        return this.TAMANHO;
    }

    public int compareTo(ParNameEpisodioID a) {
        String str1 = transforma(this.nome);
        String str2 = transforma(a.nome);

        if (str2.length() > str1.length())
            str2 = str2.substring(0, str1.length());

        if (str1.compareTo(str2) == 0)
            return this.id == -1 ? 0 : this.id - a.id;
        else
            return str1.compareTo(str2);
    }

    public String toString() {
        return this.nome + ";" + String.format("%-3d", this.id);
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        byte[] vb = new byte[TAMANHO_NOME];
        byte[] vbNome = this.nome.getBytes();
        int i = 0;
        while (i < vbNome.length) {
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
