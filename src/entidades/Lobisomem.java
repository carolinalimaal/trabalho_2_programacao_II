package entidades;

import java.util.Random;
import enums.ResultadoAtaque;

public class Lobisomem extends Monstro {
    private int regeneracao;
    private int frenesi;
    private final int hpMaximo; // Novo atributo para limitar a regeneração

    public Lobisomem(String nome, int hp, int ataque, int defesa, int destreza, int velocidade, int regeneracao, int frenesi) {
        super(nome, hp, ataque, defesa, destreza, velocidade);
        this.hpMaximo = hp; // Armazena o HP máximo original
        setRegeneracao(regeneracao);
        setFrenesi(frenesi);
    }

    public int getRegeneracao() {
        return regeneracao;
    }

    public void setRegeneracao(int regeneracao) {
        this.regeneracao = regeneracao;
    }

    public int getFrenesi() {
        return frenesi;
    }

    public void setFrenesi(int frenesi) {
        this.frenesi = frenesi;
    }

    @Override
    public ResultadoAtaque realizarAtaque(Jogador alvo) {
        Random random = new Random();

        // Nova fórmula de chance de acerto baseada na destreza e velocidade do alvo
        double chanceDeAcerto = (double) this.getDestreza() / (this.getDestreza() + alvo.getVelocidade());

        if (random.nextDouble() > chanceDeAcerto) {
            return ResultadoAtaque.ERROU; // O ataque falhou
        }

        // Cálculo do dano base
        int dano = Math.max(1, this.getAtaque() - alvo.getDefesa()); // Garante pelo menos 1 de dano

        // 30% de chance de ativar Frenesi e adicionar dano extra
        if (random.nextInt(100) <= 30) {
            dano += this.getFrenesi();
        }

        // 15% de chance de ataque crítico (2x dano)
        if (random.nextInt(100) < 15) {
            dano *= 2; // Dano crítico = 2x dano normal
            alvo.setHp(alvo.getHp() - dano);
            regenerarVida(); // Aplica regeneração limitada ao HP máximo
            return ResultadoAtaque.CRITICAL_HIT;
        } else {
            alvo.setHp(alvo.getHp() - dano);
            regenerarVida(); // Aplica regeneração limitada ao HP máximo
            return ResultadoAtaque.ACERTOU;
        }
    }

    /**
     * Método para regenerar HP sem ultrapassar o HP máximo.
     */
    private void regenerarVida() {
        this.setHp(Math.min(this.getHp() + this.getRegeneracao(), this.hpMaximo));
    }
}
