package dao;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import models.Partie;

public class PartieDAO {

    public static void creerTable() {
        String sql = "CREATE TABLE IF NOT EXISTS parties (" +
                     "id INT AUTO_INCREMENT PRIMARY KEY," +
                     "joueur1_id INT NOT NULL," +
                     "joueur2_id INT NOT NULL," +
                     "gagnant_id INT," +
                     "date_partie TIMESTAMP NOT NULL," +
                     "FOREIGN KEY (joueur1_id) REFERENCES joueurs(id)," +
                     "FOREIGN KEY (joueur2_id) REFERENCES joueurs(id)" +
                     ");";

        try (Connection conn = ConnexionH2.connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Erreur création table parties:");
            e.printStackTrace();
        }
    }

    public static boolean enregistrerPartie(Partie partie) {
        String sql = "INSERT INTO parties(joueur1_id, joueur2_id, gagnant_id, date_partie) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnexionH2.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, partie.getJoueur1Id());
            pstmt.setInt(2, partie.getJoueur2Id());
            if (partie.getGagnantId() != 0) {
                pstmt.setInt(3, partie.getGagnantId());
            } else {
                pstmt.setNull(3, Types.INTEGER);
            }
            pstmt.setTimestamp(4, Timestamp.valueOf(partie.getDate()));
            
            int rowsAffected = pstmt.executeUpdate();
            
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    partie.setId(rs.getInt(1));
                }
            }
            
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur enregistrement partie:");
            e.printStackTrace();
            return false;
        }
    }

    public static List<Partie> getHistorique() {
        String sql = "SELECT * FROM parties ORDER BY date_partie DESC LIMIT 10";
        List<Partie> historique = new ArrayList<>();
        
        try (Connection conn = ConnexionH2.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Partie p = new Partie(
                    rs.getInt("joueur1_id"),
                    rs.getInt("joueur2_id"),
                    rs.getInt("gagnant_id")
                );
                p.setId(rs.getInt("id"));
                historique.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return historique;
    }

    public static int compterVictoires(int joueurId) {
        String sql = "SELECT COUNT(*) FROM parties WHERE gagnant_id = ?";
        try (Connection conn = ConnexionH2.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, joueurId);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}