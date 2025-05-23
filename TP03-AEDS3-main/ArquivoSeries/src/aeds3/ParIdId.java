package aeds3; 

/*
Esta classe representa um objeto para uma entidade
que será armazenado em uma árvore B+

Neste caso em particular, este objeto é representado
por dois números inteiros para que possa conter
relacionamentos entre dois IDs de entidades quaisquer
 
Implementado pelo Prof. Marcos Kutova
v1.0 - 2021
*/

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ParIdId implements aeds3.RegistroArvoreBMais<ParIdId> {
  private int id1; // chave
  private int id2;     // valor
  private final short TAMANHO = 8;  // tamanho em bytes

  public ParIdId() {
      this.id1 = -1;
      this.id2 = -1;
  }

  public ParIdId(int id1, int id2) throws Exception {
      try {
          this.id1 = id1; 
          this.id2 = id2; 
        } catch (Exception ec) {
          ec.printStackTrace();
        }
  }

  public int getId1() {
      return this.id1;
  }

  public int getId2() {
      return this.id2;
  }

  @Override
  public ParIdId clone() {
      try {
          return new ParIdId(this.id1, this.id2);
      } catch (Exception e) {
          e.printStackTrace();
      }
      return null;
  }

  public short size(){
      return this.TAMANHO;
  }

  public int compareTo(ParIdId a) {
      if (this.id1 != a.id1)
        return this.id1 - a.id1;
      else
        // Só compara os valores de id2, se o id2 da busca for diferente de -1
        // Isso é necessário para que seja possível a busca de lista
        return this.id2 == -1 ? 0 : this.id2 - a.id2;
    }

  public String toString() {
      return String.format("%3d", this.id1) + ";" + String.format("%-3d", this.id2);
  }

  public byte[] toByteArray() throws IOException{
      ByteArrayOutputStream bao = new ByteArrayOutputStream();
      DataOutputStream dos = new DataOutputStream(bao);
      dos.writeInt(this.id1);
      dos.writeInt(this.id2);
      return bao.toByteArray();
  }

  public void fromByteArray(byte[] byteArray) throws IOException{
      ByteArrayInputStream bai = new ByteArrayInputStream(byteArray);
      DataInputStream dis = new DataInputStream(bai);
      this.id1 = dis.readInt();
      this.id2 = dis.readInt();
  }

}