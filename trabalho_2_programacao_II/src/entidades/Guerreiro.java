package entidades;

import java.util.Random;
import enums.ResultadoAtaque;
import jogo.Log;

public class Guerreiro extends Heroi {
    private int escudo;
    private int danoExtra;
    private Log log;

    public Guerreiro(String nome, int hp, int ataque, int defesa, int destreza, int velocidade, int escudo, int danoExtra, Log log) {
        super(nome, hp, ataque, defesa + escudo, destreza, velocidade, 0, log);
        this.escudo = escudo;
        this.danoExtra = danoExtra;
        this.log = log;
    }

    public int getEscudo() {
        return escudo;
    }

    public void setEscudo(int escudo) {
        this.escudo = escudo;
    }

    public int getDanoExtra() {
        return danoExtra;
    }

    public void setDanoExtra(int danoExtra) {
        this.danoExtra = danoExtra;
    }

    @Override
    public ResultadoAtaque realizarAtaque(Jogador alvo) {
        Random random = new Random();

        // Nova fórmula de chance de acerto
        double chanceDeAcerto = (double) (this.getDestreza() + 10) / (this.getDestreza() + alvo.getVelocidade() + 10);

        if (random.nextDouble() > chanceDeAcerto) {
            log.logAtaque(this.getNome(), alvo.getNome(), "ERROU", 0);
            return ResultadoAtaque.ERROU;  // Ataque falhou
        }

        // Cálculo do dano base
        int dano = Math.max(1, this.getAtaque() - alvo.getDefesa()); // Garante pelo menos 1 de dano
        boolean ataqueCritico = random.nextInt(100) < 15; // 15% de chance de ataque crítico
        boolean aplicouDanoExtra = random.nextBoolean(); // 50% de chance de dano extra

        // Se for ataque crítico, dobra o dano
        if (ataqueCritico) {
            dano *= 2;
        }

        // Se aplicou dano extra, adiciona ao total
        if (aplicouDanoExtra) {
            dano += this.getDanoExtra();
            log.adicionarLog(this.getNome() + " usou sua FORÇA BRUTA e causou dano extra!");
        }

        // Aplica dano ao alvo
        alvo.setHp(alvo.getHp() - dano);

        // Registra um único log de ataque
        log.logAtaque(this.getNome(), alvo.getNome(), ataqueCritico ? "ATAQUE CRÍTICO!" : "ATAQUE NORMAL", dano);

        // Se o alvo morreu, loga a morte
        if (alvo.getHp() <= 0) {
            log.logMorte(alvo.getNome());
        }

        // Registra o estado atualizado do Guerreiro e do alvo no log
        log.logEstadoJogador(this);
        log.logEstadoJogador(alvo);

        return ataqueCritico ? ResultadoAtaque.CRITICAL_HIT : ResultadoAtaque.ACERTOU;
    }

}
