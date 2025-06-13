package jeu;

public class Elephant extends Animal {
    public Elephant(int joueur, int x, int y) {
        super("Elephant", 8, joueur, x, y);
    }

    @Override
    public boolean peutCapturer(Animal autre) {
        if (autre == null) return false;
        // Ne peut pas capturer le rat si le rat sort de l’eau (sera géré ailleurs)
        if (autre.getNom().equals("Rat")) return false;
        return super.peutCapturer(autre);
    }

    @Override
    public String getSymbole() {
        return joueur == 1 ? "elephante" : "ELEPHANTE";
    }
}