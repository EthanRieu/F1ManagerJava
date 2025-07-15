# 🗄️ Base de données F1 Manager - Guide d'installation

## 📋 Prérequis

1. **MySQL Server** installé (version 8.0 ou supérieure)
2. **MySQL Workbench** ou client MySQL pour exécuter les scripts
3. **Java MySQL Connector** (ajouté automatiquement via Maven)

## 🚀 Installation

### 1. Créer la base de données

```sql
-- Connexion à MySQL en tant qu'administrateur
CREATE DATABASE f1_manager CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Créer un utilisateur dédié (optionnel)
CREATE USER 'f1_user'@'localhost' IDENTIFIED BY 'f1_password';
GRANT ALL PRIVILEGES ON f1_manager.* TO 'f1_user'@'localhost';
FLUSH PRIVILEGES;
```

### 2. Exécuter le script de création

```bash
# Via MySQL CLI
mysql -u root -p f1_manager < database/create_tables.sql

# Via MySQL Workbench
# 1. Ouvrir MySQL Workbench
# 2. Se connecter à votre serveur MySQL
# 3. Sélectionner la base f1_manager
# 4. Ouvrir le fichier create_tables.sql
# 5. Exécuter le script
```

### 3. Configurer l'application

Modifier le fichier `database/database_config.properties` :

```properties
db.url=jdbc:mysql://localhost:3306/f1_manager
db.username=f1_user
db.password=f1_password
```

## 📊 Structure de la base de données

### Tables principales :

1. **`circuits`** - Circuits de course
2. **`pilotes`** - Pilotes de F1
3. **`voitures`** - Voitures et leurs assignations
4. **`courses`** - Courses organisées
5. **`course_pilotes`** - Participation des pilotes aux courses
6. **`arrets_aux_stands`** - Historique des arrêts aux stands

### Vues utiles :

- **`v_pilotes_voitures`** - Pilotes avec leurs voitures
- **`v_courses_details`** - Détails des courses
- **`v_classement_course`** - Classements des courses

## 🔧 Données de test

Le script inclut des données de test :

- 5 circuits célèbres (Monaco, Silverstone, etc.)
- 8 pilotes de F1 actuels
- 8 voitures correspondantes

## 🛠️ Maintenance

### Sauvegardes

```bash
# Sauvegarde complète
mysqldump -u root -p f1_manager > backup_f1_manager.sql

# Restauration
mysql -u root -p f1_manager < backup_f1_manager.sql
```

### Requêtes utiles

```sql
-- Voir tous les pilotes avec leurs voitures
SELECT * FROM v_pilotes_voitures;

-- Statistiques par équipe
SELECT equipe, COUNT(*) as nb_pilotes FROM pilotes GROUP BY equipe;

-- Courses en cours
SELECT * FROM v_courses_details WHERE statut = 'en_cours';
```

## 🔍 Dépannage

### Problèmes courants :

1. **Erreur de connexion** : Vérifiez les paramètres dans `database_config.properties`
2. **Droits insuffisants** : Assurez-vous que l'utilisateur a les bonnes permissions
3. **Timezone** : Ajustez `db.serverTimezone` selon votre région

### Logs de debug :

Activez les logs SQL dans `database_config.properties` :

```properties
db.show.sql=true
db.log.level=DEBUG
```
