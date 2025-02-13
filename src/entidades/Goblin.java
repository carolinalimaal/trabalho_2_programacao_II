package entidades;

import java.util.Random;
import enums.ResultadoAtaque;

public class Goblin extends Monstro {
    private int dardos;

    public Goblin(String nome, int hp, int ataque, int defesa, int destreza, int velocidade, int dardos) {
        super(nome, hp, ataque, defesa, destreza, velocidade);
        setDardos(dardos);
    }

    public int getDardos() {
        return dardos;
    }

    public void setDardos(int dardos) {
        this.dardos = dardos;
    }

    @Override
    public ResultadoAtaque realizarAtaque(Jogador alvo) {
        Random random = new Random();

        // Se não tiver dardos, o Goblin foge
        if (this.getDardos() <= 0) {
            return ResultadoAtaque.ERROU;
        }

        // Reduz o número de dardos
        this.setDardos(this.getDardos() - 1);

        // Nova fórmula de chance de acerto
        double chanceDeAcerto = (double) this.getDestreza() / (this.getDestreza() + alvo.getVelocidade());

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

            

            return ResultadoAtaque.ACERTOU;
        }
    }
}
