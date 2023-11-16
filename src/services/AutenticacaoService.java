package services;

import java.util.Scanner;

public class AutenticacaoService {
    private static final String USUARIO_CORRETO = "admin";
    private static final int SENHA_CORRETA = 123;
    private static final int LIMITE_TENTATIVAS = 5;

    private int tentativas;

    public AutenticacaoService() {
        this.tentativas = 0;
    }

    public boolean realizarLogin(Scanner scanner) {
        while (tentativas < LIMITE_TENTATIVAS) {
            System.out.print("Digite o seu login: ");
            String usuario = scanner.nextLine();

            System.out.print("Digite a sua senha: ");
            String senhaSistema = scanner.nextLine();

            try {
                int senha = Integer.parseInt(senhaSistema);

                if (usuario.equals(USUARIO_CORRETO) && senha == SENHA_CORRETA) {
                    System.out.println("Usuário logado com sucesso.");
                    System.out.println();
                    return true;
                } else {
                    System.out.println("Usuário ou senha inválidos. Tentativas restantes: " + (LIMITE_TENTATIVAS - tentativas - 1));
                    tentativas++;
                }
            } catch (NumberFormatException e) {
                System.out.println("Senha inválida. A senha deve ser composta de números inteiros. Tentativas restantes: " + (LIMITE_TENTATIVAS - tentativas - 1));
                tentativas++;
            }
        }

        if (tentativas >= LIMITE_TENTATIVAS) {
            System.out.println("Número máximo de tentativas excedido. O sistema será bloqueado por questões de segurança.");
            return false;
        }

        return false;
    }
}
