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
    public Pilote trouverPiloteParNumero(int numero) {
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

    // Méthodes utilitaires
    public boolean piloteExiste(int numero) {
        return piloteDAO.existsByNumero(numero);
    }

    public List<Pilote> obtenirPilotesDisponibles() {
        return piloteDAO.findAll().stream()
                .filter(p -> "garage".equals(p.getStatut()))
                .collect(Collectors.toList());
    }

    public List<Pilote> obtenirPilotesEnPiste() {
        return piloteDAO.findAll().stream()
                .filter(p -> "piste".equals(p.getStatut()))
                .collect(Collectors.toList());
    }

    public void reinitialiserTousLesPilotes() {
        List<Pilote> pilotes = obtenirTousLesPilotes();
        for (Pilote pilote : pilotes) {
            pilote.setStatut("garage");
        }
        System.out.println("🔄 Tous les pilotes ont été réinitialisés au garage.");
    }
}
