package entities;
import java.sql.*;
import java.util.ArrayList;

public class Medicamento {
    private final String nome;
    private final String descricao;
    private final double preco;

    private  int quantidade_estoque;


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


    public static ArrayList<Medicamento> carregarDoBanco() {
        ArrayList<Medicamento> medicamentos = new ArrayList<>();

        String url = "jdbc:mysql://localhost:3306/projetoCoding";
        String usuario = "root";
        String senha = "Ch@34462341";

        try (Connection connection = DriverManager.getConnection(url, usuario, senha)) {
            String query = "SELECT * FROM medicamentos";
            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    String nome = resultSet.getString("nome");
                    String descricao = resultSet.getString("descricao");
                    double preco = resultSet.getDouble("preco");
                    int quantidade_estoque = resultSet.getInt("quantidade_estoque");
                    medicamentos.add(new Medicamento(nome, descricao, preco, quantidade_estoque));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return medicamentos;
    }

    public void salvarNoBanco() {
        String url = "jdbc:mysql://localhost:3306/projetoCoding";
        String usuario = "root";
        String senha = "Ch@34462341";

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
        String senha = "Ch@34462341";

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

    public void atualizarEstoqueNoBanco(Connection connection) {
        try {
            connection.setAutoCommit(false);

            String query = "UPDATE medicamentos SET quantidade_estoque = ? WHERE nome = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, this.quantidade_estoque);
                preparedStatement.setString(2, this.nome);
                preparedStatement.executeUpdate();

                connection.commit();
            }
        } catch (SQLException e) {
            // Em caso de erro, faça o rollback para desfazer as alterações
            System.err.println("Erro ao atualizar estoque no banco: " + e.getMessage());
            e.printStackTrace();
            // Faça o rollback em caso de erro
            try {
                connection.rollback();
            } catch (SQLException rollbackException) {
                System.err.println("Erro durante o rollback: " + rollbackException.getMessage());
                rollbackException.printStackTrace();
            }
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

}

