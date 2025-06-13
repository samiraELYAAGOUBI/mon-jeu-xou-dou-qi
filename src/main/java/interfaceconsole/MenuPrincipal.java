package interfaceconsole;

import joueur.GestionJoueurs;
import models.Joueur;
import jeu.Plateau;
import jeu.PlateauGUI;
import dao.PartieDAO;
import models.Partie;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class MenuPrincipal {

    public static void lancer() {
        // Création de la fenêtre principale
        JFrame frame = new JFrame("XOU DOU QI - Menu Principal");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        // Panel principal avec image de fond
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(240, 240, 240));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Titre
        JLabel titleLabel = new JLabel("XOU DOU QI", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Bouton Commencer
        JButton startButton = new JButton("Commencer une nouvelle partie");
        startButton.setFont(new Font("Arial", Font.PLAIN, 18));
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setMaximumSize(new Dimension(300, 50));
        startButton.addActionListener(e -> {
            frame.dispose();
            demarrerPartie();
        });

        mainPanel.add(startButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Bouton Quitter
        JButton quitButton = new JButton("Quitter");
        quitButton.setFont(new Font("Arial", Font.PLAIN, 18));
        quitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        quitButton.setMaximumSize(new Dimension(300, 50));
        quitButton.addActionListener(e -> System.exit(0));

        mainPanel.add(quitButton);

        frame.add(mainPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private static void demarrerPartie() {
        // Fenêtre pour le joueur 1
        Joueur joueur1 = demanderJoueur("Joueur 1");
        if (joueur1 == null) return;

        // Obtenir un joueur 2 valide (nom différent de joueur1)
        Joueur joueur2 = obtenirJoueur2Valide(joueur1);
        if (joueur2 == null) return;

        // Charger l'historique des parties
        List<Partie> historique = PartieDAO.getHistorique();

        // Lancer la partie avec l'interface graphique
        SwingUtilities.invokeLater(() -> {
            Plateau plateau = new Plateau(joueur1, joueur2);
            PlateauGUI gui = new PlateauGUI(plateau, historique);
            gui.setTitle("XOU DOU QI - " + joueur1.getNom() + " vs " + joueur2.getNom());
            gui.setLocationRelativeTo(null);
        });
    }

    private static Joueur obtenirJoueur2Valide(Joueur joueur1) {
        while (true) {
            Joueur joueur2 = demanderJoueur("Joueur 2");
            if (joueur2 == null) {
                return null; // L'utilisateur a annulé
            }
            
            if (!joueur2.getNom().equals(joueur1.getNom())) {
                return joueur2; // Nom valide
            }
            
            // Afficher message d'erreur si même nom
            JOptionPane.showMessageDialog(null,
                "Ce joueur est déjà connecté. Veuillez en choisir un autre.",
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private static Joueur demanderJoueur(String titre) {
        JPanel panel = new JPanel(new GridLayout(3, 1, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel label = new JLabel(titre + " :");
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        panel.add(label);
        panel.add(new JLabel("Nom d'utilisateur:"));
        panel.add(usernameField);
        panel.add(new JLabel("Mot de passe:"));
        panel.add(passwordField);

        int option = JOptionPane.showConfirmDialog(null, panel, 
            "Connexion - " + titre,
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            String nom = usernameField.getText();
            String mdp = new String(passwordField.getPassword());
            
            if (nom.isEmpty() || mdp.isEmpty()) {
                JOptionPane.showMessageDialog(null,
                    "Nom d'utilisateur et mot de passe requis!",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
                return demanderJoueur(titre);
            }

            // Appel correct à la méthode avec les paramètres
            Joueur joueur = GestionJoueurs.demanderConnexionOuInscription();
            if (joueur == null) {
                JOptionPane.showMessageDialog(null,
                    "Échec de la connexion/inscription",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
                return demanderJoueur(titre);
            }
            return joueur;
        }
        return null;
    }
}