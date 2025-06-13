# Gestionnaire d'Écurie F1 – Projet Java/Maven

## 🌱 Structure générale du projet

```
EvalFinaleJava2/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── f1manager/
│                   ├── controller/
│                   ├── model/
│                   ├── service/
│                   ├── ui/
│                   ├── util/
│                   └── exception/
└── target/ (généré)
```

- **`src/main/java`** : code source principal (ce que tu écris).
- **`target`** : généré par Maven, contient les `.class` compilés, les JAR, etc.

---

## 🏗️ Organisation par packages

### 1. `model/` : Les entités métier (données du domaine F1)

- `Course.java` : représente une course F1 (circuit, pilotes, voitures, chrono, etc.)
- `Pilote.java` : représente un pilote.
- `VoitureF1.java` : représente une voiture de F1.
- `Circuit.java` : circuit de la course.
- `ArretAuxStands.java` : arrêt aux stands (historique, type, etc.)

**Exemple : début de `Course.java`**

```java
public class Course {
    private String id;
    private Circuit circuit;
    private List<Pilote> pilotes;
    private List<VoitureF1> voitures;
    private int tourActuel;
    private boolean courseTerminee;
    private boolean courseDemarree;
    private List<ArretAuxStands> arretsEffectues;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    // ...
}
```

- **Rôle** : stocke tout l'état d'une course, gère la simulation, les arrêts, les abandons, etc.

---

### 2. `service/` : La logique métier (gestion, règles, opérations)

- `PiloteService.java` : gestion CRUD des pilotes.
- `VoitureService.java` : gestion CRUD des voitures.
- `CourseService.java` : gestion des courses (création, démarrage, arrêt, etc.).

**Exemple : début de `PiloteService.java`**

```java
public class PiloteService {
    private List<Pilote> pilotes;

    public PiloteService() {
        this.pilotes = new ArrayList<>();
    }
    // méthodes : ajouter, modifier, supprimer, trouver, etc.
}
```

- **Rôle** : centralise la gestion des entités, applique les règles métier (ex : max 20 pilotes).

---

### 3. `controller/` : Le contrôleur principal (coordonne tout)

- `ApplicationController.java` : le cœur de l'application, fait le lien entre UI, services et modèles.

**Exemple : début de `ApplicationController.java`**

```java
public class ApplicationController {
    private PiloteService piloteService;
    private VoitureService voitureService;
    private CourseService courseService;
    // ...
}
```

- **Rôle** : reçoit les actions de l'UI, appelle les services, met à jour les modèles, expose des méthodes pour l'UI.

---

### 4. `ui/` : L'interface graphique (JavaFX)

- `MainWindow.java` : fenêtre principale.
- `CourseMenu.java` : suivi de la course (tableau, chrono, boutons d'action).
- `PilotesMenu.java`, `VoituresMenu.java`, `StatistiquesMenu.java` : gestion des entités et stats.

**Exemple : création du tableau de suivi de course dans `CourseMenu.java`**

```java
TableView<Pilote> table = new TableView<>();
TableColumn<Pilote, Integer> numeroCol = new TableColumn<>("Numéro");
numeroCol.setCellValueFactory(new PropertyValueFactory<>("numero"));
// ... autres colonnes
table.setItems(FXCollections.observableArrayList(controller.getPilotesList()));
```

- **Rôle** : affiche les données, permet les interactions utilisateur (ajout, arrêt, abandon, etc.).

---

### 5. `util/` : Utilitaires

- `Logger.java`, `Utils.java`, `Constants.java` : fonctions d'aide, constantes, logs, etc.

---

### 6. `exception/` : Exceptions personnalisées

- `CourseException.java`, `PiloteException.java` : pour gérer les erreurs métier de façon claire.

---

## ⚙️ Relation avec Maven

- **Maven** gère la compilation, les dépendances (ex : JavaFX), les tests, la génération du JAR.
- `src/main/java` → compilé dans `target/classes`.
- `pom.xml` (non affiché ici) : décrit les dépendances (JavaFX, JUnit, etc.), la version Java, les plugins.

---

## 🔗 Relations entre les couches

- **UI** (JavaFX) ←→ **Controller** (`ApplicationController`) ←→ **Services** (`PiloteService`, etc.) ←→ **Model** (`Pilote`, `Course`, etc.)
- **Util** et **Exception** sont utilisés partout où besoin.

---

## 🏎️ Exemple de flux complet (démarrage d'une course)

1. **L'utilisateur clique sur « Démarrer une course » dans l'UI** (`CourseMenu.java`).
2. **L'UI appelle** une méthode du contrôleur :
   ```java
   controller.demarrerCourse(nomCircuit, nbTours, dureeTour, pilotesSansVoiture);
   ```
3. **Le contrôleur** configure la course via le service :
   ```java
   courseService.configurerCourse(new Circuit(...));
   ```
4. **Le service** crée une nouvelle instance de `Course`, ajoute les pilotes, etc.
5. **La simulation** démarre, les temps sont mis à jour dans le modèle.
6. **L'UI** se met à jour en temps réel grâce aux données du modèle.

---

## 📦 Résumé visuel des dépendances

```
[UI JavaFX] <--> [ApplicationController] <--> [Services] <--> [Model]
         ^                                         |
         |                                         v
      [Util] <--------------------------------> [Exception]
```

---

## 📝 Conclusion

- **src/** = tout ton code source, organisé par responsabilité (modèle, service, UI, etc.).
- **target/** = tout ce qui est généré automatiquement (compilation, exécution).
- **Maven** orchestre la compilation, les dépendances, la génération du JAR.
- **Le code est structuré de façon professionnelle** : séparation claire des responsabilités, MVC, services, exceptions, utilitaires.

---

Si tu veux un focus sur un fichier précis, une explication détaillée d'une classe, ou un schéma UML, demande-le !
