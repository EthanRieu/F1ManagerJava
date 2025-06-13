package com.f1manager.model;

public class Pilote {
    private String nom;
    private String prenom;
    private int numero;
    private boolean enPiste;
    private VoitureF1 voiture;
    private boolean abandonne;
    private double tempsTotal = 0;
    private double tempsTourEnCours = 0;
    private double meilleurTour = Double.MAX_VALUE;
    private double dernierTour = Double.MAX_VALUE;
    private int toursEffectues = 0;
    private int meilleurTourNum = 0;

    // Constructeurs
    public Pilote() {
    }

    public Pilote(String nom, String prenom, int numero) {
        this.nom = nom;
        this.prenom = prenom;
        this.numero = numero;
        this.enPiste = false;
        this.abandonne = false;
    }

    // Méthodes de gestion
    public void sortirDesStands() {
        if (!enPiste && !abandonne) {
            this.enPiste = true;
            if (voiture != null) {
                voiture.setStatut("circuit");
            }
        }
    }

    public void rentrerAuxStands() {
        if (enPiste && !abandonne) {
            this.enPiste = false;
            if (voiture != null) {
                voiture.setStatut("stands");
            }
        }
    }

    public void abandonner() {
        this.abandonne = true;
        this.enPiste = false;
        if (voiture != null) {
            voiture.setStatut("garage");
            voiture.setAbandonne(true);
        }
    }

    // Getters et Setters
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public boolean isEnPiste() {
        return enPiste;
    }

    public void setEnPiste(boolean enPiste) {
        this.enPiste = enPiste;
    }

    public VoitureF1 getVoiture() {
        return voiture;
    }

    public void setVoiture(VoitureF1 voiture) {
        this.voiture = voiture;
    }

    public boolean isAbandonne() {
        return abandonne;
    }

    public void setAbandonne(boolean abandonne) {
        this.abandonne = abandonne;
    }

    public String getNomComplet() {
        return prenom + " " + nom;
    }

    @Override
    public String toString() {
        return String.format("#%d - %s %s (%s)",
                numero, prenom, nom,
                abandonne ? "Abandonné" : (enPiste ? "En piste" : "Aux stands"));
    }

    public double getTempsTotal() {
        return tempsTotal;
    }

    public void setTempsTotal(double tempsTotal) {
        this.tempsTotal = tempsTotal;
    }

    public double getTempsTourEnCours() {
        return tempsTourEnCours;
    }

    public void setTempsTourEnCours(double tempsTourEnCours) {
        this.tempsTourEnCours = tempsTourEnCours;
    }

    public double getMeilleurTour() {
        return meilleurTour;
    }

    public void setMeilleurTour(double meilleurTour) {
        this.meilleurTour = meilleurTour;
    }

    public double getDernierTour() {
        return dernierTour;
    }

    public void setDernierTour(double dernierTour) {
        this.dernierTour = dernierTour;
    }

    public int getToursEffectues() {
        return toursEffectues;
    }

    public void setToursEffectues(int toursEffectues) {
        this.toursEffectues = toursEffectues;
    }

    public int getMeilleurTourNum() {
        return meilleurTourNum;
    }

    public void setMeilleurTourNum(int meilleurTourNum) {
        this.meilleurTourNum = meilleurTourNum;
    }
}