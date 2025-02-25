package entidades;

import jogo.Log;

public abstract class Heroi extends Jogador{
	private int  posicao;

	public int getPosicao() {
		return posicao;
	}
	public void setPosicao(int posicao) {
		this.posicao = posicao;
	}
	
	public Heroi(String nome, int hp, int ataque, int defesa, int destreza, int velocidade, int posicao, Log log) {
		super(nome, hp, ataque, defesa, destreza, velocidade, log);
		this.posicao = posicao;
	}
}
