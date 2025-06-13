package com.f1manager.util;

public final class Constants {

    // Constantes pour les pneus
    public static final String PNEUS_SOFT = "soft";
    public static final String PNEUS_MEDIUM = "medium";
    public static final String PNEUS_HARD = "hard";

    // Constantes pour les statuts de voiture
    public static final String STATUT_GARAGE = "garage";
    public static final String STATUT_STANDS = "stands";
    public static final String STATUT_CIRCUIT = "circuit";

    // Constantes pour les types d'arrêts
    public static final String TYPE_CHANGEMENT_PNEUS = "changement_pneus";
    public static final String TYPE_ABANDON = "abandon";

    // Constantes pour les fichiers
    public static final String FICHIER_COURSES = "courses.json";
    public static final String FICHIER_BACKUP = "courses_backup.json";

    // Constantes pour les limites
    public static final int MAX_PILOTES_PAR_COURSE = 20;
    public static final int MIN_TOURS_COURSE = 1;
    public static final int MAX_TOURS_COURSE = 100;
    public static final int MIN_DUREE_TOUR = 30; // secondes
    public static final int MAX_DUREE_TOUR = 600; // secondes (10 minutes)

    // Constantes pour les numéros de pilotes
    public static final int MIN_NUMERO_PILOTE = 1;
    public static final int MAX_NUMERO_PILOTE = 99;

    // Constantes pour les noms
    public static final int MIN_LONGUEUR_NOM = 2;
    public static final int MAX_LONGUEUR_NOM = 50;

    // Messages d'erreur
    public static final String ERR_PILOTE_INEXISTANT = "Le pilote spécifié n'existe pas";
    public static final String ERR_PILOTE_DEJA_EN_PISTE = "Le pilote est déjà en piste";
    public static final String ERR_PILOTE_PAS_EN_PISTE = "Le pilote n'est pas en piste";
    public static final String ERR_PILOTE_ABANDONNE = "Le pilote a abandonné la course";
    public static final String ERR_COURSE_NON_DEMARREE = "La course n'a pas encore démarré";
    public static final String ERR_COURSE_TERMINEE = "La course est terminée";
    public static final String ERR_COURSE_EN_COURS = "La course est en cours";
    public static final String ERR_CIRCUIT_INVALIDE = "Le circuit configuré n'est pas valide";
    public static final String ERR_NUMERO_PILOTE_INVALIDE = "Le numéro de pilote doit être entre " + MIN_NUMERO_PILOTE
            + " et " + MAX_NUMERO_PILOTE;
    public static final String ERR_NOM_INVALIDE = "Le nom doit contenir entre " + MIN_LONGUEUR_NOM + " et "
            + MAX_LONGUEUR_NOM + " caractères";
    public static final String ERR_PNEUS_INVALIDES = "Type de pneus invalide. Utilisez: " + PNEUS_SOFT + ", "
            + PNEUS_MEDIUM + " ou " + PNEUS_HARD;
    public static final String ERR_SAUVEGARDE = "Erreur lors de la sauvegarde des données";
    public static final String ERR_CHARGEMENT = "Erreur lors du chargement des données";

    // Messages de succès
    public static final String MSG_PILOTE_AJOUTE = "Pilote ajouté avec succès";
    public static final String MSG_PILOTE_MODIFIE = "Pilote modifié avec succès";
    public static final String MSG_PILOTE_SUPPRIME = "Pilote supprimé avec succès";
    public static final String MSG_COURSE_DEMARREE = "Course démarrée avec succès";
    public static final String MSG_COURSE_TERMINEE = "Course terminée et sauvegardée";
    public static final String MSG_ARRET_EFFECTUE = "Arrêt aux stands effectué";
    public static final String MSG_ABANDON_EFFECTUE = "Pilote retiré de la course";
    public static final String MSG_SAUVEGARDE_OK = "Sauvegarde effectuée avec succès";
    public static final String MSG_CHARGEMENT_OK = "Données chargées avec succès";

    // Messages d'information
    public static final String INFO_AUCUN_PILOTE = "Aucun pilote enregistré";
    public static final String INFO_AUCUNE_COURSE = "Aucune course dans l'historique";
    public static final String INFO_COURSE_PRETE = "Course prête à démarrer";
    public static final String INFO_TOUS_PILOTES_GARAGE = "Tous les pilotes sont au garage";

    // Emojis pour l'interface
    public static final String EMOJI_SUCCES = "✅";
    public static final String EMOJI_ERREUR = "❌";
    public static final String EMOJI_ATTENTION = "⚠️";
    public static final String EMOJI_INFO = "ℹ️";
    public static final String EMOJI_COURSE = "🏁";
    public static final String EMOJI_VOITURE = "🚗";
    public static final String EMOJI_PILOTE = "👤";
    public static final String EMOJI_TEMPS = "⏱️";
    public static final String EMOJI_PNEUS = "🛞";
    public static final String EMOJI_GARAGE = "🏠";
    public static final String EMOJI_CIRCUIT = "🏎️";
    public static final String EMOJI_ABANDON = "🚫";

    // Constructeur privé pour empêcher l'instanciation
    private Constants() {
        throw new AssertionError("Cette classe ne doit pas être instanciée");
    }
}