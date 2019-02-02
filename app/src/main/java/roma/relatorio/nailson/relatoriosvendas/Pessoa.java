package roma.relatorio.nailson.relatoriosvendas;

public class Pessoa {

    private String fornecedor;
    private String nome;
    private String descricao;
    private String imagem;
    private String titulo;
    private String files;

    public Pessoa() {
    }

    public Pessoa(String fornecedor, String nome, String descricao, String imagem, String titulo, String files) {
        this.fornecedor = fornecedor;
        this.nome = nome;
        this.descricao = descricao;
        this.imagem = imagem;
        this.titulo = titulo;
        this.files = files;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getFiles() {
        return files;
    }

    public void setFiles(String files) {
        this.files = files;
    }

    public String getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }
}