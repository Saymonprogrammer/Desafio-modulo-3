import java.util.Random;
import java.util.Scanner;

public class Main {
    static Scanner teclado = new Scanner(System.in);
    static Random rand = new Random();

    static int[] limites = {50, 100, 200};
    static int[] tentativasMax = {10, 7, 5};
    static int[] pontuacoesBase = {100,200,300};

    static int[] historicoPontuacoes = new int[10];
    static String[] historicoNiveis = new String[10];
    static int quantidadeHistorico = 0;

    static int[] recordes = {0,0,0};

    public static void main(String[] args) {
        int opcao;

        do {
            System.out.println("\n==============================");
            System.out.println("     JOGO DE ADIVINHAÇÃO");
            System.out.println("==============================");
            System.out.println("1 - Iniciar Novo Jogo");
            System.out.println("2 - Ver Regras");
            System.out.println("3 - Ver Histórico");
            System.out.println("4 - Ver Recordes");
            System.out.println("5 - Modo Sequência");
            System.out.println("6 - Sair");
            System.out.print("Escolha uma opção: ");

            opcao = teclado.nextInt();

            if (opcao == 1) {
                iniciarJogo();
            } else if (opcao == 2) {
                mostrarRegras();
            } else if (opcao == 3) {
                mostrarHistorico();
            } else if (opcao == 4) {
                mostrarRecordes();
            } else if (opcao == 5) {
                modoSequencia();
            } else if (opcao == 6) {
                System.out.println("Saindo do jogo...");
            }
        } while (opcao != 6);

        teclado.close();
    }

    public static void iniciarJogo(){
        System.out.println("\n===== ESCOLHA A DIFICULDADE =====");
        System.out.println("1 - Fácil");
        System.out.println("2 - Médio");
        System.out.println("3 - Difícil");
        System.out.print("Escolha: ");

        int nivel = teclado.nextInt();

        if (nivel < 1 || nivel > 3) {
            System.out.println("Nível inválido!");
            return;
        }

        int indice = nivel - 1;

        int limite = limites[indice];
        int tentativasMaximas = tentativasMax[indice];
        int pontuacao = pontuacoesBase[indice];

        String nomeNivel = obterNomeNível(nivel);

        int numeroSorteado = rand.nextInt(limite) +1;

        int tentativas = 0;
        boolean acerto = false;
        int ultimoPalpite = 0;

        System.out.println("\n===== NOVO JOGO =====");
        System.out.println("Nível: " + nomeNivel);
        System.out.println("Número entre 1 e " + limite);
        System.out.println("Tentativas: " + tentativasMaximas);
        
        while (tentativas < tentativasMaximas && !acerto){
            System.out.println("\nPontuação atual: " + pontuacao);
            System.out.println("1 - Fazer tentativa");
            System.out.println("2 - Usar dica");
            System.out.print("Escolha: ");

            int opcao = teclado.nextInt();
            if (opcao == 1) {
                tentativas++;

                System.out.println("Digite seu palpite:");
                int palpite = teclado.nextInt();
                ultimoPalpite = palpite;

                pontuacao -= 10;

                if (pontuacao < 0) {
                    pontuacao = 0;
                }

                if (palpite == numeroSorteado){
                    acerto = true;

                    int tentativasNaoUsadas = tentativasMaximas - tentativas;

                    pontuacao += tentativasNaoUsadas * 50;

                    if (pontuacao < 0){
                        pontuacao = 0;
                    }

                    System.out.println("\nPARABÉNS! Você acertou!");
                    System.out.println("Número correto: " + numeroSorteado);
                    System.out.println("Pontuação final: " + pontuacao);

                    adicionarHistorico(pontuacao, nomeNivel);

                    atualizarRecorde(indice, pontuacao);
                } else {
                    if (palpite < numeroSorteado){
                        System.out.println("MAIOR");
                    } else {
                        System.out.println("MENOR");
                    }
                    int diferenca = Math.abs(numeroSorteado - palpite);

                    if (diferenca <= 5){
                        System.out.println("Você está muito perto!");
                    } else if (diferenca <= 15){
                        System.out.println("Você está perto!");
                    } else {
                        System.out.println("Você está longe!");
                    }
                }
            } else if (opcao == 2) {
                pontuacao = usarDica(numeroSorteado, limite, pontuacao, ultimoPalpite);
            } else {
                System.out.println("Opção inválida!");
            }
        }

        if (!acerto){
            System.out.println("Você perdeu!");
            System.out.println("O número correto era: " + numeroSorteado);

            adicionarHistorico(0, nomeNivel);
        }
    }

