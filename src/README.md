# Space Invaders

Ein Java-Swing-basiertes Space-Invaders-Spiel mit Highscore-Datenbank, Sprite-System und einem integrierten HTTP-Server.

---

## Voraussetzungen

- **Java** 17 oder höher
- **MySQL** oder **MariaDB** (lokal installiert oder erreichbar)
- **IntelliJ IDEA** (empfohlen) oder ein anderes Java-IDE

---

## ⚠️ Datenbank-Setup (einmalig vor dem ersten Start)

Bevor das Spiel zum ersten Mal gestartet wird, muss das SQL-Skript in MySQL/MariaDB ausgeführt werden. Das Skript legt die Datenbank, die Tabelle und den Datenbankbenutzer an.

### SQL-Skript ausführen

Das Skript befindet sich unter:

```
src/main/java/de/game/Datenbank/Datenbank-Highscore.sql
```

**Option 1 – MySQL-Kommandozeile:**

```bash
mysql -u root -p < src/main/java/de/game/Datenbank/Datenbank-Highscore.sql
```

**Option 2 – MySQL Workbench / DBeaver:**

1. Datei `Datenbank-Highscore.sql` öffnen
2. Gesamten Inhalt markieren und ausführen

**Option 3 – IntelliJ Database Tool:**

1. Im „Database"-Panel eine Verbindung zum MySQL-Server herstellen
2. Rechtsklick → „Run SQL Script" → die Datei auswählen

### Was das Skript macht

```sql
-- Erstellt die Datenbank
CREATE DATABASE Spaceinvaders;

USE Spaceinvaders;

-- Erstellt die Highscore-Tabelle
CREATE TABLE highscore (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    player     VARCHAR(30),
    score      INT,
    quote      DOUBLE,
    level      INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Legt den Datenbankbenutzer an
CREATE USER 'spieler'@'%' IDENTIFIED BY 'Carlotta19!';

-- Vergibt vollständige Rechte (inkl. GRANT OPTION)
GRANT ALL PRIVILEGES ON *.* TO 'spieler'@'%' WITH GRANT OPTION;

FLUSH PRIVILEGES;
```

> **Hinweis:** Die Verbindungsdaten sind in `Datenbank.java` hinterlegt:
> - Host: `localhost:3306`
> - Datenbank: `Spaceinvaders`
> - Benutzer: `spieler`
> - Passwort: `Carlotta19!`

---

## Projekt starten

1. Projekt in IntelliJ IDEA öffnen
2. Sicherstellen, dass `src/main/java` als **Sources Root** markiert ist und `src/main/resources` als **Resources Root**
3. `Main.java` ausführen (`de.game.Main`)

Beim Start wird automatisch ein HTTP-Server auf **Port 8080** gestartet, der die Highscore-API bereitstellt.

---

## Projektstruktur

```
spaceinvaders/
├── src/main/java/de/game/
│   ├── Main.java                  # Einstiegspunkt
│   ├── core/
│   │   ├── Game.java              # Fenster & Initialisierung
│   │   ├── GameLoop.java          # Spielschleife (60 FPS)
│   │   ├── GamePanel.java         # Haupt-Panel (Swing)
│   │   └── Updatable.java         # Interface für Update-Logik
│   ├── Datenbank/
│   │   ├── Datenbank.java         # JDBC-Verbindung
│   │   ├── Highscore.java         # Highscore-Logik
│   │   ├── HighscoreEintrag.java  # Datentransfer-Objekt
│   │   ├── Server.java            # HTTP-Server (Port 8080)
│   │   └── Datenbank-Highscore.sql # ⚠️ Setup-Skript (einmalig ausführen!)
│   ├── entity/
│   │   ├── Entity.java            # Basis-Klasse für Spielobjekte
│   │   ├── Player.java            # Spieler-Logik & Dash
│   │   ├── Invader.java           # Einzelner Invasor
│   │   ├── InvaderSwarm.java      # Invasoren-Schwarm
│   │   └── Bullet.java            # Schuss (Spieler & Invasor)
│   ├── input/
│   │   └── InputHandler.java      # Tastatureingabe
│   ├── renderer/
│   │   └── GameRenderer.java      # Zeichenlogik (Sprites, UI)
│   ├── state/
│   │   ├── GameState.java         # Spielzustand (Phase)
│   │   └── GameStateManager.java  # Zustandsverwaltung
│   └── util/
│       └── Constants.java         # Zentrale Spielkonstanten
├── src/main/resources/sprites/    # Sprite-Grafiken (PNG/JPG)
└── lib/
    ├── mysql-connector-j-9.3.0.jar
    └── gson-2.13.1.jar
```

---

## Steuerung

| Taste       | Aktion |
|-------------|--------|
| `←` / `→`   | Spieler bewegen |
| `Leertaste` | Schießen |
| `Shift`     | Dash |
| `ESC`       | Pause |


---

## Highscore-API

Der integrierte HTTP-Server läuft auf `http://localhost:8080` und stellt folgende Endpunkte bereit:

| Methode | Endpunkt | Beschreibung |
|---------|----------|--------------|
| `POST` | `/highscore` | Neuen Eintrag speichern |
| `GET` | `/highscores` | Top-10-Highscores abrufen |

**Beispiel-Payload für POST:**
```json
{
  "name": "Spieler1",
  "score": 1500,
  "quote": 0.75
}
```

---

## Verwendete Bibliotheken

| Bibliothek | Version | Zweck |
|------------|---------|-------|
| `mysql-connector-j` | 9.3.0 | JDBC-Verbindung zu MySQL |
| `gson` | 2.13.1 | JSON-Serialisierung für die HTTP-API |

---

## Bekannte Hinweise

- Ist der Datenbankserver beim Start nicht erreichbar, zeigt das Spiel eine Fehlermeldung und beendet sich. MySQL muss vor dem Spielstart laufen.
- Die Sprites müssen im Classpath unter `/sprites/` erreichbar sein. In IntelliJ muss `src/main/resources` als Resources Root eingestellt sein.
