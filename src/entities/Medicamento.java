package entities;
public class Medicamento { //Nova classe criada para armazenar informações sobre os medicamentos
    private  String nome;
    private  String descricao;
    private  double preco;

    private  int qtdEstoque;


    public Medicamento(String nome, String descricao, double preco, int qtdEstoque) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.qtdEstoque = qtdEstoque;
    }


    public void adicionarEstoque(int quantidade) {
        if (quantidade >= 0) {
            this.qtdEstoque += quantidade;
        } else {
            System.out.println("Operação não foi realizada devido a um erro");
        }
    }

    public void subtrairEstoque(int quantidade) {
        if (quantidade > 0 && quantidade <= this.qtdEstoque) {
            this.qtdEstoque -= quantidade;
        } else {
            System.out.println("Não é possível subtrair mais do que o estoque atual");
        }
    }

    public boolean vender(int quantidade) {
        if (quantidade > 0 && quantidade <= qtdEstoque) {
            this.qtdEstoque -= quantidade;
            return true;
        }
        return false;
    }


    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public double getPreco() {
        return preco;
    }

    public int getQtdEstoque(){
        return qtdEstoque;
    }
}

