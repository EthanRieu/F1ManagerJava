package com.f1manager.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.f1manager.model.ArretAuxStands;
import com.f1manager.model.Circuit;
import com.f1manager.model.Course;
import com.f1manager.model.Pilote;
import com.f1manager.model.VoitureF1;
import com.f1manager.service.CourseService;
import com.f1manager.service.PiloteService;
import com.f1manager.service.VoitureService;

public class ApplicationController {
    private PiloteService piloteService;
    private VoitureService voitureService;
    private CourseService courseService;
    private Scanner scanner;
    private boolean running;

    public ApplicationController() {
        this.piloteService = new PiloteService();
        this.voitureService = new VoitureService();
        this.courseService = new CourseService(piloteService);
        this.scanner = new Scanner(System.in);
        this.running = true;
    }

    public String processCommand(String command) {
        String[] parts = command.trim().split("\\s+");
        if (parts.length == 0)
            return "Commande invalide";

        switch (parts[0].toLowerCase()) {
            case "help":
                return getHelpText();
            case "pilotes":
                return afficherListePilotesGUI();
            case "ajouter":
                if (parts.length < 4)
                    return "Usage: ajouter <nom> <prenom> <numero>";
                return ajouterPiloteGUI(parts[1], parts[2], Integer.parseInt(parts[3]));
            case "modifier":
                if (parts.length < 4)
                    return "Usage: modifier <numero> <nom> <prenom>";
                return modifierPiloteGUI(Integer.parseInt(parts[1]), parts[2], parts[3]);
            case "supprimer":
                if (parts.length < 2)
                    return "Usage: supprimer <numero>";
                return supprimerPiloteGUI(Integer.parseInt(parts[1]));
            case "sortir":
                if (parts.length < 2)
                    return "Usage: sortir <numero>";
                return faireSortirPiloteGUI(Integer.parseInt(parts[1]));
            case "rentrer":
                if (parts.length < 2)
                    return "Usage: rentrer <numero>";
                return faireRentrerPiloteGUI(Integer.parseInt(parts[1]));
            case "course":
                return gererCourseGUI();
            case "config":
                return configurerCourseGUI();
            case "historique":
                return voirHistoriqueGUI();
            case "quitter":
                running = false;
                return "Au revoir !";
            default:
                return "Commande non reconnue. Tapez 'help' pour voir les commandes disponibles.";
        }
    }

    private String getHelpText() {
        StringBuilder help = new StringBuilder();
        help.append("=== Commandes disponibles ===\n");
        help.append("help - Affiche cette aide\n");
        help.append("pilotes - Liste tous les pilotes\n");
        help.append("ajouter <nom> <prenom> <numero> - Ajoute un nouveau pilote\n");
        help.append("modifier <numero> <nom> <prenom> - Modifie un pilote existant\n");
        help.append("supprimer <numero> - Supprime un pilote\n");
        help.append("sortir <numero> - Fait sortir un pilote\n");
        help.append("rentrer <numero> - Fait rentrer un pilote\n");
        help.append("course - Gère la course en cours\n");
        help.append("config - Configure une nouvelle course\n");
        help.append("historique - Affiche l'historique des courses\n");
        help.append("quitter - Quitte l'application\n");
        return help.toString();
    }

    private String afficherListePilotesGUI() {
        List<Pilote> pilotes = piloteService.obtenirTousLesPilotes();
        StringBuilder result = new StringBuilder();

        if (pilotes.isEmpty()) {
            return "Aucun pilote enregistré.";
        }

        result.append("=== Liste des Pilotes ===\n");
        for (Pilote pilote : pilotes) {
            result.append(pilote.toString()).append("\n");
        }

        return result.toString();
    }

    private String ajouterPiloteGUI(String nom, String prenom, int numero) {
        if (piloteService.ajouterPilote(nom, prenom, numero)) {
            return "✅ Pilote " + prenom + " " + nom + " (#" + numero + ") ajouté avec succès !";
        } else {
            return "❌ Impossible d'ajouter le pilote. Vérifiez les données ou le numéro existe déjà.";
        }
    }

