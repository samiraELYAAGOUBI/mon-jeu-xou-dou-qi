package jeu;

public class Rat extends Animal {
    public Rat(int joueur, int x, int y) {
        super("Rat", 1, joueur, x, y);
    }

    @Override
    public boolean peutCapturer(Animal autre) {
        if (autre == null) return false;
        // Le rat peut capturer l'éléphant
        if (autre.getNom().equals("Elephant")) return true;
        return super.peutCapturer(autre);
    }

    @Override
    public String getSymbole() {
        return joueur == 1 ? "rat" : "RAT";
    }
}