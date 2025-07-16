package com.f1manager.service;

import java.util.List;

import com.f1manager.database.dao.CircuitDAO;
import com.f1manager.model.Circuit;

public class CircuitService {
    private CircuitDAO circuitDAO;

    public CircuitService() {
        this.circuitDAO = new CircuitDAO();
    }

    // Méthodes CRUD de base
    public boolean ajouterCircuit(String nom, int nombreTours, int tempsMoyenParTour) {
        // Vérifier si le circuit existe déjà
        if (circuitDAO.existsByNom(nom)) {
            System.out.println("❌ Un circuit avec le nom '" + nom + "' existe déjà.");
            return false;
        }

        // Créer le nouveau circuit
        Circuit nouveauCircuit = new Circuit(nom, nombreTours, tempsMoyenParTour);

        // Sauvegarder en base
        if (circuitDAO.save(nouveauCircuit)) {
            System.out.println("✅ Circuit " + nom + " ajouté avec succès.");
            return true;
        } else {
            System.out.println("❌ Erreur lors de l'ajout du circuit.");
            return false;
        }
    }

    public boolean modifierCircuit(int id, String nom, int nombreTours, int tempsMoyenParTour) {
        Circuit circuit = circuitDAO.findById(id);
        if (circuit == null) {
            System.out.println("❌ Circuit avec ID " + id + " non trouvé.");
            return false;
        }

        circuit.setNom(nom);
        circuit.setNombreTours(nombreTours);
        circuit.setTempsMoyenParTour(tempsMoyenParTour);

        if (circuitDAO.update(circuit)) {
            System.out.println("✅ Circuit " + nom + " modifié avec succès.");
            return true;
        } else {
            System.out.println("❌ Erreur lors de la modification du circuit.");
            return false;
        }
    }

    public boolean supprimerCircuit(int id) {
        if (circuitDAO.findById(id) == null) {
            System.out.println("❌ Circuit avec ID " + id + " non trouvé.");
            return false;
        }

        if (circuitDAO.deleteById(id)) {
            System.out.println("✅ Circuit supprimé avec succès.");
            return true;
        } else {
            System.out.println("❌ Erreur lors de la suppression du circuit.");
            return false;
        }
    }

    // Méthodes de recherche
    public Circuit trouverCircuit(int id) {
        return circuitDAO.findById(id);
    }

    public Circuit trouverCircuitParNom(String nom) {
        return circuitDAO.findByNom(nom);
    }

    public List<Circuit> obtenirTousLesCircuits() {
        return circuitDAO.findAll();
    }

    // Méthodes utilitaires
    public boolean circuitExiste(String nom) {
        return circuitDAO.existsByNom(nom);
    }
}