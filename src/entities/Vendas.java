package entities;

import java.text.DecimalFormat;

public class Vendas {
    DecimalFormat df = new DecimalFormat("#.00");
    private final String nomeMedicamento; // Alterado para uma única String
    private final int quantidade;
    private final double valorTotal;

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


