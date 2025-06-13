package com.f1manager.service;

import java.util.ArrayList;
import java.util.List;

import com.f1manager.DataManager;
import com.f1manager.model.ArretAuxStands;
import com.f1manager.model.Circuit;
import com.f1manager.model.Course;
import com.f1manager.model.Pilote;

public class CourseService {
    private Course courseActuelle;
    private DataManager dataManager;
    private PiloteService piloteService;

    public CourseService(PiloteService piloteService) {
        this.piloteService = piloteService;
        this.dataManager = new DataManager();
    }

    // Gestion de course
    public boolean creerNouvelleCourse(Circuit circuit) {
        if (circuit == null || !circuit.isValide()) {
            return false;
        }

        this.courseActuelle = new Course(circuit);
        return true;
    }

    public boolean ajouterPiloteACourse(int numeroPilote) {
        if (courseActuelle == null || courseActuelle.isCourseDemarree()) {
            return false;
        }

        Pilote pilote = piloteService.trouverPiloteParNumero(numeroPilote);
        if (pilote == null) {
            return false;
        }

        return courseActuelle.ajouterPilote(pilote);
    }

    public boolean supprimerPiloteDeCourse(int numeroPilote) {
        if (courseActuelle == null || courseActuelle.isCourseDemarree()) {
            return false;
        }

        Pilote pilote = piloteService.trouverPiloteParNumero(numeroPilote);
        if (pilote == null) {
            return false;
        }

        return courseActuelle.supprimerPilote(pilote);
    }

    public boolean demarrerCourse() {
        if (courseActuelle == null || !courseActuelle.peutDemarrer()) {
            return false;
        }

        return courseActuelle.demarrerCourse();
    }

    public boolean passerAuTourSuivant() {
        if (courseActuelle == null || !courseActuelle.estEnCours()) {
            return false;
        }

        return courseActuelle.incrementerTour();
    }

    public boolean terminerCourse() {
        if (courseActuelle == null || !courseActuelle.isCourseDemarree()) {
            return false;
        }

        courseActuelle.terminerCourse();

        // Sauvegarder automatiquement la course terminée
        return dataManager.sauvegarderCourse(courseActuelle);
    }

    // Gestion des arrêts
    public boolean effectuerArretAuxStands(int numeroPilote, String typePneus) {
        if (courseActuelle == null || !courseActuelle.estEnCours()) {
            return false;
        }

        Pilote pilote = piloteService.trouverPiloteParNumero(numeroPilote);
        if (pilote == null) {
            return false;
        }

        return courseActuelle.effectuerArretAuxStands(pilote, typePneus);
    }

    public boolean rentrerPiloteAuGarage(int numeroPilote) {
        if (courseActuelle == null || !courseActuelle.estEnCours()) {
            return false;
        }

        Pilote pilote = piloteService.trouverPiloteParNumero(numeroPilote);
        if (pilote == null) {
            return false;
        }

        return courseActuelle.rentrerPiloteAuGarage(pilote);
    }

    // Consultation
    public Course getCourseActuelle() {
        return courseActuelle;
    }

    public boolean aCourseEnCours() {
        return courseActuelle != null && courseActuelle.estEnCours();
    }

    public boolean aCoursePreparee() {
        return courseActuelle != null && !courseActuelle.isCourseDemarree();
    }

    public String getStatutCourse() {
        if (courseActuelle == null) {
            return "Aucune course";
        }
        return courseActuelle.getStatutCourse();
    }

    public List<ArretAuxStands> getHistoriqueArrets() {
        if (courseActuelle == null) {
            return new ArrayList<>();
        }
        return courseActuelle.getArretsEffectues();
    }

    // Persistance
    public boolean sauvegarderCourseActuelle() {
        if (courseActuelle == null) {
            return false;
        }
        return dataManager.sauvegarderCourse(courseActuelle);
    }

    public List<Course> chargerHistoriqueCourses() {
        return dataManager.chargerCourses();
    }

    public Course chargerCourse(String courseId) {
        return dataManager.chargerCourse(courseId);
    }

    // Reset
    public void reinitialiser() {
        this.courseActuelle = null;
        piloteService.reinitialiserTousLesPilotes();
    }

    // Méthodes pour l'interface graphique
    public boolean estCourseConfiguree() {
        return courseActuelle != null;
    }

    public boolean estCourseEnCours() {
        return courseActuelle != null && courseActuelle.estEnCours();
    }

    public void reinitialiserCourse() {
        this.courseActuelle = null;
    }

    public List<Course> getHistoriqueCourses() {
        return dataManager.chargerCourses();
    }

    public boolean configurerCourse(Circuit circuit) {
        if (circuit == null || !circuit.isValide()) {
            return false;
        }
        this.courseActuelle = new Course(circuit);
        return true;
    }

    public boolean changerPneus(Pilote pilote, String typePneus) {
        if (courseActuelle == null || !courseActuelle.estEnCours() || pilote == null) {
            return false;
        }
        return courseActuelle.effectuerArretAuxStands(pilote, typePneus);
    }

    public boolean arreterCourse() {
        if (courseActuelle == null || !courseActuelle.estEnCours()) {
            return false;
        }
        courseActuelle.terminerCourse();
        return true;
    }
}