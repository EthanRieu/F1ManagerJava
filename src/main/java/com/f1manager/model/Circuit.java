package com.f1manager.model;

public class Circuit {
    private String nom;
    private int nombreTours;
    private int dureeParTour; // Durée en secondes

    // Constructeurs
    public Circuit() {
    }

    public Circuit(String nom, int nombreTours, int dureeParTour) {
        this.nom = nom;
        this.nombreTours = nombreTours;
        this.dureeParTour = dureeParTour;
    }

    // Méthodes utilitaires
    public int getDureeTotaleEstimee() {
        return nombreTours * dureeParTour;
    }

    public String getDureeTotaleFormatee() {
        int total = getDureeTotaleEstimee();
        int heures = total / 3600;
        int minutes = (total % 3600) / 60;
        int secondes = total % 60;

        if (heures > 0) {
            return String.format("%dh %02dm %02ds", heures, minutes, secondes);
        } else {
            return String.format("%dm %02ds", minutes, secondes);
        }
    }

    public String getDureeParTourFormatee() {
        int minutes = dureeParTour / 60;
        int secondes = dureeParTour % 60;
        return String.format("%dm %02ds", minutes, secondes);
    }

    public boolean isValide() {
        return nom != null && !nom.trim().isEmpty() &&
                nombreTours > 0 && dureeParTour > 0;
    }

    // Getters et Setters
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getNombreTours() {
        return nombreTours;
    }

    public void setNombreTours(int nombreTours) {
        this.nombreTours = nombreTours;
    }

    public int getDureeParTour() {
        return dureeParTour;
    }

    public void setDureeParTour(int dureeParTour) {
        this.dureeParTour = dureeParTour;
    }

    @Override
    public String toString() {
        return String.format("Circuit: %s - %d tours - %s par tour (Total: %s)",
                nom, nombreTours, getDureeParTourFormatee(), getDureeTotaleFormatee());
    }
}