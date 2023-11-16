package farmacia;

import entities.Medicamento;
import entities.Vendas;
import services.AutenticacaoService;
import services.MedicamentoService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static services.BancoDeDadosService.carregarVendasDoBanco;
import static services.BancoDeDadosService.inserirVenda;


public class Main {
    static ArrayList<Vendas> vendasDia = new ArrayList<>();

    private static LocalDate ultimaDataReset = LocalDate.now();

    public Main() throws SQLException {
    }

    public static void main(String[] args) {


        ArrayList<Medicamento> medicamentos = new ArrayList<>(MedicamentoService.carregarDoBanco());
        AutenticacaoService autenticacaoService = new AutenticacaoService();
        Connection connection = null;
        String url = "jdbc:mysql://localhost:3306/projetoCoding";
        String user = "root";
        String password = "123456";

        Scanner scanner = new Scanner(System.in);

        try {
            connection = DriverManager.getConnection(url, user, password);
            carregarVendasDoBanco(connection);
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

        int vendasDoDia = 0;

        System.out.println();
        if (!autenticacaoService.realizarLogin(scanner)) {
            scanner.close();
            System.exit(1);
        }

        MedicamentoService medicamentoService = new MedicamentoService();
        List<Medicamento> medicamentos1 = MedicamentoService.carregarMedicamentos();

        int escolhaUsuario = 0;
        double valorTotalDia = 0;

        do {
            LocalDateTime horaLocal = java.time.LocalDateTime.now();
            LocalDate dataHoje = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            String horaFormatada = horaLocal.format(formatter);

            if (!dataHoje.equals(ultimaDataReset)) {
                valorTotalDia = 0;
                ultimaDataReset = dataHoje;
            }
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
                        if (MedicamentoService.inserirMedicamento(medicamentos, scanner)) {
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
                            if (MedicamentoService.removerMedicamento(medicamentos, removerMedicamento)) {
                                System.out.println("Medicamento removido com sucesso.");
                            }
                        } else {
                            System.out.println("Medicamento não encontrado ou não foi possível remover.");
                        }
                        break;

                    case 5:
                        MedicamentoService.alterarEstoque(medicamentos, scanner, connection);
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


                                    Vendas vendas = new Vendas(nomeVendido, quantidadeASerVendida, valorProdutos);
                                    vendasDia.add(vendas);
                                    inserirVenda(vendas, connection);
                                    if (medicamentoVendido.getQuantidadeEstoque() <= 0) {
                                        medicamentos.remove(medicamentoVendido);
                                    }
                                } else {
                                    System.out.println("Não há estoque suficiente para a venda.");
                                }
                            } else {
                                System.out.println("Medicamento não encontrado.");
                            }

                            do {
                                System.out.println("Deseja adicionar outro medicamento à compra atual? (1-Sim, 2-Não): ");
                                System.out.print("Sua escolha: ");
                                String input = scanner.nextLine();
                                System.out.println();
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

                        for (Medicamento medicamento : medicamentos) {
                            if (medicamento != null) {
                                medicamento.atualizarEstoqueNoBanco(connection);
                            }
                        }
                        valorTotalDia += valorCompra;

                        break;


                    case 7:
                        vendasDia.clear();
                        vendasDia.addAll(carregarVendasDoBanco(connection));

                        if (!vendasDia.isEmpty()) {
                            LocalDate dataAtual = LocalDate.now();
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
}

