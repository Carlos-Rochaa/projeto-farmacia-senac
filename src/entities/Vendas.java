package entities;


public class Vendas {
    private String nomeMedicamento; // Alterado para uma única String
    private int quantidade;
    private double valorTotal;

    public Vendas(String nomeMedicamento, int quantidade, double valorTotal) {
        this.nomeMedicamento = nomeMedicamento;
        this.quantidade = quantidade;
        this.valorTotal = valorTotal;
    }

    public String getNomeMedicamento() {
        return nomeMedicamento;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public double getValorTotal() {
        return valorTotal;
    }
}


