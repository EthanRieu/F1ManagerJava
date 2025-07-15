package com.f1manager.database.dao;

import com.f1manager.database.DatabaseConnection;
import com.f1manager.model.Pilote;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO pour la gestion des pilotes en base de données
 */
public class PiloteDAO {

    private DatabaseConnection dbConnection;

    public PiloteDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    /**
     * Récupère tous les pilotes
     */
    public List<Pilote> findAll() {
        List<Pilote> pilotes = new ArrayList<>();
        String sql = "SELECT * FROM pilotes ORDER BY numero";

        try (Connection conn = dbConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                pilotes.add(mapResultSetToPilote(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des pilotes : " + e.getMessage());
        }

        return pilotes;
    }

    /**
     * Trouve un pilote par son numéro
     */
    public Pilote findByNumero(int numero) {
        String sql = "SELECT * FROM pilotes WHERE numero = ?";

        try (Connection conn = dbConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, numero);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToPilote(rs);
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche du pilote " + numero + " : " + e.getMessage());
        }

        return null;
    }

    /**
     * Trouve un pilote par son ID
     */
    public Pilote findById(int id) {
        String sql = "SELECT * FROM pilotes WHERE id = ?";

        try (Connection conn = dbConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToPilote(rs);
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche du pilote ID " + id + " : " + e.getMessage());
        }

        return null;
    }

    /**
     * Ajoute un nouveau pilote
     */
    public boolean save(Pilote pilote) {
        String sql = "INSERT INTO pilotes (numero, nom, prenom) VALUES (?, ?, ?)";

        try (Connection conn = dbConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, pilote.getNumero());
            stmt.setString(2, pilote.getNom());
            stmt.setString(3, pilote.getPrenom());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                // Récupérer l'ID généré
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    pilote.setId(generatedKeys.getInt(1));
                }
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du pilote : " + e.getMessage());
        }

        return false;
    }

    /**
     * Met à jour un pilote existant
     */
    public boolean update(Pilote pilote) {
        String sql = "UPDATE pilotes SET nom = ?, prenom = ? WHERE numero = ?";

        try (Connection conn = dbConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, pilote.getNom());
            stmt.setString(2, pilote.getPrenom());
            stmt.setInt(3, pilote.getNumero());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du pilote : " + e.getMessage());
        }

        return false;
    }

    /**
     * Supprime un pilote par son numéro
     */
    public boolean deleteByNumero(int numero) {
        String sql = "DELETE FROM pilotes WHERE numero = ?";

        try (Connection conn = dbConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, numero);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du pilote : " + e.getMessage());
        }

        return false;
    }

    /**
     * Vérifie si un numéro de pilote existe déjà
     */
    public boolean existsByNumero(int numero) {
        String sql = "SELECT COUNT(*) FROM pilotes WHERE numero = ?";

        try (Connection conn = dbConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, numero);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification du pilote : " + e.getMessage());
        }

        return false;
    }

    /**
     * Mappe un ResultSet vers un objet Pilote
     */
    private Pilote mapResultSetToPilote(ResultSet rs) throws SQLException {
        Pilote pilote = new Pilote();
        pilote.setId(rs.getInt("id"));
        pilote.setNumero(rs.getInt("numero"));
        pilote.setNom(rs.getString("nom"));
        pilote.setPrenom(rs.getString("prenom"));

        return pilote;
    }
}