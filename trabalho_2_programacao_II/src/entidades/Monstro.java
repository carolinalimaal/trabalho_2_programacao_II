package entidades;

import jogo.Log;

public abstract class Monstro extends Jogador{

	public Monstro(String nome, int hp, int ataque, int defesa, int destreza, int velocidade, Log log) {
		super(nome, hp, ataque, defesa, destreza, velocidade, log);
	}

}
