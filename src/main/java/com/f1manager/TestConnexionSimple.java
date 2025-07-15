package com.f1manager;

import com.f1manager.database.DatabaseConnection;
import com.f1manager.database.dao.PiloteDAO;
import com.f1manager.model.Pilote;
import com.f1manager.service.PiloteService;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

/**
 * Test simple pour la nouvelle structure de base de données
 */
public class TestConnexionSimple {

    public static void main(String[] args) {
        System.out.println("🔍 Test de la nouvelle structure de base de données F1Manager");
        System.out.println("=".repeat(70));

        // Test 1: Connexion de base
        testConnexion();

        // Test 2: Test des pilotes
        testPilotes();

        // Test 3: Test des circuits
        testCircuits();

        // Test 4: Test des voitures
        testVoitures();

        System.out.println("\n✅ Tests terminés !");
    }

    private static void testConnexion() {
        System.out.println("\n1️⃣ Test de connexion");
        System.out.println("-".repeat(30));

        try {
            DatabaseConnection dbConnection = DatabaseConnection.getInstance();
            if (dbConnection.testConnection()) {
                System.out.println("✅ Connexion réussie !");
            } else {
                System.out.println("❌ Échec de la connexion");
            }
        } catch (Exception e) {
            System.err.println("❌ Erreur de connexion : " + e.getMessage());
        }
    }

    private static void testPilotes() {
        System.out.println("\n2️⃣ Test des pilotes");
        System.out.println("-".repeat(30));

        try {
            PiloteDAO piloteDAO = new PiloteDAO();
            List<Pilote> pilotes = piloteDAO.findAll();

            System.out.println("📋 Pilotes trouvés : " + pilotes.size());
            pilotes.forEach(p -> System.out.println("  - #" + p.getNumero() + " " + p.getNomComplet()));

            // Test d'ajout
            Pilote nouveauPilote = new Pilote("Test", "Pilote", 99);
            if (piloteDAO.save(nouveauPilote)) {
                System.out.println("✅ Ajout de pilote réussi");
                // Suppression du pilote test
                piloteDAO.deleteByNumero(99);
                System.out.println("🗑️ Pilote test supprimé");
            }

        } catch (Exception e) {
            System.err.println("❌ Erreur pilotes : " + e.getMessage());
        }
    }

    private static void testCircuits() {
        System.out.println("\n3️⃣ Test des circuits");
        System.out.println("-".repeat(30));

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM circuits")) {

            System.out.println("🏁 Circuits disponibles :");
            while (rs.next()) {
                System.out.printf("  - %s (%d tours, %ds/tour)%n",
                        rs.getString("nom"),
                        rs.getInt("nombre_tours"),
                        rs.getInt("temps_moyen_par_tour"));
            }

        } catch (Exception e) {
            System.err.println("❌ Erreur circuits : " + e.getMessage());
        }
    }

    private static void testVoitures() {
        System.out.println("\n4️⃣ Test des voitures");
        System.out.println("-".repeat(30));

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(
                        "SELECT v.numero_voiture, p.nom, p.prenom FROM voitures v JOIN pilotes p ON v.pilote_id = p.id")) {

            System.out.println("🏎️ Voitures avec pilotes :");
            while (rs.next()) {
                System.out.printf("  - %s conduite par %s %s%n",
                        rs.getString("numero_voiture"),
                        rs.getString("prenom"),
                        rs.getString("nom"));
            }

        } catch (Exception e) {
            System.err.println("❌ Erreur voitures : " + e.getMessage());
        }
    }
}