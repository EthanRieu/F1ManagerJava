package com.f1manager.service;

import java.util.ArrayList;
import java.util.List;

import com.f1manager.model.Pilote;
import com.f1manager.model.VoitureF1;

public class VoitureService {
    private List<VoitureF1> voitures;

    public VoitureService() {
        this.voitures = new ArrayList<>();
    }

    public List<VoitureF1> getVoituresList() {
        return new ArrayList<>(voitures);
    }

    public boolean ajouterVoiture(String numero, Pilote pilote) {
        if (numero == null) {
            return false;
        }

        // Vérifier si le numéro existe déjà
        for (VoitureF1 voiture : voitures) {
            if (voiture.getNumero().equals(numero)) {
                return false;
            }
        }

        VoitureF1 nouvelleVoiture = new VoitureF1(numero, pilote);
        return voitures.add(nouvelleVoiture);
    }

    public boolean modifierVoiture(String numero, String nouveauNumero) {
        if (numero == null || nouveauNumero == null) {
            return false;
        }

        // Vérifier si le nouveau numéro existe déjà
        for (VoitureF1 voiture : voitures) {
            if (voiture.getNumero().equals(nouveauNumero)) {
                return false;
            }
        }

        for (VoitureF1 voiture : voitures) {
            if (voiture.getNumero().equals(numero)) {
                voiture.setNumero(nouveauNumero);
                return true;
            }
        }
        return false;
    }

    public boolean supprimerVoiture(String numero) {
        if (numero == null) {
            return false;
        }

        return voitures.removeIf(voiture -> voiture.getNumero().equals(numero));
    }
}