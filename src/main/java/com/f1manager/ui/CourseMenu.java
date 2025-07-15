package com.f1manager.ui;

import com.f1manager.controller.ApplicationController;
import com.f1manager.model.Pilote;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
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
import javafx.util.Duration;

public class CourseMenu {
    private final ApplicationController controller;
    private final Stage parentStage;
    private final Stage stage;
    private final ObservableList<Pilote> classementList;
    private Timeline trackingTimeline;

    public CourseMenu(ApplicationController controller, Stage parentStage) {
        this.controller = controller;
        this.parentStage = parentStage;
        this.stage = new Stage();
        this.classementList = FXCollections.observableArrayList();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(parentStage);
        stage.setTitle("Gestion de la Course");
    }

    public void show() {
        VBox menu = createMenu();
        Scene scene = new Scene(menu, 800, 600);
        stage.setScene(scene);
        stage.show();
        refreshClassement();
    }

    private VBox createMenu() {
        VBox menu = new VBox(10);
        menu.setPadding(new Insets(20));
        menu.setStyle("-fx-background-color: #f0f0f0;");

        // Titre
        Label title = new Label("Gestion de la Course");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        menu.getChildren().add(title);

        // TableView pour le classement
        VBox tableBox = createClassementTable();
        menu.getChildren().add(tableBox);

        // Boutons du menu
        HBox buttonsBox = new HBox(10);
        buttonsBox.setPadding(new Insets(10, 0, 0, 0));

        Button demarrerBtn = createMenuButton("Démarrer une Course", this::showDemarrerCourseDialog);
        Button arreterBtn = createMenuButton("Arrêter la Course", this::showArreterCourseDialog);
        Button pneusBtn = createMenuButton("Changer les Pneus", this::showChangerPneusDialog);
        Button retourBtn = createMenuButton("Retour", () -> stage.close());

        buttonsBox.getChildren().addAll(demarrerBtn, arreterBtn, pneusBtn, retourBtn);
        menu.getChildren().add(buttonsBox);

        return menu;
    }

    private VBox createClassementTable() {
        VBox tableBox = new VBox(10);
        tableBox.setPadding(new Insets(20));

        TableView<Pilote> table = new TableView<>();
        table.setEditable(false);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Colonnes
        TableColumn<Pilote, Integer> positionCol = new TableColumn<>("Position");
        positionCol.setCellValueFactory(cellData -> {
            int position = classementList.indexOf(cellData.getValue()) + 1;
            return new SimpleIntegerProperty(position).asObject();
        });

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

        table.getColumns().addAll(positionCol, numeroCol, nomCol, prenomCol, statutCol);
        table.setItems(classementList);

        tableBox.getChildren().add(table);
        return tableBox;
    }

    private void refreshClassement() {
        classementList.clear();
        classementList.addAll(controller.getPilotesList());
    }

    private Button createMenuButton(String text, Runnable action) {
        Button button = new Button(text);
        button.setStyle("-fx-font-size: 14px; -fx-padding: 8px;");
        button.setOnAction(e -> action.run());
        return button;
    }

    private void showDemarrerCourseDialog() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(stage);
        dialog.setTitle("Démarrer une Course");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(10);
        grid.setVgap(10);

        // ComboBox pour sélectionner le circuit
        ComboBox<com.f1manager.model.Circuit> circuitCombo = new ComboBox<>();
        circuitCombo.setPromptText("Sélectionner un circuit");

        // Charger les circuits depuis la base de données
        try {
            com.f1manager.service.CircuitService circuitService = new com.f1manager.service.CircuitService();
            circuitCombo.getItems().addAll(circuitService.obtenirTousLesCircuits());
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des circuits : " + e.getMessage());
        }

        // Personnaliser l'affichage des circuits dans la ComboBox
        circuitCombo.setCellFactory(param -> new javafx.scene.control.ListCell<com.f1manager.model.Circuit>() {
            @Override
            protected void updateItem(com.f1manager.model.Circuit circuit, boolean empty) {
                super.updateItem(circuit, empty);
                if (empty || circuit == null) {
                    setText(null);
                } else {
                    setText(circuit.getNom() + " (" + circuit.getNombreTours() + " tours)");
                }
            }
        });

