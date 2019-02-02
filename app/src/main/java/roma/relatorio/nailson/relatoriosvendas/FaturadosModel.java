package roma.relatorio.nailson.relatoriosvendas;

public class FaturadosModel {

    private String cliente;
    private String vendedor;
    private double valor;
    private Long nfe;
    private String data_emissao;
    private String cnpj;

    public FaturadosModel() {
    }

    public FaturadosModel(String cliente, String vendedor, double valor, Long nfe, String data_emissao, String cnpj) {
        this.cliente = cliente;
        this.vendedor = vendedor;
        this.valor = valor;
        this.nfe = nfe;
        this.data_emissao = data_emissao;
        this.cnpj = cnpj;
    }

    public Long getNfe() {
        return nfe;
    }

    public void setNfe(Long nfe) {
        this.nfe = nfe;
    }

    public String getData_emissao() {
        return data_emissao;
    }

    public void setData_emissao(String data_emissao) {
        this.data_emissao = data_emissao;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
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

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}