    private String modifierPiloteGUI(int numero, String nouveauNom, String nouveauPrenom) {
        Pilote pilote = piloteService.trouverPiloteParNumero(numero);
        if (pilote == null) {
            return "❌ Pilote non trouvé.";
        }

        if (piloteService.modifierPilote(numero, nouveauNom, nouveauPrenom)) {
            return "✅ Pilote modifié avec succès !";
        } else {
            return "❌ Erreur lors de la modification.";
        }
    }

    private String supprimerPiloteGUI(int numero) {
        Pilote pilote = piloteService.trouverPiloteParNumero(numero);
        if (pilote == null) {
            return "❌ Pilote non trouvé.";
        }

        if (piloteService.supprimerPilote(numero)) {
            return "✅ Pilote supprimé avec succès !";
        } else {
            return "❌ Impossible de supprimer le pilote (peut-être en course).";
        }
    }

    private String faireSortirPiloteGUI(int numero) {
        if (piloteService.faireSortirPilote(numero)) {
            return "✅ Pilote sorti avec succès !";
        } else {
            return "❌ Impossible de faire sortir le pilote.";
        }
    }

    private String faireRentrerPiloteGUI(int numero) {
        if (piloteService.faireRentrerPilote(numero)) {
            return "✅ Pilote rentré avec succès !";
        } else {
            return "❌ Impossible de faire rentrer le pilote.";
        }
    }

    private String gererCourseGUI() {
        if (!courseService.estCourseConfiguree()) {
            return "❌ Aucune course n'est configurée. Utilisez la commande 'config' d'abord.";
        }

        if (!courseService.estCourseEnCours()) {
            return "❌ Aucune course n'est en cours. Utilisez la commande 'config' pour configurer une course.";
        }

        StringBuilder result = new StringBuilder();
        result.append("=== État de la Course ===\n");
        result.append(courseService.getStatutCourse());
        return result.toString();
    }

    private String configurerCourseGUI() {
        if (courseService.estCourseEnCours()) {
            return "❌ Une course est déjà en cours. Terminez-la d'abord.";
        }

        courseService.reinitialiserCourse();
        return "✅ Course réinitialisée. Utilisez les commandes suivantes pour configurer la course:\n" +
                "- circuit <nom> <longueur> <nombreTours>\n" +
                "- ajouter <numero>\n" +
                "- retirer <numero>\n" +
                "- demarrer";
    }

    private String voirHistoriqueGUI() {
        return "Historique des courses non disponible (fonctionnalité à implémenter).";
    }

    // Point d'entrée principal
    public void demarrerApplication() {
        System.out.println("=== BIENVENUE DANS LE GESTIONNAIRE D'ÉCURIE F1 ===\n");

        while (running) {
            afficherMenuPrincipal();
            int choix = lireEntier("Votre choix: ");
            traiterChoixPrincipal(choix);
        }

        System.out.println("\nMerci d'avoir utilisé le gestionnaire F1. À bientôt !");
        scanner.close();
    }

    // Menu principal
    private void afficherMenuPrincipal() {
        System.out.println("\n=== MENU PRINCIPAL ===");
        System.out.println("1. Gestion des Pilotes");
        System.out.println("2. Configuration de Course");
        System.out.println("3. Démarrer/Gérer Course");
        System.out.println("4. Voir Historique");
        System.out.println("5. Quitter");
        System.out.print("-> ");
    }

    private void traiterChoixPrincipal(int choix) {
        System.out.println();

        switch (choix) {
            case 1 -> gererPilotes();
            case 2 -> configurerCourse();
            case 3 -> gererCourse();
            case 4 -> voirHistorique();
            case 5 -> {
                running = false;
                return;
            }
            default -> System.out.println("❌ Choix invalide. Veuillez choisir entre 1 et 5.");
        }

        attendreEntree();
    }

