package jogo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import entidades.Dragao;
import entidades.Heroi;
import entidades.Jogador;
import entidades.Monstro;
import enums.Dificuldade;
import enums.ResultadoAtaque;
import enums.StatusJogador;

public class Turno {
    private Log log;
    private Dificuldade dificuldade = null;
    
    public Turno(List<Heroi> herois, List<Monstro> monstros, Log log, Dificuldade dificuldade) {
        setLog(log);
        this.dificuldade = dificuldade;
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
        System.out.println(personagens);
        System.out.println("====================================");
        System.out.println("TURNO " + contador);
        System.out.println("------------------");
        
        for (Jogador atacante : personagens) {
            Jogador alvo = escolherAlvo(atacante);
            
            if (!atacante.estaVivo()) {
                continue; // Se o atacante morreu durante o turno e ainda não tiver atacado, ele não pode atacar
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
            } else {
                // Realiza o ataque
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
        } else if (j instanceof Monstro && !j.estaVivo()) {
            System.out.println(j.getNome() + " morreu!");
            Jogo.getMonstros().remove(j);
            Jogo.setNumMonstros(Jogo.getNumMonstros() - 1);
        }
    }
    
    private Jogador escolherAlvo(Jogador atacante) {
        if (atacante instanceof Heroi) {
            // Heróis ainda escolhem sempre o monstro com menor HP
            if (Jogo.getMonstros().isEmpty()) {
                System.out.println("Erro: Não há monstros disponíveis como alvo.");
                return null;
            }
            return Jogo.getMonstros().stream()
                .filter(Monstro::estaVivo)
                .min((m1, m2) -> Double.compare(m1.getHp(), m2.getHp())) // Menor HP
                .orElse(null);
        } else {
            // Ordena os heróis por posição (Guerreiro -> Arqueiro -> Mago) e menor HP dentro da classe
            List<Heroi> heroisOrdenados = Jogo.getHerois().stream()
                .filter(Heroi::estaVivo)
                .sorted((h1, h2) -> {
                    // Primeiro ordena por posição (Guerreiro -> Arqueiro -> Mago)
                    int posicaoComparacao = Integer.compare(h1.getPosicao(), h2.getPosicao());
                    if (posicaoComparacao != 0) return posicaoComparacao;

                    // Depois ordena por menor HP dentro da classe
                    return Double.compare(h1.getHp(), h2.getHp());
                })
                .toList(); // Converte para lista ordenada

            if (heroisOrdenados.isEmpty()) {
                System.out.println("Erro: Não há heróis disponíveis como alvo.");
                return null;
            }

            // No modo fácil, escolhe aleatoriamente um herói da lista ordenada
            if (dificuldade == Dificuldade.FACIL) {
                Random rand = new Random();
                return heroisOrdenados.get(rand.nextInt(heroisOrdenados.size()));
            }

            // No modo médio, 50% de chance de atacar um herói de menor HP ou um aleatório (dano -15% se for aleatório)
            if (dificuldade == Dificuldade.MEDIO) {
                Random rand = new Random();
                if (rand.nextInt(100) < 70) {
                    return heroisOrdenados.get(0); // Escolhe o herói prioritário (classe mais forte com menor HP)
                } else {
                    return heroisOrdenados.get(rand.nextInt(heroisOrdenados.size())); // Escolhe um herói aleatório
                }
            }

            // No modo difícil, sempre ataca o primeiro da lista (classe prioritária com menor HP)
            Jogador alvo = heroisOrdenados.get(0);
            
            // Verifica se o alvo é um Dragão e se ele está voando
            if (alvo instanceof Dragao && ((Dragao) alvo).getVoo()) {
                // Se o Dragão estiver voando, muda para o próximo alvo
                System.out.println(atacante.getNome() + " vê que " + alvo.getNome() + " está voando e muda de alvo.");
                // Tenta escolher outro alvo se o atual for um Dragão voando
                for (Heroi heroi : heroisOrdenados) {
                    if (heroi != alvo) {  // Ignora o Dragão voando
                        return heroi;
                    }
                }
            }

            return alvo; // Retorna o alvo final
        }
    }



}
