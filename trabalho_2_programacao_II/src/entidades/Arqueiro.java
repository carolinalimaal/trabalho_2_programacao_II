package entidades;

import java.util.Random;
import enums.ResultadoAtaque;
import jogo.Log;

public class Arqueiro extends Heroi {
    private int precisao;
    private int flechas;
    private Log log;

    public Arqueiro(String nome, int hp, int ataque, int defesa, int destreza, int velocidade, int precisao, int flechas, Log log) {
        super(nome, hp, ataque, defesa, destreza, velocidade, 1, log);
        this.precisao = precisao;
        this.flechas = flechas;
        this.log = log;
    }

    public int getPrecisao() {
        return precisao;
    }

    public void setPrecisao(int precisao) {
        this.precisao = precisao;
    }

    public int getFlechas() {
        return flechas;
    }

    public void setFlechas(int flechas) {
        this.flechas = flechas;
    }

    @Override
    public ResultadoAtaque realizarAtaque(Jogador alvo) {
        Random random = new Random();

        // Se não houver flechas, o ataque falha automaticamente
        if (this.getFlechas() <= 0) {
            log.adicionarLog(this.getNome() + " tentou atacar, mas está sem flechas!");
            return ResultadoAtaque.ERROU;
        }

        // Reduz o número de flechas
        this.setFlechas(this.getFlechas() - 1);

        // Nova fórmula de chance de acerto
        double chanceDeAcerto = (double) (this.getDestreza() + 10) / (this.getDestreza() + alvo.getVelocidade() + 10);

        if (random.nextDouble() > chanceDeAcerto) {
            log.logAtaque(this.getNome(), alvo.getNome(), "ERROU", 0);
            return ResultadoAtaque.ERROU; // O ataque falhou
        }

        // Cálculo do dano base
        int dano = Math.max(1, this.getAtaque() - alvo.getDefesa()); // Garante pelo menos 1 de dano
        boolean ataqueCritico = random.nextInt(100) < (20 + this.getPrecisao()); // Chance de crítico baseada na precisão

        // Se for ataque crítico, dobra o dano
        if (ataqueCritico) {
            dano *= 2;
        }

        // Aplica dano ao alvo
        alvo.setHp(alvo.getHp() - dano);

        // Registra um único log de ataque
        log.logAtaque(this.getNome(), alvo.getNome(), ataqueCritico ? "ATAQUE CRÍTICO!" : "ATAQUE NORMAL", dano);

        // Se o alvo morreu, loga a morte
        if (alvo.getHp() <= 0) {
            log.logMorte(alvo.getNome());
        }

        // Registra o estado atualizado do Arqueiro e do alvo no log
        log.logEstadoJogador(this);
        log.logEstadoJogador(alvo);

        return ataqueCritico ? ResultadoAtaque.CRITICAL_HIT : ResultadoAtaque.ACERTOU;
    }

}
