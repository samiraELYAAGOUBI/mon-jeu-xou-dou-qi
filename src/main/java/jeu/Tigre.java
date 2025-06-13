package jeu;

public class Tigre extends Animal {
    public Tigre(int joueur, int x, int y) {
        super("Tigre", 6, joueur, x, y);
    }

    @Override
    public String getSymbole() {
        return joueur == 1 ? "tigre" : "TIGRE";
    }
}