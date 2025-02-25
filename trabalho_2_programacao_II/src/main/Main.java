package main;

import enums.Dificuldade;
import jogo.Jogo;
import jogo.Log;

public class Main {

	public static void main(String[] args) {
		// Modifique para "true", caso queira salvar os logs em um arquivo.txt
		Log log = new Log(false);
		Jogo jogo = new Jogo(Dificuldade.MEDIO, log);
		jogo.iniciarJogo();
		jogo.gerenciarTurnos();
	}

}
