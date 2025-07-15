package com.f1manager.service;

import java.util.List;

import com.f1manager.database.dao.VoitureDAO;
import com.f1manager.model.Pilote;
import com.f1manager.model.VoitureF1;

public class VoitureService {
    private VoitureDAO voitureDAO;

    public VoitureService() {
        this.voitureDAO = new VoitureDAO();
    }

    // Méthodes CRUD de base
    public boolean ajouterVoiture(String numero, Pilote pilote) {
        if (numero == null || numero.trim().isEmpty()) {
            System.out.println("❌ Le numéro de voiture est obligatoire.");
            return false;
        }

        // Vérifier si le numéro existe déjà
        if (voitureDAO.existsByNumero(numero)) {
            System.out.println("❌ Le numéro de voiture " + numero + " existe déjà.");
            return false;
        }

        // Créer la nouvelle voiture
        VoitureF1 nouvelleVoiture = new VoitureF1(numero, pilote);

        // Sauvegarder en base
        if (voitureDAO.save(nouvelleVoiture)) {
            System.out.println("✅ Voiture " + numero + " ajoutée avec succès.");
            return true;
        } else {
            System.out.println("❌ Erreur lors de l'ajout de la voiture.");
            return false;
        }
    }

    public boolean modifierVoiture(String numero, String nouveauNumero) {
        if (numero == null || nouveauNumero == null) {
            System.out.println("❌ Les numéros de voiture sont obligatoires.");
            return false;
        }

        // Vérifier si l'ancienne voiture existe
        VoitureF1 voiture = voitureDAO.findByNumero(numero);
        if (voiture == null) {
            System.out.println("❌ Voiture " + numero + " non trouvée.");
            return false;
        }

        // Vérifier si le nouveau numéro n'existe pas déjà
        if (!numero.equals(nouveauNumero) && voitureDAO.existsByNumero(nouveauNumero)) {
            System.out.println("❌ Le numéro de voiture " + nouveauNumero + " existe déjà.");
            return false;
        }

        // Modifier la voiture
        voiture.setNumero(nouveauNumero);

        if (voitureDAO.update(voiture)) {
            System.out.println("✅ Voiture modifiée avec succès.");
            return true;
        } else {
            System.out.println("❌ Erreur lors de la modification de la voiture.");
            return false;
        }
    }

    public boolean supprimerVoiture(String numero) {
        if (numero == null) {
            System.out.println("❌ Le numéro de voiture est obligatoire.");
            return false;
        }

        // Vérifier si la voiture existe
        if (!voitureDAO.existsByNumero(numero)) {
            System.out.println("❌ Voiture " + numero + " non trouvée.");
            return false;
        }

        if (voitureDAO.deleteByNumero(numero)) {
            System.out.println("✅ Voiture " + numero + " supprimée avec succès.");
            return true;
        } else {
            System.out.println("❌ Erreur lors de la suppression de la voiture.");
            return false;
        }
    }

    // Méthodes de recherche
    public VoitureF1 trouverVoiture(String numero) {
        return voitureDAO.findByNumero(numero);
    }

    public VoitureF1 trouverVoitureParPilote(Pilote pilote) {
        if (pilote == null) {
            return null;
        }
        return voitureDAO.findByPiloteId(pilote.getId());
    }

    public List<VoitureF1> obtenirToutesLesVoitures() {
        return voitureDAO.findAll();
    }

    // Méthodes utilitaires
    public boolean voitureExiste(String numero) {
        return voitureDAO.existsByNumero(numero);
    }

    public int getNombreVoitures() {
        return voitureDAO.findAll().size();
    }

    public boolean assignerPilote(String numeroVoiture, Pilote pilote) {
        if (numeroVoiture == null || pilote == null) {
            System.out.println("❌ Numéro de voiture et pilote sont obligatoires.");
            return false;
        }

        if (voitureDAO.assignerPilote(numeroVoiture, pilote.getId())) {
            System.out.println("✅ Pilote " + pilote.getNomComplet() + " assigné à la voiture " + numeroVoiture);
            return true;
        } else {
            System.out.println("❌ Erreur lors de l'assignation du pilote.");
            return false;
        }
    }

    public void afficherToutesLesVoitures() {
        List<VoitureF1> voitures = voitureDAO.findAll();

        if (voitures.isEmpty()) {
            System.out.println("ℹ️ Aucune voiture enregistrée.");
            return;
        }

        System.out.println("\n🏎️ Liste des voitures :");
        System.out.println("─".repeat(80));
        System.out.printf("%-15s %-20s %-15s %-15s%n",
                "Numéro", "Pilote", "Statut", "Pneus");
        System.out.println("─".repeat(80));

        for (VoitureF1 voiture : voitures) {
            String piloteNom = voiture.getPilote() != null ? voiture.getPilote().getNomComplet() : "Aucun";
            System.out.printf("%-15s %-20s %-15s %-15s%n",
                    voiture.getNumero(),
                    piloteNom,
                    voiture.getStatut(),
                    voiture.getTypePneus());
        }

        System.out.println("─".repeat(80));
        System.out.println("🏎️ Total : " + voitures.size() + " voitures");
    }

    // Méthodes pour compatibilité avec l'ancien code
    public List<VoitureF1> getVoituresList() {
        return obtenirToutesLesVoitures();
    }
}