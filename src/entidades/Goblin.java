package entidades;

import java.util.Random;

import enums.ResultadoAtaque;

public class Goblin extends Monstro{
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
		
		int chanceAcerto = random.nextInt(100) + 1;

		
		if (chanceAcerto <= this.getDestreza() & this.getDardos() > 0) {
			int dano = this.getAtaque() - alvo.getDefesa();
			this.setDardos(this.getDardos() - 1);
			
			if (dano <= 0)
				dano = 0;
			
			// 10% de ataque crítico
			if (random.nextInt(100) < 10) {
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
