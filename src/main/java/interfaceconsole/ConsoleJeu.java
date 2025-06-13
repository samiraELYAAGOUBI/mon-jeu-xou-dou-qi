package interfaceconsole;

import jeu.Plateau;
import jeu.PlateauGUI;
import models.Joueur;
import dao.PartieDAO;
import models.Partie;
import javax.swing.*;
import java.util.*;
import java.util.List;

public class ConsoleJeu {

    public static void lancerPartie(Joueur joueur1, Joueur joueur2) {
        // Initialisation du plateau avec les joueurs
        Plateau plateau = new Plateau(joueur1, joueur2);

        // Charger l'historique des parties
        List<Partie> historique = PartieDAO.getHistorique();
        
        // Création de l'interface graphique
        SwingUtilities.invokeLater(() -> {
            PlateauGUI gui = new PlateauGUI(plateau, historique); // Correction ici
            configurerFenetre(gui, joueur1, joueur2);
            afficherMessageDebut(gui, joueur1, joueur2);
        });
    }

    private static void configurerFenetre(PlateauGUI gui, Joueur j1, Joueur j2) {
        gui.setTitle(String.format("Jeu de Jungle - %s vs %s", j1.getNom(), j2.getNom()));
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setSize(700, 750);
        gui.setLocationRelativeTo(null);
        gui.setResizable(false);
    }

    private static void afficherMessageDebut(PlateauGUI gui, Joueur j1, Joueur j2) {
        JOptionPane.showMessageDialog(gui,
            "La partie commence!\n\n" +
            "🔵 " + j1.getNom() + " (Joueur 1)\n" +
            "🔴 " + j2.getNom() + " (Joueur 2)\n\n" +
            "Cliquez sur une pièce puis sur sa destination.",
            "Début de partie",
            JOptionPane.INFORMATION_MESSAGE);
    }

    public static void lancerVersionConsole(Joueur joueur1, Joueur joueur2) {
        Plateau plateau = new Plateau(joueur1, joueur2);
        Scanner scanner = new Scanner(System.in);

        while (!plateau.partieTerminee()) {
            plateau.afficher();
            Joueur joueurActuel = plateau.getJoueurActuel();
            System.out.println("\n🔵 Tour de " + joueurActuel.getNom());

            System.out.print("Déplacer depuis (x y): ");
            int x1 = scanner.nextInt();
            int y1 = scanner.nextInt();

            System.out.print("Vers (x y): ");
            int x2 = scanner.nextInt();
            int y2 = scanner.nextInt();

            plateau.deplacerAnimal(x1, y1, x2, y2);
        }

        // Affichage du gagnant avec vérification null
        Joueur gagnant = plateau.getGagnant();
        System.out.println("\n🎉 Partie terminée ! " + 
            (gagnant != null ? gagnant.getNom() + " a gagné !" : "Match nul"));
        scanner.close();
    }
}