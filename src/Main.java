import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        String correctUser = "admin";
        int correctPassword = 123;
        int option;
        int maxAttempts = 5; // Limite de tentativas
        int attempts = 0;

        System.out.println("Bem-vindo ao nosso sistema!\nFaça login para continuar.");

        Scanner scanner = new Scanner(System.in);

        while (attempts < maxAttempts) {
            System.out.print("Digite o seu login: ");
            String usuario = scanner.nextLine();

            System.out.print("Digite a sua senha: ");
            int senha = scanner.nextInt();
            scanner.nextLine(); // Limpa a quebra de linha pendente

            if (usuario.equals(correctUser) && senha == correctPassword) {
                System.out.println("Usuário logado com sucesso.");
                break; // Sai do loop se as credenciais estiverem corretas
            } else {
                System.out.println("Usuário ou senha inválidos. Tentativas restantes: " + (maxAttempts - attempts - 1));
                attempts++;
            }
        }

        if (attempts >= maxAttempts) {
            System.out.println("Número máximo de tentativas excedido. O sistema será bloqueado por questões de segurança.");
            System.exit(1);
        }

        System.out.println("Menu principal:\n1- Inserir\n2- Alterar\n3- Consultar \n4- Remover");
        option = scanner.nextInt();
        switch (option) {
            case 1:
                System.out.println("escolheu inserir");
                break;
            case 2:
                System.out.println("escolheu alterar");
                break;
            case 3:
                System.out.println("escolheu consultar");
                break;
            case 4:
                System.out.println("escolheu remover");
                break;

        }
    }
}