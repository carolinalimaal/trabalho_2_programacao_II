package jogo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import entidades.Heroi;
import entidades.Jogador;
import entidades.Monstro;
import enums.Dificuldade;

public class Jogo {
	private static List<Heroi> herois;
	private static List<Monstro> monstros;
	private static int numHerois, numMonstros;
	private Jogador jogadorMaiorAtaque, jogadorMenorVida;
	private final Dificuldade dificuldade;
	private Log logs;
	
	public Jogo(Dificuldade dificuldade) {
		Jogo.herois = new ArrayList<Heroi>();
		Jogo.monstros = new ArrayList<Monstro>();
		this.jogadorMaiorAtaque = null;
		this.jogadorMenorVida = null;
		this.dificuldade = dificuldade;
		this.logs = new Log();		
	}

	public static List<Heroi> getHerois() {
		return herois;
	}

	public static List<Monstro> getMonstros() {
		return monstros;
	}

	public static int getNumHerois() {
		return numHerois;
	}

	public static void setNumHerois(int numHerois) {
		Jogo.numHerois = numHerois;
	}

	public static int getNumMonstros() {
		return numMonstros;
	}

	public static void setNumMonstros(int numMonstros) {
		Jogo.numMonstros = numMonstros;
	}

	public Jogador getJogadorMaiorAtaque() {
		return jogadorMaiorAtaque;
	}

	public void setJogadorMaiorAtaque() {
		List<Jogador> personagens = new ArrayList<Jogador>();
		personagens.addAll(Jogo.herois);
		personagens.addAll(Jogo.monstros);
		Jogador maiorAtaque = personagens.get(0);
		for (int i = 1; i < personagens.size(); i++) {
			if (personagens.get(i).getAtaque() > maiorAtaque.getAtaque())
				maiorAtaque = personagens.get(i);
		}
		this.jogadorMaiorAtaque = maiorAtaque;
	}

	public Jogador getJogadorMenorVida() {
		return jogadorMenorVida;
	}

	public void setJogadorMenorVida() {
		List<Jogador> personagens = new ArrayList<Jogador>();
		personagens.addAll(Jogo.herois);
		personagens.addAll(Jogo.monstros);
		Jogador menorHP = personagens.get(0);
		for (int i = 1; i < personagens.size(); i++) {
			if (personagens.get(i).getHp() < menorHP.getHp())
				menorHP = personagens.get(i);
		}
		this.jogadorMaiorAtaque = menorHP;
	}

	public Log getLogs() {
		return logs;
	}

	public Dificuldade getDificuldade() {
		return dificuldade;
	}
	
	public void iniciarJogo() {
		Random random = new Random();
		int quantidadeHerois, quantidadeMonstros;
		
		if (this.getDificuldade() == Dificuldade.FACIL) {
			quantidadeHerois = random.nextInt(3) + 7;
			quantidadeMonstros = random.nextInt(3) + 1;	
		}
		else if (this.getDificuldade() == Dificuldade.MEDIO) {
			quantidadeHerois = random.nextInt(3) + 4;
			quantidadeMonstros = random.nextInt(3) + 4;
		}
		else {
			quantidadeHerois = random.nextInt(3) + 1;
			quantidadeMonstros = random.nextInt(3) + 7;
		}
		
		while (Jogo.getNumHerois() < quantidadeHerois) {
			Jogador j = Jogador.gerarJogadorAleatorio();
			if (j instanceof Heroi) {
				j.setNome(j.getNome() + "(" + (Jogo.getNumHerois() + 1) +")");
				Jogo.herois.add((Heroi) j);
				Jogo.setNumHerois(Jogo.getNumHerois() + 1);
			}
		}
		while (Jogo.getNumMonstros() < quantidadeMonstros) {
			Jogador j = Jogador.gerarJogadorAleatorio();
			if (j instanceof Monstro) {
				j.setNome(j.getNome() + "(" + (Jogo.getNumMonstros() + 1) +")");
				Jogo.monstros.add((Monstro) j);
				Jogo.setNumMonstros(Jogo.getNumMonstros() + 1);
			}
		}
		// ADICIONAR LOG INICIAL 
		System.out.println("===============");
		System.out.println("Início do Jogo!");
		System.out.println("===============\n");
		
		System.out.println("===============");
		System.out.println("Heróis: ");
		System.out.println("===============");
		for (Heroi h: Jogo.herois) {
			System.out.println(h);
		}
		System.out.println("===============");
		
		System.out.println("Monstros: ");
		System.out.println("===============");
		for (Monstro m: Jogo.monstros) {
			System.out.println(m);
		}
		System.out.println("===============\n");
	}
	
	public void terminarJogo() {
		if (Jogo.herois.isEmpty()) {
			System.out.println("Monstros venceram");
			// adicionar log 
			//  log.addLog("Monstros venceram!");
        } else {
        	System.out.println("Herois venceram");
        	// adicionar log 
        	//  log.addLog("Heróis venceram!");
        }
		// mostrar todos os logs 
	}
	
	public void gerenciarTurnos() {
		Turno turno = new Turno(Jogo.herois, Jogo.monstros, this.logs);
		int contador = 1;
		while (true) {
			if (Jogo.herois.isEmpty())
				break;
			if (Jogo.monstros.isEmpty())
				break;
			turno.executarTurno(contador);
			contador++;
		}
		this.terminarJogo();
	}
}
