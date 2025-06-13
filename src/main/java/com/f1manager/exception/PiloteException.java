package com.f1manager.exception;

import com.f1manager.util.Constants;

public class PiloteException extends Exception {

    /**
     * Code d'erreur pour identifier le type d'exception
     */
    private final String errorCode;

    /**
     * Numéro du pilote concerné (optionnel)
     */
    private final Integer numeroPilote;

    /**
     * Constructeur avec message uniquement
     * 
     * @param message le message d'erreur
     */
    public PiloteException(String message) {
        super(message);
        this.errorCode = "PILOTE_ERROR";
        this.numeroPilote = null;
    }

    /**
     * Constructeur avec message et numéro de pilote
     * 
     * @param message      le message d'erreur
     * @param numeroPilote le numéro du pilote concerné
     */
    public PiloteException(String message, int numeroPilote) {
        super(message);
        this.errorCode = "PILOTE_ERROR";
        this.numeroPilote = numeroPilote;
    }

    /**
     * Constructeur avec message et code d'erreur
     * 
     * @param message   le message d'erreur
     * @param errorCode le code d'erreur
     */
    public PiloteException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode != null ? errorCode : "PILOTE_ERROR";
        this.numeroPilote = null;
    }

    /**
     * Constructeur complet
     * 
     * @param message      le message d'erreur
     * @param errorCode    le code d'erreur
     * @param numeroPilote le numéro du pilote concerné
     */
    public PiloteException(String message, String errorCode, Integer numeroPilote) {
        super(message);
        this.errorCode = errorCode != null ? errorCode : "PILOTE_ERROR";
        this.numeroPilote = numeroPilote;
    }

    /**
     * Constructeur avec message et cause
     * 
     * @param message le message d'erreur
     * @param cause   la cause de l'exception
     */
    public PiloteException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "PILOTE_ERROR";
        this.numeroPilote = null;
    }

    /**
     * Constructeur complet avec cause
     * 
     * @param message      le message d'erreur
     * @param errorCode    le code d'erreur
     * @param numeroPilote le numéro du pilote concerné
     * @param cause        la cause de l'exception
     */
    public PiloteException(String message, String errorCode, Integer numeroPilote, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode != null ? errorCode : "PILOTE_ERROR";
        this.numeroPilote = numeroPilote;
    }

    /**
     * Obtient le code d'erreur
     * 
     * @return le code d'erreur
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * Obtient le numéro du pilote concerné
     * 
     * @return le numéro du pilote ou null si non spécifié
     */
    public Integer getNumeroPilote() {
        return numeroPilote;
    }

    /**
     * Méthodes statiques pour créer des exceptions spécifiques
     */

    /**
     * Exception pour un pilote inexistant
     * 
     * @param numero le numéro du pilote
     * @return PiloteException avec message approprié
     */
    public static PiloteException piloteInexistant(int numero) {
        return new PiloteException(
                String.format("Le pilote #%d n'existe pas", numero),
                "PILOTE_NOT_FOUND",
                numero);
    }

    /**
     * Exception pour un numéro de pilote déjà utilisé
     * 
     * @param numero le numéro déjà utilisé
     * @return PiloteException avec message approprié
     */
    public static PiloteException numeroDejaPris(int numero) {
        return new PiloteException(
                String.format("Le numéro #%d est déjà utilisé par un autre pilote", numero),
                "NUMERO_ALREADY_TAKEN",
                numero);
    }

    /**
     * Exception pour un numéro de pilote invalide
     * 
     * @param numero le numéro invalide
     * @return PiloteException avec message approprié
     */
    public static PiloteException numeroInvalide(int numero) {
        return new PiloteException(
                String.format("Numéro de pilote invalide: %d. Doit être entre %d et %d",
                        numero, Constants.MIN_NUMERO_PILOTE, Constants.MAX_NUMERO_PILOTE),
                "INVALID_NUMERO",
                numero);
    }

    /**
     * Exception pour un nom invalide
     * 
     * @param nom le nom invalide
     * @return PiloteException avec message approprié
     */
    public static PiloteException nomInvalide(String nom) {
        return new PiloteException(
                String.format("Nom invalide: '%s'. Doit contenir entre %d et %d caractères",
                        nom != null ? nom : "null",
                        Constants.MIN_LONGUEUR_NOM,
                        Constants.MAX_LONGUEUR_NOM),
                "INVALID_NAME");
    }

    /**
     * Exception pour un pilote déjà en piste
     * 
     * @param numero le numéro du pilote
     * @return PiloteException avec message approprié
     */
    public static PiloteException piloteDejaSurPiste(int numero) {
        return new PiloteException(
                String.format("Le pilote #%d est déjà en piste", numero),
                "PILOTE_ALREADY_ON_TRACK",
                numero);
    }

    /**
     * Exception pour un pilote pas en piste
     * 
     * @param numero le numéro du pilote
     * @return PiloteException avec message approprié
     */
    public static PiloteException pilotePasEnPiste(int numero) {
        return new PiloteException(
                String.format("Le pilote #%d n'est pas en piste", numero),
                "PILOTE_NOT_ON_TRACK",
                numero);
    }

    /**
     * Exception pour un pilote qui a abandonné
     * 
     * @param numero le numéro du pilote
     * @return PiloteException avec message approprié
     */
    public static PiloteException piloteAbandonne(int numero) {
        return new PiloteException(
                String.format("Le pilote #%d a abandonné la course", numero),
                "PILOTE_RETIRED",
                numero);
    }

    /**
     * Exception pour un pilote déjà dans la course
     * 
     * @param numero le numéro du pilote
     * @return PiloteException avec message approprié
     */
    public static PiloteException piloteDejaDansCourse(int numero) {
        return new PiloteException(
                String.format("Le pilote #%d est déjà sélectionné pour cette course", numero),
                "PILOTE_ALREADY_IN_RACE",
                numero);
    }

    /**
     * Exception pour un pilote pas dans la course
     * 
     * @param numero le numéro du pilote
     * @return PiloteException avec message approprié
     */
    public static PiloteException pilotePasDansCourse(int numero) {
        return new PiloteException(
                String.format("Le pilote #%d ne participe pas à cette course", numero),
                "PILOTE_NOT_IN_RACE",
                numero);
    }

    /**
     * Exception pour une suppression impossible
     * 
     * @param numero le numéro du pilote
     * @param raison la raison de l'impossibilité
     * @return PiloteException avec message approprié
     */
    public static PiloteException suppressionImpossible(int numero, String raison) {
        return new PiloteException(
                String.format("Impossible de supprimer le pilote #%d: %s",
                        numero, raison != null ? raison : "pilote en activité"),
                "CANNOT_DELETE_PILOTE",
                numero);
    }

    /**
     * Exception pour une voiture non assignée
     * 
     * @param numero le numéro du pilote
     * @return PiloteException avec message approprié
     */
    public static PiloteException voitureNonAssignee(int numero) {
        return new PiloteException(
                String.format("Aucune voiture n'est assignée au pilote #%d", numero),
                "NO_CAR_ASSIGNED",
                numero);
    }

    /**
     * Exception pour un changement de pneus impossible
     * 
     * @param numero le numéro du pilote
     * @param raison la raison de l'impossibilité
     * @return PiloteException avec message approprié
     */
    public static PiloteException changementPneusImpossible(int numero, String raison) {
        return new PiloteException(
                String.format("Impossible de changer les pneus du pilote #%d: %s",
                        numero, raison != null ? raison : "pilote pas aux stands"),
                "CANNOT_CHANGE_TYRES",
                numero);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(errorCode).append("]");

        if (numeroPilote != null) {
            sb.append(" Pilote #").append(numeroPilote).append(":");
        }

        sb.append(" ").append(getMessage());

        return sb.toString();
    }
}
