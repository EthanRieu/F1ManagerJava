package com.f1manager.service;

import java.util.List;
import java.util.stream.Collectors;

import com.f1manager.database.dao.PiloteDAO;
import com.f1manager.database.dao.VoitureDAO;
import com.f1manager.model.Pilote;
import com.f1manager.model.VoitureF1;

public class PiloteService {
    private PiloteDAO piloteDAO;
    private VoitureDAO voitureDAO;

    public PiloteService() {
        this.piloteDAO = new PiloteDAO();
        this.voitureDAO = new VoitureDAO();
    }

    // Méthodes CRUD de base
    public boolean ajouterPilote(String nom, String prenom, int numero) {
        // Vérifier si le numéro existe déjà
        if (piloteDAO.existsByNumero(numero)) {
            System.out.println("❌ Le numéro " + numero + " est déjà utilisé par un autre pilote.");
            return false;
        }

        // Créer le nouveau pilote
        Pilote nouveauPilote = new Pilote(nom, prenom, numero);

        // Sauvegarder en base
        if (piloteDAO.save(nouveauPilote)) {
            System.out.println("✅ Pilote " + nom + " " + prenom + " (#" + numero + ") ajouté avec succès.");
            return true;
        } else {
            System.out.println("❌ Erreur lors de l'ajout du pilote.");
            return false;
        }
    }

    public boolean modifierPilote(int numero, String nouveauNom, String nouveauPrenom) {
        Pilote pilote = piloteDAO.findByNumero(numero);
        if (pilote == null) {
            System.out.println("❌ Pilote #" + numero + " non trouvé.");
            return false;
        }

        pilote.setNom(nouveauNom);
        pilote.setPrenom(nouveauPrenom);

        if (piloteDAO.update(pilote)) {
            System.out.println("✅ Pilote #" + numero + " modifié avec succès.");
            return true;
        } else {
            System.out.println("❌ Erreur lors de la modification du pilote.");
            return false;
        }
    }

    public boolean supprimerPilote(int numero) {
        if (!piloteDAO.existsByNumero(numero)) {
            System.out.println("❌ Pilote #" + numero + " non trouvé.");
            return false;
        }

        if (piloteDAO.deleteByNumero(numero)) {
            System.out.println("✅ Pilote #" + numero + " supprimé avec succès.");
            return true;
        } else {
            System.out.println("❌ Erreur lors de la suppression du pilote.");
            return false;
        }
    }

    // Méthodes de recherche
    public Pilote trouverPilote(int numero) {
        Pilote pilote = piloteDAO.findByNumero(numero);
        if (pilote != null) {
            // Charger la voiture associée
            VoitureF1 voiture = voitureDAO.findByPiloteId(pilote.getId());
            if (voiture != null) {
                pilote.setVoiture(voiture);
            }
        }
        return pilote;
    }

    public List<Pilote> obtenirTousLesPilotes() {
        List<Pilote> pilotes = piloteDAO.findAll();

        // Charger les voitures associées pour chaque pilote
        for (Pilote pilote : pilotes) {
            VoitureF1 voiture = voitureDAO.findByPiloteId(pilote.getId());
            if (voiture != null) {
                pilote.setVoiture(voiture);
            }
        }

        return pilotes;
    }

    // Méthode simplifiée pour les pilotes sans voiture (pas de table de liaison)
    public List<Pilote> obtenirPilotesSansVoiture() {
        List<Pilote> pilotes = obtenirTousLesPilotes();
        return pilotes.stream()
                .filter(p -> p.getVoiture() == null)
                .collect(Collectors.toList());
    }

    // Méthodes de gestion du statut (version simplifiée - statut en mémoire
    // seulement)
    public boolean faireSortirPilote(int numero) {
        Pilote pilote = piloteDAO.findByNumero(numero);
        if (pilote == null) {
            System.out.println("❌ Pilote #" + numero + " non trouvé.");
            return false;
        }

        if ("piste".equals(pilote.getStatut())) {
            System.out.println("⚠️ Le pilote #" + numero + " est déjà en piste.");
            return false;
        }

        if ("abandonne".equals(pilote.getStatut())) {
            System.out.println("❌ Le pilote #" + numero + " a abandonné et ne peut pas sortir.");
            return false;
        }

        pilote.setStatut("piste");
        System.out.println("🏁 Pilote #" + numero + " (" + pilote.getNom() + ") est maintenant en piste.");
        return true;
    }

