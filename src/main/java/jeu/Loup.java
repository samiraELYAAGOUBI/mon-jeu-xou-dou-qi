package jeu;

public class Loup extends Animal {
    public Loup(int joueur, int x, int y) {
        super("Loup", 3, joueur, x, y);
    }

    @Override
    public String getSymbole() {
        return joueur == 1 ? "loup" : "LOUP";
    }
}