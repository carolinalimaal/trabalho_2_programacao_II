package jogo;

import enums.Dificuldade;


public class Estatistica {

    public static void main(String[] args) {
        System.out.println("Iniciando simulações...\n");

        for (Dificuldade dificuldade : Dificuldade.values()) {
            int vitoriasHerois = 0;
            int vitoriasMonstros = 0;
            int totalHeroisRestantes = 0;
            int totalMonstrosRestantes = 0;

            for (int i = 0; i < 100; i++) {
                Log log = new Log(false);
                Jogo jogo = new Jogo(dificuldade, log);
                jogo.iniciarJogo();
                jogo.gerenciarTurnos();
                
                int heroisRestantes = jogo.getHerois().size();
             
                int monstrosRestantes = jogo.getMonstros().size();

                if (heroisRestantes == 0) {
                    vitoriasMonstros++;
                } else {
                    vitoriasHerois++;
                }

                totalHeroisRestantes += heroisRestantes;
                totalMonstrosRestantes += monstrosRestantes;
            }

            // Cálculo das estatísticas
            double porcentagemHerois = (vitoriasHerois * 100.0) / 100;
            double porcentagemMonstros = (vitoriasMonstros * 100.0) / 100;
            double mediaHeroisRestantes = totalHeroisRestantes / 100.0;
            double mediaMonstrosRestantes = totalMonstrosRestantes / 100.0;

            // Exibição dos resultados
            System.out.println("Dificuldade: " + dificuldade);
            System.out.println("Vitórias dos Heróis: " + vitoriasHerois + " (" + String.format("%.2f", porcentagemHerois) + "%)");
            System.out.println("Vitórias dos Monstros: " + vitoriasMonstros + " (" + String.format("%.2f", porcentagemMonstros) + "%)");
            System.out.println("Média de Heróis Restantes: " + String.format("%.2f", mediaHeroisRestantes));
            System.out.println("Média de Monstros Restantes: " + String.format("%.2f", mediaMonstrosRestantes));
            System.out.println();
        }

        System.out.println("Simulações concluídas!");
    }
}
