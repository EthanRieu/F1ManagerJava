package com.f1manager.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Gestionnaire de connexions à la base de données MySQL
 * Utilise HikariCP pour le pool de connexions
 */
public class DatabaseConnection {

    private static DatabaseConnection instance;
    private HikariDataSource dataSource;
    private Properties config;

    private DatabaseConnection() {
        loadConfiguration();
        initializeDataSource();
    }

    /**
     * Singleton pour obtenir l'instance unique
     */
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    /**
     * Charge la configuration depuis le fichier properties
     */
    private void loadConfiguration() {
        config = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("database_config.properties")) {
            if (input == null) {
                // Si le fichier n'est pas dans resources, essayer dans database/
                try (InputStream fallback = getClass().getClassLoader()
                        .getResourceAsStream("../../../database/database_config.properties")) {
                    if (fallback != null) {
                        config.load(fallback);
                    } else {
                        // Configuration par défaut
                        setDefaultConfiguration();
                    }
                }
            } else {
                config.load(input);
            }
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de la configuration : " + e.getMessage());
            setDefaultConfiguration();
        }
    }

    /**
     * Configuration par défaut si le fichier n'est pas trouvé
     */
    private void setDefaultConfiguration() {
        config.setProperty("db.url", "jdbc:mysql://localhost:3306/F1Manager");
        config.setProperty("db.username", "root");
        config.setProperty("db.password", "");
        config.setProperty("db.driver", "com.mysql.cj.jdbc.Driver");
        config.setProperty("db.pool.initial.size", "5");
        config.setProperty("db.pool.max.size", "20");
        config.setProperty("db.pool.timeout", "30000");
        config.setProperty("db.autoReconnect", "true");
        config.setProperty("db.useSSL", "false");
        config.setProperty("db.allowPublicKeyRetrieval", "true");
        config.setProperty("db.serverTimezone", "Europe/Paris");
    }

    /**
     * Initialise le pool de connexions HikariCP
     */
    private void initializeDataSource() {
        try {
            HikariConfig hikariConfig = new HikariConfig();

            // Configuration de base
            hikariConfig.setJdbcUrl(config.getProperty("db.url"));
            hikariConfig.setUsername(config.getProperty("db.username"));
            hikariConfig.setPassword(config.getProperty("db.password"));
            hikariConfig.setDriverClassName(config.getProperty("db.driver"));

            // Configuration du pool
            hikariConfig.setMinimumIdle(Integer.parseInt(config.getProperty("db.pool.initial.size", "5")));
            hikariConfig.setMaximumPoolSize(Integer.parseInt(config.getProperty("db.pool.max.size", "20")));
            hikariConfig.setConnectionTimeout(Long.parseLong(config.getProperty("db.pool.timeout", "30000")));

            // Propriétés MySQL
            hikariConfig.addDataSourceProperty("autoReconnect", config.getProperty("db.autoReconnect", "true"));
            hikariConfig.addDataSourceProperty("useSSL", config.getProperty("db.useSSL", "false"));
            hikariConfig.addDataSourceProperty("allowPublicKeyRetrieval",
                    config.getProperty("db.allowPublicKeyRetrieval", "true"));
            hikariConfig.addDataSourceProperty("serverTimezone",
                    config.getProperty("db.serverTimezone", "Europe/Paris"));

            // Optimisations
            hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
            hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
            hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            hikariConfig.addDataSourceProperty("useServerPrepStmts", "true");

            // Nom du pool
            hikariConfig.setPoolName("F1Manager-HikariCP");

            dataSource = new HikariDataSource(hikariConfig);

            System.out.println("✅ Connexion à la base de données F1Manager établie");

        } catch (Exception e) {
            System.err.println("❌ Erreur lors de l'initialisation de la base de données : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Obtient une connexion du pool
     */
    public Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("DataSource non initialisé");
        }
        return dataSource.getConnection();
    }

    /**
     * Teste la connexion à la base de données
     */
    public boolean testConnection() {
        try (Connection connection = getConnection()) {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            System.err.println("Test de connexion échoué : " + e.getMessage());
            return false;
        }
    }

    /**
     * Ferme le pool de connexions
     */
    public void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            System.out.println("🔒 Pool de connexions fermé");
        }
    }

    /**
     * Obtient les informations de configuration
     */
    public String getConnectionInfo() {
        return "URL: " + config.getProperty("db.url") +
                ", User: " + config.getProperty("db.username") +
                ", Pool Size: " + config.getProperty("db.pool.max.size");
    }
}