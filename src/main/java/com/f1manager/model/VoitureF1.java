package com.f1manager.model;

public class VoitureF1 {
    private int id; // ID de la base de données
    private String numero;
    private Pilote pilote;
    private String typePneus;
    private String statut; // "garage", "stands", "circuit"
    private boolean abandonne;

    public static final String PNEUS_SOFT = "soft";
    public static final String PNEUS_MEDIUM = "medium";
    public static final String PNEUS_HARD = "hard";

    public static final String STATUT_GARAGE = "garage";
    public static final String STATUT_STANDS = "stands";
    public static final String STATUT_CIRCUIT = "circuit";

    // Constructeurs
    public VoitureF1() {
        this.typePneus = PNEUS_MEDIUM; // Pneus par défaut
        this.statut = STATUT_GARAGE; // Statut par défaut
        this.abandonne = false;
    }

    public VoitureF1(String numero, Pilote pilote) {
        this();
        this.numero = numero;
        this.pilote = pilote;
        if (pilote != null) {
            pilote.setVoiture(this);
        }
    }

    // Méthodes principales
    public boolean changerPneus(String typePneus) {
        if (!abandonne && isValidTypePneus(typePneus)) {
            this.typePneus = typePneus;
            return true;
        }
        return false;
    }

    public void rentrerAuGarage() {
        this.statut = STATUT_GARAGE;
        this.abandonne = true;
        if (pilote != null) {
            pilote.setEnPiste(false);
            pilote.setAbandonne(true);
        }
    }

    public boolean sortirDesStands() {
        if (!abandonne && statut.equals(STATUT_STANDS)) {
            this.statut = STATUT_CIRCUIT;
            if (pilote != null) {
                pilote.setEnPiste(true);
            }
            return true;
        }
        return false;
    }

    public boolean rentrerAuxStands() {
        if (!abandonne && statut.equals(STATUT_CIRCUIT)) {
            this.statut = STATUT_STANDS;
            if (pilote != null) {
                pilote.setEnPiste(false);
            }
            return true;
        }
        return false;
    }

    public boolean sortirDuGarage() {
        if (!abandonne && statut.equals(STATUT_GARAGE)) {
            this.statut = STATUT_STANDS;
            return true;
        }
        return false;
    }

    // Méthodes utilitaires
    private boolean isValidTypePneus(String type) {
        return PNEUS_SOFT.equals(type) ||
                PNEUS_MEDIUM.equals(type) ||
                PNEUS_HARD.equals(type);
    }

    public boolean peutSortir() {
        return !abandonne && (statut.equals(STATUT_GARAGE) || statut.equals(STATUT_STANDS));
    }

    public boolean peutRentrer() {
        return !abandonne && statut.equals(STATUT_CIRCUIT);
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Pilote getPilote() {
        return pilote;
    }

    public void setPilote(Pilote pilote) {
        this.pilote = pilote;
        if (pilote != null) {
            pilote.setVoiture(this);
        }
    }

    public String getTypePneus() {
        return typePneus;
    }

    public void setTypePneus(String typePneus) {
        if (isValidTypePneus(typePneus)) {
            this.typePneus = typePneus;
        }
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public boolean isAbandonne() {
        return abandonne;
    }

    public void setAbandonne(boolean abandonne) {
        this.abandonne = abandonne;
    }

    @Override
    public String toString() {
        String piloteNom = (pilote != null) ? pilote.getNomComplet() : "Aucun pilote";
        return String.format("Voiture #%s - %s - Statut: %s - Pneus: %s%s",
                numero, piloteNom, statut, typePneus,
                abandonne ? " (ABANDONNÉ)" : "");
    }
}