    // ===== GESTION DES PILOTES =====
    private void gererPilotes() {
        boolean retour = false;

        while (!retour) {
            System.out.println("=== GESTION DES PILOTES ===");
            System.out.println("1. Ajouter Pilote");
            System.out.println("2. Modifier Pilote");
            System.out.println("3. Supprimer Pilote");
            System.out.println("4. Voir Liste Pilotes");
            System.out.println("5. Faire Sortir Pilote");
            System.out.println("6. Faire Rentrer Pilote");
            System.out.println("7. Retour");

            int choix = lireEntier("Votre choix: ");

            switch (choix) {
                case 1 -> ajouterPilote();
                case 2 -> modifierPilote();
                case 3 -> supprimerPilote();
                case 4 -> afficherListePilotes();
                case 5 -> faireSortirPilote();
                case 6 -> faireRentrerPilote();
                case 7 -> retour = true;
                default -> System.out.println("❌ Choix invalide.");
            }

            if (!retour) {
                attendreEntree();
            }
        }
    }

    private void ajouterPilote() {
        System.out.println("--- Ajouter un Pilote ---");

        String nom = lireChaine("Nom du pilote: ");
        String prenom = lireChaine("Prénom du pilote: ");
        int numero = lireEntier("Numéro du pilote: ");

        if (piloteService.ajouterPilote(nom, prenom, numero)) {
            System.out.println("✅ Pilote " + prenom + " " + nom + " (#" + numero + ") ajouté avec succès !");
        } else {
            System.out.println("❌ Impossible d'ajouter le pilote. Vérifiez les données ou le numéro existe déjà.");
        }
    }

    private void modifierPilote() {
        System.out.println("--- Modifier un Pilote ---");

        afficherListePilotes();
        int numero = lireEntier("Numéro du pilote à modifier: ");

        Pilote pilote = piloteService.trouverPiloteParNumero(numero);
        if (pilote == null) {
            System.out.println("❌ Pilote non trouvé.");
            return;
        }

        System.out.println("Pilote actuel: " + pilote.toString());
        System.out.println("(Laissez vide pour conserver la valeur actuelle)");

        String nouveauNom = lireChaine("Nouveau nom [" + pilote.getNom() + "]: ");
        String nouveauPrenom = lireChaine("Nouveau prénom [" + pilote.getPrenom() + "]: ");

        if (piloteService.modifierPilote(numero, nouveauNom, nouveauPrenom)) {
            System.out.println("✅ Pilote modifié avec succès !");
        } else {
            System.out.println("❌ Erreur lors de la modification.");
        }
    }

    private void supprimerPilote() {
        System.out.println("--- Supprimer un Pilote ---");

        afficherListePilotes();
        int numero = lireEntier("Numéro du pilote à supprimer: ");

        Pilote pilote = piloteService.trouverPiloteParNumero(numero);
        if (pilote == null) {
            System.out.println("❌ Pilote non trouvé.");
            return;
        }

        System.out.println("Pilote à supprimer: " + pilote.toString());
        String confirmation = lireChaine("Êtes-vous sûr ? (oui/non): ");

        if ("oui".equalsIgnoreCase(confirmation.trim())) {
            if (piloteService.supprimerPilote(numero)) {
                System.out.println("✅ Pilote supprimé avec succès !");
            } else {
                System.out.println("❌ Impossible de supprimer le pilote (peut-être en course).");
            }
        } else {
            System.out.println("Suppression annulée.");
        }
    }

    private void afficherListePilotes() {
        List<Pilote> pilotes = piloteService.obtenirTousLesPilotes();

        if (pilotes.isEmpty()) {
            System.out.println("Aucun pilote enregistré.");
            return;
        }

        System.out.println("--- Liste des Pilotes ---");
        for (Pilote pilote : pilotes) {
            System.out.println(pilote.toString());
        }
    }

    private void faireSortirPilote() {
        System.out.println("--- Faire Sortir un Pilote ---");

        List<Pilote> pilotesDisponibles = piloteService.obtenirPilotesDisponibles();
        if (pilotesDisponibles.isEmpty()) {
            System.out.println("Aucun pilote disponible pour sortir.");
            return;
        }

        System.out.println("Pilotes disponibles:");
        pilotesDisponibles.forEach(System.out::println);

        int numero = lireEntier("Numéro du pilote à faire sortir: ");

        if (piloteService.faireSortirPilote(numero)) {
            System.out.println("✅ Pilote #" + numero + " est maintenant en piste !");
        } else {
            System.out.println("❌ Impossible de faire sortir ce pilote.");
        }
    }

