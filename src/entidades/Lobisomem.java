package entidades;

import java.util.Random;
import enums.ResultadoAtaque;
import jogo.Log;

public class Lobisomem extends Monstro {
    private int regeneracao;
    private int frenesi;
    private final int hpMaximo; // Atributo para limitar a regeneração
    private Log log;

    public Lobisomem(String nome, int hp, int ataque, int defesa, int destreza, int velocidade, int regeneracao, int frenesi, Log log) {
        super(nome, hp, ataque, defesa, destreza, velocidade, log);
        this.hpMaximo = hp; // Armazena o HP máximo original
        this.regeneracao = regeneracao;
        this.frenesi = frenesi;
        this.log = log;
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
            // ⚠️ Agora o erro é registrado apenas uma vez e SEM calcular dano!
            log.logAtaque(this.getNome(), alvo.getNome(), "ERROU", 0);
            return ResultadoAtaque.ERROU;
        }

        // ⚠️ Se chegou aqui, significa que o ataque ACERTOU
        int dano = Math.max(1, this.getAtaque() - alvo.getDefesa()); // Garante pelo menos 1 de dano
        boolean ataqueCritico = random.nextInt(100) < 15; // 15% de chance de ataque crítico
        boolean aplicouFrenesi = random.nextInt(100) < 30; // 30% de chance de ativar Frenesi

        // Se aplicou Frenesi, adiciona dano extra
        if (aplicouFrenesi) {
            dano += this.getFrenesi();
            log.adicionarLog(this.getNome() + " entrou em FRENESI e causou dano extra!");
        }

        // Se for ataque crítico, dobra o dano
        if (ataqueCritico) {
            dano *= 2;
        }

        // Aplica dano ao alvo
        alvo.setHp(alvo.getHp() - dano);

        // Registra um único log de ataque correto
        log.logAtaque(this.getNome(), alvo.getNome(), ataqueCritico ? "ATAQUE CRÍTICO!" : "ATAQUE NORMAL", dano);

        // Se o alvo morreu, loga a morte
        if (alvo.getHp() <= 0) {
            log.logMorte(alvo.getNome());
        }

        // Aplica regeneração de vida uma única vez após o ataque
        regenerarVida();

        // Registra o estado atualizado do Lobisomem e do alvo no log
        log.logEstadoJogador(this);
        log.logEstadoJogador(alvo);

        return ataqueCritico ? ResultadoAtaque.CRITICAL_HIT : ResultadoAtaque.ACERTOU;
    }


    /**
     * Método para regenerar HP sem ultrapassar o HP máximo.
     */
    private void regenerarVida() {
        Double vidaAntes = this.getHp();
        this.setHp(Math.min(this.getHp() + this.getRegeneracao(), this.hpMaximo));
        Double vidaDepois = this.getHp();

        if (vidaDepois > vidaAntes) {
            log.adicionarLog(this.getNome() + " se REGENERA e recupera " + (vidaDepois - vidaAntes) + " pontos de vida!");
        }
    }

}
