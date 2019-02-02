package roma.relatorio.nailson.relatoriosvendas.RegistroPonto;

public class ModelPonto {
    private String imagem;
    private String telefone;
    private String nome;
    private String funcionario;
    private String localEditado;
    private String localOriginal;
    private String nomeLoja;
    private String cliente;
    private String vendedor;
    private String endereco;
    private String fantasia;
    private String status;
    private String data;
    private String hora;
    private String codigo;
    private String cep;
    private String loja;
    private String motivo;
    private String pedido;
    private Double porcentagem;

    public ModelPonto(){

    }

    public ModelPonto(String imagem, String telefone, String nome, String funcionario, String localEditado, String localOriginal, String nomeLoja, String cliente, String vendedor, String endereco, String fantasia, String status, String data, String hora, String codigo, String cep, String loja, String motivo, String pedido, Double porcentagem) {
        this.imagem = imagem;
        this.telefone = telefone;
        this.nome = nome;
        this.funcionario = funcionario;
        this.localEditado = localEditado;
        this.localOriginal = localOriginal;
        this.nomeLoja = nomeLoja;
        this.cliente = cliente;
        this.vendedor = vendedor;
        this.endereco = endereco;
        this.fantasia = fantasia;
        this.status = status;
        this.data = data;
        this.hora = hora;
        this.codigo = codigo;
        this.cep = cep;
        this.loja = loja;
        this.motivo = motivo;
        this.pedido = pedido;
        this.porcentagem = porcentagem;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(String funcionario) {
        this.funcionario = funcionario;
    }

    public String getLocalEditado() {
        return localEditado;
    }

    public void setLocalEditado(String localEditado) {
        this.localEditado = localEditado;
    }

    public String getLocalOriginal() {
        return localOriginal;
    }

    public void setLocalOriginal(String localOriginal) {
        this.localOriginal = localOriginal;
    }

    public String getNomeLoja() {
        return nomeLoja;
    }

    public void setNomeLoja(String nomeLoja) {
        this.nomeLoja = nomeLoja;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getVendedor() {
        return vendedor;
    }

    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getFantasia() {
        return fantasia;
    }

    public void setFantasia(String fantasia) {
        this.fantasia = fantasia;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getLoja() {
        return loja;
    }

    public void setLoja(String loja) {
        this.loja = loja;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getPedido() {
        return pedido;
    }

    public void setPedido(String pedido) {
        this.pedido = pedido;
    }

    public Double getPorcentagem() {
        return porcentagem;
    }

    public void setPorcentagem(Double porcentagem) {
        this.porcentagem = porcentagem;
    }
}