    public boolean faireRentrerPilote(int numero) {
        Pilote pilote = piloteDAO.findByNumero(numero);
        if (pilote == null) {
            System.out.println("❌ Pilote #" + numero + " non trouvé.");
            return false;
        }

        if ("garage".equals(pilote.getStatut())) {
            System.out.println("⚠️ Le pilote #" + numero + " est déjà au garage.");
            return false;
        }

        if ("abandonne".equals(pilote.getStatut())) {
            System.out.println("❌ Le pilote #" + numero + " a abandonné.");
            return false;
        }

        pilote.setStatut("garage");
        System.out.println("🏠 Pilote #" + numero + " (" + pilote.getNom() + ") est rentré au garage.");
        return true;
    }

    public boolean abandonnerPilote(int numero) {
        Pilote pilote = piloteDAO.findByNumero(numero);
        if (pilote == null) {
            System.out.println("❌ Pilote #" + numero + " non trouvé.");
            return false;
        }

        if ("abandonne".equals(pilote.getStatut())) {
            System.out.println("⚠️ Le pilote #" + numero + " a déjà abandonné.");
            return false;
        }

        pilote.setStatut("abandonne");
        System.out.println("🚫 Pilote #" + numero + " (" + pilote.getNom() + ") a abandonné la course.");
        return true;
    }

    // Méthodes de statistiques
    public int getNombrePilotes() {
        return piloteDAO.findAll().size();
    }

    public int getNombrePilotesEnPiste() {
        return (int) piloteDAO.findAll().stream()
                .filter(p -> "piste".equals(p.getStatut()))
                .count();
    }

    public int getNombrePilotesAuGarage() {
        return (int) piloteDAO.findAll().stream()
                .filter(p -> "garage".equals(p.getStatut()))
                .count();
    }

    public int getNombrePilotesAbandonne() {
        return (int) piloteDAO.findAll().stream()
                .filter(p -> "abandonne".equals(p.getStatut()))
                .count();
    }

    public List<Pilote> getPilotesEnPiste() {
        return piloteDAO.findAll().stream()
                .filter(p -> "piste".equals(p.getStatut()))
                .collect(Collectors.toList());
    }

    public List<Pilote> getPilotesAuGarage() {
        return piloteDAO.findAll().stream()
                .filter(p -> "garage".equals(p.getStatut()))
                .collect(Collectors.toList());
    }

    // Méthodes utilitaires
    public boolean piloteExiste(int numero) {
        return piloteDAO.existsByNumero(numero);
    }

    public String getStatutPilote(int numero) {
        Pilote pilote = piloteDAO.findByNumero(numero);
        return pilote != null ? pilote.getStatut() : "inexistant";
    }

    // Méthodes pour compatibilité avec l'ancien code
    public Pilote trouverPiloteParNumero(int numero) {
        return trouverPilote(numero);
    }

    public int getNombreTotalPilotes() {
        return getNombrePilotes();
    }

    public int getNombrePilotesDisponibles() {
        return getNombrePilotesAuGarage();
    }

    public List<Pilote> obtenirPilotesDisponibles() {
        return getPilotesAuGarage();
    }

    public List<Pilote> obtenirPilotesEnPiste() {
        return getPilotesEnPiste();
    }

    public List<Pilote> getPilotesList() {
        return obtenirTousLesPilotes();
    }

    public void reinitialiserTousLesPilotes() {
        List<Pilote> pilotes = obtenirTousLesPilotes();
        for (Pilote pilote : pilotes) {
            pilote.setStatut("garage");
        }
        System.out.println("🔄 Tous les pilotes ont été réinitialisés au garage.");
    }

    public void afficherTousLesPilotes() {
        List<Pilote> pilotes = piloteDAO.findAll();

        if (pilotes.isEmpty()) {
            System.out.println("ℹ️ Aucun pilote enregistré.");
            return;
        }

        System.out.println("\n📋 Liste des pilotes :");
        System.out.println("─".repeat(60));
        System.out.printf("%-4s %-15s %-15s %-15s%n",
                "N°", "Nom", "Prénom", "Statut");
        System.out.println("─".repeat(60));

        for (Pilote pilote : pilotes) {
            String statut = getStatutEmoji(pilote.getStatut());
            System.out.printf("%-4d %-15s %-15s %-15s%n",
                    pilote.getNumero(),
                    pilote.getNom(),
                    pilote.getPrenom(),
                    statut);
        }

        System.out.println("─".repeat(60));
        System.out.println("📊 Total : " + pilotes.size() + " pilotes");
    }

    private String getStatutEmoji(String statut) {
        switch (statut) {
            case "piste":
                return "🏁 En piste";
            case "garage":
                return "🏠 Garage";
            case "abandonne":
                return "🚫 Abandonné";
            default:
                return "❓ " + statut;
        }
    }
}