    private void faireRentrerPilote() {
        System.out.println("--- Faire Rentrer un Pilote ---");

        List<Pilote> pilotesEnPiste = piloteService.obtenirPilotesEnPiste();
        if (pilotesEnPiste.isEmpty()) {
            System.out.println("Aucun pilote en piste.");
            return;
        }

        System.out.println("Pilotes en piste:");
        pilotesEnPiste.forEach(System.out::println);

        int numero = lireEntier("Numéro du pilote à faire rentrer: ");

        if (piloteService.faireRentrerPilote(numero)) {
            System.out.println("✅ Pilote #" + numero + " est maintenant aux stands !");
        } else {
            System.out.println("❌ Impossible de faire rentrer ce pilote.");
        }
    }

    // ===== CONFIGURATION DE COURSE =====
    private void configurerCourse() {
        boolean retour = false;

        while (!retour) {
            System.out.println("=== CONFIGURATION DE COURSE ===");

            Course courseActuelle = courseService.getCourseActuelle();
            if (courseActuelle != null) {
                System.out.println("Course préparée: " + courseActuelle.toString());
            }

            System.out.println("1. Définir Circuit");
            System.out.println("2. Sélectionner Pilotes");
            System.out.println("3. Voir Configuration Actuelle");
            System.out.println("4. Réinitialiser Course");
            System.out.println("5. Retour");

            int choix = lireEntier("Votre choix: ");

            switch (choix) {
                case 1 -> definirCircuit();
                case 2 -> selectionnerPilotes();
                case 3 -> voirConfigurationCourse();
                case 4 -> reinitialiserCourse();
                case 5 -> retour = true;
                default -> System.out.println("❌ Choix invalide.");
            }

            if (!retour) {
                attendreEntree();
            }
        }
    }

    private void definirCircuit() {
        System.out.println("--- Définir le Circuit ---");

        String nom = lireChaine("Nom du circuit: ");
        if (nom == null || nom.isEmpty()) {
            System.out.println("❌ Le nom du circuit est obligatoire.");
            return;
        }

        int nombreTours = lireEntier("Nombre de tours: ");
        if (nombreTours <= 0) {
            System.out.println("❌ Le nombre de tours doit être positif.");
            return;
        }

        int dureeParTour = lireEntier("Durée par tour (en secondes): ");
        if (dureeParTour <= 0) {
            System.out.println("❌ La durée par tour doit être positive.");
            return;
        }

        Circuit circuit = new Circuit(nom, nombreTours, dureeParTour);

        if (courseService.creerNouvelleCourse(circuit)) {
            System.out.println("✅ Circuit configuré avec succès !");
            System.out.println(circuit.toString());
        } else {
            System.out.println("❌ Erreur lors de la configuration du circuit.");
        }
    }

    private void selectionnerPilotes() {
        System.out.println("--- Sélectionner les Pilotes ---");

        if (!courseService.aCoursePreparee()) {
            System.out.println("❌ Veuillez d'abord configurer un circuit.");
            return;
        }

        Course course = courseService.getCourseActuelle();

        boolean continuer = true;
        while (continuer) {
            System.out.println("\nPilotes actuellement sélectionnés: " + course.getPilotes().size());
            course.getPilotes().forEach(p -> System.out.println("  - " + p.toString()));

            System.out.println("\n1. Ajouter Pilote");
            System.out.println("2. Retirer Pilote");
            System.out.println("3. Voir Pilotes Disponibles");
            System.out.println("4. Terminer Sélection");

            int choix = lireEntier("Votre choix: ");

            switch (choix) {
                case 1 -> ajouterPiloteCourse();
                case 2 -> retirerPiloteCourse();
                case 3 -> afficherListePilotes();
                case 4 -> continuer = false;
                default -> System.out.println("❌ Choix invalide.");
            }
        }
    }

