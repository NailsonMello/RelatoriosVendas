package roma.relatorio.nailson.relatoriosvendas;

public class ModelCatalogo {

    private String fornecedor;
    private String produto;
    private String imagem;
    private double preco;

    public ModelCatalogo() {
    }

    public ModelCatalogo(String fornecedor, String produto, String imagem, double preco) {
        this.fornecedor = fornecedor;
        this.produto = produto;
        this.imagem = imagem;
        this.preco = preco;

    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public String getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }

    public String getProduto() {
        return produto;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

}
