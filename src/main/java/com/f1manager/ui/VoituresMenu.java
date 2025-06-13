package com.f1manager.ui;

import com.f1manager.controller.ApplicationController;
import com.f1manager.model.Pilote;
import com.f1manager.model.VoitureF1;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class VoituresMenu {
    private final ApplicationController controller;
    private final Stage parentStage;
    private final Stage stage;
    private final ObservableList<VoitureF1> voituresList;

    public VoituresMenu(ApplicationController controller, Stage parentStage) {
        this.controller = controller;
        this.parentStage = parentStage;
        this.stage = new Stage();
        this.voituresList = FXCollections.observableArrayList();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(parentStage);
        stage.setTitle("Gestion des Voitures");
    }

    public void show() {
        VBox menu = createMenu();
        Scene scene = new Scene(menu, 800, 600);
        stage.setScene(scene);
        stage.show();
        refreshVoituresList();
    }

    private VBox createMenu() {
        VBox menu = new VBox(10);
        menu.setPadding(new Insets(20));
        menu.setStyle("-fx-background-color: #f0f0f0;");

        // Titre
        Label title = new Label("Gestion des Voitures");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        menu.getChildren().add(title);

        // TableView pour la liste des voitures
        TableView<VoitureF1> table = createVoituresTable();
        menu.getChildren().add(table);

        // Boutons du menu
        HBox buttonsBox = new HBox(10);
        buttonsBox.setPadding(new Insets(10, 0, 0, 0));

        Button ajouterBtn = createMenuButton("Ajouter une Voiture", this::showAjouterVoitureDialog);
        Button modifierBtn = createMenuButton("Modifier une Voiture", () -> {
            VoitureF1 selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                showModifierVoitureDialog(selected);
            } else {
                showError("Veuillez sélectionner une voiture à modifier");
            }
        });
        Button supprimerBtn = createMenuButton("Supprimer une Voiture", () -> {
            VoitureF1 selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                showSupprimerVoitureDialog(selected);
            } else {
                showError("Veuillez sélectionner une voiture à supprimer");
            }
        });
        Button retourBtn = createMenuButton("Retour", () -> stage.close());

        buttonsBox.getChildren().addAll(ajouterBtn, modifierBtn, supprimerBtn, retourBtn);
        menu.getChildren().add(buttonsBox);

        return menu;
    }

    private TableView<VoitureF1> createVoituresTable() {
        TableView<VoitureF1> table = new TableView<>();
        table.setEditable(false);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Colonnes
        TableColumn<VoitureF1, String> numeroCol = new TableColumn<>("Numéro");
        numeroCol.setCellValueFactory(new PropertyValueFactory<>("numero"));

        TableColumn<VoitureF1, String> piloteCol = new TableColumn<>("Pilote");
        piloteCol.setCellValueFactory(cellData -> {
            VoitureF1 voiture = cellData.getValue();
            String pilote = voiture.getPilote() != null ? voiture.getPilote().getNomComplet() : "Aucun";
            return new SimpleStringProperty(pilote);
        });

        TableColumn<VoitureF1, String> statutCol = new TableColumn<>("Statut");
        statutCol.setCellValueFactory(new PropertyValueFactory<>("statut"));

        TableColumn<VoitureF1, String> pneusCol = new TableColumn<>("Pneus");
        pneusCol.setCellValueFactory(new PropertyValueFactory<>("typePneus"));

        table.getColumns().addAll(numeroCol, piloteCol, statutCol, pneusCol);
        table.setItems(voituresList);

        return table;
    }

    private void refreshVoituresList() {
        voituresList.clear();
        voituresList.addAll(controller.getVoituresList());
    }

    private Button createMenuButton(String text, Runnable action) {
        Button button = new Button(text);
        button.setStyle("-fx-font-size: 14px; -fx-padding: 8px;");
        button.setOnAction(e -> action.run());
        return button;
    }

    private void showAjouterVoitureDialog() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(stage);
        dialog.setTitle("Ajouter une Voiture");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(10);
        grid.setVgap(10);

        // Champs de saisie
        TextField numeroField = new TextField();
        TextField numeroPiloteField = new TextField();

        grid.add(new Label("Numéro:"), 0, 0);
        grid.add(numeroField, 1, 0);
        grid.add(new Label("Numéro du Pilote:"), 0, 1);
        grid.add(numeroPiloteField, 1, 1);

        // Boutons
        Button validerBtn = new Button("Valider");
        Button annulerBtn = new Button("Annuler");

        validerBtn.setOnAction(e -> {
            try {
                String numero = numeroField.getText();
                int numeroPilote = Integer.parseInt(numeroPiloteField.getText());
                Pilote pilote = controller.getPiloteByNumero(numeroPilote);

                if (pilote == null) {
                    showError("Pilote non trouvé");
                    return;
                }

                if (controller.ajouterVoiture(numero, pilote)) {
                    showSuccess("Voiture ajoutée avec succès");
                    refreshVoituresList();
                    dialog.close();
                } else {
                    showError("Impossible d'ajouter la voiture");
                }
            } catch (NumberFormatException ex) {
                showError("Le numéro du pilote doit être un nombre entier");
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        });

        annulerBtn.setOnAction(e -> dialog.close());

        HBox buttons = new HBox(10, validerBtn, annulerBtn);
        grid.add(buttons, 1, 2);

        Scene scene = new Scene(grid);
        dialog.setScene(scene);
        dialog.show();
    }

    private void showModifierVoitureDialog(VoitureF1 voiture) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(stage);
        dialog.setTitle("Modifier une Voiture");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(10);
        grid.setVgap(10);

        // Champs de saisie pré-remplis
        TextField numeroField = new TextField(voiture.getNumero());
        Label piloteLabel = new Label(voiture.getPilote() != null ? voiture.getPilote().getNomComplet() : "Aucun");

        grid.add(new Label("Numéro:"), 0, 0);
        grid.add(numeroField, 1, 0);
        grid.add(new Label("Pilote:"), 0, 1);
        grid.add(piloteLabel, 1, 1);

        // Boutons
        Button validerBtn = new Button("Valider");
        Button annulerBtn = new Button("Annuler");

        validerBtn.setOnAction(e -> {
            try {
                String nouveauNumero = numeroField.getText();

                if (controller.modifierVoiture(voiture.getNumero(), nouveauNumero)) {
                    showSuccess("Voiture modifiée avec succès");
                    refreshVoituresList();
                    dialog.close();
                } else {
                    showError("Impossible de modifier la voiture");
                }
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        });

        annulerBtn.setOnAction(e -> dialog.close());

        HBox buttons = new HBox(10, validerBtn, annulerBtn);
        grid.add(buttons, 1, 2);

        Scene scene = new Scene(grid);
        dialog.setScene(scene);
        dialog.show();
    }

    private void showSupprimerVoitureDialog(VoitureF1 voiture) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer la voiture");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer la voiture #" + voiture.getNumero() + " ?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (controller.supprimerVoiture(voiture.getNumero())) {
                    showSuccess("Voiture supprimée avec succès");
                    refreshVoituresList();
                } else {
                    showError("Impossible de supprimer la voiture");
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