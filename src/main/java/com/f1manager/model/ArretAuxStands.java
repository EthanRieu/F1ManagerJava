package com.f1manager.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ArretAuxStands {
    private String piloteNom;
    private int tourArret;
    private String typePneusChoisis;
    private String typeArret; // "changement_pneus" ou "abandon"
    private LocalDateTime heureArret;

    public static final String TYPE_CHANGEMENT_PNEUS = "changement_pneus";
    public static final String TYPE_ABANDON = "abandon";

    // Constructeurs
    public ArretAuxStands() {
        this.heureArret = LocalDateTime.now();
    }

    public ArretAuxStands(String piloteNom, int tourArret, String typePneusChoisis, String typeArret) {
        this();
        this.piloteNom = piloteNom;
        this.tourArret = tourArret;
        this.typePneusChoisis = typePneusChoisis;
        this.typeArret = typeArret;
    }

    // Méthodes utilitaires
    public boolean isAbandon() {
        return TYPE_ABANDON.equals(typeArret);
    }

    public boolean isChangementPneus() {
        return TYPE_CHANGEMENT_PNEUS.equals(typeArret);
    }

    public String getHeureArretFormatee() {
        return heureArret.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    // Getters et Setters
    public String getPiloteNom() {
        return piloteNom;
    }

    public void setPiloteNom(String piloteNom) {
        this.piloteNom = piloteNom;
    }

    public int getTourArret() {
        return tourArret;
    }

    public void setTourArret(int tourArret) {
        this.tourArret = tourArret;
    }

    public String getTypePneusChoisis() {
        return typePneusChoisis;
    }

    public void setTypePneusChoisis(String typePneusChoisis) {
        this.typePneusChoisis = typePneusChoisis;
    }

    public String getTypeArret() {
        return typeArret;
    }

    public void setTypeArret(String typeArret) {
        this.typeArret = typeArret;
    }

    public LocalDateTime getHeureArret() {
        return heureArret;
    }

    public void setHeureArret(LocalDateTime heureArret) {
        this.heureArret = heureArret;
    }

    @Override
    public String toString() {
        String action = isAbandon() ? "Abandon" : "Changement pneus (" + typePneusChoisis + ")";
        return String.format("Tour %d - %s: %s à %s",
                tourArret, piloteNom, action, getHeureArretFormatee());
    }
}