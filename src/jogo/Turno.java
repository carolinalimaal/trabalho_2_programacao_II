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
	private Log log;
	
	public Turno(List<Heroi> herois, List<Monstro> monstros, Log log) {
		setLog(log);
	}

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
	    
	    System.out.println("====================================");
	    System.out.println("TURNO " + contador);
	    System.out.println("------------------");
	    
	    for (Jogador atacante : personagens) {
	        Jogador alvo = escolherAlvo(atacante);
	        
	        if (!atacante.estaVivo()) {
	        	continue; // Se o atacante morrer durante o turno e ainda não tiver atacado, ele não pode atacar mesmo estando no array
	        }

	        // Verifica se um alvo foi encontrado
	        if (alvo == null) {
	            System.out.println(atacante.getNome() + " não encontrou um alvo válido e não atacará neste turno.");
	            System.out.println("------------------\n");
	            continue; // Pula para o próximo jogador
	        }
	        
	        // Se o alvo estiver voando, não há ataque
	        if (alvo.getStatus() == StatusJogador.VOANDO) {
	        	System.out.println(alvo.getNome() + " está voando, não é possível atacá-lo!");
	        // Realiza o ataque 
	        } else {
	        	System.out.println(atacante.getNome() + " atacará " + alvo.getNome() + "\n");
	        	System.out.println("STATS ANTES DO ATAQUE: \n" + atacante.toString() + "\n" + alvo.toString());
	        	
	            ResultadoAtaque ataque = atacante.realizarAtaque(alvo);
	            System.out.println("\nResultado do ataque: " + ataque + "\n");
	            
	            // Verifica se o alvo ainda está vivo
	            if (alvo.estaVivo()) {
	            	System.out.println("STATS DEPOIS DO ATAQUE: \n" + atacante.toString() + "\n" + alvo.toString() + "\n");
	            	verificarEnvenenado(alvo); // Verifica envenenamento e queimaduras
	            	verificarQueimando(alvo);
	            } else {
	            	removerPersonagem(alvo); // Remove o alvo caso tenha morrido
	            }
	        }
	        
	        // Atualiza Status do atacante
	        if (atacante.getStatus() == StatusJogador.VOANDO) {
	            ((Dragao) atacante).setVoo(false);
	        } else {
	        	verificarEnvenenado(atacante);
	        	verificarQueimando(atacante);
	        }
	        
	        System.out.println("------------------\n");
	    }
	}
	
	private void verificarEnvenenado(Jogador j) {
		if (j.getStatus() == StatusJogador.ENVENENADO) {
        	j.setHp(j.getHp() - 10);
        	System.out.println("Oh não, " + j.getNome() + " está envenenado. -10 pontos de vida.");
        	if (j.estaVivo()) 
        		System.out.println("Stats atualizado do " + j.getNome() + "\n" + j.toString() + "\n");
        	else
        		removerPersonagem(j);
		}
	}
	
	private void verificarQueimando(Jogador j) {
		if (j.getStatus() == StatusJogador.QUEIMANDO) {
            j.setHp(j.getHp() - 15);
            System.out.println("Oh não, " + j.getNome() + " está queimando. -15 pontos de vida.");
            if (j.estaVivo())
        		System.out.println("Stats atualizado do " + j.getNome() + "\n" + j.toString() + "\n");
        	else
        		removerPersonagem(j);
        }
	}

	private void removerPersonagem(Jogador j) {
		
		if (j instanceof Heroi && !j.estaVivo()) {
            System.out.println(j.getNome() + " morreu!");
            Jogo.getHerois().remove(j);
            Jogo.setNumHerois(Jogo.getNumHerois() - 1);
        } else if (j instanceof Monstro && !j.estaVivo()){
            System.out.println(j.getNome() + " morreu!");
            Jogo.getMonstros().remove(j);
            Jogo.setNumMonstros(Jogo.getNumMonstros() - 1);
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
