#!/bin/bash

# Script de lancement pour l'application F1 Manager sur MacOS
# Résout les problèmes de timeout JavaFX sur Mac

echo "🏎️ Démarrage de l'application F1 Manager..."

# Compiler le projet
echo "🔨 Compilation du projet..."
mvn clean compile -q

# Générer le classpath
echo "📦 Génération du classpath..."
mvn dependency:build-classpath -Dmdep.outputFile=classpath.txt -q

# Lire le classpath
CLASSPATH=$(cat classpath.txt):target/classes

# Options JVM spécifiques pour MacOS (sans modules)
JAVA_OPTS="-XstartOnFirstThread"
JAVA_OPTS="$JAVA_OPTS -Djava.awt.headless=false"
JAVA_OPTS="$JAVA_OPTS -Dprism.order=sw"
JAVA_OPTS="$JAVA_OPTS -Djavafx.platform=desktop"

echo "🚀 Lancement de l'application..."
java $JAVA_OPTS -cp "$CLASSPATH" com.f1manager.Main

echo "✅ Application fermée" 