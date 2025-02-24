package Main;

import enums.Dificuldade;
import jogo.Jogo;
import jogo.Log;

public class Main {

	public static void main(String[] args) {
		Log log =new Log();
		Jogo jogo = new Jogo(Dificuldade.DIFICIL, log);
		jogo.iniciarJogo();
		jogo.gerenciarTurnos();
		
//		System.out.println(Jogo.getNumHerois());
		log.adicionarLog("Heróis restantes: " + Jogo.getNumHerois());
//		System.out.println(Jogo.getNumMonstros());
		log.adicionarLog("Monstros restantes: " + Jogo.getNumMonstros());
		
		log.exibirLogs();
	}

}
