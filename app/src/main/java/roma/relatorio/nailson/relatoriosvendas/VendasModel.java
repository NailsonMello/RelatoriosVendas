package roma.relatorio.nailson.relatoriosvendas;

public class VendasModel {

    private String cliente;
    private String vendedor;
    private String bisc_tapioca;
    private String torrada;
    private String chips;

    public VendasModel() {
    }

    public VendasModel(String cliente, String vendedor, String bisc_tapioca, String torrada, String chips) {
        this.cliente = cliente;
        this.vendedor = vendedor;
        this.bisc_tapioca = bisc_tapioca;
        this.torrada = torrada;
        this.chips = chips;
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

    public String getBisc_tapioca() {
        return bisc_tapioca;
    }

    public void setBisc_tapioca(String bisc_tapioca) {
        this.bisc_tapioca = bisc_tapioca;
    }

    public String getTorrada() {
        return torrada;
    }

    public void setTorrada(String torrada) {
        this.torrada = torrada;
    }

    public String getChips() {
        return chips;
    }

    public void setChips(String chips) {
        this.chips = chips;
    }
}
