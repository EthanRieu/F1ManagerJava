package com.f1manager.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

/**
 * Classe utilitaire avec des méthodes helper pour l'application
 */
public final class Utils {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private static final DateTimeFormatter DATE_FORMATTER_COURT = DateTimeFormatter.ofPattern("dd/MM HH:mm");

    /**
     * Formate une date en string lisible (format long)
     * 
     * @param date la date à formater
     * @return la date formatée ou "Non défini" si null
     */
    public static String formaterDate(LocalDateTime date) {
        return date != null ? date.format(DATE_FORMATTER) : "Non défini";
    }

    /**
     * Formate une date en string lisible (format court)
     * 
     * @param date la date à formater
     * @return la date formatée ou "Non défini" si null
     */
    public static String formaterDateCourt(LocalDateTime date) {
        return date != null ? date.format(DATE_FORMATTER_COURT) : "Non défini";
    }

    /**
     * Formate une durée en secondes en format lisible
     * 
     * @param secondes la durée en secondes
     * @return la durée formatée (ex: "1h 23m 45s", "2m 30s", "45s")
     */
    public static String formaterDuree(int secondes) {
        if (secondes < 0) {
            return "0s";
        }

        if (secondes < 60) {
            return secondes + "s";
        } else if (secondes < 3600) {
            int minutes = secondes / 60;
            int sec = secondes % 60;
            return sec > 0 ? String.format("%dm %02ds", minutes, sec) : minutes + "m";
        } else {
            int heures = secondes / 3600;
            int minutes = (secondes % 3600) / 60;
            int sec = secondes % 60;

            StringBuilder result = new StringBuilder();
            result.append(heures).append("h");

            if (minutes > 0) {
                result.append(String.format(" %02dm", minutes));
            }
            if (sec > 0) {
                result.append(String.format(" %02ds", sec));
            }

            return result.toString();
        }
    }

    /**
     * Valide un numéro de pilote
     * 
     * @param numero le numéro à valider
     * @return true si le numéro est valide
     */
    public static boolean isNumeroPiloteValide(int numero) {
        return numero >= Constants.MIN_NUMERO_PILOTE && numero <= Constants.MAX_NUMERO_PILOTE;
    }

    /**
     * Valide un nom/prénom
     * 
     * @param nom le nom à valider
     * @return true si le nom est valide
     */
    public static boolean isNomValide(String nom) {
        return nom != null &&
                !nom.trim().isEmpty() &&
                nom.trim().length() >= Constants.MIN_LONGUEUR_NOM &&
                nom.trim().length() <= Constants.MAX_LONGUEUR_NOM;
    }

    /**
     * Valide un type de pneus
     * 
     * @param typePneus le type à valider
     * @return true si le type est valide
     */
    public static boolean isTypePneusValide(String typePneus) {
        return Constants.PNEUS_SOFT.equals(typePneus) ||
                Constants.PNEUS_MEDIUM.equals(typePneus) ||
                Constants.PNEUS_HARD.equals(typePneus);
    }

    /**
     * Valide un circuit
     * 
     * @param nom          nom du circuit
     * @param nbTours      nombre de tours
     * @param dureeParTour durée par tour en secondes
     * @return true si le circuit est valide
     */
    public static boolean isCircuitValide(String nom, int nbTours, int dureeParTour) {
        return isNomValide(nom) &&
                nbTours >= Constants.MIN_TOURS_COURSE &&
                nbTours <= Constants.MAX_TOURS_COURSE &&
                dureeParTour >= Constants.MIN_DUREE_TOUR &&
                dureeParTour <= Constants.MAX_DUREE_TOUR;
    }

    /**
     * Affiche une liste avec numérotation
     * 
     * @param liste la liste à afficher
     * @param <T>   le type des éléments de la liste
     */
    public static <T> void afficherListeNumerotee(List<T> liste) {
        afficherListeNumerotee(liste, "  ");
    }

    /**
     * Affiche une liste avec numérotation et indentation personnalisée
     * 
     * @param liste       la liste à afficher
     * @param indentation l'indentation à utiliser
     * @param <T>         le type des éléments de la liste
     */
    public static <T> void afficherListeNumerotee(List<T> liste, String indentation) {
        if (liste == null || liste.isEmpty()) {
            System.out.println(indentation + "(Aucun élément)");
            return;
        }

        for (int i = 0; i < liste.size(); i++) {
            System.out.println(indentation + (i + 1) + ". " + liste.get(i).toString());
        }
    }

