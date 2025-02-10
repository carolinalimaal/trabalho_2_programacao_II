package entidades;

import java.util.Random;

import enums.ResultadoAtaque;
import enums.StatusJogador;

public class Mago extends Heroi{
	private int mana;

	public Mago(String nome, int hp, int ataque, int defesa, int destreza, int velocidade, int mana) {
		super(nome, hp, ataque, defesa, destreza, velocidade);
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
		
		int chanceAcerto = random.nextInt(100) + 1;

		
		if (chanceAcerto <= this.getDestreza() & this.getMana() > 10) {
			int dano = this.getAtaque() - alvo.getDefesa();
			this.setMana(this.getMana() - 10);
			
			if (random.nextBoolean()) {
				alvo.setStatus(StatusJogador.ENVENENADO);
			}
			
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
