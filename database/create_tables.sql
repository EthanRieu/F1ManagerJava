-- Script de création des tables pour F1 Manager
-- Base de données MySQL

-- Suppression des tables existantes (dans l'ordre inverse des dépendances)
DROP TABLE IF EXISTS arrets_aux_stands;
DROP TABLE IF EXISTS course_pilotes;
DROP TABLE IF EXISTS courses;
DROP TABLE IF EXISTS voitures;
DROP TABLE IF EXISTS pilotes;
DROP TABLE IF EXISTS circuits;

-- =============================================
-- Table des circuits
-- =============================================
CREATE TABLE circuits (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL UNIQUE,
    nombre_tours INT NOT NULL DEFAULT 50,
    duree_par_tour INT NOT NULL DEFAULT 90, -- en secondes
    longueur DECIMAL(5,2) DEFAULT 0.0, -- en km
    pays VARCHAR(50),
    description TEXT,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    date_modification TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- Contraintes
    CONSTRAINT chk_nombre_tours CHECK (nombre_tours >= 1 AND nombre_tours <= 100),
    CONSTRAINT chk_duree_par_tour CHECK (duree_par_tour >= 30 AND duree_par_tour <= 600)
);

-- =============================================
-- Table des pilotes
-- =============================================
CREATE TABLE pilotes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    numero INT NOT NULL UNIQUE,
    nom VARCHAR(50) NOT NULL,
    prenom VARCHAR(50) NOT NULL,
    nationalite VARCHAR(50),
    age INT,
    statut ENUM('garage', 'piste', 'abandonne') DEFAULT 'garage',
    equipe VARCHAR(50),
    points_championnat INT DEFAULT 0,
    victoires INT DEFAULT 0,
    podiums INT DEFAULT 0,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    date_modification TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- Contraintes
    CONSTRAINT chk_numero_pilote CHECK (numero >= 1 AND numero <= 99),
    CONSTRAINT chk_age CHECK (age >= 18 AND age <= 50),
    CONSTRAINT chk_points CHECK (points_championnat >= 0)
);

-- =============================================
-- Table des voitures
-- =============================================
CREATE TABLE voitures (
    id INT AUTO_INCREMENT PRIMARY KEY,
    numero VARCHAR(10) NOT NULL UNIQUE,
    modele VARCHAR(50) NOT NULL,
    equipe VARCHAR(50) NOT NULL,
    pilote_id INT,
    couleur VARCHAR(30),
    puissance INT DEFAULT 1000, -- en chevaux
    poids DECIMAL(6,2) DEFAULT 798.0, -- en kg
    statut ENUM('disponible', 'en_course', 'maintenance', 'accident') DEFAULT 'disponible',
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    date_modification TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- Clé étrangère
    FOREIGN KEY (pilote_id) REFERENCES pilotes(id) ON DELETE SET NULL,
    
    -- Contraintes
    CONSTRAINT chk_puissance CHECK (puissance >= 800 AND puissance <= 1200),
    CONSTRAINT chk_poids CHECK (poids >= 700 AND poids <= 900)
);

-- =============================================
-- Table des courses
-- =============================================
CREATE TABLE courses (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    circuit_id INT NOT NULL,
    date_course DATETIME NOT NULL,
    statut ENUM('planifiee', 'en_cours', 'terminee', 'annulee') DEFAULT 'planifiee',
    tour_actuel INT DEFAULT 0,
    nombre_tours_total INT NOT NULL,
    duree_estimee INT, -- en minutes
    duree_reelle INT, -- en minutes
    meteo ENUM('sec', 'pluie', 'orage') DEFAULT 'sec',
    temperature DECIMAL(4,1), -- en Celsius
    date_debut TIMESTAMP NULL,
    date_fin TIMESTAMP NULL,
    vainqueur_id INT,
    notes TEXT,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    date_modification TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- Clés étrangères
    FOREIGN KEY (circuit_id) REFERENCES circuits(id) ON DELETE CASCADE,
    FOREIGN KEY (vainqueur_id) REFERENCES pilotes(id) ON DELETE SET NULL,
    
    -- Contraintes
    CONSTRAINT chk_tour_actuel CHECK (tour_actuel >= 0),
    CONSTRAINT chk_nombre_tours_course CHECK (nombre_tours_total >= 1 AND nombre_tours_total <= 100),
    CONSTRAINT chk_temperature CHECK (temperature >= -20 AND temperature <= 50)
);