    /**
     * Affiche un séparateur
     */
    public static void afficherSeparateur() {
        afficherSeparateur(50);
    }

    /**
     * Affiche un séparateur de largeur personnalisée
     * 
     * @param largeur la largeur du séparateur
     */
    public static void afficherSeparateur(int largeur) {
        System.out.println("=".repeat(Math.max(1, largeur)));
    }

    /**
     * Affiche un titre centré avec séparateurs
     * 
     * @param titre le titre à afficher
     */
    public static void afficherTitre(String titre) {
        afficherTitre(titre, 50);
    }

    /**
     * Affiche un titre centré avec séparateurs et largeur personnalisée
     * 
     * @param titre   le titre à afficher
     * @param largeur la largeur totale
     */
    public static void afficherTitre(String titre, int largeur) {
        afficherSeparateur(largeur);
        System.out.println(centrerTexte(titre, largeur));
        afficherSeparateur(largeur);
    }

    /**
     * Centre un texte sur une largeur donnée
     * 
     * @param texte   le texte à centrer
     * @param largeur la largeur totale
     * @return le texte centré
     */
    public static String centrerTexte(String texte, int largeur) {
        if (texte == null)
            texte = "";

        if (texte.length() >= largeur) {
            return texte;
        }

        int espaces = (largeur - texte.length()) / 2;
        return " ".repeat(espaces) + texte;
    }

    /**
     * Demande confirmation à l'utilisateur
     * 
     * @param scanner le scanner pour lire l'input
     * @param message le message de confirmation
     * @return true si l'utilisateur confirme
     */
    public static boolean demanderConfirmation(Scanner scanner, String message) {
        System.out.print(message + " (oui/non): ");
        String reponse = scanner.nextLine().trim().toLowerCase();
        return "oui".equals(reponse) || "o".equals(reponse) ||
                "yes".equals(reponse) || "y".equals(reponse);
    }

    /**
     * Nettoie une chaîne de caractères (trim + null check)
     * 
     * @param input la chaîne à nettoyer
     * @return la chaîne nettoyée ou null
     */
    public static String nettoyerChaine(String input) {
        return input == null ? null : input.trim();
    }

    /**
     * Vérifie si une chaîne est vide ou null
     * 
     * @param input la chaîne à vérifier
     * @return true si la chaîne est vide ou null
     */
    public static boolean estVide(String input) {
        return input == null || input.trim().isEmpty();
    }

    /**
     * Capitalise la première lettre d'une chaîne
     * 
     * @param input la chaîne à capitaliser
     * @return la chaîne avec la première lettre en majuscule
     */
    public static String capitaliser(String input) {
        if (estVide(input)) {
            return input;
        }

        String cleaned = nettoyerChaine(input);
        if (cleaned == null || cleaned.length() == 0) {
            return cleaned;
        }

        return cleaned.substring(0, 1).toUpperCase() +
                (cleaned.length() > 1 ? cleaned.substring(1).toLowerCase() : "");
    }

    /**
     * Convertit un temps en millisecondes en secondes
     * 
     * @param millis le temps en millisecondes
     * @return le temps en secondes
     */
    public static int millisToSeconds(long millis) {
        return (int) (millis / 1000);
    }

    /**
     * Affiche un message avec emoji
     * 
     * @param emoji   l'emoji à afficher
     * @param message le message
     */
    public static void afficherMessage(String emoji, String message) {
        System.out.println(emoji + " " + message);
    }

    /**
     * Affiche un message de succès
     * 
     * @param message le message
     */
    public static void afficherSucces(String message) {
        afficherMessage(Constants.EMOJI_SUCCES, message);
    }

    /**
     * Affiche un message d'erreur
     * 
     * @param message le message
     */
    public static void afficherErreur(String message) {
        afficherMessage(Constants.EMOJI_ERREUR, message);
    }

    /**
     * Affiche un message d'attention
     * 
     * @param message le message
     */
    public static void afficherAttention(String message) {
        afficherMessage(Constants.EMOJI_ATTENTION, message);
    }

    /**
     * Affiche un message d'information
     * 
     * @param message le message
     */
    public static void afficherInfo(String message) {
        afficherMessage(Constants.EMOJI_INFO, message);
    }

    // Constructeur privé pour empêcher l'instanciation
    private Utils() {
        throw new AssertionError("Cette classe ne doit pas être instanciée");
    }
}