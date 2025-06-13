package jeu;

public abstract class Animal {
    protected String nom;
    protected int force;
    protected int joueur; // 1 ou 2
    protected int x, y;

    public Animal(String nom, int force, int joueur, int x, int y) {
        this.nom = nom;
        this.force = force;
        this.joueur = joueur;
        this.x = x;
        this.y = y;
    }

    public int getForce() { return force; }
    public int getJoueur() { return joueur; }
    public String getNom() { return nom; }
    public int getX() { return x; }
    public int getY() { return y; }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean peutCapturer(Animal autre) {
        if (autre == null || autre.joueur == this.joueur) return false;
        return this.force >= autre.force;
    }

    public abstract String getSymbole();
}