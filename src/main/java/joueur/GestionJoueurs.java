package joueur;

import dao.JoueurDAO;
import models.Joueur;
import javax.swing.*;
import java.awt.*;
import java.util.Scanner;

public class GestionJoueurs {

    private static Joueur connectedPlayer; // Field to store the connected player

    public static Joueur demanderConnexionOuInscription() {
        // Création de la fenêtre
        JDialog dialog = new JDialog();
        dialog.setTitle("Connexion / Inscription");
        dialog.setModal(true);
        dialog.setSize(400, 300);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(null);

        // Panel principal
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Composants
        JLabel titleLabel = new JLabel("XOU DOU QI - Connexion", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JTabbedPane tabbedPane = new JTabbedPane();

        // Panel Connexion
        JPanel loginPanel = createLoginPanel(dialog);
        tabbedPane.addTab("Connexion", loginPanel);

        // Panel Inscription
        JPanel registerPanel = createRegisterPanel(dialog);
        tabbedPane.addTab("Inscription", registerPanel);

        // Ajout au panel principal
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        gbc.gridy = 1;
        mainPanel.add(tabbedPane, gbc);

        dialog.add(mainPanel);
        dialog.setVisible(true);

        // Retourne le joueur connecté ou null si annulé
        return connectedPlayer;
    }

    private static JPanel createLoginPanel(JDialog parent) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField usernameField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);
        JButton loginButton = new JButton("Se connecter");

        // Positionnement
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Nom d'utilisateur:"), gbc);

        gbc.gridy = 1;
        panel.add(usernameField, gbc);

        gbc.gridy = 2;
        panel.add(new JLabel("Mot de passe:"), gbc);

        gbc.gridy = 3;
        panel.add(passwordField, gbc);

        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(loginButton, gbc);

        // Action du bouton
        loginButton.addActionListener(e -> {
            String nom = usernameField.getText();
            String mdp = new String(passwordField.getPassword());

            if (nom.isEmpty() || mdp.isEmpty()) {
                JOptionPane.showMessageDialog(parent,
                    "Veuillez remplir tous les champs",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            Joueur joueur = JoueurDAO.connecter(nom, mdp);
            if (joueur != null) {
                connectedPlayer = joueur;
                parent.dispose();
            } else {
                JOptionPane.showMessageDialog(parent,
                    "Nom ou mot de passe incorrect",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    private static JPanel createRegisterPanel(JDialog parent) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField usernameField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);
        JButton registerButton = new JButton("S'inscrire");

        // Positionnement
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Nom d'utilisateur:"), gbc);

        gbc.gridy = 1;
        panel.add(usernameField, gbc);

        gbc.gridy = 2;
        panel.add(new JLabel("Mot de passe:"), gbc);

        gbc.gridy = 3;
        panel.add(passwordField, gbc);

        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(registerButton, gbc);

        // Action du bouton
        registerButton.addActionListener(e -> {
            String nom = usernameField.getText();
            String mdp = new String(passwordField.getPassword());

            if (nom.isEmpty() || mdp.isEmpty()) {
                JOptionPane.showMessageDialog(parent,
                    "Veuillez remplir tous les champs",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            Joueur joueur = new Joueur(nom, mdp);
            boolean success = JoueurDAO.inscrire(joueur);

            if (success) {
                connectedPlayer = JoueurDAO.connecter(nom, mdp);
                parent.dispose();
                JOptionPane.showMessageDialog(parent,
                    "Compte créé avec succès!",
                    "Succès",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(parent,
                    "Ce nom d'utilisateur est déjà pris",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    // Méthode utilitaire pour la version console (au cas où)
    public static Joueur demanderConnexionOuInscriptionConsole() {
        Scanner scanner = new Scanner(System.in);
        JoueurDAO.creerTable();

        while (true) {
            System.out.println("=== MENU ===");
            System.out.println("1. Créer un compte");
            System.out.println("2. Se connecter");
            System.out.print("Choix : ");
            String choix = scanner.nextLine();

            System.out.print("Nom : ");
            String nom = scanner.nextLine();

            System.out.print("Mot de passe : ");
            String mdp = scanner.nextLine();

            if (choix.equals("1")) {
                Joueur joueur = new Joueur(nom, mdp);
                boolean ok = JoueurDAO.inscrire(joueur);
                if (ok) {
                    System.out.println("✅ Compte créé.");
                    return JoueurDAO.connecter(nom, mdp);
                } else {
                    System.out.println("❌ Nom déjà utilisé.");
                }
            } else if (choix.equals("2")) {
                Joueur joueur = JoueurDAO.connecter(nom, mdp);
                if (joueur != null) {
                    System.out.println("✅ Bienvenue " + joueur.getNom());
                    return joueur;
                } else {
                    System.out.println("❌ Nom ou mot de passe incorrect.");
                }
            } else {
                System.out.println("Choix invalide.");
            }
        }
    }
}