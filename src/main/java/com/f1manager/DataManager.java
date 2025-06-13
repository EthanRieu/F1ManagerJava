package com.f1manager;
// import com.google.gson.Gson;

// import com.google.gson.GsonBuilder;
// import com.google.gson.reflect.TypeToken;

import com.f1manager.model.Course;
// import com.f1manager.util.LocalDateTimeAdapter;

import java.io.*;
// import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private static final String FICHIER_JSON = "courses.json";
    // private static final Gson gson = new GsonBuilder()
    // .setPrettyPrinting()
    // .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
    // .create();

    // Classe wrapper pour la structure JSON
    public static class CourseData {
        private List<Course> courses;

        public CourseData() {
            this.courses = new ArrayList<>();
        }

        public List<Course> getCourses() {
            return courses;
        }

        public void setCourses(List<Course> courses) {
            this.courses = courses;
        }
    }

    // Sauvegarder une course dans le fichier JSON
    public boolean sauvegarderCourse(Course course) {
        // TODO: Implémenter la sauvegarde sans Gson pour l'instant
        System.out.println("Sauvegarde de la course: " + course.toString());
        return true;
    }

    // Charger toutes les courses depuis le fichier JSON
    public CourseData chargerToutesLesCourses() {
        // TODO: Implémenter le chargement sans Gson pour l'instant
        return new CourseData();
    }

    // Obtenir la liste des courses
    public List<Course> chargerCourses() {
        return chargerToutesLesCourses().getCourses();
    }

    // Ajouter une nouvelle course
    public boolean ajouterCourse(Course course) {
        return sauvegarderCourse(course);
    }

    // Supprimer une course
    public boolean supprimerCourse(String courseId) {
        // TODO: Implémenter la suppression
        return true;
    }

    // Obtenir une course par ID
    public Course chargerCourse(String courseId) {
        List<Course> courses = chargerCourses();
        return courses.stream()
                .filter(c -> c.getId().equals(courseId))
                .findFirst()
                .orElse(null);
    }

    // Vérifier si le fichier JSON existe
    public boolean fichierExiste() {
        return new File(FICHIER_JSON).exists();
    }

    // Créer un fichier JSON vide
    public boolean initialiserFichier() {
        return true; // TODO: Implémenter
    }
}