-- =============================================
-- Table de liaison course-pilotes (participation)
-- =============================================
CREATE TABLE course_pilotes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    course_id INT NOT NULL,
    pilote_id INT NOT NULL,
    voiture_id INT NOT NULL,
    position_depart INT NOT NULL,
    position_arrivee INT,
    tours_effectues INT DEFAULT 0,
    temps_total_ms BIGINT DEFAULT 0, -- en millisecondes
    meilleur_tour_ms BIGINT,
    meilleur_tour_numero INT,
    dernier_tour_ms BIGINT,
    statut_course ENUM('en_course', 'abandonne', 'disqualifie', 'termine') DEFAULT 'en_course',
    type_pneus ENUM('soft', 'medium', 'hard', 'intermediate', 'wet') DEFAULT 'medium',
    points_obtenus INT DEFAULT 0,
    date_abandon TIMESTAMP NULL,
    raison_abandon VARCHAR(100),
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    date_modification TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- Clés étrangères
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
    FOREIGN KEY (pilote_id) REFERENCES pilotes(id) ON DELETE CASCADE,
    FOREIGN KEY (voiture_id) REFERENCES voitures(id) ON DELETE CASCADE,
    
    -- Contraintes
    UNIQUE KEY unique_course_pilote (course_id, pilote_id),
    UNIQUE KEY unique_course_voiture (course_id, voiture_id),
    UNIQUE KEY unique_course_position_depart (course_id, position_depart),
    CONSTRAINT chk_position_depart CHECK (position_depart >= 1 AND position_depart <= 20),
    CONSTRAINT chk_position_arrivee CHECK (position_arrivee >= 1 AND position_arrivee <= 20),
    CONSTRAINT chk_tours_effectues CHECK (tours_effectues >= 0),
    CONSTRAINT chk_temps_total CHECK (temps_total_ms >= 0),
    CONSTRAINT chk_points_obtenus CHECK (points_obtenus >= 0 AND points_obtenus <= 25)
);

-- =============================================
-- Table des arrêts aux stands
-- =============================================
CREATE TABLE arrets_aux_stands (
    id INT AUTO_INCREMENT PRIMARY KEY,
    course_pilote_id INT NOT NULL,
    tour_arret INT NOT NULL,
    type_arret ENUM('changement_pneus', 'reparation', 'carburant', 'abandon') NOT NULL,
    ancien_type_pneus ENUM('soft', 'medium', 'hard', 'intermediate', 'wet'),
    nouveau_type_pneus ENUM('soft', 'medium', 'hard', 'intermediate', 'wet'),
    duree_arret_ms BIGINT NOT NULL, -- en millisecondes
    temps_perdu_ms BIGINT DEFAULT 0, -- temps perdu par rapport au temps normal
    probleme_technique TEXT,
    heure_arret TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    efficacite ENUM('excellent', 'bon', 'moyen', 'mauvais') DEFAULT 'bon',
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Clé étrangère
    FOREIGN KEY (course_pilote_id) REFERENCES course_pilotes(id) ON DELETE CASCADE,
    
    -- Contraintes
    CONSTRAINT chk_tour_arret CHECK (tour_arret >= 1),
    CONSTRAINT chk_duree_arret CHECK (duree_arret_ms >= 1000 AND duree_arret_ms <= 120000), -- 1s à 2min
    CONSTRAINT chk_temps_perdu CHECK (temps_perdu_ms >= 0)
);

-- =============================================
-- Index pour optimiser les performances
-- =============================================

-- Index sur les recherches fréquentes
CREATE INDEX idx_pilotes_numero ON pilotes(numero);
CREATE INDEX idx_pilotes_statut ON pilotes(statut);
CREATE INDEX idx_voitures_pilote ON voitures(pilote_id);
CREATE INDEX idx_courses_circuit ON courses(circuit_id);
CREATE INDEX idx_courses_statut ON courses(statut);
CREATE INDEX idx_courses_date ON courses(date_course);
CREATE INDEX idx_course_pilotes_course ON course_pilotes(course_id);
CREATE INDEX idx_course_pilotes_pilote ON course_pilotes(pilote_id);
CREATE INDEX idx_arrets_course_pilote ON arrets_aux_stands(course_pilote_id);
CREATE INDEX idx_arrets_tour ON arrets_aux_stands(tour_arret);

