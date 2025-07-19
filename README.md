# PlayCraftex Discord Bot

## Beschreibung
Ein maßgeschneiderter Discord-Bot für den PlayCraftex Minecraft-Server. Der Bot wurde entwickelt, um die Community-Interaktion zu verbessern und administrative Aufgaben zu automatisieren.

## Funktionen

- **Ticket-System**: Ermöglicht Benutzern, Support-Tickets zu erstellen und zu verwalten
- **Automatisierte Befehle**: Registrierung und Verwaltung von Discord-Slash-Commands
- **JDA-Integration**: Vollständige Integration mit der JDA (Java Discord API)

## Technische Details

- Entwickelt mit Kotlin 2.1
- Java 21 JVM
- Verwendet JDA 5.6.1 für Discord-Interaktionen
- MariaDB-Datenbankanbindung
- Hibernate ORM für Datenpersistenz

## Installation

### Voraussetzungen
- Java JDK 21 oder höher
- Gradle Build-Tool
- MariaDB-Datenbank
- Discord-Bot-Token

### Einrichtung

1. Repository klonen:
   ```
   git clone https://github.com/yourusername/playcraftex-bot.git
   cd playcraftex-bot
   ```

2. Konfiguration:
   - Erstelle eine `default.properties`-Datei im `src/main/resources/`-Verzeichnis
   - Füge folgende Einstellungen hinzu (ersetze die Werte entsprechend):
     ```
     bot_token=DEIN_DISCORD_BOT_TOKEN
     # weitere Konfigurationseinstellungen
     ```

3. Anwendung bauen:
   ```
   ./gradlew build
   ```

4. Anwendung starten:
   ```
   ./gradlew run
   ```

## Verwendung

Nach dem Start verbindet sich der Bot mit Discord und zeigt "www.playcraftex.net" als Aktivitätsstatus an. Das Ticket-System wird automatisch initialisiert.

## Mitwirken

1. Fork das Repository
2. Erstelle einen Feature-Branch (`git checkout -b feature/deine-funktion`)
3. Committe deine Änderungen (`git commit -am 'Füge neue Funktion hinzu'`)
4. Pushe zum Branch (`git push origin feature/deine-funktion`)
5. Erstelle einen Pull Request

## Lizenz

Dieses Projekt ist urheberrechtlich geschützt. Alle Rechte vorbehalten.

## Kontakt

Website: [www.playcraftex.net](https://www.playcraftex.net)
