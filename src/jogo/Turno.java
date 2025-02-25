package jogo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import entidades.Dragao;
import entidades.Heroi;
import entidades.Jogador;
import entidades.Monstro;
import enums.Dificuldade;
import enums.StatusJogador;

public class Turno {
    private List<Heroi> herois;
    private List<Monstro> monstros;
    private Log log;
    private Dificuldade dificuldade;

    public Turno(List<Heroi> herois, List<Monstro> monstros, Log log, Dificuldade dificuldade) {
        this.herois = herois;
        this.monstros = monstros;
        this.log = log;
        this.dificuldade = dificuldade;
    }

    private List<Jogador> determinarOrdem() {    
        List<Jogador> personagens = new ArrayList<>();
        personagens.addAll(herois);
        personagens.addAll(monstros);
        personagens.sort((j1, j2) -> Integer.compare(j2.getVelocidade(), j1.getVelocidade()));
        return personagens;
    }
    
    public void executarTurno(int contador) {
        List<Jogador> personagens = determinarOrdem();
        log.logOrdemTurno(contador);

        List<Jogador> personagensRemovidos = new ArrayList<>();

        for (Jogador atacante : personagens) {
            if (!atacante.estaVivo()) {
                continue;
            }

            Jogador alvo = escolherAlvo(atacante);
            if (alvo == null) {
                log.adicionarLog(atacante.getNome() + " não encontrou um alvo válido e não atacará neste turno.");
                continue;
            }

            if (alvo.getStatus() == StatusJogador.VOANDO) {
                log.adicionarLog(alvo.getNome() + " está voando, não é possível atacá-lo!");
            } else {
                atacante.realizarAtaque(alvo);

                if (!alvo.estaVivo()) {
                    personagensRemovidos.add(alvo);
                } else {
                    verificarEnvenenado(alvo);
                    verificarQueimando(alvo);
                }
            }

            if (atacante.getStatus() == StatusJogador.VOANDO) {
                ((Dragao) atacante).setVoo(false);
            } else {
                verificarEnvenenado(atacante);
                verificarQueimando(atacante);
            }
        }

        // Remover os personagens mortos após o turno
        for (Jogador j : personagensRemovidos) {
            removerPersonagem(j);
        }
    }

    private void verificarEnvenenado(Jogador j) {
        if (j.getStatus() == StatusJogador.ENVENENADO) {
            j.setHp(j.getHp() - 10);
            log.logDanoStatus(j.getNome(), "envenenamento", 10);
            if (!j.estaVivo()) removerPersonagem(j);
        }
    }
    
    private void verificarQueimando(Jogador j) {
        if (j.getStatus() == StatusJogador.QUEIMANDO) {
            j.setHp(j.getHp() - 15);
            log.logDanoStatus(j.getNome(), "queimadura", 15);
            if (!j.estaVivo()) removerPersonagem(j);
        }
    }

    private void removerPersonagem(Jogador j) {
        if (j instanceof Heroi) {
            herois.remove(j);
        } else if (j instanceof Monstro) {
            monstros.remove(j);
        }
        log.logMorte(j.getNome());
    }

    private Jogador escolherAlvo(Jogador atacante) {
        if (atacante instanceof Heroi) {
            if (monstros.isEmpty()) {
                log.adicionarLog("Erro: Não há monstros disponíveis como alvo.");
                return null;
            }
            return monstros.stream()
                .filter(Monstro::estaVivo)
                .min((m1, m2) -> Double.compare(m1.getHp(), m2.getHp()))
                .orElse(null);
        } else {
            List<Heroi> heroisOrdenados = herois.stream()
                .filter(Heroi::estaVivo)
                .sorted((h1, h2) -> {
                    int posicaoComparacao = Integer.compare(h1.getPosicao(), h2.getPosicao());
                    if (posicaoComparacao != 0) return posicaoComparacao;
                    return Double.compare(h1.getHp(), h2.getHp());
                })
                .toList();

            if (heroisOrdenados.isEmpty()) {
                log.adicionarLog("Erro: Não há heróis disponíveis como alvo.");
                return null;
            }

            if (dificuldade == Dificuldade.FACIL) {
                Random rand = new Random();
                return heroisOrdenados.get(rand.nextInt(heroisOrdenados.size()));
            }

            if (dificuldade == Dificuldade.MEDIO) {
                Random rand = new Random();
                if (rand.nextInt(100) < 70) {
                    return heroisOrdenados.get(0);
                } else {
                    return heroisOrdenados.get(rand.nextInt(heroisOrdenados.size()));
                }
            }

            Jogador alvo = heroisOrdenados.get(0);

            if (alvo instanceof Dragao && ((Dragao) alvo).getVoo()) {
                log.logMudancaAlvo(atacante.getNome(), "outro herói", "o alvo está voando");
                for (Heroi heroi : heroisOrdenados) {
                    if (heroi != alvo) {
                        return heroi;
                    }
                }
            }

            return alvo;
        }
    }
}
