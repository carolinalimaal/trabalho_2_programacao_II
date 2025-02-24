package jogo;

import java.util.ArrayList;
import java.util.List;

import entidades.Dragao;
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

    public Jogo(Dificuldade dificuldade, Log log) {
        Jogo.herois = new ArrayList<>();
        Jogo.monstros = new ArrayList<>();
        this.jogadorMaiorAtaque = null;
        this.jogadorMenorVida = null;
        this.dificuldade = dificuldade;
        this.logs = log;        
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

    public Jogador getJogadorMenorVida() {
        return jogadorMenorVida;
    }

    public Log getLogs() {
        return logs;
    }

    public Dificuldade getDificuldade() {
        return dificuldade;
    }

    public void iniciarJogo() {
        int quantidadeHerois, quantidadeMonstros;
        
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

        criarJogadores(quantidadeHerois, Heroi.class);
        criarJogadores(quantidadeMonstros, Monstro.class);
        logarInicioJogo();
    }

    private void criarJogadores(int quantidade, Class<? extends Jogador> tipoJogador) {
        if (tipoJogador == Monstro.class && this.dificuldade == Dificuldade.DIFICIL) {
            if (Jogo.monstros.stream().noneMatch(m -> m instanceof Dragao)) {
                Dragao dragao = (Dragao) Jogador.gerarJogadorEspecifico(Dragao.class, this.dificuldade, logs);
                dragao.setNome(dragao.getNome() + "(1)");
                Jogo.monstros.add(dragao);
                Jogo.setNumMonstros(Jogo.getNumMonstros() + 1);
                logs.adicionarLog("Dragão adicionado no modo difícil.");
            }
        }

        while (tipoJogador == Heroi.class ? Jogo.getNumHerois() < quantidade : Jogo.getNumMonstros() < quantidade) {
            Jogador j = Jogador.gerarJogadorAleatorio(this.dificuldade, logs);

            if (j instanceof Dragao && this.dificuldade == Dificuldade.DIFICIL) {
                continue;
            }

            if (tipoJogador.isInstance(j)) {
                j.setNome(j.getNome() + "(" + (tipoJogador == Heroi.class ? Jogo.getNumHerois() : Jogo.getNumMonstros() + 1) + ")");
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

    private void logarInicioJogo() {
        logs.adicionarLog("===============");
        logs.adicionarLog("Início do Jogo!");
        logs.adicionarLog("Dificuldade: " + this.dificuldade);
        logs.adicionarLog("===============");
        
        logs.adicionarLog("Heróis:");
        for (Heroi h : Jogo.herois) {
            logs.adicionarLog(h.toString());
        }
        logs.adicionarLog("===============");
        
        logs.adicionarLog("Monstros:");
        for (Monstro m : Jogo.monstros) {
            logs.adicionarLog(m.toString());
        }
        logs.adicionarLog("===============\n");
    }

    public void terminarJogo() {
        boolean heroisVenceram = !Jogo.herois.isEmpty();
        logs.logFimJogo(heroisVenceram);
    }

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
