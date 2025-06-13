package com.f1manager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.f1manager.controller.ApplicationController;

public class MainWindow extends JFrame {
    private JTextArea outputArea;
    private JTextField inputField;
    private ApplicationController appController;

    public MainWindow() {
        super("F1 Manager");
        appController = new ApplicationController();
        initializeUI();
    }

    private void initializeUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Zone de sortie
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(outputArea);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Panel de saisie
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputField = new JTextField();
        JButton sendButton = new JButton("Envoyer");

        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        mainPanel.add(inputPanel, BorderLayout.SOUTH);

        // Gestionnaire d'événements pour le bouton et la touche Entrée
        ActionListener sendAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = inputField.getText().trim();
                if (!input.isEmpty()) {
                    processInput(input);
                    inputField.setText("");
                }
            }
        };

        sendButton.addActionListener(sendAction);
        inputField.addActionListener(sendAction);

        add(mainPanel);
    }

    private void processInput(String input) {
        // Afficher l'entrée de l'utilisateur
        outputArea.append("> " + input + "\n");

        try {
            // Traiter la commande via le contrôleur
            String response = appController.processCommand(input);
            outputArea.append(response + "\n");
        } catch (Exception e) {
            outputArea.append("❌ Erreur: " + e.getMessage() + "\n");
        }

        // Faire défiler vers le bas
        outputArea.setCaretPosition(outputArea.getDocument().getLength());
    }

    public void start() {
        setVisible(true);
        outputArea.append("Bienvenue dans F1 Manager!\n");
        outputArea.append("Tapez 'help' pour voir les commandes disponibles.\n\n");
    }
}