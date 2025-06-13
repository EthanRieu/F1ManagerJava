package com.f1manager.ui;

import com.f1manager.controller.ApplicationController;
import com.f1manager.model.Pilote;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PilotesMenu {
    private final ApplicationController controller;
    private final Stage parentStage;
    private final Stage stage;
    private final ObservableList<Pilote> pilotesList;

    public PilotesMenu(ApplicationController controller, Stage parentStage) {
        this.controller = controller;
        this.parentStage = parentStage;
        this.stage = new Stage();
        this.pilotesList = FXCollections.observableArrayList();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(parentStage);
        stage.setTitle("Gestion des Pilotes");
    }

    public void show() {
        VBox menu = createMenu();
        Scene scene = new Scene(menu, 800, 600);
        stage.setScene(scene);
        stage.show();
        refreshPilotesList();
    }

    private VBox createMenu() {
        VBox menu = new VBox(10);
        menu.setPadding(new Insets(20));
        menu.setStyle("-fx-background-color: #f0f0f0;");

        // Titre
        Label title = new Label("Gestion des Pilotes");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        menu.getChildren().add(title);

        // TableView pour la liste des pilotes
        TableView<Pilote> table = createPilotesTable();
        menu.getChildren().add(table);

        // Boutons du menu
        HBox buttonsBox = new HBox(10);
        buttonsBox.setPadding(new Insets(10, 0, 0, 0));

        Button ajouterBtn = createMenuButton("Ajouter un Pilote", this::showAjouterPiloteDialog);
        Button modifierBtn = createMenuButton("Modifier un Pilote", () -> {
            Pilote selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                showModifierPiloteDialog(selected);
            } else {
                showError("Veuillez sélectionner un pilote à modifier");
            }
        });
        Button supprimerBtn = createMenuButton("Supprimer un Pilote", () -> {
            Pilote selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                showSupprimerPiloteDialog(selected);
            } else {
                showError("Veuillez sélectionner un pilote à supprimer");
            }
        });
        Button retourBtn = createMenuButton("Retour", () -> stage.close());

        buttonsBox.getChildren().addAll(ajouterBtn, modifierBtn, supprimerBtn, retourBtn);
        menu.getChildren().add(buttonsBox);

        return menu;
    }

    private TableView<Pilote> createPilotesTable() {
        TableView<Pilote> table = new TableView<>();
        table.setEditable(false);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Colonnes
        TableColumn<Pilote, Integer> numeroCol = new TableColumn<>("Numéro");
        numeroCol.setCellValueFactory(new PropertyValueFactory<>("numero"));

        TableColumn<Pilote, String> nomCol = new TableColumn<>("Nom");
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));

        TableColumn<Pilote, String> prenomCol = new TableColumn<>("Prénom");
        prenomCol.setCellValueFactory(new PropertyValueFactory<>("prenom"));

        TableColumn<Pilote, String> statutCol = new TableColumn<>("Statut");
        statutCol.setCellValueFactory(cellData -> {
            Pilote pilote = cellData.getValue();
            String statut = pilote.isEnPiste() ? "En piste" : pilote.isAbandonne() ? "Abandonné" : "Disponible";
            return new SimpleStringProperty(statut);
        });

        table.getColumns().addAll(numeroCol, nomCol, prenomCol, statutCol);
        table.setItems(pilotesList);

        return table;
    }

    private void refreshPilotesList() {
        pilotesList.clear();
        pilotesList.addAll(controller.getPilotesList());
    }

    private Button createMenuButton(String text, Runnable action) {
        Button button = new Button(text);
        button.setStyle("-fx-font-size: 14px; -fx-padding: 8px;");
        button.setOnAction(e -> action.run());
        return button;
    }

    private void showAjouterPiloteDialog() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(stage);
        dialog.setTitle("Ajouter un Pilote");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(10);
        grid.setVgap(10);

        // Champs de saisie
        TextField nomField = new TextField();
        TextField prenomField = new TextField();
        TextField numeroField = new TextField();

        grid.add(new Label("Nom:"), 0, 0);
        grid.add(nomField, 1, 0);
        grid.add(new Label("Prénom:"), 0, 1);
        grid.add(prenomField, 1, 1);
        grid.add(new Label("Numéro:"), 0, 2);
        grid.add(numeroField, 1, 2);

        // Boutons
        Button validerBtn = new Button("Valider");
        Button annulerBtn = new Button("Annuler");

        validerBtn.setOnAction(e -> {
            try {
                String nom = nomField.getText();
                String prenom = prenomField.getText();
                int numero = Integer.parseInt(numeroField.getText());

                if (controller.ajouterPilote(nom, prenom, numero)) {
                    showSuccess("Pilote ajouté avec succès");
                    refreshPilotesList();
                    dialog.close();
                } else {
                    showError("Impossible d'ajouter le pilote");
                }
            } catch (NumberFormatException ex) {
                showError("Le numéro doit être un nombre entier");
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        });

        annulerBtn.setOnAction(e -> dialog.close());

        HBox buttons = new HBox(10, validerBtn, annulerBtn);
        grid.add(buttons, 1, 3);

        Scene scene = new Scene(grid);
        dialog.setScene(scene);
        dialog.show();
    }

    private void showModifierPiloteDialog(Pilote pilote) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(stage);
        dialog.setTitle("Modifier un Pilote");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(10);
        grid.setVgap(10);

        // Champs de saisie pré-remplis
        TextField nomField = new TextField(pilote.getNom());
        TextField prenomField = new TextField(pilote.getPrenom());
        Label numeroLabel = new Label(String.valueOf(pilote.getNumero()));

        grid.add(new Label("Nom:"), 0, 0);
        grid.add(nomField, 1, 0);
        grid.add(new Label("Prénom:"), 0, 1);
        grid.add(prenomField, 1, 1);
        grid.add(new Label("Numéro:"), 0, 2);
        grid.add(numeroLabel, 1, 2);

        // Boutons
        Button validerBtn = new Button("Valider");
        Button annulerBtn = new Button("Annuler");

        validerBtn.setOnAction(e -> {
            try {
                String nouveauNom = nomField.getText();
                String nouveauPrenom = prenomField.getText();

                if (controller.modifierPilote(pilote.getNumero(), nouveauNom, nouveauPrenom)) {
                    showSuccess("Pilote modifié avec succès");
                    refreshPilotesList();
                    dialog.close();
                } else {
                    showError("Impossible de modifier le pilote");
                }
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        });

        annulerBtn.setOnAction(e -> dialog.close());

        HBox buttons = new HBox(10, validerBtn, annulerBtn);
        grid.add(buttons, 1, 3);

        Scene scene = new Scene(grid);
        dialog.setScene(scene);
        dialog.show();
    }

    private void showSupprimerPiloteDialog(Pilote pilote) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer le pilote");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer le pilote " + pilote.getNomComplet() + " ?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (controller.supprimerPilote(pilote.getNumero())) {
                    showSuccess("Pilote supprimé avec succès");
                    refreshPilotesList();
                } else {
                    showError("Impossible de supprimer le pilote");
                }
            }
        });
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
}