-- =============================================
-- Insertion de données de test
-- =============================================

-- Circuits de test
INSERT INTO circuits (nom, nombre_tours, duree_par_tour, longueur, pays) VALUES
('Monaco', 78, 75, 3.34, 'Monaco'),
('Silverstone', 52, 90, 5.89, 'Royaume-Uni'),
('Monza', 53, 82, 5.79, 'Italie'),
('Spa-Francorchamps', 44, 105, 7.00, 'Belgique'),
('Suzuka', 53, 88, 5.81, 'Japon');

-- Pilotes de test
INSERT INTO pilotes (numero, nom, prenom, nationalite, age, equipe) VALUES
(1, 'Verstappen', 'Max', 'Pays-Bas', 26, 'Red Bull Racing'),
(44, 'Hamilton', 'Lewis', 'Royaume-Uni', 39, 'Mercedes'),
(16, 'Leclerc', 'Charles', 'Monaco', 26, 'Ferrari'),
(55, 'Sainz', 'Carlos', 'Espagne', 29, 'Ferrari'),
(63, 'Russell', 'George', 'Royaume-Uni', 25, 'Mercedes'),
(81, 'Piastri', 'Oscar', 'Australie', 22, 'McLaren'),
(4, 'Norris', 'Lando', 'Royaume-Uni', 24, 'McLaren'),
(11, 'Perez', 'Sergio', 'Mexique', 34, 'Red Bull Racing');

-- Voitures de test
INSERT INTO voitures (numero, modele, equipe, pilote_id, couleur) VALUES
('RB01', 'RB19', 'Red Bull Racing', 1, 'Bleu'),
('W01', 'W14', 'Mercedes', 2, 'Argent'),
('F01', 'SF-23', 'Ferrari', 3, 'Rouge'),
('F02', 'SF-23', 'Ferrari', 4, 'Rouge'),
('W02', 'W14', 'Mercedes', 5, 'Argent'),
('M01', 'MCL60', 'McLaren', 6, 'Orange'),
('M02', 'MCL60', 'McLaren', 7, 'Orange'),
('RB02', 'RB19', 'Red Bull Racing', 8, 'Bleu');

-- =============================================
-- Vues utiles pour l'application
-- =============================================

-- Vue des pilotes avec leurs voitures
CREATE VIEW v_pilotes_voitures AS
SELECT 
    p.id as pilote_id,
    p.numero,
    p.nom,
    p.prenom,
    p.statut,
    p.equipe,
    v.id as voiture_id,
    v.numero as voiture_numero,
    v.modele
FROM pilotes p
LEFT JOIN voitures v ON p.id = v.pilote_id;

-- Vue des courses avec détails
CREATE VIEW v_courses_details AS
SELECT 
    c.id as course_id,
    c.nom as course_nom,
    c.statut,
    c.tour_actuel,
    c.nombre_tours_total,
    ci.nom as circuit_nom,
    ci.pays,
    COUNT(cp.id) as nb_participants
FROM courses c
JOIN circuits ci ON c.circuit_id = ci.id
LEFT JOIN course_pilotes cp ON c.id = cp.course_id
GROUP BY c.id, c.nom, c.statut, c.tour_actuel, c.nombre_tours_total, ci.nom, ci.pays;

-- Vue du classement d'une course
CREATE VIEW v_classement_course AS
SELECT 
    cp.course_id,
    cp.position_arrivee,
    p.numero,
    p.nom,
    p.prenom,
    cp.tours_effectues,
    cp.temps_total_ms,
    cp.meilleur_tour_ms,
    cp.statut_course,
    cp.points_obtenus
FROM course_pilotes cp
JOIN pilotes p ON cp.pilote_id = p.id
ORDER BY cp.course_id, cp.position_arrivee;

COMMIT; 