package com.f1manager.service;

import java.util.ArrayList;
import java.util.List;

import com.f1manager.model.Pilote;
import com.f1manager.model.VoitureF1;

public class PiloteService {
    private List<Pilote> pilotes;

    public PiloteService() {
        this.pilotes = new ArrayList<>();
    }

    // CRUD Operations
    public boolean ajouterPilote(String nom, String prenom, int numero) {
        // Limite à 20 pilotes
        if (pilotes.size() >= 20) {
            return false;
        }
        // Vérifier que le numéro n'est pas déjà utilisé
        if (piloteExiste(numero)) {
            return false;
        }

        // Valider les données
        if (nom == null || nom.trim().isEmpty() ||
                prenom == null || prenom.trim().isEmpty() ||
                numero <= 0) {
            return false;
        }

        Pilote nouveauPilote = new Pilote(nom.trim(), prenom.trim(), numero);
        return pilotes.add(nouveauPilote);
    }

    public boolean modifierPilote(int numero, String nouveauNom, String nouveauPrenom) {
        Pilote pilote = trouverPiloteParNumero(numero);
        if (pilote == null) {
            return false;
        }

        if (nouveauNom != null && !nouveauNom.trim().isEmpty()) {
            pilote.setNom(nouveauNom.trim());
        }

        if (nouveauPrenom != null && !nouveauPrenom.trim().isEmpty()) {
            pilote.setPrenom(nouveauPrenom.trim());
        }

        return true;
    }

    public boolean supprimerPilote(int numero) {
        Pilote pilote = trouverPiloteParNumero(numero);
        if (pilote == null) {
            return false;
        }

        // Ne pas supprimer un pilote en cours de course
        if (pilote.isEnPiste() || (pilote.getVoiture() != null &&
                !pilote.getVoiture().getStatut().equals(VoitureF1.STATUT_GARAGE))) {
            return false;
        }

        return pilotes.remove(pilote);
    }

    // Recherche et navigation
    public Pilote trouverPiloteParNumero(int numero) {
        return pilotes.stream()
                .filter(p -> p.getNumero() == numero)
                .findFirst()
                .orElse(null);
    }

    public List<Pilote> obtenirTousLesPilotes() {
        return new ArrayList<>(pilotes);
    }

    public List<Pilote> obtenirPilotesDisponibles() {
        return pilotes.stream()
                .filter(p -> !p.isAbandonne() && !p.isEnPiste())
                .toList();
    }

    public List<Pilote> obtenirPilotesEnPiste() {
        return pilotes.stream()
                .filter(Pilote::isEnPiste)
                .toList();
    }

    // Validation
    public boolean piloteExiste(int numero) {
        return trouverPiloteParNumero(numero) != null;
    }

    public boolean peutSortirDesStands(int numero) {
        Pilote pilote = trouverPiloteParNumero(numero);
        return pilote != null && !pilote.isEnPiste() && !pilote.isAbandonne();
    }

    public boolean peutRentrerAuxStands(int numero) {
        Pilote pilote = trouverPiloteParNumero(numero);
        return pilote != null && pilote.isEnPiste() && !pilote.isAbandonne();
    }

    // Actions de course
    public boolean faireSortirPilote(int numero) {
        Pilote pilote = trouverPiloteParNumero(numero);
        if (pilote != null && peutSortirDesStands(numero)) {
            pilote.sortirDesStands();
            return true;
        }
        return false;
    }

    public boolean faireRentrerPilote(int numero) {
        Pilote pilote = trouverPiloteParNumero(numero);
        if (pilote != null && peutRentrerAuxStands(numero)) {
            pilote.rentrerAuxStands();
            return true;
        }
        return false;
    }

    // Statistiques
    public int getNombreTotalPilotes() {
        return pilotes.size();
    }

    public int getNombrePilotesEnPiste() {
        return (int) pilotes.stream().filter(Pilote::isEnPiste).count();
    }

    public int getNombrePilotesDisponibles() {
        return (int) pilotes.stream()
                .filter(p -> !p.isAbandonne() && !p.isEnPiste())
                .count();
    }

    // Reset pour nouvelle course
    public void reinitialiserTousLesPilotes() {
        for (Pilote pilote : pilotes) {
            pilote.setEnPiste(false);
            pilote.setAbandonne(false);
            if (pilote.getVoiture() != null) {
                pilote.getVoiture().setStatut(VoitureF1.STATUT_GARAGE);
                pilote.getVoiture().setAbandonne(false);
            }
        }
    }

    public boolean ajouterVoiture(Pilote pilote, VoitureF1 voiture) {
        if (pilote == null || voiture == null) {
            return false;
        }
        pilote.setVoiture(voiture);
        return true;
    }

    public List<Pilote> getPilotesList() {
        return pilotes;
    }
}
