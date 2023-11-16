package services;
import entities.Vendas;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BancoDeDadosService {
    private static final String URL = "jdbc:mysql://localhost:3306/projetoCoding";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void fecharConexao(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
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
            System.err.println("Erro ao inserir venda no banco de dados: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static List<Vendas> carregarVendasDoBanco(Connection connection) {
        List<Vendas> vendasCarregadas = new ArrayList<>();

        String sqlSelect = "SELECT * FROM vendas";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlSelect);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                String nomeMedicamento = resultSet.getString("nome_medicamento");
                int quantidade = resultSet.getInt("quantidade");
                double valorTotal = resultSet.getDouble("valor_total");

                Vendas venda = new Vendas(nomeMedicamento, quantidade, valorTotal);
                vendasCarregadas.add(venda);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao carregar vendas do banco de dados: " + e.getMessage());
            e.printStackTrace();
        }

        return vendasCarregadas;
    }

}

