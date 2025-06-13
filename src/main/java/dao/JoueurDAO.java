package dao;

import java.sql.*;
import models.Joueur;

public class JoueurDAO {

    public static void creerTable() {
        String sql = "CREATE TABLE IF NOT EXISTS joueurs (" +
                     "id INT AUTO_INCREMENT PRIMARY KEY," +
                     "nom VARCHAR(50) NOT NULL UNIQUE," +
                     "mot_de_passe VARCHAR(50) NOT NULL" +
                     ");";

        try (Connection conn = ConnexionH2.connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean inscrire(Joueur joueur) {
        String sql = "INSERT INTO joueurs(nom, mot_de_passe) VALUES(?, ?)";

        try (Connection conn = ConnexionH2.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, joueur.getNom());
            pstmt.setString(2, joueur.getMotDePasse());
            pstmt.executeUpdate();
            
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    joueur.setId(rs.getInt(1));
                }
            }
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static Joueur connecter(String nom, String motDePasse) {
        String sql = "SELECT * FROM joueurs WHERE nom = ? AND mot_de_passe = ?";

        try (Connection conn = ConnexionH2.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nom);
            pstmt.setString(2, motDePasse);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Joueur(rs.getInt("id"), rs.getString("nom"), rs.getString("mot_de_passe"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Joueur getJoueurById(int id) {
        String sql = "SELECT * FROM joueurs WHERE id = ?";
        
        try (Connection conn = ConnexionH2.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Joueur(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("mot_de_passe")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}