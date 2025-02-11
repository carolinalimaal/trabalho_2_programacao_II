package entidades;

import java.util.Random;

import enums.ResultadoAtaque;

public class Lobisomem extends Monstro{
	private int regeneracao;
	private int frenesi;
	
	public Lobisomem(String nome, int hp, int ataque, int defesa, int destreza, int velocidade, int regeneracao, int frenesi) {
		super(nome, hp, ataque, defesa, destreza, velocidade);
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
		
		int chanceAcerto = random.nextInt(100) + 1;
		
		if (chanceAcerto <= this.getDestreza()) {
			int dano = this.getAtaque() - alvo.getDefesa();
			
			if (random.nextBoolean()) {
				dano += this.getFrenesi();
			}
			
			if (dano <= 0)
				dano = 0;
			
			// 15% de ataque crítico
			if (random.nextInt(100) < 15) {
				alvo.setHp(0);
				return ResultadoAtaque.CRITICAL_HIT;
			} else {
				alvo.setHp(alvo.getHp() - dano);
				return ResultadoAtaque.ACERTOU;
			}
			
		} else {
			return ResultadoAtaque.ERROU;
		}
	}

}
