package jogo;

import java.util.ArrayList;
import java.util.List;

import entidades.Dragao;
import entidades.Heroi;
import entidades.Jogador;
import entidades.Monstro;
import enums.Dificuldade;

public class Jogo {
    // Listas para armazenar heróis e monstros
    private static List<Heroi> herois;
    private static List<Monstro> monstros;
    
    // Contadores para o número de heróis e monstros
    private static int numHerois, numMonstros;
    
    // Jogadores com maior ataque e menor vida
    private Jogador jogadorMaiorAtaque, jogadorMenorVida;
    
    // Nível de dificuldade e logs do jogo
    private final Dificuldade dificuldade;
    private Log logs;

    // Construtor que inicializa o jogo com a dificuldade selecionada
    public Jogo(Dificuldade dificuldade) {
        Jogo.herois = new ArrayList<>();
        Jogo.monstros = new ArrayList<>();
        this.jogadorMaiorAtaque = null;
        this.jogadorMenorVida = null;
        this.dificuldade = dificuldade;
        this.logs = new Log();        
    }

    // Métodos getters e setters para heróis, monstros e contadores
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

    public Jogador getJogadorMenorVida() {
        return jogadorMenorVida;
    }

    // Método para encontrar o jogador com maior ataque
    public void setJogadorMaiorAtaque() {
        jogadorMaiorAtaque = encontrarJogadorPorAtributo("maiorataque");
    }

    // Método para encontrar o jogador com menor vida
    public void setJogadorMenorVida() {
        jogadorMenorVida = encontrarJogadorPorAtributo("menorvida");
    }

    // Método genérico para encontrar o jogador com maior ataque ou menor vida
    private Jogador encontrarJogadorPorAtributo(String atributo) {
        List<Jogador> personagens = new ArrayList<>();
        personagens.addAll(Jogo.herois);
        personagens.addAll(Jogo.monstros);

        Jogador melhorJogador = personagens.get(0);
        for (Jogador currentJogador : personagens) {
            if (atributo.equals("maiorataque") && currentJogador.getAtaque() > melhorJogador.getAtaque()) {
                melhorJogador = currentJogador;
            } else if (atributo.equals("menorvida") && currentJogador.getHp() < melhorJogador.getHp()) {
                melhorJogador = currentJogador;
            }
        }
        return melhorJogador;
    }

    public Log getLogs() {
        return logs;
    }

    public Dificuldade getDificuldade() {
        return dificuldade;
    }

    // Inicia o jogo com base na dificuldade
    public void iniciarJogo() {
        int quantidadeHerois, quantidadeMonstros;
        
        // Define a quantidade de heróis e monstros conforme a dificuldade
        switch (this.getDificuldade()) {
            case FACIL:
                quantidadeHerois = 5;
                quantidadeMonstros = 10;
                break;
            case MEDIO:
                quantidadeHerois = 5;
                quantidadeMonstros = 13;
                break;
            case DIFICIL:
                quantidadeHerois = 6;
                quantidadeMonstros = 10;
                break;
            default:
                quantidadeHerois = 5;
                quantidadeMonstros = 5;
        }
        
        // Criação de heróis e monstros
        criarJogadores(quantidadeHerois, Heroi.class);
        criarJogadores(quantidadeMonstros, Monstro.class);

        // Adiciona log de início do jogo
        logarInicioJogo();
    }

 // Método genérico para criar jogadores (heróis ou monstros)
    private void criarJogadores(int quantidade, Class<? extends Jogador> tipoJogador) {
        if (tipoJogador == Monstro.class && this.dificuldade == Dificuldade.DIFICIL) {
            // Primeiro, adiciona um Dragão se ainda não houver nenhum
            if (Jogo.monstros.stream().noneMatch(m -> m instanceof Dragao)) {
                Dragao dragao = (Dragao) Jogador.gerarJogadorEspecifico(Dragao.class, this.dificuldade);
                dragao.setNome(dragao.getNome() + "(1)");
                Jogo.monstros.add(dragao);
                Jogo.setNumMonstros(Jogo.getNumMonstros() + 1);
            }
        }

        // Agora gera os outros jogadores normalmente
        while (tipoJogador == Heroi.class ? Jogo.getNumHerois() < quantidade : Jogo.getNumMonstros() < quantidade) {
            Jogador j = Jogador.gerarJogadorAleatorio(this.dificuldade);

            // Evita gerar mais Dragões se já houver um
            if (j instanceof Dragao && this.dificuldade == Dificuldade.DIFICIL) {
                continue; // Pula essa iteração e tenta gerar outro monstro
            }

            if (tipoJogador.isInstance(j)) {
                j.setNome(j.getNome() + "(" + (tipoJogador == Heroi.class ? Jogo.getNumHerois() + 1 : Jogo.getNumMonstros() + 1) + ")");
                if (tipoJogador == Heroi.class) {
                    Jogo.herois.add((Heroi) j);
                    Jogo.setNumHerois(Jogo.getNumHerois() + 1);
                } else {
                    Jogo.monstros.add((Monstro) j);
                    Jogo.setNumMonstros(Jogo.getNumMonstros() + 1);
                }
            }
        }
    }


    // Método para logar as informações de início do jogo
    private void logarInicioJogo() {
    	// add log 
        System.out.println("============================================================");
        System.out.println("Início do Jogo!");
        System.out.println("Dificuldade: "+ this.dificuldade);
        System.out.println("============================================================\n");

        // add log
        System.out.println("Heróis:\n");
        for (Heroi h : Jogo.herois) {
            // add log
            System.out.println(h);
        }
        // add log
        System.out.println("============================================================");
        
        // add log
        System.out.println("Monstros:\n");
        for (Monstro m : Jogo.monstros) {
            // add log
            System.out.println(m);
        }
        // add log
        System.out.println("============================================================\n");
    }

    // Método para finalizar o jogo
    public void terminarJogo() {
        if (Jogo.herois.isEmpty()) {
            System.out.println("Monstros venceram"); // adicionar log 
            
        } else {
            System.out.println("Heróis venceram"); // adicionar log 
        }
        // Mostrar todos os logs quando o jogo acabar
    }

    // Método para gerenciar os turnos de jogo
    public void gerenciarTurnos() {
        Turno turno = new Turno(Jogo.herois, Jogo.monstros, this.logs, this.dificuldade);
        int contador = 1;
        while (true) {
            if (Jogo.herois.isEmpty() || Jogo.monstros.isEmpty()) {
                break;
            }
            turno.executarTurno(contador);
            contador++;
        }
        this.terminarJogo();
    }
}
