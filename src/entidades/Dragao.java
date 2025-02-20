package entidades;

import java.util.Random;

import enums.ResultadoAtaque;
import enums.StatusJogador;

public class Dragao extends Monstro{
    private boolean voo;
    private int turnosVoo;

    public Dragao(String nome, int hp, int ataque, int defesa, int destreza, int velocidade) {
        super(nome, hp, ataque, defesa, destreza, velocidade);
        setVoo(false);
        this.turnosVoo = 0; // Inicializa com 0 turnos no voo
    }

    public boolean getVoo() {
        return voo;
    }

    public void setVoo(boolean voo) {
        if (voo) {
            super.setStatus(StatusJogador.VOANDO);
            this.turnosVoo = 2; // Fica voando por 2 turnos
        } else {
            super.setStatus(StatusJogador.NORMAL);
            this.turnosVoo = 0; // Quando volta ao normal, zera os turnos de voo
        }
        this.voo = voo;
    }

    @Override
    public ResultadoAtaque realizarAtaque(Jogador alvo) {
        Random random = new Random();

        // Se o Dragão estiver voando e for o segundo turno, ele não pode atacar
        if (this.voo && this.turnosVoo == 2) {
            System.out.println(this.getNome() + " não pode atacar enquanto está voando no segundo turno!");
            this.setVoo(false); // O Dragão volta ao normal após o segundo turno
            return ResultadoAtaque.ERROU;
        }

        // Se o Dragão não está voando, há 50% de chance de entrar no voo
        if (!this.voo && random.nextBoolean()) { // 50% de chance
            this.setVoo(true); // O Dragão entra no voo
            System.out.println(this.getNome() + " começa a voar!");
        }

        // Cálculo da chance de acerto
        int chanceAcerto = random.nextInt(100) + 1;

        // Se o ataque acertou
        if (chanceAcerto <= this.getDestreza()) {
            int dano = this.getAtaque() - alvo.getDefesa();
            
            // Ataque adicional se o Dragão quiser
            if (random.nextBoolean()) {
                alvo.setStatus(StatusJogador.QUEIMANDO);
            }

            // Verifica dano mínimo
            if (dano <= 0) dano = 0;
            
            // 25% de chance de ataque crítico
            if (random.nextInt(100) < 25) {
                alvo.setHp(0);
                return ResultadoAtaque.CRITICAL_HIT;
            } else {
                alvo.setHp(alvo.getHp() - dano);
                // Incrementa o turno de voo
                if (this.turnosVoo > 0) {
                    this.turnosVoo++; // Incrementa o turno de voo
                }
                return ResultadoAtaque.ACERTOU;
            }
        } else {
            // Se o ataque falhou
            return ResultadoAtaque.ERROU;
        }
    }


}
