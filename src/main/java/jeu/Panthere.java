package jeu;

public class Panthere extends Animal {
    public Panthere(int joueur, int x, int y) {
        super("Panthere", 5, joueur, x, y);
    }

    @Override
    public String getSymbole() {
        return joueur == 1 ? "panthere" : "PANTHERE";
    }
}