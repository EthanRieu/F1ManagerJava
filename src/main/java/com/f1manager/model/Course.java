package com.f1manager.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class Course {
    private String id;
    private Circuit circuit;
    private List<Pilote> pilotes;
    private List<VoitureF1> voitures;
    private int tourActuel;
    private boolean courseTerminee;
    private boolean courseDemarree;
    private List<ArretAuxStands> arretsEffectues;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private Timer timer;
    private double chronoGlobal = 0;
    private boolean simulationActive = false;
    private Random random = new Random();

    // Ajout d'une référence statique temporaire au contrôleur pour debug (à
    // remplacer par une meilleure archi si besoin)
    public static com.f1manager.controller.ApplicationController debugController;

    // Constructeurs
    public Course() {
        this.id = UUID.randomUUID().toString();
        this.pilotes = new ArrayList<>();
        this.voitures = new ArrayList<>();
        this.arretsEffectues = new ArrayList<>();
        this.tourActuel = 0;
        this.courseTerminee = false;
        this.courseDemarree = false;
    }

    public Course(Circuit circuit) {
        this();
        this.circuit = circuit;
    }

    // Méthodes principales de gestion de course
    public boolean demarrerCourse() {
        if (circuit == null || !circuit.isValide() || pilotes.isEmpty()) {
            return false;
        }

        this.courseDemarree = true;
        this.dateDebut = LocalDateTime.now();
        this.tourActuel = 1;

        // Sortir toutes les voitures du garage vers les stands, puis sur le circuit
        for (VoitureF1 voiture : voitures) {
            voiture.sortirDuGarage(); // garage -> stands
            voiture.sortirDesStands(); // stands -> circuit
        }

        return true;
    }

    public void terminerCourse() {
        this.courseTerminee = true;
        this.dateFin = LocalDateTime.now();

        // Ramener toutes les voitures au garage
        for (VoitureF1 voiture : voitures) {
            if (!voiture.isAbandonne()) {
                voiture.setStatut(VoitureF1.STATUT_GARAGE);
                if (voiture.getPilote() != null) {
                    voiture.getPilote().setEnPiste(false);
                }
            }
        }
    }

    public boolean incrementerTour() {
        if (!courseDemarree || courseTerminee) {
            return false;
        }

        tourActuel++;

        // Vérifier si la course est terminée
        if (circuit != null && tourActuel > circuit.getNombreTours()) {
            terminerCourse();
            return false;
        }

        return true;
    }

    public boolean effectuerArretAuxStands(Pilote pilote, String typePneus) {
        if (!courseDemarree || courseTerminee || pilote == null) {
            return false;
        }

        VoitureF1 voiture = pilote.getVoiture();
        if (voiture == null || !voiture.peutRentrer()) {
            return false;
        }

        // Rentrer la voiture aux stands
        voiture.rentrerAuxStands();

        // Changer les pneus
        voiture.changerPneus(typePneus);

        // Enregistrer l'arrêt
        ArretAuxStands arret = new ArretAuxStands(
                pilote.getNomComplet(),
                tourActuel,
                typePneus,
                ArretAuxStands.TYPE_CHANGEMENT_PNEUS);
        arretsEffectues.add(arret);

        // Ressortir immédiatement des stands
        voiture.sortirDesStands();

        return true;
    }

    public boolean rentrerPiloteAuGarage(Pilote pilote) {
        if (!courseDemarree || pilote == null) {
            return false;
        }

        VoitureF1 voiture = pilote.getVoiture();
        if (voiture == null) {
            return false;
        }

        // Rentrer au garage (abandon)
        voiture.rentrerAuGarage();

        // Enregistrer l'abandon
        ArretAuxStands arret = new ArretAuxStands(
                pilote.getNomComplet(),
                tourActuel,
                voiture.getTypePneus(),
                ArretAuxStands.TYPE_ABANDON);
        arretsEffectues.add(arret);

        return true;
    }

    // Méthodes d'ajout/suppression
    public boolean ajouterPilote(Pilote pilote) {
        if (pilote == null || courseDemarree) {
            return false;
        }

        // Vérifier que le numéro n'est pas déjà pris
        for (Pilote p : pilotes) {
            if (p.getNumero() == pilote.getNumero()) {
                return false;
            }
        }

        pilotes.add(pilote);

        // Créer une voiture pour ce pilote
        VoitureF1 voiture = new VoitureF1(String.valueOf(pilote.getNumero()), pilote);
        voitures.add(voiture);

        return true;
    }

    public boolean supprimerPilote(Pilote pilote) {
        if (pilote == null || courseDemarree) {
            return false;
        }

        // Supprimer la voiture associée
        VoitureF1 voiture = pilote.getVoiture();
        if (voiture != null) {
            voitures.remove(voiture);
        }

        return pilotes.remove(pilote);
    }

    // Méthodes utilitaires
    public int getNombrePilotesEnCourse() {
        return (int) pilotes.stream().filter(p -> !p.isAbandonne()).count();
    }

    public int getNombrePilotesAbandonnes() {
        return (int) pilotes.stream().filter(Pilote::isAbandonne).count();
    }

    public List<Pilote> getPilotesEnCourse() {
        return pilotes.stream().filter(p -> !p.isAbandonne()).toList();
    }

    public List<Pilote> getPilotesAbandonnes() {
        return pilotes.stream().filter(Pilote::isAbandonne).toList();
    }

    public boolean peutDemarrer() {
        return circuit != null && circuit.isValide() &&
                !pilotes.isEmpty() && !courseDemarree;
    }

    public boolean estEnCours() {
        return courseDemarree && !courseTerminee;
    }

    public int getToursRestants() {
        if (circuit == null)
            return 0;
        return Math.max(0, circuit.getNombreTours() - tourActuel + 1);
    }

    public String getStatutCourse() {
        if (!courseDemarree)
            return "Non démarrée";
        if (courseTerminee)
            return "Terminée";
        return "En cours";
    }

    public void demarrerSimulation() {
        if (simulationActive || !courseDemarree || courseTerminee)
            return;
        simulationActive = true;
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                tickSimulation();
            }
        }, 0, 100); // tick toutes les 100ms
    }

    public void arreterSimulation() {
        simulationActive = false;
        if (timer != null)
            timer.cancel();
    }

    private void tickSimulation() {
        if (!courseDemarree || courseTerminee)
            return;
        chronoGlobal += 0.1; // 0.1 seconde par tick
        boolean courseFinie = true;
        for (Pilote pilote : pilotes) {
            if (pilote.isAbandonne())
                continue;
            if (pilote.getToursEffectues() < circuit.getNombreTours()) {
                courseFinie = false;
                double moyenne = circuit.getDureeParTour();
                double ecart = (random.nextDouble() * 2.0) - 1.0; // [-1, +1] sec
                double tempsCible = moyenne + ecart;
                pilote.setTempsTourEnCours(pilote.getTempsTourEnCours() + 0.1);
                pilote.setTempsTotal(pilote.getTempsTotal() + 0.1);
                if (pilote.getTempsTourEnCours() >= tempsCible) {
                    // Fin du tour pour ce pilote
                    pilote.setToursEffectues(pilote.getToursEffectues() + 1);
                    pilote.setDernierTour(pilote.getTempsTourEnCours());
                    if (pilote.getTempsTourEnCours() < pilote.getMeilleurTour()) {
                        pilote.setMeilleurTour(pilote.getTempsTourEnCours());
                        pilote.setMeilleurTourNum(pilote.getToursEffectues());
                    }
                    pilote.setTempsTourEnCours(0);
                    // Debug : affichage des données pilotes à la fin de chaque tour
                    if (debugController != null)
                        debugController.debugAffichagePilotesCourse();
                }
            }
        }
        if (courseFinie) {
            courseTerminee = true;
            arreterSimulation();
        }
    }

    public double getChronoGlobal() {
        return chronoGlobal;
    }

    public void resetSimulation() {
        chronoGlobal = 0;
        simulationActive = false;
        if (timer != null)
            timer.cancel();
        for (Pilote pilote : pilotes) {
            pilote.setTempsTotal(0);
            pilote.setTempsTourEnCours(0);
            pilote.setMeilleurTour(Double.MAX_VALUE);
            pilote.setToursEffectues(0);
        }
    }

    // Getters et Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Circuit getCircuit() {
        return circuit;
    }

    public void setCircuit(Circuit circuit) {
        this.circuit = circuit;
    }

    public List<Pilote> getPilotes() {
        return new ArrayList<>(pilotes);
    }

    public void setPilotes(List<Pilote> pilotes) {
        this.pilotes = new ArrayList<>(pilotes);
    }

    public List<VoitureF1> getVoitures() {
        return new ArrayList<>(voitures);
    }

    public void setVoitures(List<VoitureF1> voitures) {
        this.voitures = new ArrayList<>(voitures);
    }

    public int getTourActuel() {
        return tourActuel;
    }

    public void setTourActuel(int tourActuel) {
        this.tourActuel = tourActuel;
    }

    public boolean isCourseTerminee() {
        return courseTerminee;
    }

    public void setCourseTerminee(boolean courseTerminee) {
        this.courseTerminee = courseTerminee;
    }

    public boolean isCourseDemarree() {
        return courseDemarree;
    }

    public void setCourseDemarree(boolean courseDemarree) {
        this.courseDemarree = courseDemarree;
    }

    public List<ArretAuxStands> getArretsEffectues() {
        return new ArrayList<>(arretsEffectues);
    }

    public void setArretsEffectues(List<ArretAuxStands> arretsEffectues) {
        this.arretsEffectues = new ArrayList<>(arretsEffectues);
    }

    public LocalDateTime getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDateTime dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDateTime getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDateTime dateFin) {
        this.dateFin = dateFin;
    }

    @Override
    public String toString() {
        String circuitNom = (circuit != null) ? circuit.getNom() : "Aucun circuit";
        return String.format("Course: %s - %s - Tour %d - %d pilotes - Statut: %s",
                circuitNom,
                (dateDebut != null) ? dateDebut.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                        : "Non planifiée",
                tourActuel, pilotes.size(), getStatutCourse());
    }
}