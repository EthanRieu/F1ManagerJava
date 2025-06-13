package com.f1manager.exception;

public class CourseException extends Exception {
    
    /**
     * Code d'erreur pour identifier le type d'exception
     */
    private final String errorCode;
    
    /**
     * Constructeur avec message uniquement
     * @param message le message d'erreur
     */
    public CourseException(String message) {
        super(message);
        this.errorCode = "COURSE_ERROR";
    }
    
    /**
     * Constructeur avec message et code d'erreur
     * @param message le message d'erreur
     * @param errorCode le code d'erreur
     */
    public CourseException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode != null ? errorCode : "COURSE_ERROR";
    }
    
    /**
     * Constructeur avec message et cause
     * @param message le message d'erreur
     * @param cause la cause de l'exception
     */
    public CourseException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "COURSE_ERROR";
    }
    
    /**
     * Constructeur complet
     * @param message le message d'erreur
     * @param errorCode le code d'erreur
     * @param cause la cause de l'exception
     */
    public CourseException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode != null ? errorCode : "COURSE_ERROR";
    }
    
    /**
     * Obtient le code d'erreur
     * @return le code d'erreur
     */
    public String getErrorCode() {
        return errorCode;
    }
    
    /**
     * Méthodes statiques pour créer des exceptions spécifiques
     */
    
    /**
     * Exception pour une course non configurée
     * @return CourseException avec message approprié
     */
    public static CourseException courseNonConfiguree() {
        return new CourseException(
            "Aucune course n'est configurée. Veuillez d'abord configurer une course.",
            "COURSE_NOT_CONFIGURED"
        );
    }
    
    /**
     * Exception pour une course déjà démarrée
     * @return CourseException avec message approprié
     */
    public static CourseException courseDejaDemarree() {
        return new CourseException(
            "La course est déjà démarrée. Impossible de modifier la configuration.",
            "COURSE_ALREADY_STARTED"
        );
    }
    
    /**
     * Exception pour une course non démarrée
     * @return CourseException avec message approprié
     */
    public static CourseException courseNonDemarree() {
        return new CourseException(
            "La course n'a pas encore démarrée. Veuillez d'abord démarrer la course.",
            "COURSE_NOT_STARTED"
        );
    }
    
    /**
     * Exception pour une course terminée
     * @return CourseException avec message approprié
     */
    public static CourseException courseTerminee() {
        return new CourseException(
            "La course est terminée. Aucune action n'est possible.",
            "COURSE_FINISHED"
        );
    }
    
    /**
     * Exception pour un circuit invalide
     * @param raison la raison de l'invalidité
     * @return CourseException avec message approprié
     */
    public static CourseException circuitInvalide(String raison) {
        return new CourseException(
            "Circuit invalide: " + (raison != null ? raison : "configuration incorrecte"),
            "INVALID_CIRCUIT"
        );
    }
    
    /**
     * Exception pour un nombre insuffisant de pilotes
     * @param nombreActuel le nombre actuel de pilotes
     * @return CourseException avec message approprié
     */
    public static CourseException pasAssezPilotes(int nombreActuel) {
        return new CourseException(
            String.format("Nombre insuffisant de pilotes pour démarrer la course (%d pilote(s)). " +
                         "Au moins 1 pilote est requis.", nombreActuel),
            "INSUFFICIENT_DRIVERS"
        );
    }
    
    /**
     * Exception pour un trop grand nombre de pilotes
     * @param nombreActuel le nombre actuel de pilotes
     * @param maximum le nombre maximum autorisé
     * @return CourseException avec message approprié
     */
    public static CourseException tropPilotes(int nombreActuel, int maximum) {
        return new CourseException(
            String.format("Trop de pilotes sélectionnés (%d). Maximum autorisé: %d", 
                         nombreActuel, maximum),
            "TOO_MANY_DRIVERS"
        );
    }
    
    /**
     * Exception pour une erreur de sauvegarde
     * @param cause la cause de l'erreur
     * @return CourseException avec message approprié
     */
    public static CourseException erreurSauvegarde(Throwable cause) {
        return new CourseException(
            "Erreur lors de la sauvegarde de la course: " + 
            (cause != null ? cause.getMessage() : "erreur inconnue"),
            "SAVE_ERROR",
            cause
        );
    }
    
    /**
     * Exception pour une erreur de chargement
     * @param cause la cause de l'erreur
     * @return CourseException avec message approprié
     */
    public static CourseException erreurChargement(Throwable cause) {
        return new CourseException(
            "Erreur lors du chargement de la course: " + 
            (cause != null ? cause.getMessage() : "erreur inconnue"),
            "LOAD_ERROR",
            cause
        );
    }
    
    /**
     * Exception pour un tour invalide
     * @param tourActuel le tour actuel
     * @param tourMax le tour maximum
     * @return CourseException avec message approprié
     */
    public static CourseException tourInvalide(int tourActuel, int tourMax) {
        return new CourseException(
            String.format("Tour invalide: %d (maximum: %d)", tourActuel, tourMax),
            "INVALID_LAP"
        );
    }
    
    @Override
    public String toString() {
        return String.format("[%s] %s", errorCode, getMessage());
    }
}