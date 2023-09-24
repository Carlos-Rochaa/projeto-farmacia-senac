package farmacia;

import java.util.Scanner;

import entities.Medicamento;

public class Main {


    public static void main(String[] args) {
        Medicamento[] medicamentos = new Medicamento[10];
        medicamentos[0] = new Medicamento("Aspirina", "Analgésico e anti-inflamatório", 5.99, 30);
        medicamentos[1] = new Medicamento("Paracetamol", "Analgésico", 4.49, 25);
        medicamentos[2] = new Medicamento("Ibuprofeno", "Anti-inflamatóro", 6.99, 20);
        medicamentos[3] = new Medicamento("Omeprazol", "Inibidor de ácido gástrico", 7.49, 12);
        medicamentos[4] = new Medicamento("Dipirona", "Analgésico", 4.99, 15);
        medicamentos[5] = new Medicamento("Dorflex", "Relaxante muscular", 8.99, 22);
        medicamentos[6] = new Medicamento("Amoxicilina", "Antibiótico", 9.99, 27);
        medicamentos[7] = new Medicamento("Histamin", "Anti alergico", 3.99, 30);
        medicamentos[8] = new Medicamento("Expec", "Xarope para tosse", 15.99, 7);


        System.out.println("Farmácia Senackers");
        System.out.println("Bem-vindo ao nosso sistema!\nFaça login para continuar.");

        Scanner scanner = new Scanner(System.in);

        String usuarioCorreto = "admin";
        int senhaCorreta = 123;
        int limiteTentativas = 5;
        int tentativas = 0;

        while (tentativas < limiteTentativas) {
            System.out.print("Digite o seu login: ");
            String usuario = scanner.nextLine();

            System.out.print("Digite a sua senha: ");
            String senhaStr = scanner.nextLine();


            try {
                int senha = Integer.parseInt(senhaStr);

                if (usuario.equals(usuarioCorreto) && senha == senhaCorreta) {
                    System.out.println("Usuário logado com sucesso.");
                    break;
                } else {
                    System.out.println("Usuário ou senha inválidos. Tentativas restantes: " + (limiteTentativas - tentativas - 1));
                    tentativas++;
                }
            } catch (NumberFormatException e) {
                System.out.println("Senha inválida. A senha deve ser um número inteiro. Tentativas restantes: " + (limiteTentativas - tentativas - 1));
                tentativas++;

            }

        }

        if (tentativas >= limiteTentativas) {
            System.out.println("Número máximo de tentativas excedido. O sistema será bloqueado por questões de segurança.");
            scanner.close();
            System.exit(1);
        }

        int option = 0;
        do {
            System.out.println("Menu principal:\n1- Listar medicamentos\n2- Buscar medicamento\n3- Inserir medicamento\n4- Remover medicamento\n5- Alterar estoque\n6- Finalizar programa.");

            try {
                System.out.print("Sua escolha: "); option = Integer.parseInt(scanner.nextLine());
                switch (option) {
                    case 1:
                        System.out.println("Listagem de Medicamentos:");
                        for (Medicamento medicamento : medicamentos) {
                            if (medicamento != null) {
                                System.out.println("Nome: " + medicamento.getNome());
                                System.out.println("Descrição: " + medicamento.getDescricao());
                                System.out.println("Preço: " + medicamento.getPreco());
                                System.out.println("Quantidade em estoque " + medicamento.getQtdEstoque());
                                System.out.println();
                            }
                        }
                        break;
                    case 2:
                        System.out.print("Digite o nome do medicamento a ser buscado: ");
                        String nomeBuscado = scanner.nextLine();
                        boolean encontrado = false;
                        for (Medicamento medicamento : medicamentos) {
                            if (medicamento != null && medicamento.getNome().equalsIgnoreCase(nomeBuscado)) {
                                System.out.println("Nome: " + medicamento.getNome());
                                System.out.println("Descrição: " + medicamento.getDescricao());
                                System.out.println("Preço: " + medicamento.getPreco());
                                System.out.println("Quantidade em estoque: " + medicamento.getQtdEstoque());
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
                        } else {
                            System.out.println("Não foi possível inserir o medicamento. O limite do array foi atingido.");
                        }
                        break;

                    case 4:
                        System.out.print("Digite o nome do medicamento a ser removido: ");
                        String nomeRemover = scanner.nextLine();
                        if (removerMedicamento(medicamentos, nomeRemover)) {
                            System.out.println("Medicamento removido com sucesso.");
                        } else {
                            System.out.println("Medicamento não encontrado ou não foi possível remover.");
                        }
                        break;

                    case 5:
                        alterarEstoque(medicamentos, scanner);
                        break;


                    case 6:
                        System.out.println("Saindo do programa.");
                        break;
                    default:
                        System.out.println("Opção inválida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Opção deve ser um número válido.");
            }
        } while (option != 6);

        scanner.close();
    }

    private static boolean inserirMedicamento(Medicamento[] medicamentos, Scanner scanner) {
        for (int i = 0; i < medicamentos.length; i++) {
            if (medicamentos[i] == null) {
                System.out.print("Digite o nome do medicamento: ");
                String nome = scanner.nextLine();
                System.out.print("Digite a descrição do medicamento: ");
                String descricao = scanner.nextLine();
                System.out.print("Digite o preço do medicamento: ");
                double preco = Double.parseDouble(scanner.nextLine());
                System.out.print("Digite a quantidade inicial no estoque do medicamento: ");
                int quantidadeInicial = Integer.parseInt(scanner.nextLine());

                medicamentos[i] = new Medicamento(nome, descricao, preco, quantidadeInicial);
                return true;
            }
        }
        return false;
    }


    private static boolean removerMedicamento(Medicamento[] medicamentos, String nome) {
        for (int i = 0; i < medicamentos.length; i++) {
            if (medicamentos[i] != null && medicamentos[i].getNome().equalsIgnoreCase(nome)) {
                medicamentos[i] = null;
                return true;
            }
        }
        return false;
    }

    private static void alterarEstoque(Medicamento[] medicamentos, Scanner scanner) {
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
            System.out.println("Você deseja adicionar ou subtrair a quantidade atual? \n 1- Adicionar\n 2- Subtrair");
            int alterarQuantidade = Integer.parseInt(scanner.nextLine());

            if (alterarQuantidade == 1) {
                System.out.print("Digite a quantidade que deseja adicionar ao estoque: ");
                int quantidadeDesejada = Integer.parseInt(scanner.nextLine());
                medicamento.alterarEstoque(quantidadeDesejada);
            } else if (alterarQuantidade == 2) {
                System.out.print("Digite a quantidade que deseja subtrair do estoque: ");
                int quantidadeDesejada = Integer.parseInt(scanner.nextLine());
                int novoEstoque = medicamento.getQtdEstoque() - quantidadeDesejada;

                if (novoEstoque < 0) {
                    System.out.println("Não é possível subtrair mais do que o estoque atual.");
                } else {
                    medicamento.alterarEstoque(novoEstoque);
                }
            } else {
                System.out.println("Opção inválida.");
            }

            System.out.println("Estoque de " + medicamento.getNome() + " atualizado para " + medicamento.getQtdEstoque());
        } else {
            System.out.println("Medicamento não encontrado.");
        }
    }


}