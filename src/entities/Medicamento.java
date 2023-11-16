package entities;

import java.sql.*;

public class Medicamento {
    private final String nome;
    private final String descricao;
    private final double preco;
    private int quantidadeEstoque;

    public Medicamento(String nome, String descricao, double preco, int quantidadeEstoque) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public void adicionarEstoque(int quantidade) {
        if (quantidade >= 0) {
            this.quantidadeEstoque += quantidade;
        } else {
            System.out.println("Operação não foi realizada devido a um erro");
        }
    }

    public void subtrairEstoque(int quantidade) {
        if (quantidade > 0 && quantidade <= this.quantidadeEstoque) {
            this.quantidadeEstoque -= quantidade;
        } else {
            System.out.println("Não é possível subtrair mais do que o estoque atual");
        }
    }

    public void salvarNoBanco(Connection connection) throws SQLException {
        String query = "INSERT INTO medicamentos (nome, descricao, preco, quantidade_estoque) VALUES (?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, nome);
            preparedStatement.setString(2, descricao);
            preparedStatement.setDouble(3, preco);
            preparedStatement.setInt(4, quantidadeEstoque);

            preparedStatement.executeUpdate();
        }
    }

    public void removerDoBanco(Connection connection) throws SQLException {
        String query = "DELETE FROM medicamentos WHERE nome = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, nome);
            preparedStatement.executeUpdate();
        }
    }

    public void atualizarEstoqueNoBanco(Connection connection) throws SQLException {
        String sqlUpdate = "UPDATE medicamentos SET quantidade_estoque = ? WHERE nome = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdate)) {
            preparedStatement.setInt(1, this.quantidadeEstoque);
            preparedStatement.setString(2, this.nome);
            preparedStatement.executeUpdate();
        }
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

    public int getQuantidadeEstoque() {
        return quantidadeEstoque;
    }
}
