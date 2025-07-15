package com.f1manager.model;

public class Circuit {
    private int id; // ID de la base de données
    private String nom;
    private int nombreTours;
    private int tempsMoyenParTour; // Temps moyen en secondes

    // Constructeurs
    public Circuit() {
    }

    public Circuit(String nom, int nombreTours, int tempsMoyenParTour) {
        this.nom = nom;
        this.nombreTours = nombreTours;
        this.tempsMoyenParTour = tempsMoyenParTour;
    }

    // Méthodes utilitaires
    public int getDureeTotaleEstimee() {
        return nombreTours * tempsMoyenParTour;
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

    public String getTempsMoyenFormate() {
        int minutes = tempsMoyenParTour / 60;
        int secondes = tempsMoyenParTour % 60;
        return String.format("%dm %02ds", minutes, secondes);
    }

    public boolean isValide() {
        return nom != null && !nom.trim().isEmpty() &&
                nombreTours > 0 && tempsMoyenParTour > 0;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public int getTempsMoyenParTour() {
        return tempsMoyenParTour;
    }

    public void setTempsMoyenParTour(int tempsMoyenParTour) {
        this.tempsMoyenParTour = tempsMoyenParTour;
    }

    // Méthodes de compatibilité avec l'ancien code
    public int getDureeParTour() {
        return tempsMoyenParTour;
    }

    public void setDureeParTour(int dureeParTour) {
        this.tempsMoyenParTour = dureeParTour;
    }

    public String getDureeParTourFormatee() {
        return getTempsMoyenFormate();
    }

    @Override
    public String toString() {
        return String.format("Circuit: %s - %d tours - %s par tour (Total: %s)",
                nom, nombreTours, getTempsMoyenFormate(), getDureeTotaleFormatee());
    }
}