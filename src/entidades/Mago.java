package entidades;

import java.util.Random;
import enums.ResultadoAtaque;
import enums.StatusJogador;

public class Mago extends Heroi {
    private int mana;

    public Mago(String nome, int hp, int ataque, int defesa, int destreza, int velocidade, int mana) {
        super(nome, hp, ataque, defesa, destreza, velocidade, 2);
        setMana(mana);
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
            return ResultadoAtaque.ERROU;
        }

        // Reduz a mana para cada ataque
        this.setMana(this.getMana() - 10);

        // Nova fórmula de chance de acerto
        double chanceDeAcerto = (double) this.getDestreza()+10 / (this.getDestreza() + alvo.getVelocidade());

        if (random.nextDouble() > chanceDeAcerto) {
            return ResultadoAtaque.ERROU; // O ataque falhou
        }

        // Cálculo do dano base
        int dano = Math.max(1, this.getAtaque() - alvo.getDefesa()); // Garante pelo menos 1 de dano

        // 10% de chance de ataque crítico (2x dano)
        if (random.nextInt(100) < 10) {
            dano *= 2;
            alvo.setHp(alvo.getHp() - dano);
            return ResultadoAtaque.CRITICAL_HIT;
        } else {
            alvo.setHp(alvo.getHp() - dano);

            // 50% de chance de envenenar o alvo
            if (random.nextBoolean()) {
                alvo.setStatus(StatusJogador.ENVENENADO);
            }

            return ResultadoAtaque.ACERTOU;
        }
    }
}
