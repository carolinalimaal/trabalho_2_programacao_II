package entidades;

import java.util.Random;

import enums.ResultadoAtaque;

public class Guerreiro extends Heroi{
	private int escudo;
	private int danoExtra;

	public Guerreiro(String nome, int hp, int ataque, int defesa, int destreza, int velocidade, int escudo, int danoExtra) {
		super(nome, hp, ataque, defesa + escudo, destreza, velocidade);
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
		
		int chanceAcerto = random.nextInt(100) + 1;
		
		if (chanceAcerto <= this.getDestreza()) {
			int dano = this.getAtaque() - alvo.getDefesa();
			
			if (random.nextBoolean()) {
				dano += this.getDanoExtra();
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
