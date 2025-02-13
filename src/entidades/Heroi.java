package entidades;

public abstract class Heroi extends Jogador{
	

	public int getPosicao() {
		return posicao;
	}
	public void setPosicao(int posicao) {
		this.posicao = posicao;
	}
	private int  posicao;
	public Heroi(String nome, int hp, int ataque, int defesa, int destreza, int velocidade, int posicao) {
		super(nome, hp, ataque, defesa, destreza, velocidade);
		this.posicao = posicao;
	}
}
