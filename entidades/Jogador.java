package entidades;

import java.util.Random;

import enums.ResultadoAtaque;
import enums.StatusJogador;

public abstract class Jogador {
	private String nome;
	private int hp, ataque, defesa, destreza, velocidade;
	private StatusJogador status;

	public Jogador(String nome, int hp, int ataque, int defesa, int destreza, int velocidade) {
		setNome(nome);
		setHp(hp);
		setAtaque(ataque);
		setDefesa(defesa);
		setDestreza(destreza);
		setVelocidade(velocidade);
		setStatus(StatusJogador.NORMAL);
	}
	
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public int getHp() {
		return hp;
	}
	public void setHp(int hp) {
		this.hp = hp;
	}
	public int getAtaque() {
		return ataque;
	}
	public void setAtaque(int ataque) {
		this.ataque = ataque;
	}
	public int getDefesa() {
		return defesa;
	}
	public void setDefesa(int defesa) {
		this.defesa = defesa;
	}
	public int getDestreza() {
		return destreza;
	}
	public void setDestreza(int destreza) {
		this.destreza = destreza;
	}
	public int getVelocidade() {
		return velocidade;
	}
	public void setVelocidade(int velocidade) {
		this.velocidade = velocidade;
	}
	
	public StatusJogador getStatus() {
		return status;
	}

	public void setStatus(StatusJogador status) {
		this.status = status;
	}
		
	public boolean estaVivo() {
		return this.hp > 0;
	}
	
	public abstract ResultadoAtaque realizarAtaque(Jogador alvo);
	
	public static Jogador gerarJogadorAleatorio() {
		Random random = new Random();
		String[] nomeHerois = {"Guerreiro ", "Mago ", "Arqueiro "};
		String[] nomeMonstros = {"Goblin ", "Dragão ", "Lobisomem "};
		
		boolean isHeroi = random.nextBoolean();
		
		String nome = isHeroi ? nomeHerois[random.nextInt(3)] : nomeMonstros[random.nextInt(3)];
		int hp = random.nextInt(200) + 100;
		int ataque = random.nextInt(100) + 50;
		int defesa = random.nextInt(100) + 10;
		int destreza = random.nextInt(100) + 1;
		int velocidade = random.nextInt(100) + 20;
		
		switch (nome){
		case "Guerreiro ":
			return new Guerreiro(nome, hp, ataque, defesa, destreza, velocidade, random.nextInt(20) + 1, random.nextInt(15) + 1);
		case "Mago ":
			return new Mago(nome, hp, ataque, defesa, destreza, velocidade, random.nextInt(50) + 30);
		case "Arqueiro ":
			return new Arqueiro(nome, hp, ataque, defesa, destreza, velocidade, random.nextInt(100) + 1, random.nextInt(5) + 1);
		case "Goblin ":
			return new Goblin(nome, hp, ataque, defesa, destreza, velocidade, random.nextInt(5) + 1);
		case "Dragão ":
			return new Dragao(nome, hp, ataque, defesa, destreza, velocidade);
		case "Lobisomem ":
			return new Lobisomem(nome, hp, ataque, defesa, destreza, velocidade, random.nextInt(20) + 1, random.nextInt(15) + 1);
		}
		return null;
	}
	
	@Override
	public String toString() {
		return this.getNome() + 
				" | HP: " + this.getHp() + 
				" | ATATQUE: " + this.getAtaque() + 
				" | DEFESA: " + this.getDefesa() +
				" | DESTREZA: " + this.getDestreza() + 
				" | VELOCIDADE: " + this.getVelocidade();
	}
}
