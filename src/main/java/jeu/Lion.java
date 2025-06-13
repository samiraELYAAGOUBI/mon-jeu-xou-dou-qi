package jeu;

public class Lion extends Animal {
    public Lion(int joueur, int x, int y) {
        super("Lion", 7, joueur, x, y);
    }

    @Override
    public String getSymbole() {
        return joueur == 1 ? "lion" : "LION";
    }
}