package Main;

import enums.Dificuldade;
import jogo.Jogo;

public class Main {

	public static void main(String[] args) {
		Jogo jogo = new Jogo(Dificuldade.MEDIO);
		jogo.iniciarJogo();
		jogo.gerenciarTurnos();
		
		System.out.println(Jogo.getNumHerois());
		System.out.println(Jogo.getNumMonstros());
	}

}
