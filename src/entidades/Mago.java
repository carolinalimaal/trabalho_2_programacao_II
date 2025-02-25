package entidades;

import java.util.Random;
import enums.ResultadoAtaque;
import enums.StatusJogador;
import jogo.Log;

public class Mago extends Heroi {
    private int mana;
    private Log log;

    public Mago(String nome, int hp, int ataque, int defesa, int destreza, int velocidade, int mana, Log log) {
        super(nome, hp, ataque, defesa, destreza, velocidade, 2, log);
        this.mana = mana;
        this.log = log;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    @Override
    public ResultadoAtaque realizarAtaque(Jogador alvo) {
        Random random = new Random();

        // Se não tiver mana suficiente, o ataque falha automaticamente
        if (this.getMana() < 10) {
            log.adicionarLog(this.getNome() + " tentou atacar, mas está sem mana!");
            return ResultadoAtaque.ERROU;
        }

        // Reduz a mana para cada ataque
        this.setMana(this.getMana() - 10);

        // Nova fórmula de chance de acerto
        double chanceDeAcerto = (double) (this.getDestreza() + 10) / (this.getDestreza() + alvo.getVelocidade() + 10);

        if (random.nextDouble() > chanceDeAcerto) {
            log.logAtaque(this.getNome(), alvo.getNome(), "ERROU", 0);
            return ResultadoAtaque.ERROU; // O ataque falhou
        }

        // Cálculo do dano base
        int dano = Math.max(1, this.getAtaque() - alvo.getDefesa()); // Garante pelo menos 1 de dano
        boolean ataqueCritico = random.nextInt(100) < 10; // 10% de chance de ataque crítico

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
        } else {
            // 50% de chance de envenenar o alvo
            if (random.nextBoolean()) {
                alvo.setStatus(StatusJogador.ENVENENADO);
                log.logDanoStatus(alvo.getNome(), "envenenamento", 0);
            }
        }

        // Registra o estado atualizado do Mago e do alvo no log
        log.logEstadoJogador(this);
        log.logEstadoJogador(alvo);

        return ataqueCritico ? ResultadoAtaque.CRITICAL_HIT : ResultadoAtaque.ACERTOU;
    }

}
