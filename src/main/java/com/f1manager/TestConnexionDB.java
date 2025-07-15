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
 * Classe de test pour vérifier la connexion à la base de données
 */
public class TestConnexionDB {

    public static void main(String[] args) {
        System.out.println("🔍 Test de connexion à la base de données F1Manager");
        System.out.println("=".repeat(60));

        // Test 1: Connexion de base
        testConnexionBasique();

        // Test 2: Test du DAO
        testPiloteDAO();

        // Test 3: Test du service
        testPiloteService();

        // Test 4: Vérification des données
        verifierDonnees();

        System.out.println("\n✅ Tests terminés !");
    }

    /**
     * Test de connexion basique
     */
    private static void testConnexionBasique() {
        System.out.println("\n1️⃣ Test de connexion basique");
        System.out.println("-".repeat(30));

        try {
            DatabaseConnection dbConnection = DatabaseConnection.getInstance();
            System.out.println("📊 " + dbConnection.getConnectionInfo());

            if (dbConnection.testConnection()) {
                System.out.println("✅ Connexion réussie !");
            } else {
                System.out.println("❌ Échec de la connexion");
                return;
            }

            // Test d'une requête simple
            try (Connection conn = dbConnection.getConnection();
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as total FROM pilotes")) {

                if (rs.next()) {
                    int totalPilotes = rs.getInt("total");
                    System.out.println("📈 Nombre de pilotes en base : " + totalPilotes);
                }
            }

        } catch (Exception e) {
            System.err.println("❌ Erreur lors du test de connexion : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Test du DAO Pilote
     */
    private static void testPiloteDAO() {
        System.out.println("\n2️⃣ Test du PiloteDAO");
        System.out.println("-".repeat(30));

        try {
            PiloteDAO piloteDAO = new PiloteDAO();

            // Test de récupération de tous les pilotes
            List<Pilote> pilotes = piloteDAO.findAll();
            System.out.println("📋 Pilotes récupérés : " + pilotes.size());

            // Afficher les premiers pilotes
            pilotes.stream()
                    .limit(3)
                    .forEach(p -> System.out
                            .println("  - #" + p.getNumero() + " " + p.getNomComplet()));

            // Test de recherche par numéro
            if (!pilotes.isEmpty()) {
                Pilote premier = pilotes.get(0);
                Pilote trouve = piloteDAO.findByNumero(premier.getNumero());
                if (trouve != null) {
                    System.out.println("🔍 Recherche par numéro réussie : " + trouve.getNomComplet());
                } else {
                    System.out.println("❌ Échec de la recherche par numéro");
                }
            }

        } catch (Exception e) {
            System.err.println("❌ Erreur lors du test DAO : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Test du service Pilote
     */
    private static void testPiloteService() {
        System.out.println("\n3️⃣ Test du PiloteService");
        System.out.println("-".repeat(30));

        try {
            PiloteService piloteService = new PiloteService();

            // Statistiques
            System.out.println("📊 Statistiques :");
            System.out.println("  - Total pilotes : " + piloteService.getNombrePilotes());
            System.out.println("  - En piste : " + piloteService.getNombrePilotesEnPiste());
            System.out.println("  - Au garage : " + piloteService.getNombrePilotesAuGarage());
            System.out.println("  - Abandonnés : " + piloteService.getNombrePilotesAbandonne());

            // Test de modification de statut
            List<Pilote> pilotes = piloteService.obtenirTousLesPilotes();
            if (!pilotes.isEmpty()) {
                Pilote pilote = pilotes.get(0);
                System.out.println("\n🔄 Test de changement de statut pour " + pilote.getNomComplet());
                System.out.println("  Statut initial : " + pilote.getStatut());

                // Faire sortir le pilote
                if (piloteService.faireSortirPilote(pilote.getNumero())) {
                    System.out.println("  ✅ Pilote sorti en piste");
                }

                // Le faire rentrer
                if (piloteService.faireRentrerPilote(pilote.getNumero())) {
                    System.out.println("  ✅ Pilote rentré au garage");
                }
            }

        } catch (Exception e) {
            System.err.println("❌ Erreur lors du test service : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Vérification des données de test
     */
    private static void verifierDonnees() {
        System.out.println("\n4️⃣ Vérification des données de test");
        System.out.println("-".repeat(30));

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement()) {

            // Vérifier les tables
            String[] tables = { "circuits", "pilotes", "voitures", "courses", "course_pilotes", "arrets_aux_stands" };

            for (String table : tables) {
                try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM " + table)) {
                    if (rs.next()) {
                        int count = rs.getInt("count");
                        System.out.println("📊 Table " + table + " : " + count + " enregistrements");
                    }
                }
            }

            // Vérifier les vues
            System.out.println("\n📋 Test des vues :");
            try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM v_pilotes_voitures")) {
                if (rs.next()) {
                    System.out.println("  - v_pilotes_voitures : " + rs.getInt("count") + " lignes");
                }
            }

        } catch (Exception e) {
            System.err.println("❌ Erreur lors de la vérification : " + e.getMessage());
            e.printStackTrace();
        }
    }
}