    private void ajouterPiloteCourse() {
        List<Pilote> pilotesDisponibles = piloteService.obtenirTousLesPilotes();
        Course course = courseService.getCourseActuelle();

        // Filtrer les pilotes déjà dans la course
        pilotesDisponibles = pilotesDisponibles.stream()
                .filter(p -> !course.getPilotes().contains(p))
                .toList();

        if (pilotesDisponibles.isEmpty()) {
            System.out.println("❌ Aucun pilote disponible.");
            return;
        }

        System.out.println("Pilotes disponibles:");
        pilotesDisponibles.forEach(System.out::println);

        int numero = lireEntier("Numéro du pilote à ajouter: ");

        if (courseService.ajouterPiloteACourse(numero)) {
            System.out.println("✅ Pilote #" + numero + " ajouté à la course !");
        } else {
            System.out.println("❌ Impossible d'ajouter ce pilote.");
        }
    }

    private void retirerPiloteCourse() {
        Course course = courseService.getCourseActuelle();

        if (course.getPilotes().isEmpty()) {
            System.out.println("❌ Aucun pilote sélectionné pour la course.");
            return;
        }

        System.out.println("Pilotes dans la course:");
        course.getPilotes().forEach(System.out::println);

        int numero = lireEntier("Numéro du pilote à retirer: ");

        if (courseService.supprimerPiloteDeCourse(numero)) {
            System.out.println("✅ Pilote #" + numero + " retiré de la course !");
        } else {
            System.out.println("❌ Impossible de retirer ce pilote.");
        }
    }

    private void voirConfigurationCourse() {
        Course course = courseService.getCourseActuelle();

        if (course == null) {
            System.out.println("❌ Aucune course configurée.");
            return;
        }

        System.out.println("=== CONFIGURATION ACTUELLE ===");

        if (course.getCircuit() != null) {
            System.out.println("Circuit: " + course.getCircuit().toString());
        } else {
            System.out.println("Circuit: Non configuré");
        }

        System.out.println("Pilotes sélectionnés: " + course.getPilotes().size());
        course.getPilotes().forEach(p -> System.out.println("  - " + p.toString()));

        System.out.println("Statut: " + course.getStatutCourse());
        System.out.println("Peut démarrer: " + (course.peutDemarrer() ? "✅ Oui" : "❌ Non"));
    }

    private void reinitialiserCourse() {
        String confirmation = lireChaine("Êtes-vous sûr de vouloir réinitialiser la course ? (oui/non): ");

        if ("oui".equalsIgnoreCase(confirmation)) {
            courseService.reinitialiser();
            System.out.println("✅ Course réinitialisée.");
        } else {
            System.out.println("Réinitialisation annulée.");
        }
    }

    // ===== GESTION DE COURSE =====
    private void gererCourse() {
        Course course = courseService.getCourseActuelle();

        if (course == null) {
            System.out.println("❌ Aucune course configurée. Veuillez d'abord configurer une course.");
            return;
        }

        if (!course.peutDemarrer() && !course.isCourseDemarree()) {
            System.out.println("❌ La course ne peut pas démarrer. Vérifiez la configuration.");
            voirConfigurationCourse();
            return;
        }

        boolean retour = false;

        while (!retour) {
            afficherStatutCourse();

            if (!course.isCourseDemarree()) {
                // Menu pré-démarrage
                System.out.println("1. Démarrer la Course");
                System.out.println("2. Voir Configuration");
                System.out.println("3. Retour");

                int choix = lireEntier("Votre choix: ");

                switch (choix) {
                    case 1 -> {
                        if (demarrerCourse()) {
                            System.out.println("🏁 Course démarrée avec succès !");
                        }
                    }
                    case 2 -> voirConfigurationCourse();
                    case 3 -> retour = true;
                    default -> System.out.println("❌ Choix invalide.");
                }
            } else if (course.estEnCours()) {
                // Menu course en cours
                System.out.println("1. Passer au Tour Suivant");
                System.out.println("2. Gérer Arrêt aux Stands");
                System.out.println("3. Voir Historique Arrêts");
                System.out.println("4. Terminer Course");
                System.out.println("5. Sauvegarder Course");
                System.out.println("6. Retour");

                int choix = lireEntier("Votre choix: ");

                switch (choix) {
                    case 1 -> passerTourSuivant();
                    case 2 -> gererArretAuxStands();
                    case 3 -> voirHistoriqueArrets();
                    case 4 -> {
                        if (terminerCourse()) {
                            System.out.println("🏁 Course terminée et sauvegardée !");
                        }
                    }
                    case 5 -> {
                        System.out.println("Sauvegarde manuelle non disponible (fonctionnalité à implémenter).");
                    }
                    case 6 -> retour = true;
                    default -> System.out.println("❌ Choix invalide.");
                }
            } else {
                // Course terminée
                System.out.println("1. Voir Résultats");
                System.out.println("2. Nouvelle Course");
                System.out.println("3. Retour");

                int choix = lireEntier("Votre choix: ");

                switch (choix) {
                    case 1 -> voirResultatsCourse();
                    case 2 -> {
                        courseService.reinitialiser();
                        System.out.println("✅ Prêt pour une nouvelle course !");
                    }
                    case 3 -> retour = true;
                    default -> System.out.println("❌ Choix invalide.");
                }
            }

            if (!retour) {
                attendreEntree();
            }
        }
    }

