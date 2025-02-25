package jogo;

import java.util.ArrayList;
import java.util.List;

import entidades.Dragao;
import entidades.Heroi;
import entidades.Jogador;
import entidades.Monstro;
import enums.Dificuldade;

public class Jogo {
    private List<Heroi> herois;
    private List<Monstro> monstros;
    private int numHerois, numMonstros;
    private Jogador jogadorMaiorAtaque, jogadorMenorVida;
    private final Dificuldade dificuldade;
    private Log logs;

    public Jogo(Dificuldade dificuldade, Log log) {
        this.herois = new ArrayList<>();
        this.monstros = new ArrayList<>();
        this.jogadorMaiorAtaque = null;
        this.jogadorMenorVida = null;
        this.dificuldade = dificuldade;
        this.logs = log;        
    }

    public List<Heroi> getHerois() {
        return herois;
    }

    public List<Monstro> getMonstros() {
        return monstros;
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
                quantidadeHerois = 6;
                quantidadeMonstros = 8;
                break;
            case DIFICIL:
                quantidadeHerois = 9;
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
            if (monstros.stream().noneMatch(m -> m instanceof Dragao)) {
                Dragao dragao = (Dragao) Jogador.gerarJogadorEspecifico(Dragao.class, this.dificuldade, logs);
                dragao.setNome(dragao.getNome() + "(1)");
                monstros.add(dragao);
                numMonstros++;
                logs.adicionarLog("Dragão adicionado no modo difícil.");
            }
        }

        while ((tipoJogador == Heroi.class ? numHerois < quantidade : numMonstros < quantidade)) {
            Jogador j = Jogador.gerarJogadorAleatorio(this.dificuldade, logs);

            if (j instanceof Dragao && this.dificuldade == Dificuldade.DIFICIL) {
                continue;
            }

            if (tipoJogador.isInstance(j)) {
                j.setNome(j.getNome() + "(" + (tipoJogador == Heroi.class ? numHerois : numMonstros + 1) + ")");
                if (tipoJogador == Heroi.class) {
                    herois.add((Heroi) j);
                    numHerois++;
                } else {
                    monstros.add((Monstro) j);
                    numMonstros++;
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
        for (Heroi h : herois) {
            logs.adicionarLog(h.toString());
        }
        logs.adicionarLog("===============");
        
        logs.adicionarLog("Monstros:");
        for (Monstro m : monstros) {
            logs.adicionarLog(m.toString());
        }
        logs.adicionarLog("===============\n");
    }

    public void terminarJogo() {
        numHerois = herois.size();
        numMonstros = monstros.size();

        boolean heroisVenceram = !herois.isEmpty();

        logs.adicionarLog("Heróis restantes: " + numHerois);
        logs.adicionarLog("Monstros restantes: " + numMonstros);
        logs.logFimJogo(heroisVenceram);
        logs.exibirLogs();
    }

    public void gerenciarTurnos() {
        Turno turno = new Turno(herois, monstros, this.logs, this.dificuldade);
        int contador = 1;
        final int MAX_TURNOS = 500; // 🚀 Limite de segurança para evitar loops infinitos

        while (!herois.isEmpty() && !monstros.isEmpty() && contador <= MAX_TURNOS) {
            turno.executarTurno(contador);
            contador++;
        }

        if (contador > MAX_TURNOS) {
            logs.adicionarLog("🚨 O jogo excedeu o número máximo de turnos e foi encerrado.");
        }

        this.terminarJogo();
    }

}
