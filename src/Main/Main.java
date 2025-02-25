package Main;

import enums.Dificuldade;
import jogo.Jogo;
import jogo.Log;

public class Main {

	public static void main(String[] args) {
		Log log =new Log(true);
		Jogo jogo = new Jogo(Dificuldade.FACIL, log);
		jogo.iniciarJogo();
		jogo.gerenciarTurnos();
		
		System.out.println(jogo.getHerois().size());
		System.out.println(jogo.getMonstros().size());
	}

}