        circuitCombo.setButtonCell(new javafx.scene.control.ListCell<com.f1manager.model.Circuit>() {
            @Override
            protected void updateItem(com.f1manager.model.Circuit circuit, boolean empty) {
                super.updateItem(circuit, empty);
                if (empty || circuit == null) {
                    setText(null);
                } else {
                    setText(circuit.getNom() + " (" + circuit.getNombreTours() + " tours)");
                }
            }
        });

        grid.add(new Label("Circuit:"), 0, 0);
        grid.add(circuitCombo, 1, 0);

        // Boutons
        Button validerBtn = new Button("Valider");
        Button annulerBtn = new Button("Annuler");

        validerBtn.setOnAction(e -> {
            try {
                com.f1manager.model.Circuit circuitSelectionne = circuitCombo.getValue();
                if (circuitSelectionne == null) {
                    showError("Veuillez sélectionner un circuit");
                    return;
                }

                String nomCircuit = circuitSelectionne.getNom();
                int nbTours = circuitSelectionne.getNombreTours();
                int dureeTour = circuitSelectionne.getTempsMoyenParTour();

                java.util.List<com.f1manager.model.Pilote> pilotesSansVoiture = new java.util.ArrayList<>();
                boolean success = controller.demarrerCourse(nomCircuit, nbTours, dureeTour, pilotesSansVoiture);
                if (success) {
                    showCourseTrackingView(nbTours);
                    refreshClassement();
                    dialog.close();
                } else if (!pilotesSansVoiture.isEmpty()) {
                    StringBuilder sb = new StringBuilder(
                            "Impossible de démarrer la course. Les pilotes suivants n'ont pas de voiture assignée :\n");
                    for (com.f1manager.model.Pilote p : pilotesSansVoiture) {
                        sb.append("- ").append(p.getNomComplet()).append("\n");
                    }
                    showError(sb.toString());
                } else {
                    showError("Impossible de démarrer la course");
                }
            } catch (Exception ex) {
                showError("Erreur : " + ex.getMessage());
            }
        });

        annulerBtn.setOnAction(e -> dialog.close());

        HBox buttons = new HBox(10, validerBtn, annulerBtn);
        grid.add(buttons, 1, 1);

