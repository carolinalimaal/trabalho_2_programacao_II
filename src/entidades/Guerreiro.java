package entidades;

import java.util.Random;
import enums.ResultadoAtaque;

public class Guerreiro extends Heroi {
    private int escudo;
    private int danoExtra;

    public Guerreiro(String nome, int hp, int ataque, int defesa, int destreza, int velocidade, int escudo, int danoExtra) {
        super(nome, hp, ataque, defesa + escudo, destreza, velocidade,0);
        setEscudo(escudo);
        setDanoExtra(danoExtra);
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
        double chanceDeAcerto = (double) this.getDestreza()+10 / (this.getDestreza() + alvo.getVelocidade());

        if (random.nextDouble() > chanceDeAcerto) {
            return ResultadoAtaque.ERROU;  // Ataque falhou
        }

        // Cálculo do dano base
        int dano = Math.max(1, this.getAtaque() - alvo.getDefesa()); // Garante pelo menos 1 de dano

        // 50% de chance de adicionar dano extra
        if (random.nextBoolean()) {
            dano += this.getDanoExtra();
        }

        // 15% de chance de ataque crítico (2x dano)
        if (random.nextInt(100) < 15) {
            dano *= 2;
            alvo.setHp(alvo.getHp() - dano);
            return ResultadoAtaque.CRITICAL_HIT;
        } else {
            alvo.setHp(alvo.getHp() - dano);
            return ResultadoAtaque.ACERTOU;
        }
    }
}
