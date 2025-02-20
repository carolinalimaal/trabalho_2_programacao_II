package entidades;

import java.util.Random;
import enums.ResultadoAtaque;

public class Arqueiro extends Heroi {
    private int precisao;
    private int flechas;

    public Arqueiro(String nome, int hp, int ataque, int defesa, int destreza, int velocidade, int precisao, int flechas) {
        super(nome, hp, ataque, defesa, destreza, velocidade, 1);
        setPrecisao(precisao);
        setFlechas(flechas);
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
            return ResultadoAtaque.ERROU;
        }

        // Reduz o número de flechas
        this.setFlechas(this.getFlechas() - 1);

        // Nova fórmula de chance de acerto
        double chanceDeAcerto = (double) this.getDestreza()+10 / (this.getDestreza() + alvo.getVelocidade());

        if (random.nextDouble() > chanceDeAcerto) {
            return ResultadoAtaque.ERROU; // O ataque falhou
        }

        // Cálculo do dano base
        int dano = Math.max(1, this.getAtaque() - alvo.getDefesa()); // Garante pelo menos 1 de dano

        // Chance de ataque crítico baseada na precisão do arqueiro
        int chanceCritico = 20 + this.getPrecisao(); // Base de 20% + Precisão do Arqueiro
        if (random.nextInt(100) < chanceCritico) {
            dano *= 2; // Dano crítico = 2x o dano normal
            alvo.setHp(alvo.getHp() - dano);
            return ResultadoAtaque.CRITICAL_HIT;
        } else {
            alvo.setHp(alvo.getHp() - dano);
            return ResultadoAtaque.ACERTOU;
        }
    }
}
