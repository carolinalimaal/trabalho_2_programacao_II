package entidades;

import java.util.Random;
import enums.ResultadoAtaque;
import enums.StatusJogador;
import jogo.Log;

public class Dragao extends Monstro {
    private boolean voo;
    private int turnosVoo;
    private Log log;

    public Dragao(String nome, int hp, int ataque, int defesa, int destreza, int velocidade, Log log) {
        super(nome, hp, ataque, defesa, destreza, velocidade, log);
        this.voo = false;
        this.turnosVoo = 0;
        this.log = log;
    }

    public boolean getVoo() {
        return voo;
    }

    public void setVoo(boolean voo) {
        if (voo) {
            super.setStatus(StatusJogador.VOANDO);
            this.turnosVoo = 2; // O Dragão fica voando por 2 turnos
            log.adicionarLog(this.getNome() + " começa a voar e ficará voando por 2 turnos!");
        } else {
            super.setStatus(StatusJogador.NORMAL);
            this.turnosVoo = 0;
            log.adicionarLog(this.getNome() + " pousou e voltou ao estado normal.");
        }
        this.voo = voo;
    }

    @Override
    public ResultadoAtaque realizarAtaque(Jogador alvo) {
        Random random = new Random();

        // Se o Dragão estiver no segundo turno de voo, ele não pode atacar e desce
        if (this.voo && this.turnosVoo == 2) {
            log.adicionarLog(this.getNome() + " não pode atacar enquanto está voando no segundo turno!");
            this.setVoo(false); // O Dragão volta ao normal após o segundo turno
            log.logEstadoJogador(this);
            return ResultadoAtaque.ERROU;
        }

        // 30% de chance do Dragão começar a voar se não estiver voando
        if (!this.voo && random.nextInt(100) < 30) { 
            this.setVoo(true);
        }

        // Cálculo da chance de acerto
        int chanceAcerto = random.nextInt(100) + 1;
        if (chanceAcerto > this.getDestreza()) {
            log.logAtaque(this.getNome(), alvo.getNome(), "ERROU", 0);
            log.logEstadoJogador(this);
            return ResultadoAtaque.ERROU;
        }

        // Cálculo do dano base
        int dano = Math.max(this.getAtaque() - alvo.getDefesa(), 0); // Garante que o dano não seja negativo
        boolean ataqueCritico = random.nextInt(100) < 25; // 25% de chance de ataque crítico

        if (ataqueCritico) {
            dano *= 2;
            alvo.setHp(0);
            log.logAtaque(this.getNome(), alvo.getNome(), "ATAQUE CRÍTICO!", dano);
            log.logMorte(alvo.getNome());
        } else {
            // 50% de chance de aplicar queimadura no alvo
            if (random.nextBoolean()) {
                alvo.setStatus(StatusJogador.QUEIMANDO);
                log.logDanoStatus(alvo.getNome(), "queimadura", 0);
            }

            // Aplica dano normal ao alvo
            alvo.setHp(alvo.getHp() - dano);
            log.logAtaque(this.getNome(), alvo.getNome(), "ATAQUE NORMAL", dano);
        }

        // Incrementa o turno de voo caso já esteja voando
        if (this.voo) {
            this.turnosVoo++;
        }

        // Registra o estado atualizado do Dragão e do alvo
        log.logEstadoJogador(this);
        log.logEstadoJogador(alvo);

        return ataqueCritico ? ResultadoAtaque.CRITICAL_HIT : ResultadoAtaque.ACERTOU;
    }

}
