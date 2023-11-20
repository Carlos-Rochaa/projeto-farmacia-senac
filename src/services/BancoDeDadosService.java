package services;

import entities.Vendas;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BancoDeDadosService {
    private static final String URL = "jdbc:mysql://db4free.net:3306/projetocoding";
    private static final String USER = "carloshrocha";
    private static final String PASSWORD = "Ch@34462341";

    private BancoDeDadosService() {

    }

    public static Connection conectar() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conectado ao banco de dados!");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Erro ao conectar ao banco de dados: " + e.getMessage());
        }
        return connection;
    }

    public static void fecharConexao(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException("Erro ao fechar conexão", e);
            }
        }
    }


    public static void inserirVenda(Vendas venda, Connection connection) {
        String sqlInsert = "INSERT INTO vendas (nome_medicamento, quantidade, valor_total) VALUES (?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert)) {
            preparedStatement.setString(1, venda.getNomeMedicamento());
            preparedStatement.setInt(2, venda.getQuantidade());
            preparedStatement.setDouble(3, venda.getValorTotal());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir venda ao banco de dados", e);
        }
    }

    public static List<Vendas> carregarVendasDoBanco(Connection connection) throws SQLException {
        List<Vendas> vendasCarregadas = new ArrayList<>();
        final LocalDateTime now = LocalDateTime.now().withNano(0);
        String sqlSelect = "SELECT * FROM vendas WHERE DATE(data_venda) = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlSelect)) {
            LocalDate dataAtual = LocalDate.now();
            java.sql.Date sqlDate = java.sql.Date.valueOf(dataAtual);
            preparedStatement.setDate(1, sqlDate);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String nomeMedicamento = resultSet.getString("nome_medicamento");
                    int quantidade = resultSet.getInt("quantidade");
                    double valorTotal = resultSet.getDouble("valor_total");

                    Vendas venda = new Vendas(nomeMedicamento, quantidade, valorTotal);
                    vendasCarregadas.add(venda);
                }
            } catch (SQLException e) {
                throw new RuntimeException("Erro ao carregar vendas do banco de dados.", e);
            }
        }

        return vendasCarregadas;
    }

}







