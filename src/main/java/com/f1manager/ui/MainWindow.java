package com.f1manager.ui;

import com.f1manager.controller.ApplicationController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainWindow extends Application {
    private final ApplicationController controller;
    private Stage primaryStage;

    public MainWindow() {
        this.controller = new ApplicationController();
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Gestionnaire d'Écurie F1");

        // Création du menu principal
        VBox mainMenu = createMainMenu();
        Scene scene = new Scene(mainMenu, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createMainMenu() {
        VBox menu = new VBox(10);
        menu.setPadding(new Insets(20));
        menu.setStyle("-fx-background-color: #f0f0f0;");

        // Titre
        javafx.scene.control.Label title = new javafx.scene.control.Label("Gestionnaire d'Écurie F1");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        menu.getChildren().add(title);

        // Boutons du menu
        Button pilotesBtn = createMenuButton("Gestion des Pilotes", this::showPilotesMenu);
        Button courseBtn = createMenuButton("Gestion de la Course", this::showCourseMenu);
        Button voituresBtn = createMenuButton("Gestion des Voitures", this::showVoituresMenu);
        Button quitterBtn = createMenuButton("Quitter", () -> System.exit(0));

        menu.getChildren().addAll(pilotesBtn, courseBtn, voituresBtn, quitterBtn);
        return menu;
    }

    private Button createMenuButton(String text, Runnable action) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setStyle("-fx-font-size: 16px; -fx-padding: 10px;");
        button.setOnAction(e -> action.run());
        return button;
    }

    private void showPilotesMenu() {
        PilotesMenu pilotesMenu = new PilotesMenu(controller, primaryStage);
        pilotesMenu.show();
    }

    private void showCourseMenu() {
        CourseMenu courseMenu = new CourseMenu(controller, primaryStage);
        courseMenu.show();
    }

    private void showVoituresMenu() {
        VoituresMenu voituresMenu = new VoituresMenu(controller, primaryStage);
        voituresMenu.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}