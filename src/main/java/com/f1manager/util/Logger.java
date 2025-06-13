package com.f1manager.util;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Logger simple pour l'application
 * Utilise le pattern Singleton pour garantir une seule instance
 */
public class Logger {
    
    private static final String LOG_FILE = "f1_manager.log";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static Logger instance;
    private boolean logToFile;
    private boolean logToConsole;
    
    /**
     * Constructeur privé pour le pattern Singleton
     */
    private Logger() {
        this.logToFile = true;
        this.logToConsole = false; // Par défaut, pas d'affichage console sauf pour les erreurs
    }
    
    /**
     * Obtient l'instance unique du logger
     * @return l'instance du logger
     */
    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }
    
    /**
     * Log un message de niveau INFO
     * @param message le message à logger
     */
    public void info(String message) {
        log("INFO", message);
    }
    
    /**
     * Log un message de niveau WARN
     * @param message le message à logger
     */
    public void warn(String message) {
        log("WARN", message);
    }
    
    /**
     * Log un message de niveau ERROR
     * @param message le message à logger
     */
    public void error(String message) {
        log("ERROR", message);
    }
    
    /**
     * Log un message de niveau ERROR avec exception
     * @param message le message à logger
     * @param e l'exception associée
     */
    public void error(String message, Throwable e) {
        String errorMessage = message + " - " + (e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName());
        log("ERROR", errorMessage);
    }
    
    /**
     * Log un message de niveau DEBUG
     * @param message le message à logger
     */
    public void debug(String message) {
        log("DEBUG", message);
    }
    
    /**
     * Méthode privée pour effectuer le logging
     * @param level le niveau de log
     * @param message le message à logger
     */
    private void log(String level, String message) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String logMessage = String.format("[%s] %s: %s", timestamp, level, message);
        
        // Affichage console pour les erreurs et warnings, ou si activé
        if (logToConsole || "ERROR".equals(level) || "WARN".equals(level)) {
            if ("ERROR".equals(level) || "WARN".equals(level)) {
                System.err.println(logMessage);
            } else {
                System.out.println(logMessage);
            }
        }
        
        // Écriture dans le fichier si activée
        if (logToFile) {
            ecrireDansFichier(logMessage);
        }
    }
    
    /**
     * Écrit le message dans le fichier de log
     * @param logMessage le message à écrire
     */
    private void ecrireDansFichier(String logMessage) {
        try (FileWriter writer = new FileWriter(LOG_FILE, true)) {
            writer.write(logMessage + System.lineSeparator());
        } catch (IOException e) {
            // Si on ne peut pas écrire dans le fichier, on affiche au moins sur la console
            System.err.println("Erreur lors de l'écriture du log dans le fichier: " + e.getMessage());
            System.err.println("Message original: " + logMessage);
        }
    }
    
    /**
     * Active ou désactive l'écriture dans le fichier
     * @param logToFile true pour activer l'écriture dans le fichier
     */
    public void setLogToFile(boolean logToFile) {
        this.logToFile = logToFile;
        if (logToFile) {
            info("Logger configuré pour écrire dans le fichier: " + LOG_FILE);
        }
    }
    
    /**
     * Active ou désactive l'affichage sur la console
     * @param logToConsole true pour activer l'affichage console
     */
    public void setLogToConsole(boolean logToConsole) {
        this.logToConsole = logToConsole;
    }
    
    /**
     * Obtient le nom du fichier de log
     * @return le nom du fichier de log
     */
    public String getLogFileName() {
        return LOG_FILE;
    }
    
    /**
     * Log le démarrage de l'application
     */
    public void logStartup() {
        info("=== DÉMARRAGE DE L'APPLICATION F1 MANAGER ===");
        info("Version: 1.0.0");
        info("Fichier de log: " + LOG_FILE);
    }
    
    /**
     * Log l'arrêt de l'application
     */
    public void logShutdown() {
        info("=== ARRÊT DE L'APPLICATION F1 MANAGER ===");
    }
    
    /**
     * Log une action utilisateur
     * @param action l'action effectuée
     */
    public void logUserAction(String action) {
        info("Action utilisateur: " + action);
    }
    
    /**
     * Log une opération sur les données
     * @param operation l'opération effectuée
     */
    public void logDataOperation(String operation) {
        info("Opération données: " + operation);
    }
    
    /**
     * Log une erreur de validation
     * @param field le champ en erreur
     * @param value la valeur invalide
     * @param reason la raison de l'erreur
     */
    public void logValidationError(String field, String value, String reason) {
        warn(String.format("Erreur de validation - Champ: %s, Valeur: %s, Raison: %s", field, value, reason));
    }
}