    private void afficherStatutCourse() {
        Course course = courseService.getCourseActuelle();

        System.out.println("\n=== STATUT DE LA COURSE ===");

        if (course.getCircuit() != null) {
            System.out.println("🏁 Circuit: " + course.getCircuit().getNom());
            System.out.println("📊 Tour: " + course.getTourActuel() + "/" + course.getCircuit().getNombreTours());
            System.out.println("⏱️  Tours restants: " + course.getToursRestants());
        }

        System.out.println("🚗 Statut: " + course.getStatutCourse());
        System.out.println("👥 Pilotes en course: " + course.getNombrePilotesEnCourse());
        System.out.println("🚫 Pilotes abandonnés: " + course.getNombrePilotesAbandonnes());

        System.out.println("\n--- Statut des Voitures ---");
        for (VoitureF1 voiture : course.getVoitures()) {
            System.out.println(voiture.toString());
        }

        System.out.println();
    }

    private boolean demarrerCourse() {
        if (courseService.demarrerCourse()) {
            return true;
        } else {
            System.out.println("❌ Impossible de démarrer la course.");
            return false;
        }
    }

    private void passerTourSuivant() {
        if (courseService.passerAuTourSuivant()) {
            System.out.println("✅ Passage au tour suivant !");
        } else {
            System.out.println("🏁 Course terminée ! Tous les tours sont effectués.");
        }
    }

    private boolean terminerCourse() {
        String confirmation = lireChaine("Êtes-vous sûr de vouloir terminer la course ? (oui/non): ");

        if ("oui".equalsIgnoreCase(confirmation)) {
            return courseService.terminerCourse();
        }

        System.out.println("Annulation de la fin de course.");
        return false;
    }

    private void gererArretAuxStands() {
        Course course = courseService.getCourseActuelle();

        // Afficher les pilotes en piste
        List<Pilote> pilotesEnPiste = course.getPilotesEnCourse().stream()
                .filter(Pilote::isEnPiste)
                .toList();

        if (pilotesEnPiste.isEmpty()) {
            System.out.println("❌ Aucun pilote en piste pour un arrêt aux stands.");
            return;
        }

        System.out.println("--- Arrêt aux Stands ---");
        System.out.println("Pilotes en piste:");
        pilotesEnPiste.forEach(System.out::println);

        int numero = lireEntier("Numéro du pilote à faire rentrer: ");

        Pilote pilote = piloteService.trouverPiloteParNumero(numero);
        if (pilote == null || !pilote.isEnPiste()) {
            System.out.println("❌ Pilote non trouvé ou pas en piste.");
            return;
        }

        System.out.println("\n--- Type d'Arrêt ---");
        System.out.println("1. Changement de Pneus");
        System.out.println("2. Retour au Garage (Abandon)");
        System.out.println("3. Annuler");

        int choixArret = lireEntier("Votre choix: ");

        switch (choixArret) {
            case 1 -> effectuerChangementPneus(pilote);
            case 2 -> effectuerAbandon(pilote);
            case 3 -> System.out.println("Arrêt annulé.");
            default -> System.out.println("❌ Choix invalide.");
        }
    }