        Scene scene = new Scene(grid);
        dialog.setScene(scene);
        dialog.show();
    }

    private void showArreterCourseDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation d'arrêt");
        alert.setHeaderText("Arrêter la course");
        alert.setContentText("Êtes-vous sûr de vouloir arrêter la course en cours ?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (controller.arreterCourse()) {
                    showSuccess("Course arrêtée avec succès");
                    refreshClassement();
                } else {
                    showError("Impossible d'arrêter la course");
                }
            }
        });
    }

    private void showChangerPneusDialog() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(stage);
        dialog.setTitle("Changer les Pneus");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(10);
        grid.setVgap(10);

        // Champs de saisie
        TextField numeroPiloteField = new TextField();
        ComboBox<String> typePneusCombo = new ComboBox<>();
        typePneusCombo.getItems().addAll("Soft", "Medium", "Hard");
        typePneusCombo.setValue("Medium");

        grid.add(new Label("Numéro du Pilote:"), 0, 0);
        grid.add(numeroPiloteField, 1, 0);
        grid.add(new Label("Type de Pneus:"), 0, 1);
        grid.add(typePneusCombo, 1, 1);

        // Boutons
        Button validerBtn = new Button("Valider");
        Button annulerBtn = new Button("Annuler");

        validerBtn.setOnAction(e -> {
            try {
                int numeroPilote = Integer.parseInt(numeroPiloteField.getText());
                String typePneus = typePneusCombo.getValue();

                if (controller.changerPneus(numeroPilote, typePneus)) {
                    showSuccess("Pneus changés avec succès");
                    refreshClassement();
                    dialog.close();
                } else {
                    showError("Impossible de changer les pneus");
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

    private void showCourseTrackingView(int nbTours) {
        Stage trackingStage = new Stage();
        trackingStage.initModality(Modality.APPLICATION_MODAL);
        trackingStage.initOwner(stage);
        trackingStage.setTitle("Suivi de la Course");

        VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        Label titre = new Label("Suivi de la Course");
        titre.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        root.getChildren().add(titre);

        // Chrono global et tours
        Label chronoLabel = new Label("Chrono : 0.0s");
        Label toursLabel = new Label("Tours parcourus : 0 / " + nbTours);
        root.getChildren().addAll(chronoLabel, toursLabel);

        TableView<Pilote> table = new TableView<>();
        table.setEditable(false);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Pilote, Integer> positionCol = new TableColumn<>("Position");
        positionCol.setCellValueFactory(cellData -> {
            // Classement par nombre de tours puis temps total
            var pilotes = controller.getPilotesList();
            pilotes.sort((p1, p2) -> {
                if (p2.getToursEffectues() != p1.getToursEffectues())
                    return Integer.compare(p2.getToursEffectues(), p1.getToursEffectues());
                return Double.compare(p1.getTempsTotal(), p2.getTempsTotal());
            });
            int pos = pilotes.indexOf(cellData.getValue()) + 1;
            return new SimpleIntegerProperty(pos).asObject();
        });
        TableColumn<Pilote, Integer> numeroCol = new TableColumn<>("Numéro");
        numeroCol.setCellValueFactory(new PropertyValueFactory<>("numero"));
        TableColumn<Pilote, String> nomCol = new TableColumn<>("Nom");
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        TableColumn<Pilote, String> prenomCol = new TableColumn<>("Prénom");
        prenomCol.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        TableColumn<Pilote, Integer> toursCol = new TableColumn<>("Tours");
        toursCol.setCellValueFactory(
                cellData -> new SimpleIntegerProperty(cellData.getValue().getToursEffectues()).asObject());
        TableColumn<Pilote, String> chronoTotalCol = new TableColumn<>("Chrono total (s)");
        chronoTotalCol.setCellValueFactory(
                cellData -> new SimpleStringProperty(String.format("%.1f", cellData.getValue().getTempsTotal())));
        TableColumn<Pilote, String> dernierTourCol = new TableColumn<>("Dernier tour (s)");
        dernierTourCol.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getDernierTour() == Double.MAX_VALUE ? "-"
                        : String.format("%.1f", cellData.getValue().getDernierTour())));
        TableColumn<Pilote, String> meilleurTourCol = new TableColumn<>("Meilleur tour (s) (n°)");
        meilleurTourCol.setCellValueFactory(
                cellData -> {
                    Pilote p = cellData.getValue();
                    if (p.getMeilleurTour() == Double.MAX_VALUE)
                        return new SimpleStringProperty("-");
                    return new SimpleStringProperty(
                            String.format("%.1f (%d)", p.getMeilleurTour(), p.getMeilleurTourNum()));
                });
        TableColumn<Pilote, String> pneusCol = new TableColumn<>("Pneus");
        pneusCol.setCellValueFactory(cellData -> {
            String pneus = cellData.getValue().getVoiture() != null ? cellData.getValue().getVoiture().getTypePneus()
                    : "-";
            return new SimpleStringProperty(pneus);
        });
        TableColumn<Pilote, String> statutCol = new TableColumn<>("Statut");
        statutCol.setCellValueFactory(cellData -> {
            String statut = cellData.getValue().isAbandonne() ? "Abandonné"
                    : cellData.getValue().isEnPiste() ? "En piste" : "Disponible";
            return new SimpleStringProperty(statut);
        });

        table.getColumns().addAll(positionCol, numeroCol, nomCol, prenomCol, toursCol, chronoTotalCol, dernierTourCol,
                meilleurTourCol,
                pneusCol, statutCol);
        table.setItems(FXCollections.observableArrayList(controller.getPilotesList()));

        root.getChildren().add(table);

        HBox actionButtons = new HBox(10);
        actionButtons.setPadding(new Insets(10, 0, 0, 0));
        Button arretStandBtn = new Button("Arrêt au stand");
        Button abandonBtn = new Button("Abandon");
        arretStandBtn.setDisable(true);
        abandonBtn.setDisable(true);
        actionButtons.getChildren().addAll(arretStandBtn, abandonBtn);
        root.getChildren().add(actionButtons);

        // Gestion de la sélection dans le tableau
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            boolean enable = newSel != null && !newSel.isAbandonne();
            arretStandBtn.setDisable(!enable);
            abandonBtn.setDisable(!enable);
        });

        // Action arrêt au stand
        arretStandBtn.setOnAction(e -> {
            Pilote pilote = table.getSelectionModel().getSelectedItem();
            if (pilote == null || pilote.isAbandonne())
                return;
            // Choix des pneus
            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(stage);
            dialog.setTitle("Arrêt au stand - Changer les pneus");
            VBox vbox = new VBox(10);
            vbox.setPadding(new Insets(20));
            Label label = new Label("Choisissez le type de pneus :");
            ComboBox<String> pneusCombo = new ComboBox<>();
            pneusCombo.getItems().addAll("Soft", "Medium", "Hard");
            pneusCombo.setValue(pilote.getVoiture() != null ? pilote.getVoiture().getTypePneus() : "Medium");
            Button validerBtn = new Button("Valider");
            Button annulerBtn = new Button("Annuler");
            HBox btns = new HBox(10, validerBtn, annulerBtn);
            vbox.getChildren().addAll(label, pneusCombo, btns);
            validerBtn.setOnAction(ev -> {
                String typePneus = pneusCombo.getValue();
                // Simuler l'arrêt au stand : +1/3 du temps d'un tour +/- 0.5s
                double base = controller.getChronoGlobalCourse() / Math.max(1, pilote.getToursEffectues() + 1); // estimation
                double tourRef = base > 0 ? base : 10.0;
                double tempsArret = tourRef / 3.0 + (Math.random() - 0.5); // +/- 0.5s
                pilote.setTempsTotal(pilote.getTempsTotal() + tempsArret);
                if (pilote.getVoiture() != null)
                    pilote.getVoiture().setTypePneus(typePneus.toLowerCase());
                showSuccess(String.format("Arrêt au stand effectué (+%.1fs)", tempsArret));
                table.refresh();
                dialog.close();
            });
            annulerBtn.setOnAction(ev -> dialog.close());
            dialog.setScene(new Scene(vbox));
            dialog.show();
        });

        // Action abandon
        abandonBtn.setOnAction(e -> {
            Pilote pilote = table.getSelectionModel().getSelectedItem();
            if (pilote == null || pilote.isAbandonne())
                return;
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Abandon");
            alert.setHeaderText("Abandon du pilote");
            alert.setContentText("Êtes-vous sûr de vouloir faire abandonner ce pilote ?");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    pilote.abandonner();
                    showSuccess("Le pilote a abandonné la course.");
                    table.refresh();
                }
            });
        });

        Scene scene = new Scene(root, 1100, 600);
        trackingStage.setScene(scene);
        trackingStage.show();

        // Lancer la simulation côté modèle
        com.f1manager.model.Course.debugController = controller;
        controller.demarrerSimulationCourse();

        // Rafraîchissement automatique de l'affichage
        trackingTimeline = new Timeline(new KeyFrame(Duration.millis(100), ev -> {
            // Chrono global
            double chrono = controller.getChronoGlobalCourse();
            chronoLabel.setText(String.format("Chrono : %.1fs", chrono));
            // Tours parcourus (max des pilotes)
            int maxTours = controller.getPilotesList().stream().mapToInt(Pilote::getToursEffectues).max().orElse(0);
            toursLabel.setText("Tours parcourus : " + maxTours + " / " + nbTours);
            // Classement dynamique
            var pilotes = controller.getPilotesList();
            pilotes.sort((p1, p2) -> {
                if (p2.getToursEffectues() != p1.getToursEffectues())
                    return Integer.compare(p2.getToursEffectues(), p1.getToursEffectues());
                return Double.compare(p1.getTempsTotal(), p2.getTempsTotal());
            });
            table.setItems(FXCollections.observableArrayList(pilotes));
            table.refresh(); // Forcer le rafraîchissement
        }));
        trackingTimeline.setCycleCount(Timeline.INDEFINITE);
        trackingTimeline.play();

        trackingStage.setOnCloseRequest(e -> {
            if (trackingTimeline != null)
                trackingTimeline.stop();
            controller.arreterSimulationCourse();
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