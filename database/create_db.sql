-- Script de création des tables simplifiées pour F1 Manager
-- Base de données MySQL

-- Suppression des tables existantes
DROP TABLE IF EXISTS voitures;
DROP TABLE IF EXISTS pilotes;
DROP TABLE IF EXISTS circuits;

-- =============================================
-- Table des pilotes
-- =============================================
CREATE TABLE pilotes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    numero INT NOT NULL UNIQUE,
    nom VARCHAR(50) NOT NULL,
    prenom VARCHAR(50) NOT NULL,
    
    -- Contraintes
    CONSTRAINT chk_numero_pilote CHECK (numero >= 1 AND numero <= 99)
);

-- =============================================
-- Table des voitures
-- =============================================
CREATE TABLE voitures (
    id INT AUTO_INCREMENT PRIMARY KEY,
    numero_voiture VARCHAR(10) NOT NULL UNIQUE,
    pilote_id INT,
    
    -- Clé étrangère
    FOREIGN KEY (pilote_id) REFERENCES pilotes(id) ON DELETE SET NULL
);

-- =============================================
-- Table des circuits
-- =============================================
CREATE TABLE circuits (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL UNIQUE,
    nombre_tours INT NOT NULL DEFAULT 50,
    temps_moyen_par_tour INT NOT NULL DEFAULT 90, -- en secondes
    
    -- Contraintes
    CONSTRAINT chk_nombre_tours CHECK (nombre_tours >= 1 AND nombre_tours <= 100)
);

-- =============================================
-- Index pour optimiser les performances
-- =============================================
CREATE INDEX idx_pilotes_numero ON pilotes(numero);
CREATE INDEX idx_voitures_pilote ON voitures(pilote_id);

-- =============================================
-- Insertion de données de test
-- =============================================

-- Pilotes de test
INSERT INTO pilotes (numero, nom, prenom) VALUES
(1, 'Verstappen', 'Max'),
(44, 'Hamilton', 'Lewis'),
(16, 'Leclerc', 'Charles'),
(55, 'Sainz', 'Carlos'),
(63, 'Russell', 'George'),
(81, 'Piastri', 'Oscar'),
(4, 'Norris', 'Lando'),
(11, 'Perez', 'Sergio');

-- Voitures de test
INSERT INTO voitures (numero_voiture, pilote_id) VALUES
('RB01', 1),
('W44', 2),
('F16', 3),
('F55', 4),
('W63', 5),
('M81', 6),
('M04', 7),
('RB11', 8);

-- Circuits de test
INSERT INTO circuits (nom, nombre_tours, temps_moyen_par_tour) VALUES
('Monaco', 16, 15),
('Silverstone', 11, 18),
('Monza', 11, 16),
('Spa-Francorchamps', 10, 21),
('Suzuka', 11, 18);

-- =============================================
-- Vue utile pour l'application
-- =============================================
CREATE VIEW v_pilotes_voitures AS
SELECT 
    p.id as pilote_id,
    p.numero,
    p.nom,
    p.prenom,
    v.id as voiture_id,
    v.numero_voiture
FROM pilotes p
LEFT JOIN voitures v ON p.id = v.pilote_id;

COMMIT; 