    private void effectuerChangementPneus(Pilote pilote) {
        System.out.println("--- Changement de Pneus ---");
        System.out.println("Pneus actuels: " + pilote.getVoiture().getTypePneus());
        System.out.println("1. Soft");
        System.out.println("2. Medium");
        System.out.println("3. Hard");
        System.out.println("4. Annuler");

        int choixPneus = lireEntier("Type de pneus: ");

        String typePneus = switch (choixPneus) {
            case 1 -> VoitureF1.PNEUS_SOFT;
            case 2 -> VoitureF1.PNEUS_MEDIUM;
            case 3 -> VoitureF1.PNEUS_HARD;
            case 4 -> null;
            default -> {
                System.out.println("❌ Choix invalide.");
                yield null;
            }
        };

        if (typePneus != null) {
            if (courseService.effectuerArretAuxStands(pilote.getNumero(), typePneus)) {
                System.out.println("✅ Changement de pneus effectué ! Pilote de retour en piste.");
            } else {
                System.out.println("❌ Erreur lors du changement de pneus.");
            }
        }
    }

    private void effectuerAbandon(Pilote pilote) {
        String confirmation = lireChaine("Confirmer l'abandon du pilote " + pilote.getNomComplet() + " ? (oui/non): ");

        if ("oui".equalsIgnoreCase(confirmation)) {
            if (courseService.rentrerPiloteAuGarage(pilote.getNumero())) {
                System.out.println("🚫 Pilote " + pilote.getNomComplet() + " a abandonné la course.");
            } else {
                System.out.println("❌ Erreur lors de l'abandon.");
            }
        } else {
            System.out.println("Abandon annulé.");
        }
    }

    private void voirHistoriqueArrets() {
        List<ArretAuxStands> arrets = courseService.getHistoriqueArrets();

        if (arrets.isEmpty()) {
            System.out.println("Aucun arrêt effectué pour le moment.");
            return;
        }

        System.out.println("=== HISTORIQUE DES ARRÊTS ===");
        arrets.forEach(System.out::println);
    }

    private void voirResultatsCourse() {
        Course course = courseService.getCourseActuelle();

        System.out.println("=== RÉSULTATS DE LA COURSE ===");
        System.out.println("Circuit: " + course.getCircuit().getNom());
        System.out.println("Tours effectués: " + (course.getTourActuel() - 1));

        System.out.println("\n--- Pilotes Finissants ---");
        course.getPilotesEnCourse().forEach(p -> {
            System.out.println("✅ " + p.toString() + " - Pneus: " + p.getVoiture().getTypePneus());
        });

        System.out.println("\n--- Pilotes Abandonnés ---");
        course.getPilotesAbandonnes().forEach(p -> {
            System.out.println("🚫 " + p.toString());
        });

        System.out.println("\n--- Historique des Arrêts ---");
        voirHistoriqueArrets();
    }

    // ===== HISTORIQUE =====
    private void voirHistorique() {
        System.out.println("=== HISTORIQUE DES COURSES ===");
        System.out.println("Fonctionnalité d'historique non disponible (à implémenter avec base de données).");
    }

    // ===== UTILITAIRES =====
    private String lireChaine(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        return input.isEmpty() ? null : input;
    }

