package models;

import java.time.LocalDateTime;

public class Partie {
    private int id;
    private int joueur1Id;
    private int joueur2Id;
    private int gagnantId; // 0 si match nul
    private LocalDateTime date;

    public Partie(int joueur1Id, int joueur2Id, int gagnantId) {
        this.joueur1Id = joueur1Id;
        this.joueur2Id = joueur2Id;
        this.gagnantId = gagnantId;
        this.date = LocalDateTime.now();
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getJoueur1Id() { return joueur1Id; }
    public int getJoueur2Id() { return joueur2Id; }
    public int getGagnantId() { return gagnantId; }
    public LocalDateTime getDate() { return date; }

    public String getResultat() {
        if (gagnantId == 0) return "Match nul";
        return "Joueur " + gagnantId + " a gagné";
    }
}