package entidades;

import java.util.Random;

import enums.ResultadoAtaque;
import enums.StatusJogador;

public class Dragao extends Monstro{
	private boolean voo;

	public Dragao(String nome, int hp, int ataque, int defesa, int destreza, int velocidade) {
		super(nome, hp, ataque, defesa, destreza, velocidade);
		setVoo(false);
	}

	public boolean getVoo() {
		return voo;
	}

	public void setVoo(boolean voo) {
		if (voo)
			super.setStatus(StatusJogador.VOANDO);
		else
			super.setStatus(StatusJogador.NORMAL);
		this.voo = voo;
	}
	
	@Override
	public ResultadoAtaque realizarAtaque(Jogador alvo) {
		Random random = new Random();
		
		int chanceAcerto = random.nextInt(100) + 1;

		
		if (chanceAcerto <= this.getDestreza()) {
			int dano = this.getAtaque() - alvo.getDefesa();
			
			if (random.nextBoolean()) {
				alvo.setStatus(StatusJogador.QUEIMANDO);
			}
			
			if (random.nextBoolean()) {
				this.setVoo(true);
			}
			
			if (dano <= 0)
				dano = 0;
			
			// 25% de ataque crítico
			if (random.nextInt(100) < 25) {
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