    private int lireEntier(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("❌ Veuillez entrer un nombre valide.");
            }
        }
    }

    private void attendreEntree() {
        System.out.print("\nAppuyez sur Entrée pour continuer...");
        scanner.nextLine();
        System.out.println();
    }

    // Point d'entrée principal de l'application
    public static void main(String[] args) {
        ApplicationController app = new ApplicationController();
        app.demarrerApplication();
    }

    // Méthodes publiques pour l'interface graphique
    public boolean ajouterPilote(String nom, String prenom, int numero) {
        return piloteService.ajouterPilote(nom, prenom, numero);
    }

    public boolean modifierPilote(int numero, String nouveauNom, String nouveauPrenom) {
        return piloteService.modifierPilote(numero, nouveauNom, nouveauPrenom);
    }

    public boolean supprimerPilote(int numero) {
        return piloteService.supprimerPilote(numero);
    }

    public boolean ajouterVoiture(String numero, Pilote pilote) {
        return voitureService.ajouterVoiture(numero, pilote);
    }

    public boolean modifierVoiture(String numero, String nouveauNumero) {
        return voitureService.modifierVoiture(numero, nouveauNumero);
    }

    public boolean supprimerVoiture(String numero) {
        return voitureService.supprimerVoiture(numero);
    }

    public boolean demarrerCourse(String nomCircuit, int nbTours, int dureeTour, List<Pilote> pilotesSansVoiture) {
        if (courseService.estCourseEnCours()) {
            return false;
        }
        Circuit circuit = new Circuit(nomCircuit, nbTours, dureeTour);
        courseService.configurerCourse(circuit);

        pilotesSansVoiture.clear();
        for (Pilote p : piloteService.obtenirTousLesPilotes()) {
            if (p.getVoiture() == null) {
                pilotesSansVoiture.add(p);
            } else {
                courseService.ajouterPiloteACourse(p.getNumero());
            }
        }
        if (!pilotesSansVoiture.isEmpty()) {
            return false;
        }
        return courseService.demarrerCourse();
    }

    public boolean changerPneus(int numeroPilote, String typePneus) {
        Pilote pilote = piloteService.trouverPiloteParNumero(numeroPilote);
        if (pilote == null || !courseService.estCourseEnCours()) {
            return false;
        }
        return courseService.changerPneus(pilote, typePneus);
    }

    public List<Pilote> getPilotesList() {
        // Si une course est en cours, retourner la vraie liste des pilotes de la course
        if (courseService != null && courseService.getCourseActuelle() != null) {
            return courseService.getCourseActuelle().getPilotes();
        }
        // Sinon, retourner tous les pilotes de la base de données
        return piloteService.obtenirTousLesPilotes();
    }

    // Méthode de debug pour afficher les données pilotes à chaque fin de tour
    public void debugAffichagePilotesCourse() {
        if (courseService != null && courseService.getCourseActuelle() != null) {
            System.out.println("--- DEBUG PILOTES (fin de tour) ---");
            for (Pilote p : courseService.getCourseActuelle().getPilotes()) {
                System.out.printf(
                        "#%d %s %s | Tours: %d | Dernier: %.2f | Meilleur: %.2f | Total: %.2f | En cours: %.2f\n",
                        p.getNumero(), p.getPrenom(), p.getNom(),
                        p.getToursEffectues(),
                        p.getDernierTour() == Double.MAX_VALUE ? 0.0 : p.getDernierTour(),
                        p.getMeilleurTour() == Double.MAX_VALUE ? 0.0 : p.getMeilleurTour(),
                        p.getTempsTotal(),
                        p.getTempsTourEnCours());
            }
        }
    }

    public boolean arreterCourse() {
        return courseService.arreterCourse();
    }

    public List<VoitureF1> getVoituresList() {
        return voitureService.getVoituresList();
    }

    public boolean configurerCourse(Circuit circuit) {
        return courseService.configurerCourse(circuit);
    }

    public boolean estCourseEnCours() {
        return courseService.estCourseEnCours();
    }

    public String getStatutCourse() {
        return courseService.getStatutCourse();
    }

    public Pilote getPiloteByNumero(int numero) {
        return piloteService.trouverPiloteParNumero(numero);
    }

    public List<Pilote> getPilotesSansVoiture() {
        List<Pilote> sansVoiture = new ArrayList<>();
        for (Pilote p : piloteService.obtenirTousLesPilotes()) {
            if (p.getVoiture() == null) {
                sansVoiture.add(p);
            }
        }
        return sansVoiture;
    }

    // === Méthodes de simulation pour l'interface graphique ===
    /**
     * Démarre la simulation dynamique de la course (tick toutes les 100ms)
     */
    public void demarrerSimulationCourse() {
        Course course = courseService.getCourseActuelle();
        if (course != null) {
            course.demarrerSimulation();
        }
    }

    /**
     * Arrête la simulation dynamique de la course
     */
    public void arreterSimulationCourse() {
        Course course = courseService.getCourseActuelle();
        if (course != null) {
            course.arreterSimulation();
        }
    }

    /**
     * Retourne le chrono global de la course en cours (en secondes)
     */
    public double getChronoGlobalCourse() {
        Course course = courseService.getCourseActuelle();
        if (course != null) {
            return course.getChronoGlobal();
        }
        return 0.0;
    }
}