    public static int usarDica(int numeroSorteado, int limite, int pontuacao, int ultimoPalpite){
        System.out.println("\n===== DICAS =====");
        System.out.println("1 - Paridade (par ou ímpar) -10 pontos");
        System.out.println("2 - Intervalo (metade superior/inferior) -20 pontos");
        System.out.println("3 - Proximidade (quente/frio) -15 pontos");
        System.out.print("Escolha uma dica: ");

        int dica = teclado.nextInt();

        if (dica == 1 && pontuacao >= 10){
            pontuacao -= 10;

            if (numeroSorteado % 2 == 0){
                System.out.println("O número é PAR.");
            } else {
                System.out.println("O número é IMPAR.");
            }
        } else if (dica == 2 && pontuacao >= 20){
            pontuacao -= 20;

            int metade = limite/ 2;

            if (numeroSorteado <= metade){
                System.out.println("O número está na metade INFERIOR.");
            } else {
                System.out.println("O número está na metade SUPERIOR.");
            }
        }  else if (dica == 3 && pontuacao >= 15){
            if (ultimoPalpite == 0){
                System.out.println("Você precisa fazer uma tentativa primeiro!");
                return pontuacao;
            }
            pontuacao -= 15;

            int diferenca = Math.abs(numeroSorteado - ultimoPalpite);

            if (diferenca <= 5){
                System.out.println("MUITO QUENTE!");
            } else if (diferenca <= 15) {
                System.out.println("QUENTE!");
            }  else {
                System.out.println("FRIO!");
            }
        } else {
            System.out.println("Dica inválida ou não há pontos o suficiente.");
        }

        if (pontuacao < 0){
            pontuacao = 0;
        }
        return pontuacao;
    }

    public static void adicionarHistorico(int pontuacao, String nivel){
        if (quantidadeHistorico < 10){
            historicoPontuacoes[quantidadeHistorico] = pontuacao;
            historicoNiveis[quantidadeHistorico] = nivel;

            quantidadeHistorico++;
        } else {
            for (int i=0; i<9; i++){
                historicoPontuacoes[i] = historicoPontuacoes[i +1];

                historicoNiveis[i] = historicoNiveis[i +1];
            }

            historicoPontuacoes[9] = pontuacao;
            historicoNiveis[9] = nivel;
        }
    }

    public static void atualizarRecorde(int indice, int pontuacao){
        if (pontuacao > recordes[indice]){
            recordes[indice] = pontuacao;

            System.out.println("NOVO RECORDE!!!");
        }
    }

    public static void mostrarRecordes(){
        System.out.println("\n===== RECORDES =====");

        System.out.println("Fácil: " +recordes[0]);
        System.out.println("Médio: " +recordes[1]);
        System.out.println("Difícil: " +recordes[2]);
    }

    public static String obterNomeNível(int nivel){
        if (nivel == 1)
            return "Fácil";
        else if (nivel == 2)
            return "Médio";
        else
            return "Difícil";
    }

    public static void mostrarRegras(){
        System.out.println("\n===== REGRAS =====");

        System.out.println("\nFÁCIL");
        System.out.println("Número: 1 a 50");
        System.out.println("Tentativas: 10");
        System.out.println("Pontuação base: 100");

        System.out.println("\nMÉDIO");
        System.out.println("Número: 1 a 100");
        System.out.println("Tentativas: 7");
        System.out.println("Pontuação base: 200");

        System.out.println("\nDIFÍCIL");
        System.out.println("Número: 1 a 200");
        System.out.println("Tentativas: 5");
        System.out.println("Pontuação base: 300");

        System.out.println("\nA cada tentativa usada: -10 pontos.");
        System.out.println(
                "Cada tentativa não utilizada: +50 pontos."
        );

        System.out.println("\n===== DICAS =====");
        System.out.println("Paridade: -10 pontos");
        System.out.println("Intervalo: -20 pontos");
        System.out.println("Proximidade: -15 pontos");
    }

    public static void mostrarHistorico(){
        System.out.println("\n===== HISTÓRICO =====");

        if (quantidadeHistorico == 0){
            System.out.println("Nenhuma pontuação resgistrada.");
            return;
        }

        for (int i=0; i<quantidadeHistorico; i++){
            System.out.println(i+1
                    + " - "
                    + historicoNiveis[i]
                    + "| Pontuação: "
                    + historicoPontuacoes[i]);
        }
    }

    public static void modoSequencia(){
        System.out.println("\n===== MODO SEQUÊNCIA =====");
        System.out.println("Você deverá acertar 3 números.");

        int[] sequencia = new int[3];

        for (int i=0; i<3; i++){
            sequencia[i] = rand.nextInt(10)+1;
        }

        int pontos = 300;
        int acertos = 0;
        for (int i=0; i<3; i++){
            System.out.println("Adivinhe o número "+(i+1)+": ");

            int palpite = teclado.nextInt();

            if (palpite == sequencia[i]){
                System.out.println("Acertou!");
                acertos++;
            } else {
                System.out.println("Errou!");
                System.out.println("O número era: " + sequencia[i]);
            }

            pontos -= 50;
        }

        pontos += acertos * 100;

        if (pontos < 0){
            pontos = 0;
        }

        System.out.println("\n===== RESULTADO =====");
        System.out.println(
                "Sequência correta: ["
                        + sequencia[0] + ", "
                        + sequencia[1] + ", "
                        + sequencia[2] + "]"
        );

        System.out.println("Acertos: " + acertos);
        System.out.println("Pontuação: " + pontos);

        adicionarHistorico(pontos, "Sequência");
    }
}