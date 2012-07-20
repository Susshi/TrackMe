Trackme - Bedienungsanleitung

1. Installation
   Die Applikation TrackMe kann wie jede andere Applikation auch durch Installation
   der zugehörigen apk Datei installiert werden. Zur korrekten Funktionsweise des
   Datenaustausches ist die Anwendungen "ibr-dtn" erforderlich. Ebenso muss das Gerät
   über eine Telefonnummer verfügen.



2. Hauptmenü
   Das Hauptmenü der Anwendung TrackMe.
   
2.1 Map Button
    Durch klicken auf den mit "Map" gekennzeichneten Button wird die Karte geöffnet.
    Genaueres siehe Abschnitt 5.

2.2 Settings Button
    Durch klicken auf den mit "Settings" gekennzeichneten Button werden die Einstellungen geöffnet.
    Genaueres siehe Abschnitt 3.

2.3 Credits Button
    Durch klicken auf den mit "Credits" gekennzeichneten Button werden die Credits geöffnet.
    Genaueres siehe Abschnitt 4.

2.4 Generelles
    Drücken der "Home" oder "Zurück" Taste versetzt TrackMe in den Hintergrund.



3. Settings
   Dieser Bereich regelt die vielfältigen Einstellungsmöglichkeiten von TrackMe.

3.1 DTN Einstellungen
    Diese Einstellungen regeln den Datenaustausch zwischen Geräten.

    Data Retransmission Time (seconds) :
    Zählt die Sekunden die gewartet werden soll bis wiederholt Routen an einen bereits
    bekannten Teilnehmer geschickt werden. Sollten sich zwei oder mehr Teilnehmer längere
    Zeit im selben Netzwerk befinden, ist es nicht nötig ständig Daten erneut auszutauschen
    da sich die Route wenig bis gar nicht verändert haben wird.

    Presence Notification Delay (seconds):
    Zählt den zeitlichen Abstand zwischen zwei Presence Nachrichten in Sekunden. Die
    Presence Nachrichten empfangen alle TrackME-Teilnehmer im Netzwerk. Der Empfänger
    beantwortet eine solche Presence Nachricht nach Ablauf der Data Retransmission Time
    (siehe oben) mit Routen aus der Datenbank.

    Presence TTL (seconds):
    Zählt die Time-to-live der Presence Nachrichten in Sekunden. Diese Zeit gibt an
    wielange Presence Nachrichten innerhalb des DTN Netzes gültig bleiben.

    Data TTL (seconds):
    Zählt die Time-to-live der Daten-Nachrichten in Sekunden. Diese Zeit gibt an
    wielange Daten Nachrichten innerhalb des DTN Netzes gültig bleiben.

3.2 Datenbank Einstellungen
    Dieser Tab regelt die Einstellungen für die lokal gespeicherten Daten.
    
    Update intervall (seconds):
    Dieser Parameter gibt die Zeit in Sekunden an, die mindestens vergangen sein
    muss bis eine erneute Abfrage der eigenen Positionsdaten erfolgen darf. Ist
    diese vorgegebene Zeit noch nicht abgelaufen, wird keine neue eigene Position
    ermittelt.   

    Min distance between points (meters):
    Gibt an, wie weit die aktuell ermittelte eigene Position von der letzten
    Position entfernt sein muss, um in der eigenen Route aufgenommen zu werden.
    Positionsänderungen unterhalb dieser Entfernungen führen nicht zu einem neuen
    Wegpunkt.

    Max time between points (seconds):
    Dieser Parameter bestimmt, nach wie viele Sekunden ein neues Positionsdatum
    in die eigene Route übernommen wird, auch wenn dessen Genauigkeit schlechter
    ist, als die des letzten Positionsdatum.

    Route TTL (hours):
    Mit diesem Parameter lässt sich bestimmen, wie lange die eigenen Routendaten
    weiterverbreitet werden dürfen. Ist die Time-to-live (TTL) abgelaufen, so
    werden die entsprechenden Positionsdaten automatisch bei jedem Nutzer, der
    die entsprechenden Daten erhalten hat gelöscht und nicht mehr an weiter Nutzer
    weitergeleitet. Daten, deren Erhebung weniger als ‚Route TTL‘ Sekunden her
    ist, bleiben erhalten
        
