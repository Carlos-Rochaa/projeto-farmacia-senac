package entities;

import java.sql.*;
import java.util.ArrayList;

public class Medicamento {
    private final String nome;
    private final String descricao;
    private final double preco;

    private int quantidade_estoque;


    public Medicamento(String nome, String descricao, double preco, int quantidade_estoque) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.quantidade_estoque = quantidade_estoque;
    }


    public void adicionarEstoque(int quantidade) {
        if (quantidade >= 0) {
            this.quantidade_estoque += quantidade;
        } else {
            System.out.println("Operação não foi realizada devido a um erro");
        }
    }

    public void subtrairEstoque(int quantidade) {
        if (quantidade > 0 && quantidade <= this.quantidade_estoque) {
            this.quantidade_estoque -= quantidade;
        } else {
            System.out.println("Não é possível subtrair mais do que o estoque atual");
        }
    }


    public void salvarNoBanco() {
        String url = "jdbc:mysql://localhost:3306/projetoCoding";
        String usuario = "root";
        String senha = "123456";

        try (Connection connection = DriverManager.getConnection(url, usuario, senha)) {
            String query = "INSERT INTO medicamentos (nome, descricao, preco, quantidade_estoque) VALUES (?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, nome);
                preparedStatement.setString(2, descricao);
                preparedStatement.setDouble(3, preco);
                preparedStatement.setInt(4, quantidade_estoque);

                // Execute a inserção
                preparedStatement.executeUpdate();
            }

        } catch (SQLException e) {
            System.err.println("Erro ao inserir no banco: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void removerDoBanco() {
        String url = "jdbc:mysql://localhost:3306/projetoCoding";
        String usuario = "root";
        String senha = "123456";

        try (Connection connection = DriverManager.getConnection(url, usuario, senha)) {
            String query = "DELETE FROM medicamentos WHERE nome = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, nome);


                preparedStatement.executeUpdate();
            }

        } catch (SQLException e) {
            System.err.println("Erro ao remover do banco: " + e.getMessage());
            e.printStackTrace();
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
        return quantidade_estoque;
    }

    public void atualizarEstoqueNoBanco(Connection connection) {
        String sqlUpdate = "UPDATE medicamentos SET quantidade_estoque = ? WHERE nome = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdate)) {
            preparedStatement.setInt(1, this.quantidade_estoque);
            preparedStatement.setString(2, this.nome);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar estoque no banco de dados: " + e.getMessage());
            e.printStackTrace();
        }
    }

}

