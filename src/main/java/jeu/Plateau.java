package jeu;

import models.Joueur;

public class Plateau {
    private Case[][] grille;  // grille[0][0] en bas à gauche
    private Joueur joueur1;   // Joueur en bas
    private Joueur joueur2;   // Joueur en haut
    private Joueur joueurActuel;

    // Hiérarchie des pièces
    public enum Hierarchie {
        ELEPHANT(8), LION(7), TIGRE(6), PANTHERE(5), 
        CHIEN(4), LOUP(3), CHAT(2), RAT(1);

        private final int force;
        Hierarchie(int force) { this.force = force; }
        public int getForce() { return force; }
    }
    public Joueur getJoueur1() {
        return joueur1;
    }

    public Joueur getJoueur2() {
        return joueur2;
    }
    public Plateau(Joueur j1, Joueur j2) {
        this.joueur1 = j1;
        this.joueur2 = j2;
        this.joueurActuel = j1;
        grille = new Case[9][7];
        initialiserCases();
        placerAnimaux();
    }

    private void initialiserCases() {
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 7; x++) {
                Case.Type type = Case.Type.NORMALE;

                // Lacs (rivières)
                if ((y >= 3 && y <= 5) && (x == 1 || x == 2 || x == 4 || x == 5)) {
                    type = Case.Type.LAC;
                }

                // Pièges joueur 1 (en bas)
                if ((y == 0 && x == 2) || (y == 0 && x == 4) || (y == 1 && x == 3)) {
                    type = Case.Type.PIEGE;
                }

                // Pièges joueur 2 (en haut)
                if ((y == 8 && x == 2) || (y == 8 && x == 4) || (y == 7 && x == 3)) {
                    type = Case.Type.PIEGE;
                }

                // Sanctuaires
                if ((y == 0 && x == 3) || (y == 8 && x == 3)) {
                    type = Case.Type.SANCTUAIRE;
                }

                grille[y][x] = new Case(y, x, type);
            }
        }
    }

    private void placerAnimaux() {
        // Joueur 1 (en bas)
        grille[0][0].setAnimal(new Lion(1, 0, 0));
        grille[0][6].setAnimal(new Tigre(1, 0, 6));
        grille[1][1].setAnimal(new Chien(1, 1, 1));
        grille[1][5].setAnimal(new Chat(1, 1, 5));
        grille[2][0].setAnimal(new Rat(1, 2, 0));
        grille[2][2].setAnimal(new Panthere(1, 2, 2));
        grille[2][4].setAnimal(new Loup(1, 2, 4));
        grille[2][6].setAnimal(new Elephant(1, 2, 6));

        // Joueur 2 (en haut)
        grille[8][0].setAnimal(new Tigre(2, 8, 0));
        grille[8][6].setAnimal(new Lion(2, 8, 6));
        grille[7][1].setAnimal(new Chat(2, 7, 1));
        grille[7][5].setAnimal(new Chien(2, 7, 5));
        grille[6][0].setAnimal(new Elephant(2, 6, 0));
        grille[6][2].setAnimal(new Loup(2, 6, 2));
        grille[6][4].setAnimal(new Panthere(2, 6, 4));
        grille[6][6].setAnimal(new Rat(2, 6, 6));
    }

    public void changerJoueur() {
        joueurActuel = (joueurActuel == joueur1) ? joueur2 : joueur1;
    }

    public Joueur getJoueurActuel() {
        return joueurActuel;
    }

    public boolean partieTerminee() {
        // Vérifie si un joueur a capturé le sanctuaire adverse
        Case sanctuaireJ1 = grille[0][3]; // Sanctuaire joueur 1 (en bas)
        Case sanctuaireJ2 = grille[8][3]; // Sanctuaire joueur 2 (en haut)

        if (sanctuaireJ1.getAnimal() != null && sanctuaireJ1.getAnimal().getJoueur() == 2) {
            return true; // Joueur 2 a capturé le sanctuaire de joueur 1
        }

        if (sanctuaireJ2.getAnimal() != null && sanctuaireJ2.getAnimal().getJoueur() == 1) {
            return true; // Joueur 1 a capturé le sanctuaire de joueur 2
        }

        return false;
    }

    public Joueur getGagnant() {
        Case sanctuaireJ1 = grille[0][3];
        Case sanctuaireJ2 = grille[8][3];

        if (sanctuaireJ1.getAnimal() != null && sanctuaireJ1.getAnimal().getJoueur() == 2) {
            return joueur2;
        }

        if (sanctuaireJ2.getAnimal() != null && sanctuaireJ2.getAnimal().getJoueur() == 1) {
            return joueur1;
        }

        return null; // Partie non terminée
    }

    public void afficher() {
        System.out.println("=== Plateau de jeu (0,0 en bas gauche) ===");
        // Affiche de haut en bas pour correspondre à l'image
        for (int y = 8; y >= 0; y--) {
            for (int x = 0; x < 7; x++) {
                System.out.print(grille[y][x].toString() + " ");
            }
            System.out.println();
        }
    }

    public Case getCase(int x, int y) {
        if (x < 0 || x >= 9 || y < 0 || y >= 7) return null;
        return grille[x][y];
    }

    public void deplacerAnimal(int xDepart, int yDepart, int xArrivee, int yArrivee) {
        Case depart = getCase(xDepart, yDepart);
        Case arrivee = getCase(xArrivee, yArrivee);

        // Validations de base
        if (depart == null || arrivee == null) {
            System.out.println("Déplacement hors du plateau.");
            return;
        }

        Animal animal = depart.getAnimal();
        if (animal == null) {
            System.out.println("Aucun animal à déplacer.");
            return;
        }

        // Vérification tour du joueur
        if ((animal.getJoueur() == 1 && joueurActuel != joueur1) || 
            (animal.getJoueur() == 2 && joueurActuel != joueur2)) {
            System.out.println("Ce n'est pas votre tour!");
            return;
        }

        // Règles spéciales
        if (!validerDeplacement(animal, depart, arrivee)) {
            return;
        }

        // Exécution du déplacement
        arrivee.setAnimal(animal);
        depart.setAnimal(null);
        animal.setPosition(xArrivee, yArrivee);
        
        changerJoueur();
    }

    private boolean validerDeplacement(Animal animal, Case depart, Case arrivee) {
        // 1. Vérification déplacement en ligne droite
        if (!(depart.getX() == arrivee.getX() || depart.getY() == arrivee.getY())) {
            System.out.println("Déplacement diagonal interdit!");
            return false;
        }

        // 2. Vérification distance (1 case sauf pour Lion/Tigre sautant la rivière)
        int deltaX = Math.abs(arrivee.getX() - depart.getX());
        int deltaY = Math.abs(arrivee.getY() - depart.getY());
        
        if ((deltaX > 1 || deltaY > 1) && !(animal instanceof Lion || animal instanceof Tigre)) {
            System.out.println("Déplacement d'une case seulement!");
            return false;
        }

        // 3. Vérification rivière
        if (arrivee.getType() == Case.Type.LAC && !(animal instanceof Rat)) {
            System.out.println("Seul le rat peut nager!");
            return false;
        }

        // 4. Vérification sanctuaire ami
        if (arrivee.getType() == Case.Type.SANCTUAIRE && 
            ((animal.getJoueur() == 1 && arrivee.getY() == 8 && arrivee.getX() == 3) || 
             (animal.getJoueur() == 2 && arrivee.getY() == 0 && arrivee.getX() == 3))) {
            System.out.println("Vous ne pouvez pas entrer dans votre propre sanctuaire!");
            return false;
        }

        // 5. Vérification piège
        if (estDansPiègeAdverse(animal, arrivee)) {
            System.out.println("Votre animal est affaibli dans le piège adverse!");
        }

        // 6. Vérification capture
        if (!arrivee.estVide()) {
            return validerCapture(animal, arrivee.getAnimal());
        }

        // 7. Vérification saut rivière pour Lion/Tigre
        if ((animal instanceof Lion || animal instanceof Tigre) && 
            traverseRiviere(depart, arrivee)) {
            return validerSautRiviere(depart, arrivee);
        }

        return true;
    }

    private boolean traverseRiviere(Case depart, Case arrivee) {
        // Vérifie si le déplacement traverse une rivière
        // Note: x = ligne (0-8), y = colonne (0-6)
        int minCol = Math.min(depart.getY(), arrivee.getY());
        int maxCol = Math.max(depart.getY(), arrivee.getY());
        int minRow = Math.min(depart.getX(), arrivee.getX());
        int maxRow = Math.max(depart.getX(), arrivee.getX());

        for (int row = minRow; row <= maxRow; row++) {
            for (int col = minCol; col <= maxCol; col++) {
                if (row >= 0 && row < grille.length && 
                    col >= 0 && col < grille[0].length) {
                    if (grille[row][col].getType() == Case.Type.LAC) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean validerSautRiviere(Case depart, Case arrivee) {
        // Note: x = ligne (0-8), y = colonne (0-6)
        int startCol = depart.getY();
        int startRow = depart.getX();
        int endCol = arrivee.getY();
        int endRow = arrivee.getX();

        int colStep = Integer.compare(endCol, startCol);
        int rowStep = Integer.compare(endRow, startRow);

        int col = startCol + colStep;
        int row = startRow + rowStep;

        while (col != endCol || row != endRow) {
            if (row >= 0 && row < grille.length && 
                col >= 0 && col < grille[0].length) {
                Case current = grille[row][col];
                if (current.getType() == Case.Type.LAC && current.getAnimal() instanceof Rat) {
                    System.out.println("Saut impossible: un rat bloque le passage!");
                    return false;
                }
            }
            col += colStep;
            row += rowStep;
        }

        return true;
    }

    private boolean validerCapture(Animal attaquant, Animal defenseur) {
        // 1. Même joueur
        if (attaquant.getJoueur() == defenseur.getJoueur()) {
            System.out.println("Vous ne pouvez pas capturer vos propres pièces!");
            return false;
        }

        // 2. Piège adverse - le défenseur est affaibli
        Case caseDefenseur = getCase(defenseur.getX(), defenseur.getY());
        if (estDansPiègeAdverse(defenseur, caseDefenseur)) {
            return true; // Peut capturer n'importe quelle pièce dans piège adverse
        }

        // 3. Règles spéciales Rat vs Elephant
        if ((attaquant instanceof Rat && defenseur instanceof Elephant) ||
            (defenseur instanceof Rat && attaquant instanceof Elephant)) {
            System.out.println("Le rat peut capturer l'éléphant et vice versa!");
            return true;
        }

        // 4. Hiérarchie normale
        Hierarchie hierAttaquant = Hierarchie.valueOf(attaquant.getClass().getSimpleName().toUpperCase());
        Hierarchie hierDefenseur = Hierarchie.valueOf(defenseur.getClass().getSimpleName().toUpperCase());

        if (hierAttaquant.getForce() >= hierDefenseur.getForce()) {
            return true;
        }

        System.out.println("Capture impossible: " + defenseur.getClass().getSimpleName() + 
                         " est plus fort que " + attaquant.getClass().getSimpleName());
        return false;
    }

    private boolean estDansPiègeAdverse(Animal animal, Case caseActuelle) {
        return (animal.getJoueur() == 1 && caseActuelle.getType() == Case.Type.PIEGE && caseActuelle.getY() >= 6) ||
               (animal.getJoueur() == 2 && caseActuelle.getType() == Case.Type.PIEGE && caseActuelle.getY() <= 2);
    }
}