package jeu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import models.Joueur;
import models.Partie;
import dao.PartieDAO;

public class PlateauGUI extends JFrame {
    private Plateau plateau;
    private JPanel[][] casePanels;
    private int selectedX = -1, selectedY = -1;
    private JLabel labelJoueur;
    private JTextArea messageArea;
    private JTextArea historyArea;
    private JTextArea statsArea;
    private ByteArrayOutputStream baos;
    private PrintStream originalOut;
    private List<Partie> historique;

    public PlateauGUI(Plateau plateau, List<Partie> historique) {
        this.plateau = plateau;
        this.historique = historique;
        
        initUI();
    }

    private void initUI() {
        setTitle("Jeu de Jungle - " + plateau.getJoueur1().getNom() + " vs " + plateau.getJoueur2().getNom());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Redirection des messages console
        redirectConsoleOutput();
        
        initComponents();
        
        setSize(1000, 800);
        setLocationRelativeTo(null);
        setVisible(true);
        
        startMessageTimer();
    }

    private void redirectConsoleOutput() {
        originalOut = System.out;
        baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));
    }

    private void startMessageTimer() {
        new Timer(100, e -> updateMessages()).start();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        mainPanel.add(createGameBoardPanel(), BorderLayout.CENTER);
        mainPanel.add(createInfoPanel(), BorderLayout.SOUTH);
        
        add(mainPanel);
    }

    private JPanel createGameBoardPanel() {
        JPanel plateauPanel = new JPanel(new GridLayout(9, 7));
        casePanels = new JPanel[9][7];
        
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 7; j++) {
                casePanels[i][j] = createCasePanel(i, j);
                plateauPanel.add(casePanels[i][j]);
            }
        }
        return plateauPanel;
    }

    private JPanel createCasePanel(int x, int y) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        Case currentCase = plateau.getCase(x, y);
        panel.setBackground(currentCase.getCouleur());
        
        if (currentCase.getAnimal() != null) {
            JLabel label = new JLabel(currentCase.getAnimal().getSymbole(), SwingConstants.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 14));
            panel.add(label, BorderLayout.CENTER);
        }
        
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleCaseClick(x, y);
            }
        });
        
        return panel;
    }

    private JPanel createInfoPanel() {
        JPanel infoPanel = new JPanel(new BorderLayout());
        
        infoPanel.add(createTurnPanel(), BorderLayout.NORTH);
        infoPanel.add(createMessagePanel(), BorderLayout.CENTER);
        infoPanel.add(createHistoryStatsPanel(), BorderLayout.EAST);
        
        return infoPanel;
    }

    private JPanel createTurnPanel() {
        JPanel turnPanel = new JPanel();
        labelJoueur = new JLabel("Tour: " + plateau.getJoueurActuel().getNom());
        turnPanel.add(labelJoueur);
        return turnPanel;
    }

    private JScrollPane createMessagePanel() {
        messageArea = new JTextArea(5, 30);
        messageArea.setEditable(false);
        return new JScrollPane(messageArea);
    }

    private JTabbedPane createHistoryStatsPanel() {
        JTabbedPane tabbedPane = new JTabbedPane();
        
        tabbedPane.addTab("Historique", createHistoryPanel());
        tabbedPane.addTab("Statistiques", createStatsPanel());
        
        return tabbedPane;
    }

    private JPanel createHistoryPanel() {
        JPanel historyPanel = new JPanel(new BorderLayout());
        
        historyArea = new JTextArea(10, 30);
        styleHistoryArea();
        
        historyPanel.add(createFilterPanel(), BorderLayout.NORTH);
        historyPanel.add(new JScrollPane(historyArea), BorderLayout.CENTER);
        
        return historyPanel;
    }

    private void styleHistoryArea() {
        historyArea.setEditable(false);
        historyArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        historyArea.setForeground(new Color(50, 50, 50));
    }

    private JPanel createFilterPanel() {
        JPanel filterPanel = new JPanel();
        JComboBox<String> filterCombo = new JComboBox<>(
            new String[]{"Toutes", "Victoires " + plateau.getJoueur1().getNom(), 
                        "Victoires " + plateau.getJoueur2().getNom(), "Matchs nuls"});
        filterCombo.addActionListener(e -> updateHistory());
        filterPanel.add(new JLabel("Filtrer:"));
        filterPanel.add(filterCombo);
        return filterPanel;
    }

    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel(new BorderLayout());
        
        statsArea = new JTextArea(5, 30);
        styleStatsArea();
        
        statsPanel.add(new JScrollPane(statsArea), BorderLayout.CENTER);
        return statsPanel;
    }

    private void styleStatsArea() {
        statsArea.setEditable(false);
        statsArea.setFont(new Font("Monospaced", Font.BOLD, 12));
        statsArea.setForeground(new Color(0, 100, 0));
    }

    private void updateHistory() {
        historyArea.setText(formatHistory());
        statsArea.setText(formatStats());
    }

    private String formatHistory() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== DERNIÈRES PARTIES ===\n\n");
        
        historique.stream()
            .sorted((p1, p2) -> p2.getDate().compareTo(p1.getDate()))
            .limit(10)
            .forEach(p -> sb.append(formatPartie(p)));
            
        return sb.toString();
    }

    private String formatPartie(Partie p) {
        String resultat = p.getGagnantId() == 0 ? "Match nul" :
                         p.getGagnantId() == plateau.getJoueur1().getId() ? 
                         plateau.getJoueur1().getNom() + " gagne" : 
                         plateau.getJoueur2().getNom() + " gagne";
        
        return String.format("%1$td/%1$tm %1$tH:%1$tM\n%2$s vs %3$s\n%4$s\n\n",
            p.getDate(),
            plateau.getJoueur1().getNom(),
            plateau.getJoueur2().getNom(),
            resultat);
    }

    private String formatStats() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== STATISTIQUES ===\n\n");
        
        int victoiresJ1 = PartieDAO.compterVictoires(plateau.getJoueur1().getId());
        int victoiresJ2 = PartieDAO.compterVictoires(plateau.getJoueur2().getId());
        int totalParties = victoiresJ1 + victoiresJ2;
        
        if (totalParties > 0) {
            int pourcentageJ1 = (victoiresJ1 * 100) / totalParties;
            int pourcentageJ2 = (victoiresJ2 * 100) / totalParties;
            
            sb.append(String.format("%s:\n%d victoires (%d%%)\n\n%s:\n%d victoires (%d%%)",
                plateau.getJoueur1().getNom(), victoiresJ1, pourcentageJ1,
                plateau.getJoueur2().getNom(), victoiresJ2, pourcentageJ2));
        } else {
            sb.append("Aucune partie enregistrée");
        }
        
        return sb.toString();
    }

    private void updateMessages() {
        String newMessages = baos.toString();
        if (!newMessages.isEmpty()) {
            messageArea.append(newMessages);
            baos.reset();
        }
    }

    private void handleCaseClick(int x, int y) {
        if (selectedX == -1) {
            selectPiece(x, y);
        } else {
            movePiece(x, y);
        }
    }

    private void selectPiece(int x, int y) {
        Case caseSelectionnee = plateau.getCase(x, y);
        if (isValidSelection(caseSelectionnee)) {
            selectedX = x;
            selectedY = y;
            casePanels[x][y].setBorder(BorderFactory.createLineBorder(Color.RED, 3));
        }
    }

    private boolean isValidSelection(Case caseSelectionnee) {
        return caseSelectionnee.getAnimal() != null && 
               ((caseSelectionnee.getAnimal().getJoueur() == 1 && plateau.getJoueurActuel() == plateau.getJoueur1()) ||
                (caseSelectionnee.getAnimal().getJoueur() == 2 && plateau.getJoueurActuel() == plateau.getJoueur2()));
    }

    private void movePiece(int x, int y) {
        plateau.deplacerAnimal(selectedX, selectedY, x, y);
        updateUI();
        
        if (plateau.partieTerminee()) {
            endGame();
        }
        
        resetSelection();
    }

    private void endGame() {
        Joueur gagnant = plateau.getGagnant();
        Partie nouvellePartie = createNewGame(gagnant);
        
        if (PartieDAO.enregistrerPartie(nouvellePartie)) {
            historique.add(0, nouvellePartie);
            updateHistory();
        }
        
        showResult(gagnant);
    }

    private Partie createNewGame(Joueur gagnant) {
        return new Partie(
            plateau.getJoueur1().getId(),
            plateau.getJoueur2().getId(),
            gagnant != null ? gagnant.getId() : 0
        );
    }

    private void showResult(Joueur gagnant) {
        String message = buildResultMessage(gagnant);
        JOptionPane.showMessageDialog(this, message, "Fin de partie", JOptionPane.INFORMATION_MESSAGE);
    }

    private String buildResultMessage(Joueur gagnant) {
        StringBuilder sb = new StringBuilder();
        sb.append("Partie terminée!\n");
        sb.append(gagnant != null ? gagnant.getNom() + " a gagné!\n" : "Match nul!\n");
        
        int victoiresJ1 = PartieDAO.compterVictoires(plateau.getJoueur1().getId());
        int victoiresJ2 = PartieDAO.compterVictoires(plateau.getJoueur2().getId());
        int totalParties = victoiresJ1 + victoiresJ2;
        
        if (totalParties > 0) {
            int pourcentageJ1 = (victoiresJ1 * 100) / totalParties;
            int pourcentageJ2 = (victoiresJ2 * 100) / totalParties;
            
            sb.append("\nScore global:\n")
              .append(String.format("%s: %d victoires (%d%%)\n%s: %d victoires (%d%%)",
                  plateau.getJoueur1().getNom(), victoiresJ1, pourcentageJ1,
                  plateau.getJoueur2().getNom(), victoiresJ2, pourcentageJ2));
        }
        
        return sb.toString();
    }

    private void resetSelection() {
        if (selectedX != -1) {
            casePanels[selectedX][selectedY].setBorder(BorderFactory.createLineBorder(Color.BLACK));
            selectedX = -1;
            selectedY = -1;
        }
    }

    public void updateUI() {
        updatePlayerTurn();
        updateBoard();
    }

    private void updatePlayerTurn() {
        labelJoueur.setText("Tour: " + plateau.getJoueurActuel().getNom());
    }

    private void updateBoard() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 7; j++) {
                updateCase(i, j);
            }
        }
    }

    private void updateCase(int i, int j) {
        Case c = plateau.getCase(i, j);
        JPanel panel = casePanels[i][j];
        
        panel.removeAll();
        panel.setBackground(c.getCouleur());
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        if (c.getAnimal() != null) {
            JLabel label = new JLabel(c.getAnimal().getSymbole(), SwingConstants.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 14));
            panel.add(label, BorderLayout.CENTER);
        }
        
        panel.revalidate();
        panel.repaint();
    }
    
    @Override
    public void dispose() {
        System.setOut(originalOut);
        super.dispose();
    }
}