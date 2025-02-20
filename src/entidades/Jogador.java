package entidades;

import java.util.Random;
import enums.ResultadoAtaque;
import enums.StatusJogador;
import enums.Dificuldade; // Importação necessária

public abstract class Jogador {
    private String nome;
    double hp;
    private int ataque, defesa, destreza, velocidade;
    private StatusJogador status;

    public Jogador(String nome, int hp, int ataque, int defesa, int destreza, int velocidade) {
        setNome(nome);
        setHp(hp);
        setAtaque(ataque);
        setDefesa(defesa);
        setDestreza(destreza);
        setVelocidade(velocidade);
        setStatus(StatusJogador.NORMAL);
    }

    // Getters e Setters
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public double getHp() {
        return hp;
    }
    public void setHp(double d) {
        this.hp = d;
    }
    public int getAtaque() {
        return ataque;
    }
    public void setAtaque(int ataque) {
        this.ataque = ataque;
    }
    public int getDefesa() {
        return defesa;
    }
    public void setDefesa(int defesa) {
        this.defesa = defesa;
    }
    public int getDestreza() {
        return destreza;
    }
    public void setDestreza(int destreza) {
        this.destreza = destreza;
    }
    public int getVelocidade() {
        return velocidade;
    }
    public void setVelocidade(int velocidade) {
        this.velocidade = velocidade;
    }
    public StatusJogador getStatus() {
        return status;
    }
    public void setStatus(StatusJogador status) {
        this.status = status;
    }

    public boolean estaVivo() {
        return this.hp > 0;
    }

    public abstract ResultadoAtaque realizarAtaque(Jogador alvo);

    /**
     * Método que ajusta os atributos dos monstros conforme a dificuldade do jogo.
     */
    public void ajustarAtributosPorDificuldade(Dificuldade dificuldade) {
        if (this instanceof Monstro) { // Apenas monstros são afetados pela dificuldade
            switch (dificuldade) {
                case FACIL:
                    this.hp *= 0.7;      // Monstros com menos vida
                    this.ataque *= 0.7;  // Ataque reduzido
                    this.defesa *= 0.7;  // Defesa mais baixa
                    break;
                case MEDIO:
                    // Monstros mantêm os atributos normais
                    break;
                case DIFICIL:
                    this.hp *= 1.3;      // Mais vida
                    this.ataque *= 1.3;  // Ataque mais forte
                    this.defesa *= 1.3;  // Defesa maior
                    break;
            }
        }
    }

    /**
     * Método para gerar um jogador aleatório e ajustar os atributos se for um monstro.
     */
    public static Jogador gerarJogadorAleatorio(Dificuldade dificuldade) {
        Random random = new Random();
        String[] nomeHerois = {"Guerreiro", "Mago", "Arqueiro"};
        
        // Controle de porcentagem para garantir equilíbrio nos monstros
        double probGoblin = 0.0, probLobisomem = 0.0, probDragao = 0.0;

        switch (dificuldade) {
            case FACIL:
                probGoblin = 1.0; // Apenas Goblins (100%)
                break;
            case MEDIO:
                probGoblin = 0.5;  // 50% Goblins
                probLobisomem = 0.5;  // 50% Lobisomens
                break;
            case DIFICIL:
            default:
                probGoblin = 0.5;  // 40% Goblins
                probLobisomem = 0.4;  // 40% Lobisomens
                probDragao = 0.1;  // 20% Dragões
                break;
        }

        boolean isHeroi = random.nextBoolean();
        String nome;

        if (isHeroi) {
            nome = nomeHerois[random.nextInt(nomeHerois.length)];
        } else {
            double escolha = random.nextDouble(); // Gera um número entre 0 e 1 para decidir o monstro
            if (escolha < probGoblin) {
                nome = "Goblin";
            } else if (escolha < probGoblin + probLobisomem) {
                nome = "Lobisomem";
            } else {
                nome = "Dragão";
            }
        }

        Jogador jogadorCriado = null;

        // Definição de atributos fixos conforme a classe
        switch (nome) {
            case "Guerreiro":
                jogadorCriado = new Guerreiro(nome, 120, 30, 25, 10, 20, random.nextInt(25) + 1, random.nextInt(20) + 1);
                break;
            case "Mago":
                jogadorCriado = new Mago(nome, 80, 40, 10, 25, 15, random.nextInt(50) + 30);
                break;
            case "Arqueiro":
                jogadorCriado = new Arqueiro(nome, 90, 35, 15, 30, 25, random.nextInt(100) + 1, random.nextInt(20) + 1);
                break;
            case "Goblin":
                jogadorCriado = new Goblin(nome, 50, 15, 5, 20, 30, random.nextInt(5) + 1);
                break;
            case "Lobisomem":
                jogadorCriado = new Lobisomem(nome, 120, 35, 20, 30, 35, random.nextInt(20) + 1, random.nextInt(15) + 1);
                break;
            case "Dragão":
                jogadorCriado = new Dragao(nome, 200, 50, 40, 10, 10);
                break;
        }

        // Se for monstro, ajusta atributos conforme a dificuldade
        if (jogadorCriado instanceof Monstro) {
            jogadorCriado.ajustarAtributosPorDificuldade(dificuldade);
        }

        return jogadorCriado;
    }

    public static Jogador gerarJogadorEspecifico(Class<? extends Jogador> tipo, Dificuldade dificuldade) {
        Random random = new Random();

        if (tipo == Dragao.class) {
            // Criando um Dragão com atributos ajustados à dificuldade
            int hpBase = 200, ataqueBase = 50, defesaBase = 40, destrezaBase = 10, velocidadeBase = 10;

            switch (dificuldade) {
                case FACIL:
                    hpBase *= 0.7;
                    ataqueBase *= 0.7;
                    defesaBase *= 0.7;
                    break;
                case MEDIO:
                    // Atributos padrão
                    break;
                case DIFICIL:
                    hpBase *= 1.3;
                    ataqueBase *= 1.3;
                    defesaBase *= 1.3;
                    break;
            }

            return new Dragao("Dragão", hpBase, ataqueBase, defesaBase, destrezaBase, velocidadeBase);
        }

        throw new IllegalArgumentException("Tipo de jogador não suportado: " + tipo.getSimpleName());
    }



    @Override
    public String toString() {
        return this.getNome() +
                " | HP: " + this.getHp() +
                " | ATAQUE: " + this.getAtaque() +
                " | DEFESA: " + this.getDefesa() +
                " | DESTREZA: " + this.getDestreza() +
                " | VELOCIDADE: " + this.getVelocidade();
    }
}
