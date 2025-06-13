package models;

public class Joueur {
    private int id;
    private String nom;
    private String motDePasse;

    public Joueur(String nom, String motDePasse) {
        this.nom = nom;
        this.motDePasse = motDePasse;
    }

    public Joueur(int id, String nom, String motDePasse) {
        this.id = id;
        this.nom = nom;
        this.motDePasse = motDePasse;
    }
    public void setId(int id) { this.id=id;}
    public int getId() { return id; }
    public String getNom() { return nom; }
    public String getMotDePasse() { return motDePasse; }
}