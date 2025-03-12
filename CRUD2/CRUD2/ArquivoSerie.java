import aed3.*;

public class ArquivoSerie extends aed3.Arquivo<Serie> {

    Arquivo<Serie> arqSerie;
    HashExtensivel<ParCPFID> indiceIndiretoCPF;

    public ArquivoSerie() throws Exception {
        super("series", Serie.class.getConstructor());
        indiceIndiretoCPF = new HashExtensivel<>(
            ParCPFID.class.getConstructor(), 
            4, 
            ".\\dados\\series\\series.d.db",   // diretório
            ".\\dados\\series\\series.c.db"    // cestos 
        );
    }

    @Override
    public int create(Serie c) throws Exception {
        int id = super.create(c);
        indiceIndiretoCPF.create(new ParCPFID(c.getCpf(), id));
        return id;
    }

    public Serie read(String cpf) throws Exception {
        ParCPFID pci = indiceIndiretoCPF.read(ParCPFID.hash(cpf));
        if(pci == null)
            return null;
        return read(pci.getId());
    }
    
    public boolean delete(String cpf) throws Exception {
        ParCPFID pci = indiceIndiretoCPF.read(ParCPFID.hash(cpf));
        if(pci != null) 
            if(delete(pci.getId())) 
                return indiceIndiretoCPF.delete(ParCPFID.hash(cpf));
        return false;
    }

    @Override
    public boolean delete(int id) throws Exception {
        Serie c = super.read(id);
        if(c != null) {
            if(super.delete(id))
                return indiceIndiretoCPF.delete(ParCPFID.hash(c.getCpf()));
        }
        return false;
    }

    @Override
    public boolean update(Serie novoCliente) throws Exception {
        Serie clienteVelho = read(novoCliente.getCpf());
        if(super.update(novoCliente)) {
            if(novoCliente.getCpf().compareTo(clienteVelho.getCpf())!=0) {
                indiceIndiretoCPF.delete(ParCPFID.hash(clienteVelho.getCpf()));
                indiceIndiretoCPF.create(new ParCPFID(novoCliente.getCpf(), novoCliente.getId()));
            }
            return true;
        }
        return false;
    }
}
