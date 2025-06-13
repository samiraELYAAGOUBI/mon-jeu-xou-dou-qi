package jeu;

import java.awt.Color;

public class Case {
    public enum Type {
        NORMALE, LAC, PIEGE, SANCTUAIRE
    }

    private final int x, y;
    private final Type type;
    private Animal animal;

    public Case(int x, int y, Type type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.animal = null;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public Type getType() { return type; }
    public Animal getAnimal() { return animal; }
    public void setAnimal(Animal animal) { this.animal = animal; }
    public boolean estVide() { return animal == null; }

    // Nouvelle méthode pour obtenir la couleur de la case
    public Color getCouleur() {
        switch (type) {
            case LAC: return new Color(0, 100, 255);     // Bleu pour rivière
            case PIEGE: return new Color(0, 200, 0);      // Vert pour piège
            case SANCTUAIRE: return new Color(255, 150, 0); // Orange pour sanctuaire
            default: 
                return (x + y) % 2 == 0 ? 
                    new Color(240, 240, 240) :           // Gris clair
                    new Color(220, 220, 220);            // Gris légèrement plus foncé
        }
    }

    @Override
    public String toString() {
        if (animal != null) return animal.getSymbole();
        return ""; // Retourne une chaîne vide pour les cases spéciales
    }
}