import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String correctUser = "admin";
        int correctPassword = 123;
        int option = 0;
        int maxAttempts = 5;
        int attempts = 0;
        System.out.println("Farmácia Senackers");
        System.out.println("Bem-vindo ao nosso sistema!\nFaça login para continuar.");

        Scanner scanner = new Scanner(System.in);

        while (attempts < maxAttempts) {
            System.out.print("Digite o seu login: ");
            String usuario = scanner.nextLine();

            System.out.print("Digite a sua senha: ");
            int senha = scanner.nextInt();
            scanner.nextLine();

            if (usuario.equals(correctUser) && senha == correctPassword) {
                System.out.println("Usuário logado com sucesso.");
                break;
            } else {
                System.out.println("Usuário ou senha inválidos. Tentativas restantes: " + (maxAttempts - attempts - 1));
                attempts++;
            }
        }

        if (attempts >= maxAttempts) {
            System.out.println("Número máximo de tentativas excedido. O sistema será bloqueado por questões de segurança.");
            System.exit(1);
        }


        do {
            System.out.println("Menu principal:\n1- Inserir\n2- Alterar\n3- Consultar\n4- Remover");
            try {
                option = Integer.parseInt(scanner.nextLine());
                switch (option) {
                    case 1:
                        System.out.println("Você escolheu inserir.");
                        break;
                    case 2:
                        System.out.println("Você escolheu alterar.");
                        break;
                    case 3:
                        System.out.println("Você escolheu consultar.");
                        break;
                    case 4:
                        System.out.println("Você escolheu remover.");
                        break;
                    default:
                        System.out.println("Opção inválida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Opção deve ser um número válido.");
            }
        } while (option < 1 || option > 4);


    }
}