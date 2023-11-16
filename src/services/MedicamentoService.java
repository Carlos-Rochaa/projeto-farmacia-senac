package services;

import entities.Medicamento;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class MedicamentoService {

    private static final String URL = "jdbc:mysql://localhost:3306/projetoCoding";
    private static final String USUARIO = "root";
    private static final String SENHA = "123456";

    public static List<Medicamento> carregarDoBanco() {
        List<Medicamento> medicamentos = new ArrayList<>();
        String sqlSelect = "SELECT * FROM medicamentos";

        try (Connection connection = conectar();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlSelect);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String nome = resultSet.getString("nome");
                String descricao = resultSet.getString("descricao");
                double preco = resultSet.getDouble("preco");
                int quantidadeEstoque = resultSet.getInt("quantidade_estoque");

                Medicamento medicamento = new Medicamento(nome, descricao, preco, quantidadeEstoque);
                medicamentos.add(medicamento);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao carregar medicamentos do banco de dados: " + e.getMessage());
            e.printStackTrace();
        }

        return medicamentos;
    }

    public static boolean inserirMedicamento(List<Medicamento> medicamentos, Scanner scanner) throws SQLException {
        System.out.print("Digite o nome do medicamento: ");
        String nome = scanner.nextLine();

        for (Medicamento medicamento : medicamentos) {
            if (medicamento != null && medicamento.getNome().equalsIgnoreCase(nome)) {
                System.out.println("Já existe um medicamento com o nome: " + nome);
                return false;
            }
        }

        System.out.print("Digite a descrição do medicamento: ");
        String descricao = scanner.nextLine();
        System.out.print("Digite o preço do medicamento: ");
        double preco = Double.parseDouble(scanner.nextLine());
        System.out.print("Digite a quantidade inicial no estoque do medicamento: ");
        int quantidadeInicial = Integer.parseInt(scanner.nextLine());

        Medicamento novoMedicamento = new Medicamento(nome, descricao, preco, quantidadeInicial);
        medicamentos.add(novoMedicamento);

        novoMedicamento.salvarNoBanco(conectar());

        return true;
    }

    public static boolean removerMedicamento(List<Medicamento> medicamentos, String nome) throws SQLException {
        Iterator<Medicamento> iterator = medicamentos.iterator();
        while (iterator.hasNext()) {
            Medicamento medicamento = iterator.next();
            if (medicamento != null && medicamento.getNome().equalsIgnoreCase(nome)) {
                iterator.remove();
                medicamento.removerDoBanco(conectar());
                return true;
            }
        }
        return false;
    }

    public static void alterarEstoque(ArrayList<Medicamento> medicamentos, Scanner scanner, Connection connection) throws SQLException {
        System.out.println("Alterar Estoque");
        System.out.print("Digite o nome do medicamento: ");
        String nomeMedicamento = scanner.nextLine();
        Medicamento medicamento = buscarMedicamentoPorNome(medicamentos, nomeMedicamento);

        if (medicamento != null) {
            System.out.println("Quantidade atual do medicamento em estoque: " + medicamento.getQuantidadeEstoque());
            System.out.println("Você deseja adicionar ou subtrair a quantidade atual? \n 1- Adicionar\n 2- Subtrair");
            System.out.print("Sua escolha: ");
            int alterarQuantidade = Integer.parseInt(scanner.nextLine());

            System.out.println();

            if (alterarQuantidade == 1) {
                System.out.print("Digite a quantidade que deseja adicionar ao estoque: ");
                int adicionarQuantidade = Integer.parseInt(scanner.nextLine());
                medicamento.adicionarEstoque(adicionarQuantidade);
            } else if (alterarQuantidade == 2) {
                System.out.print("Digite a quantidade que deseja subtrair do estoque: ");
                int subtrairQuantidade = Integer.parseInt(scanner.nextLine());

                if (subtrairQuantidade <= medicamento.getQuantidadeEstoque()) {
                    medicamento.subtrairEstoque(subtrairQuantidade);
                } else {
                    System.out.println("Não foi possível realizar a operação. A quantidade desejada é maior do que o estoque disponível.");
                }
            } else {
                System.out.println("Opção inválida.");
            }

            medicamento.atualizarEstoqueNoBanco(conectar());
            System.out.println("Estoque de " + medicamento.getNome() + " atualizado para " + medicamento.getQuantidadeEstoque());
        } else {
            System.out.println("Medicamento não encontrado, tente novamente.");
        }
    }

    private static Medicamento buscarMedicamentoPorNome(List<Medicamento> medicamentos, String nome) {
        for (Medicamento medicamento : medicamentos) {
            if (medicamento != null && medicamento.getNome().equalsIgnoreCase(nome)) {
                return medicamento;
            }
        }
        return null;
    }

    private static Connection conectar() {
        try {
            return DriverManager.getConnection(URL, USUARIO, SENHA);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar ao banco de dados.", e);
        }
    }
}
