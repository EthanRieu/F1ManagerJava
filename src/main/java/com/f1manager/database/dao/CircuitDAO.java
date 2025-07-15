package com.f1manager.database.dao;

import com.f1manager.database.DatabaseConnection;
import com.f1manager.model.Circuit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO pour la gestion des circuits en base de données
 */
public class CircuitDAO {

    private DatabaseConnection dbConnection;

    public CircuitDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    /**
     * Récupère tous les circuits
     */
    public List<Circuit> findAll() {
        List<Circuit> circuits = new ArrayList<>();
        String sql = "SELECT * FROM circuits ORDER BY nom";

        try (Connection conn = dbConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                circuits.add(mapResultSetToCircuit(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des circuits : " + e.getMessage());
        }

        return circuits;
    }

    /**
     * Trouve un circuit par son ID
     */
    public Circuit findById(int id) {
        String sql = "SELECT * FROM circuits WHERE id = ?";

        try (Connection conn = dbConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToCircuit(rs);
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche du circuit ID " + id + " : " + e.getMessage());
        }

        return null;
    }

    /**
     * Trouve un circuit par son nom
     */
    public Circuit findByNom(String nom) {
        String sql = "SELECT * FROM circuits WHERE nom = ?";

        try (Connection conn = dbConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nom);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToCircuit(rs);
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche du circuit " + nom + " : " + e.getMessage());
        }

        return null;
    }

    /**
     * Ajoute un nouveau circuit
     */
    public boolean save(Circuit circuit) {
        String sql = "INSERT INTO circuits (nom, nombre_tours, temps_moyen_par_tour) VALUES (?, ?, ?)";

        try (Connection conn = dbConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, circuit.getNom());
            stmt.setInt(2, circuit.getNombreTours());
            stmt.setInt(3, circuit.getTempsMoyenParTour());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                // Récupérer l'ID généré
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    circuit.setId(generatedKeys.getInt(1));
                }
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du circuit : " + e.getMessage());
        }

        return false;
    }

    /**
     * Met à jour un circuit existant
     */
    public boolean update(Circuit circuit) {
        String sql = "UPDATE circuits SET nom = ?, nombre_tours = ?, temps_moyen_par_tour = ? WHERE id = ?";

        try (Connection conn = dbConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, circuit.getNom());
            stmt.setInt(2, circuit.getNombreTours());
            stmt.setInt(3, circuit.getTempsMoyenParTour());
            stmt.setInt(4, circuit.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du circuit : " + e.getMessage());
        }

        return false;
    }

    /**
     * Supprime un circuit par son ID
     */
    public boolean deleteById(int id) {
        String sql = "DELETE FROM circuits WHERE id = ?";

        try (Connection conn = dbConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du circuit : " + e.getMessage());
        }

        return false;
    }

    /**
     * Vérifie si un circuit existe par son nom
     */
    public boolean existsByNom(String nom) {
        String sql = "SELECT COUNT(*) FROM circuits WHERE nom = ?";

        try (Connection conn = dbConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nom);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification du circuit : " + e.getMessage());
        }

        return false;
    }

    /**
     * Mappe un ResultSet vers un objet Circuit
     */
    private Circuit mapResultSetToCircuit(ResultSet rs) throws SQLException {
        Circuit circuit = new Circuit();
        circuit.setId(rs.getInt("id"));
        circuit.setNom(rs.getString("nom"));
        circuit.setNombreTours(rs.getInt("nombre_tours"));
        circuit.setTempsMoyenParTour(rs.getInt("temps_moyen_par_tour"));

        return circuit;
    }
}