3.3 Karten Einstellungen
    Dieses Menü bietet diverse Einstellungen für die Karte und die Routen an.

    Show:
    Mit "All" oder "Friends" wird angegeben, ob die Routen aller Personen
    (anonymisiert) oder nur die Routen von Freunden (Personen im Telefonbuch) sichtbar
    seien sollen.

    Show legend:
    Bestimmt ob die Legenden standardmäßig sichtbar ist oder nicht.
    
    Update position:
    Bestimmt ob die Karte der aktuellen Position automatisch folgt
    oder nicht.
    
    Update automatically:
    Legt fest, ob Routen bei Eingang neuer Daten automatisch
    aktualisiert werden sollen oder nicht.
    
    Satellite:
    Bestimmt ob der Satelliten von Google Maps verwendet werden soll oder nicht.
    
    Traffic:
    Bestimmt ob der Verkehrsmodus von Google Maps verwendet werden soll oder nicht.
    
    Antialiasing:
    Legt fest, ob die Kanten der Routen geglättet werden sollen oder nicht.
    
    Stroke width:
    Bestimmt die Breite der eingezeichneten Routen auf der Karte.
    
3.4 Generelles
    Diese Einstellungen stellen Standardeinstellungen dar. Sie werden immer beim Erstellen
    Anwendungen geladen. Drücken der "Home" Taste versetzt TrackMe
    in den Hintergrund. Die "Zurück" Taste führt zur vorherigen Anwendung zurück.



4. Credits
   Dieser Bereich zeigt den Kontext in dem die Anwendungen entstanden ist und die
   verantwortlichen Personen.
   Drücken der "Home" Taste versetzt TrackMe in den Hintergrund.
   Die "Zurück" Taste führt zur vorherigen Anwendung zurück.



5. Karte
   Beschreibung der Funktionalität der Karte.

5.1 Legende
    Die Legende zeigt die zugehörigen Personen zu den angezeigten Routen. Ein Eintrag
    besteht aus Bild, der Anzahl der Punkte der Route und dem Namen. Name und Bild werden
    dem Telefonbuch entnommen. Durch langes Antippen eines Legendeneintrags wird die zugehörige
    Route auf der Karte zentriert.

5.2 Menü
    Das Menü kann durch Antippen der Menü Taste des Geräts geöffnet und geschlossen werden.
    Es bietet die Möglichkeit die Standardeinstellungen zeitweise zu überschreiben.
    Das Menü bietet folgende Möglichkeiten:

    Show: Für die Bedeutung von "All" und "Friends" siehe Abschnitt 3.3. Mit "Custon" wird
    die benutzerdefinierte Auswahl (siehe Abschnitt 5.3) geöffnet.
    
    Satellite: siehe Abschnitt 3.3.
    
    Traffic: siehe Abschnitt 3.3.

    Update position: siehe Abschnitt 3.3.

    Show legend: siehe Abschnitt 3.3.

    Edit default settings: Öffnet den Einstellungsdialog beschrieben in Abschnitt 3.

    Force update: Erzwingt eine Aktualisierung der Routen.

5.3 Benutzerdefinierte Routenauswahl
    Diese Auswahl dient dazu, nur bestimmte Routen ein-/auszublenden. Markierte Einträge
    werden angezeigt. Nicht markierte Einträge ausgeblendet. Mit "OK" wird die Auswahl bestätigt.
    Mit "Cancel" werden die Änderungen verworfen.

5.4 Generelles
    Drücken der "Home" Taste versetzt TrackMe in den Hintergrund.
    Die "Zurück" Taste führt zur vorherigen Anwendung zurück.

