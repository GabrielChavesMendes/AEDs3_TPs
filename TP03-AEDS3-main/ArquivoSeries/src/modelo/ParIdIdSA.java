//series e atores
package modelo;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ParIdIdSA implements aeds3.RegistroArvoreBMais<ParIdIdSA> {

  private int id1;
  private int id2;
  private short TAMANHO = 8;

  public ParIdIdSA() {
    this(-1, -1);
  }

  public ParIdIdSA(int n1) {
    this(n1, -1);
  }

  public ParIdIdSA(int n1, int n2) {
    try {
      this.id1 = n1; // ID da entidade agregadora
      this.id2 = n2; // ID da outra entidade
    } catch (Exception ec) {
      ec.printStackTrace();
    }
  }

  @Override
  public ParIdIdSA clone() {
    return new ParIdIdSA(this.id1, this.id2);
  }

  public short size() {
    return this.TAMANHO;
  }

  public int compareTo(ParIdIdSA a) {
    if (this.id1 == a.id1)
      // Só compara os valores de id2, se o id2 da busca for diferente de -1
      // Isso é necessário para que seja possível a busca de lista
      return this.id2 == -1 ? 0 : this.id2 - a.id2;
    else
      return this.id1 - a.id1;
  }

  public String toString() {
    return String.format("%3d", this.id1) + ";" + String.format("%-3d", this.id2);
  }

  public byte[] toByteArray() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(baos);
    dos.writeInt(this.id1);
    dos.writeInt(this.id2);
    return baos.toByteArray();
  }

  public void fromByteArray(byte[] ba) throws IOException {
    ByteArrayInputStream bais = new ByteArrayInputStream(ba);
    DataInputStream dis = new DataInputStream(bais);
    this.id1 = dis.readInt();
    this.id2 = dis.readInt();
  }

}