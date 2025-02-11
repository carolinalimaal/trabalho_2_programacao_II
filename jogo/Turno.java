package jogo;

import java.util.ArrayList;
import java.util.List;

import entidades.Dragao;
import entidades.Heroi;
import entidades.Jogador;
import entidades.Monstro;
import enums.ResultadoAtaque;
import enums.StatusJogador;

public class Turno {
//	private List<Heroi> herois;
//	private List<Monstro> monstros;
	private Log log;
	
	public Turno(List<Heroi> herois, List<Monstro> monstros, Log log) {
//		setHerois(herois);
//		setMonstros(monstros);
		setLog(log);
	}
	
//	public List<Heroi> getHerois() {
//		return herois;
//	}
//
//	public void setHerois(List<Heroi> herois) {
//		this.herois = new ArrayList<Heroi>();
//		this.herois = herois;
//	}
//
//	public List<Monstro> getMonstros() {
//		return monstros;
//	}
//
//	public void setMonstros(List<Monstro> monstros) {
//		this.monstros = new ArrayList<Monstro>();
//		this.monstros = monstros;
//	}

	public Log getLog() {
		return log;
	}

	public void setLog(Log log) {
		this.log = log;
	}

	private List<Jogador> determinarOrdem() {	
		List<Jogador> personagens = new ArrayList<Jogador>();
		personagens.addAll(Jogo.getHerois());
		personagens.addAll(Jogo.getMonstros());
		personagens.sort((j1, j2) -> Integer.compare(j2.getVelocidade(), j1.getVelocidade()));
		return personagens;
	}
	
	public void executarTurno(int contador) {
	    List<Jogador> personagens = determinarOrdem();
	    
	    System.out.println("===============");
	    System.out.println("Turno " + contador);
	    System.out.println("===============");
	    
	    for (Jogador jogador : personagens) {
	        Jogador alvo = escolherAlvo(jogador);

	        // Verifica se um alvo foi encontrado
	        if (alvo == null) {
	            System.out.println(jogador.getNome() + " não encontrou um alvo válido e não atacará neste turno.");
	            continue; // Pula para o próximo jogador
	        }

	        if (alvo.getStatus() == StatusJogador.VOANDO) {
	            System.out.println("Alvo voando, ataque falhou");
	        } else {
	            ResultadoAtaque ataque = jogador.realizarAtaque(alvo);
	            
	            if (alvo.getStatus() == StatusJogador.ENVENENADO) {
	                alvo.setHp(alvo.getHp() - 10);
	            } else if (alvo.getStatus() == StatusJogador.QUEIMANDO) {
	                alvo.setHp(alvo.getHp() - 15);
	            }
	        }

	        if (jogador.getStatus() == StatusJogador.VOANDO) {
	            ((Dragao) jogador).setVoo(false);
	        } else {
	            if (jogador.getStatus() == StatusJogador.ENVENENADO) {
	                jogador.setHp(jogador.getHp() - 10);
	            } else if (jogador.getStatus() == StatusJogador.QUEIMANDO) {
	                jogador.setHp(jogador.getHp() - 15);
	            }
	        }

	        if (jogador instanceof Heroi && !jogador.estaVivo()) {
	            System.out.println(jogador.getNome() + " morreu!");
	            Jogo.getHerois().remove(jogador);
	            Jogo.setNumHerois(Jogo.getNumHerois() - 1);
	        } else if (jogador instanceof Monstro && !jogador.estaVivo()){
	            System.out.println(jogador.getNome() + " morreu!");
	            Jogo.getMonstros().remove(jogador);
	            Jogo.setNumMonstros(Jogo.getNumMonstros() - 1);
	        }

	        // Evita chamar métodos em um alvo nulo
	        if (alvo != null && !alvo.estaVivo()) {
	            System.out.println(alvo.getNome() + " morreu!");
	            if (alvo instanceof Heroi) {
	                Jogo.getHerois().remove(alvo);
	                Jogo.setNumHerois(Jogo.getNumHerois() - 1);
	            } else if (alvo instanceof Monstro) {
	                Jogo.getMonstros().remove(alvo);
	                Jogo.setNumMonstros(Jogo.getNumMonstros() - 1);
	            }
	        }
	    }
	}

		
	// TODO
	// Implementar aqui como que escolhe o alvo
	private Jogador escolherAlvo(Jogador atacante) {
	    if (atacante instanceof Heroi) {
	        if (Jogo.getMonstros().isEmpty()) {
	            System.out.println("Erro: Não há monstros disponíveis como alvo.");
	            return null; 
	        }
	        return Jogo.getMonstros().get(0);
	    } else {
	        if (Jogo.getHerois().isEmpty()) {
	            System.out.println("Erro: Não há heróis disponíveis como alvo.");
	            return null; 
	        }
	        return Jogo.getHerois().get(0);
	    }
	}
}
