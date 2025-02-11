package entidades;

import java.util.Random;

import enums.ResultadoAtaque;

public class Arqueiro extends Heroi{
	private int precisao;
	private int flechas;
	

	public Arqueiro(String nome, int hp, int ataque, int defesa, int destreza, int velocidade, int precisao, int flechas) {
		super(nome, hp, ataque, defesa, destreza, velocidade);
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
		int chanceAcerto = random.nextInt(100) + 1;
		
		if ((chanceAcerto <= this.getDestreza()) & (this.getFlechas() > 0)) {
			int dano = this.getAtaque() - alvo.getDefesa();
			this.setFlechas(this.getFlechas() - 1);
			
			if (dano <= 0)
				dano = 0;
			
			// Ataque crítico depende da precisão
			if (random.nextInt(100) < 20 + this.getPrecisao()) {
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
