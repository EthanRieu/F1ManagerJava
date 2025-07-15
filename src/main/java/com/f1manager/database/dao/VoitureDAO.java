package com.f1manager.database.dao;

import com.f1manager.database.DatabaseConnection;
import com.f1manager.model.Pilote;
import com.f1manager.model.VoitureF1;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO pour la gestion des voitures en base de données
 */
public class VoitureDAO {

    private DatabaseConnection dbConnection;
    private PiloteDAO piloteDAO;

    public VoitureDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
        this.piloteDAO = new PiloteDAO();
    }

    /**
     * Récupère toutes les voitures avec leurs pilotes
     */
    public List<VoitureF1> findAll() {
        List<VoitureF1> voitures = new ArrayList<>();
        String sql = "SELECT v.id, v.numero_voiture, v.pilote_id, " +
                "p.numero, p.nom, p.prenom " +
                "FROM voitures v " +
                "LEFT JOIN pilotes p ON v.pilote_id = p.id " +
                "ORDER BY v.numero_voiture";

        try (Connection conn = dbConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                voitures.add(mapResultSetToVoiture(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des voitures : " + e.getMessage());
        }

        return voitures;
    }

    /**
     * Trouve une voiture par son numéro
     */
    public VoitureF1 findByNumero(String numero) {
        String sql = "SELECT v.id, v.numero_voiture, v.pilote_id, " +
                "p.numero, p.nom, p.prenom " +
                "FROM voitures v " +
                "LEFT JOIN pilotes p ON v.pilote_id = p.id " +
                "WHERE v.numero_voiture = ?";

        try (Connection conn = dbConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, numero);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToVoiture(rs);
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de la voiture " + numero + " : " + e.getMessage());
        }

        return null;
    }

    /**
     * Trouve une voiture par l'ID du pilote
     */
    public VoitureF1 findByPiloteId(int piloteId) {
        String sql = "SELECT v.id, v.numero_voiture, v.pilote_id, " +
                "p.numero, p.nom, p.prenom " +
                "FROM voitures v " +
                "LEFT JOIN pilotes p ON v.pilote_id = p.id " +
                "WHERE v.pilote_id = ?";

        try (Connection conn = dbConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, piloteId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToVoiture(rs);
            }

        } catch (SQLException e) {
            System.err.println(
                    "Erreur lors de la recherche de la voiture du pilote " + piloteId + " : " + e.getMessage());
        }

        return null;
    }

    /**
     * Ajoute une nouvelle voiture
     */
    public boolean save(VoitureF1 voiture) {
        String sql = "INSERT INTO voitures (numero_voiture, pilote_id) VALUES (?, ?)";

        try (Connection conn = dbConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, voiture.getNumero());
            stmt.setInt(2, voiture.getPilote() != null ? voiture.getPilote().getId() : 0);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                // Récupérer l'ID généré
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    voiture.setId(generatedKeys.getInt(1));
                }
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de la voiture : " + e.getMessage());
        }

        return false;
    }

    /**
     * Met à jour une voiture existante
     */
    public boolean update(VoitureF1 voiture) {
        String sql = "UPDATE voitures SET numero_voiture = ?, pilote_id = ? WHERE id = ?";

        try (Connection conn = dbConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, voiture.getNumero());
            stmt.setInt(2, voiture.getPilote() != null ? voiture.getPilote().getId() : 0);
            stmt.setInt(3, voiture.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de la voiture : " + e.getMessage());
        }

        return false;
    }

    /**
     * Supprime une voiture par son numéro
     */
    public boolean deleteByNumero(String numero) {
        String sql = "DELETE FROM voitures WHERE numero_voiture = ?";

        try (Connection conn = dbConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, numero);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de la voiture : " + e.getMessage());
        }

        return false;
    }

    /**
     * Vérifie si un numéro de voiture existe déjà
     */
    public boolean existsByNumero(String numero) {
        String sql = "SELECT COUNT(*) FROM voitures WHERE numero_voiture = ?";

        try (Connection conn = dbConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, numero);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification de la voiture : " + e.getMessage());
        }

        return false;
    }

    /**
     * Assigne un pilote à une voiture
     */
    public boolean assignerPilote(String numeroVoiture, int piloteId) {
        String sql = "UPDATE voitures SET pilote_id = ? WHERE numero_voiture = ?";

        try (Connection conn = dbConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, piloteId);
            stmt.setString(2, numeroVoiture);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de l'assignation du pilote : " + e.getMessage());
        }

        return false;
    }

    /**
     * Mappe un ResultSet vers un objet VoitureF1
     */
    private VoitureF1 mapResultSetToVoiture(ResultSet rs) throws SQLException {
        VoitureF1 voiture = new VoitureF1();
        voiture.setId(rs.getInt("id"));
        voiture.setNumero(rs.getString("numero_voiture"));

        // Créer le pilote si il existe
        int piloteId = rs.getInt("pilote_id");
        if (piloteId > 0) {
            Pilote pilote = new Pilote();
            pilote.setId(piloteId);
            pilote.setNumero(rs.getInt("numero"));
            pilote.setNom(rs.getString("nom"));
            pilote.setPrenom(rs.getString("prenom"));
            voiture.setPilote(pilote);
        }

        return voiture;
    }
}