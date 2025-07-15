#!/bin/bash

echo "🏎️ F1 Manager - Démarrage..."

# Aller dans le répertoire du projet
cd "$(dirname "$0")"

# Lancer avec Maven (le plus simple)
mvn clean javafx:run

echo "✅ Application fermée" 