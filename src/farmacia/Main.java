package farmacia;

import java.sql.*;
import java.util.*;
import java.text.DecimalFormat;

import entities.Medicamento;
import entities.Vendas;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Main {
    static ArrayList<Vendas> vendasDia = new ArrayList<>();
    private static final Connection connection = null;

    public static void main(String[] args) {



        ArrayList<Medicamento> medicamentos = new ArrayList<>(Medicamento.carregarDoBanco());

        Connection connection = null;
        String url = "jdbc:mysql://localhost:3306/projetoCoding";
        String user = "root";
        String password = "Ch@34462341";

        Scanner scanner = new Scanner(System.in);

        try {
            connection = DriverManager.getConnection(url, user, password);
            carregarVendasDoBanco(connection);
        } catch (SQLException e) {
            System.err.println("Erro ao obter a conexão com o banco de dados");
            e.printStackTrace();
            System.exit(1);
        }

        try {
            connection = DriverManager.getConnection(url, user, password);
            carregarVendasDoBanco(connection);  // Carregar vendas do banco de dados
        } catch (SQLException e) {
            System.err.println("Erro ao obter a conexão com o banco de dados");
            e.printStackTrace();
            System.exit(1);
        }


        DecimalFormat df = new DecimalFormat("#.00");
        System.out.println("|-------------------------|");
        System.out.println("|    FARMÁCIA SENACKERS   |");
        System.out.println("|-------------------------|");
        System.out.println();
        System.out.println("BEM-VINDO AO NOSSO SISTEMA!\nFAÇA LOGIN PARA CONTINUAR");
        System.out.println();


        String usuarioCorreto = "admin";
        int senhaCorreta = 123;
        int limiteTentativas = 5;
        int tentativas = 0;
        int vendasDoDia = 0;

        System.out.println();
        while (tentativas < limiteTentativas) {
            System.out.print("Digite o seu login: ");
            String usuario = scanner.nextLine();

            System.out.print("Digite a sua senha: ");
            String senhaSistema = scanner.nextLine();


            try {
                int senha = Integer.parseInt(senhaSistema);

                if (usuario.equals(usuarioCorreto) && senha == senhaCorreta) {
                    System.out.println("Usuário logado com sucesso.");
                    System.out.println();
                    break;
                } else {
                    System.out.println("Usuário ou senha inválidos. Tentativas restantes: " + (limiteTentativas - tentativas - 1));
                    tentativas++;
                }
            } catch (NumberFormatException e) {
                System.out.println("Senha inválida. A senha deve ser composta de números inteiros. Tentativas restantes: " + (limiteTentativas - tentativas - 1));
                tentativas++;

            }

        }

        if (tentativas >= limiteTentativas) {
            System.out.println("Número máximo de tentativas excedido. O sistema será bloqueado por questões de segurança.");
            scanner.close();
            System.exit(1);
        }

        int escolhaUsuario = 0;
        double valorTotalDia = 0;

        do {
            LocalDateTime horaLocal = java.time.LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            String horaFormatada = horaLocal.format(formatter);
            System.out.println("Hora atual: " + horaFormatada);
            System.out.println("Menu principal:\n\n1- Listar medicamentos\n\n2- Buscar medicamento\n\n3- Inserir medicamento\n\n4- Remover medicamento\n\n5- Alterar estoque\n\n6- Realizar venda\n\n7- Exibir quantas vendas foram feitas no dia\n\n8- Finalizar programa\n");

            try {
                System.out.print("Sua escolha: ");
                escolhaUsuario = Integer.parseInt(scanner.nextLine());
                switch (escolhaUsuario) {
                    case 1:
                        if (medicamentos.isEmpty()) {
                            System.out.println("A lista de medicamentos está vazia");
                            System.out.println();
                        } else {
                            System.out.println("Listagem de Medicamentos:");
                            for (Medicamento medicamento : medicamentos) {
                                if (medicamento != null) {
                                    System.out.println("|----------------------------------------------------------|");
                                    System.out.println("Nome: " + medicamento.getNome());
                                    System.out.println();
                                    System.out.println("Descrição: " + medicamento.getDescricao());
                                    System.out.println();
                                    System.out.println("Preço: " + medicamento.getPreco());
                                    System.out.println();
                                    System.out.println("Quantidade em estoque " + medicamento.getQuantidadeEstoque());
                                    System.out.println();
                                }
                            }
                        }
                        break;
                    case 2:
                        System.out.print("Digite o nome do medicamento a ser buscado: ");
                        String nomeBuscado = scanner.nextLine();
                        System.out.println();
                        boolean encontrado = false;
                        for (Medicamento medicamento : medicamentos) {
                            if (medicamento != null && medicamento.getNome().equalsIgnoreCase(nomeBuscado)) {
                                System.out.println("Nome: " + medicamento.getNome());
                                System.out.println("Descrição: " + medicamento.getDescricao());
                                System.out.println("Preço: " + medicamento.getPreco());
                                System.out.println("Quantidade em estoque: " + medicamento.getQuantidadeEstoque());
                                encontrado = true;
                                break;
                            }
                        }
                        if (!encontrado) {
                            System.out.println("Medicamento não encontrado.");
                        }
                        break;
                    case 3:
                        if (inserirMedicamento(medicamentos, scanner)) {
                            System.out.println("Medicamento inserido com sucesso.");
                            System.out.println();
                        }
                        break;

                    case 4:
                        System.out.print("Digite o nome do medicamento a ser removido: ");
                        String removerMedicamento = scanner.nextLine();
                        System.out.println("Tem certeza de que deseja remover este medicamento?\n 1-Sim\n 2-Não");
                        System.out.print("Sua escolha: ");
                        int escolhaRemocao = scanner.nextInt();
                        scanner.nextLine();

                        if (escolhaRemocao == 1) {
                            if (removerMedicamento(medicamentos, removerMedicamento)) {
                                System.out.println("Medicamento removido com sucesso.");
                            }
                        } else {
                            System.out.println("Medicamento não encontrado ou não foi possível remover.");
                        }
                        break;

                    case 5:
                        alterarEstoque(medicamentos, scanner);
                        break;

                    case 6:
                        int continuarComprando = 0;
                        double valorCompra = 0;


                        do {
                            String nomeVendido;
                            int quantidadeASerVendida = 0;
                            double valorProdutos = 0;
                            Medicamento medicamentoVendido = null;

                            System.out.print("Digite o nome do medicamento a ser vendido: ");
                            nomeVendido = scanner.nextLine();

                            for (Medicamento medicamento : medicamentos) {
                                if (medicamento != null && medicamento.getNome().equalsIgnoreCase(nomeVendido)) {
                                    medicamentoVendido = medicamento;
                                    break;
                                }
                            }

                            if (medicamentoVendido != null) {
                                System.out.println("Quantidade do medicamento " + nomeVendido + " em estoque: " + medicamentoVendido.getQuantidadeEstoque());
                                System.out.println("Preço do medicamento " + nomeVendido + ": " + medicamentoVendido.getPreco());
                                System.out.print("Digite a quantidade a ser vendida: ");
                                quantidadeASerVendida = Integer.parseInt(scanner.nextLine());

                                if (quantidadeASerVendida <= medicamentoVendido.getQuantidadeEstoque()) {
                                    valorProdutos += quantidadeASerVendida * medicamentoVendido.getPreco();
                                    valorCompra += valorProdutos;

                                    System.out.println("Venda realizada com sucesso, valor total: " + df.format(valorProdutos));


                                    conexao(medicamentoVendido, quantidadeASerVendida, connection);


                                    Vendas vendas = new Vendas(nomeVendido, quantidadeASerVendida, valorProdutos);
                                    vendasDia.add(vendas);
                                    inserirVenda(vendas, connection);
                                } else {
                                    System.out.println("Não há estoque suficiente para a venda.");
                                }
                            } else {
                                System.out.println("Medicamento não encontrado.");
                            }

                            do {
                                System.out.print("Deseja adicionar outro medicamento à compra atual? (1-Sim, 2-Não): ");
                                String input = scanner.nextLine();
                                try {
                                    continuarComprando = Integer.parseInt(input);
                                    if (continuarComprando != 1 && continuarComprando != 2) {
                                        System.out.println("Digite 1 para sim ou 2 para não.");
                                    }
                                } catch (NumberFormatException e) {
                                    System.out.println("Por favor, digite um número válido, 1 para sim ou 2 para não.");
                                }
                            } while (continuarComprando != 1 && continuarComprando != 2);

                            if (continuarComprando == 2) {
                                System.out.println("Seu valor total foi: " + df.format(valorCompra) +
                                        " Venda realizada com sucesso, muito obrigado pela preferência!!");
                                System.out.println();
                                vendasDoDia++;
                                medicamentoVendido.subtrairEstoque(quantidadeASerVendida);


                            }


                        } while (continuarComprando == 1);


                        valorTotalDia += valorCompra;

                        break;


                    case 7:
                        carregarVendasDoBanco(connection);
                        if (!vendasDia.isEmpty()) {
                            LocalDateTime dataAtual = LocalDateTime.now();
                            DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                            String dataFormatada = dataAtual.format(formatoData);
                            System.out.println("|--------------------------------------------------|");
                            System.out.println("Quantidade de vendas do dia (" + dataFormatada + "): " + vendasDia.size());
                            System.out.println("Vendas de hoje");
                            System.out.println();
                            valorTotalDia = 0;
                            for (Vendas venda : vendasDia) {
                                System.out.println("Nome: " + venda.getNomeMedicamento());
                                System.out.println("Quantidade vendida: " + venda.getQuantidade());
                                System.out.printf("Valor total: %.2f\n ", venda.getValorTotal());
                                System.out.println("|--------------------------------------------------|");

                                valorTotalDia += venda.getValorTotal();
                            }
                            System.out.printf("Valor total das vendas do dia: %.2f\n", valorTotalDia);
                        } else {
                            System.out.println("Ainda não foram realizadas vendas no dia.");
                        }


                        System.out.println();


                        break;
                    case 8:
                        break;
                    default:
                        System.out.println("Opção inválida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Opção deve ser um número válido.");
            }
        } while (escolhaUsuario != 8);
        try {
            System.out.println("Encerrando o programa...");
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            System.out.println("Houve um problema ao aguardar antes de encerrar");
            e.printStackTrace();
        }
    }

    public static void conexao(Medicamento medicamentoVendido, int quantidadeVendida, Connection connection) {
        String sqlUpdate = "UPDATE medicamentos SET quantidade_estoque = quantidade_estoque - ? WHERE nome = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdate)) {
            preparedStatement.setInt(1, quantidadeVendida);
            preparedStatement.setString(2, medicamentoVendido.getNome());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    private static boolean inserirMedicamento(List<Medicamento> medicamentos, Scanner scanner) {
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

        novoMedicamento.salvarNoBanco();

        return true;
    }



    private static boolean removerMedicamento(List<Medicamento> medicamentos, String nome) {
        Iterator<Medicamento> iterator = medicamentos.iterator();
        while (iterator.hasNext()) {
            Medicamento medicamento = iterator.next();
            if (medicamento != null && medicamento.getNome().equalsIgnoreCase(nome)) {
                iterator.remove();

                medicamento.removerDoBanco();
                return true;
            }
        }
        return false;
    }



    private static void alterarEstoque(List<Medicamento> medicamentos, Scanner scanner) {
        System.out.println("Alterar Estoque");
        System.out.print("Digite o nome do medicamento: ");
        String nomeMedicamento = scanner.nextLine();
        Medicamento medicamento = null;

        for (Medicamento value : medicamentos) {
            if (value != null && value.getNome().equalsIgnoreCase(nomeMedicamento)) {
                medicamento = value;
                break;
            }
        }

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
                String url = "jdbc:mysql://localhost:3306/projetoCoding";
                String usuario = "root";
                String senha = "Ch@34462341";
                try (Connection connection = DriverManager.getConnection(url, usuario, senha)) {
                    medicamento.atualizarEstoqueNoBanco(connection);
                } catch (SQLException e) {
                    System.err.println("Erro ao obter a conexão: " + e.getMessage());
                    e.printStackTrace();
                }
            } else if (alterarQuantidade == 2) {
                System.out.print("Digite a quantidade que deseja subtrair do estoque: ");
                int subtrairQuantidade = Integer.parseInt(scanner.nextLine());

                if (subtrairQuantidade <= medicamento.getQuantidadeEstoque()) {
                    medicamento.subtrairEstoque(subtrairQuantidade);


                    String url = "jdbc:mysql://localhost:3306/projetoCoding";
                    String usuario = "root";
                    String senha = "Ch@34462341";
                    try (Connection connection = DriverManager.getConnection(url, usuario, senha)) {
                        medicamento.atualizarEstoqueNoBanco(connection);
                    } catch (SQLException e) {
                        System.err.println("Erro ao obter a conexão: " + e.getMessage());
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Não foi possível realizar a operação. A quantidade desejada é maior do que o estoque disponível.");
                }
            } else {
                System.out.println("Opção inválida.");
            }

            System.out.println("Estoque de " + medicamento.getNome() + " atualizado para " + medicamento.getQuantidadeEstoque());
        } else {
            System.out.println("Medicamento não encontrado, tente novamente.");
        }

    }

    public static void inserirVenda(Vendas venda, Connection connection) {
        String sqlInsert = "INSERT INTO vendas (nome_medicamento, quantidade, valor_total) VALUES (?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert)) {
            preparedStatement.setString(1, venda.getNomeMedicamento());
            preparedStatement.setInt(2, venda.getQuantidade());
            preparedStatement.setDouble(3, venda.getValorTotal());
            preparedStatement.executeUpdate();

            vendasDia.add(venda);
        } catch (SQLException e) {
            System.err.println("Erro ao inserir venda no banco de dados: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void carregarVendasDoBanco(Connection connection) {

        vendasDia.clear();


        String sqlSelect = "SELECT * FROM vendas";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlSelect);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                String nomeMedicamento = resultSet.getString("nome_medicamento");
                int quantidade = resultSet.getInt("quantidade");
                double valorTotal = resultSet.getDouble("valor_total");

                Vendas venda = new Vendas(nomeMedicamento, quantidade, valorTotal);
                vendasDia.add(venda);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}

