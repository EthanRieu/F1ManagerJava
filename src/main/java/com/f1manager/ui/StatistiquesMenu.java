package com.f1manager.ui;

import com.f1manager.controller.ApplicationController;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class StatistiquesMenu {
    private final ApplicationController controller;
    private final Stage parentStage;
    private final Stage stage;

    public StatistiquesMenu(ApplicationController controller, Stage parentStage) {
        this.controller = controller;
        this.parentStage = parentStage;
        this.stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(parentStage);
        stage.setTitle("Statistiques");
    }

    public void show() {
        VBox menu = createMenu();
        Scene scene = new Scene(menu, 600, 400);
        stage.setScene(scene);
        stage.show();
    }

    private VBox createMenu() {
        VBox menu = new VBox(10);
        menu.setPadding(new Insets(20));
        menu.setStyle("-fx-background-color: #f0f0f0;");

        // Titre
        Label title = new Label("Statistiques");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        menu.getChildren().add(title);

        // Boutons du menu
        Button pilotesBtn = createMenuButton("Statistiques des Pilotes", this::showStatistiquesPilotes);
        Button voituresBtn = createMenuButton("Statistiques des Voitures", this::showStatistiquesVoitures);
        Button coursesBtn = createMenuButton("Statistiques des Courses", this::showStatistiquesCourses);
        Button retourBtn = createMenuButton("Retour", () -> stage.close());

        menu.getChildren().addAll(pilotesBtn, voituresBtn, coursesBtn, retourBtn);
        return menu;
    }

    private Button createMenuButton(String text, Runnable action) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setStyle("-fx-font-size: 16px; -fx-padding: 10px;");
        button.setOnAction(e -> action.run());
        return button;
    }

    private void showStatistiquesPilotes() {
        // TODO: Implémenter l'affichage des statistiques des pilotes
        showInfo("Fonctionnalité à venir");
    }

    private void showStatistiquesVoitures() {
        // TODO: Implémenter l'affichage des statistiques des voitures
        showInfo("Fonctionnalité à venir");
    }

    private void showStatistiquesCourses() {
        // TODO: Implémenter l'affichage des statistiques des courses
        showInfo("Fonctionnalité à venir");
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}