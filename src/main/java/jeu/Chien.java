package jeu;

public class Chien extends Animal {
    public Chien(int joueur, int x, int y) {
        super("Chien", 4, joueur, x, y);
    }

    @Override
    public String getSymbole() {
        return joueur == 1 ? "chien" : "CHIEN";
    }
}