# Projekt "MyGrover.com", `Informationstechnische Projekte 5AHITN 2022 / 2023`

## Aufgabenstellung
Es ist die Managementsoftware für einen Verleiher von Computer(-Hardware) umzusetzen.
In dieser Software soll der gesamte Kundenstamm (Privatpersonen) verwaltet werden. Neben dem Kundenstamm gibt es mehrere Computermarken (z.B. DELL, Lenovo, Apple, ...) die ebenfalls über die Software gewartet werden sollen. Jeder Marke können mehrere Modelle zugeordnet werden (z.B. Latitude, Yoga, MacBook, ...).
Kunden können Computer entleihen. Hierfür wird das Ausleihe- und das geplante Rückgabedatum gespeichert. Jeder Kunde kann im selben Zeitraum (oder in überschneidenden Zeiträumen) mehrere Computer gleichzeitig ausleihen.

> **Beispiel:** Herr Müller ist aufgrund einer Dienstreise der Suche nach einem Mietlaptop. Es soll sich dabei um einen Lenovo Yoga 7 handeln. Er benötigt ihn ab dem 26.01.2023 bis zum 02.03.2023. 
> Sein mitreisender Softwareentwickler benötigt ebenfalls ein Leihgerät. Da der Mitarbeiter später zur Dienstreise dazustößt (am 02.02.2023) wird dieser erst sieben Tage später als der von Herr Müller benötigt. Die Wahl des Softwareentwicklers fällt auf ein MacBook Pro M2 von Apple.

Die entstehenden Kosten setzen sich aus einzelnen Tagessätzen und entstandenen Ladezyklen zusammen. So gibt es folgende Regeln:
* Die Tagessätze sind je Gerät variabel und für jeden Computer getrennt zu speichern
* Die Kosten pro Ladezyklus betragen immer 0,05€.

> **Beispiel:** Herr Müller bringt am 02.03.2023 den entliehenen Laptop. Der Tagessatz für den Lenovo Yoga ist mit 7,50€ festgelegt. Der Laut Akku kamen 10 Ladezyklen hinzu. In Summe ergibt das also:
> 36 Tagessätze * 7,50€ pro Tag + 10 Ladezyklen * 0,05€ pro Ladezyklus = 270,50€.
> Für das MacBook bezahlt er am 02.03.2023 29 Tagessätze mit jeweils 13,50€. Sein Mitarbeiter hatte 18 Ladezyklen. Daraus ergibt sich ein Endbetrag von: 29 Tagessätze * 13,50€ pro Tag + 18 Ladezyklen * 0,05€ pro Ladezyklus = 392,40€.

## Umsetzung
Erstellen Sie für den obenstehenden Sachverhalt eine SpringWeb - Anwendung. Erstellen Sie für die Entitäten jeweils eine Model - Klasse (z.B. Marke, Computer, Kunde, Ausleihung, ...) in denen Sie die Eigenschaften und die Beziehungen der einzelnen Klassen zueinander abbilden. Diese Model - Klassen sollen über die  entsprechenden Repositories und der JPA mit der Datenbank kommunizieren.

Erstellen Sie in Ihrer Anwendung Endpunkte zum Verwalten der Kunden (neu anlegen / bearbeiten / löschen), zum Verwalten der Computermarken (neu anlegen / bearbeiten / löschen), der Computer (neu anlegen / bearbeiten / löschen) und zum Entleihen eines PCs (entleihen / zurückbringen).

Beim Entleihen eines Coputers muss zuerst der Zeitraum übermittelt werden. Anschließend werden alle Computer, die im gewählten Zeitraum noch verfügbar sind, geliefert. Nun können die Ergebnisse wiederum nach Modell gefiltert werden. Anschließend wird der Kunde eingetragen und die Entleihung gespeichert.

Bei der Rückgabe eines Computers muss zuerst der Kunde aus einer Liste gewählt werden. Anschließend werden alle entliehenen Computer, die derzeit für den Kunden noch "offen" sind, angezeigt. Aus dieser Liste kann ein Computer gewählt werden. Nun müssen die Ladezyklen eingegeben werden und es wird der Preis berechnet und ausgegeben.

Befüllen Sie Ihre Anwendung mit sinnvollen Testdaten (Kunden, Marken, Computerhardware, Entleihungen, ...). Gerne können Sie bei Unklarheiten eigene Annahmen machen. 

## docker-compose.yml
```yml
version: '3.8'

services:
  database:
    container_name: database_mygrover
    image: mysql:8.0
    command: --default-authentication-plugin=mysql_native_password --log_bin_trust_function_creators=1
    environment:
      MYSQL_ROOT_PASSWORD: rootpwd
      MYSQL_DATABASE: mygrover
      MYSQL_USER: my
      MYSQL_PASSWORD: grover
    ports:
      - '4306:3306'
    volumes:
      - ./mysql:/var/lib/mysql
```

## Endpunkte
### Login
   * `POST` - Request
   * Endpunkt: `/login`
   * Erfoderliche Daten werden im Requst - Body als JSON - Objekt mitgeschickt.
     `{"email":"ratp@htl-steyr.ac.at", "password": "test"}`
   * Im Erfolgsfall wird der Statuscode `200` / der Accesstoken, der für 10 Minuten gültig ist, zurückgegeben.
   * Bei Misserfolg wird der Statuscode `401` zurückgebeben.

### Kunden erzeugen
   * `POST` - Request
   * Endpunkt: `/customer/create`
   * Erforderlicher Token wird als Bearer - Token im Autorizationheader mitgeschickt.
   * Erfoderliche Daten werden im Requst - Body als JSON - Objekt mitgeschickt.
     `{"name":"Peter Rathgeb", "email": "ratp@htl-steyr.ac.at"}`
   * Im Erfolgsfall wird der Statuscode `200` zurückgegeben.
   * Bei Misserfolg wird der Statuscode `400` / `401` zurückgebeben.

### Kunden ändern
   * `POST` - Request
   * Endpunkt: `/customer/update`
   * Erforderlicher Token wird als Bearer - Token im Autorizationheader mitgeschickt.
   * Erfoderliche Daten werden im Requst - Body als JSON - Objekt mitgeschickt.
     `{"id": 1, "name":"Peter Rathgeb", "email": "peter@rathgeb.at"}`
   * Im Erfolgsfall wird der Statuscode `200` zurückgegeben.
   * Bei Misserfolg wird der Statuscode `400` / `401` zurückgebeben.

### Kunden löschen
   * `DELETE` - Request
   * Endpunkt: `/customer/delete/{id}`
   * Erforderlicher Token wird als Bearer - Token im Autorizationheader mitgeschickt.
   * Im Erfolgsfall wird der Statuscode `200` zurückgegeben.
   * Bei Misserfolg wird der Statuscode `400` / `401` zurückgebeben.

### Kunden laden
   * `GET` - Request
   * Endpunkt: `/customer/load`
   * Erforderlicher Token wird als Bearer - Token im Autorizationheader mitgeschickt.
   * Im Erfolgsfall wird der Statuscode `200` zurückgegeben sowie die Daten im Requestbody zurückgegeben: `[{"id": 1, "name": "Peter Rathgeb", "email": "ratp@htl-steyr.ac.at"}, {"id": 2, "name": "Max Mustermann", "email": "max@mustermann.at"}, ...]`
   * Bei Misserfolg wird der Statuscode `400` / `401` zurückgebeben.

### Kunde laden
   * `GET` - Request
   * Endpunkt: `/customer/load/{id}`
   * Erforderlicher Token wird als Bearer - Token im Autorizationheader mitgeschickt.
   * Im Erfolgsfall wird der Statuscode `200` zurückgegeben sowie die Daten im Requestbody zurückgegeben: `{"id": 1, "name": "Peter Rathgeb", "email": "ratp@htl-steyr.ac.at"}`
   * Bei Misserfolg wird der Statuscode `400` / `401` zurückgebeben.

### Marken erzeugen
   * `POST` - Request
   * Endpunkt: `/brand/create`
   * Erforderlicher Token wird als Bearer - Token im Autorizationheader mitgeschickt.
   * Erfoderliche Daten werden im Requst - Body als JSON - Objekt mitgeschickt.
     `{"name":"Dell"}`
   * Im Erfolgsfall wird der Statuscode `200` zurückgegeben.
   * Bei Misserfolg wird der Statuscode `400` / `401` zurückgebeben.

### Marken ändern
   * `POST` - Request
   * Endpunkt: `/brand`
   * Erforderlicher Token wird als Bearer - Token im Autorizationheader mitgeschickt.
   * Erfoderliche Daten werden im Requst - Body als JSON - Objekt mitgeschickt.
     `{"id": 1, "name":"Apple"}`
   * Im Erfolgsfall wird der Statuscode `200` zurückgegeben.
   * Bei Misserfolg wird der Statuscode `400` / `401` zurückgebeben.

### Marken löschen
   * `DELETE` - Request
   * Endpunkt: `/brand/delete/{id}`
   * Erforderlicher Token wird als Bearer - Token im Autorizationheader mitgeschickt.
   * Im Erfolgsfall wird der Statuscode `200` zurückgegeben.
   * Bei Misserfolg wird der Statuscode `400` / `401` zurückgebeben.

### Marken laden
   * `GET` - Request
   * Endpunkt: `/brand/load`
   * Erforderlicher Token wird als Bearer - Token im Autorizationheader mitgeschickt.
   * Im Erfolgsfall wird der Statuscode `200` zurückgegeben sowie die Daten im Requestbody zurückgegeben: `[{"id": 1, "name": "Dell"}, {"id": 2, "name": "Apple"}, ...]`
   * Bei Misserfolg wird der Statuscode `400` / `401` zurückgebeben.

### Modell erzeugen
   * `POST` - Request
   * Endpunkt: `/model/create`
   * Erforderlicher Token wird als Bearer - Token im Autorizationheader mitgeschickt.
   * Erfoderliche Daten werden im Requst - Body als JSON - Objekt mitgeschickt.
     `{"name": "Lenovo Yoga 7 14arb", "price": 7.5, "brand": 1}`
   * Im Erfolgsfall wird der Statuscode `200` zurückgegeben.
   * Bei Misserfolg wird der Statuscode `400` / `401` zurückgebeben.

### Modell ändern
   * `POST` - Request
   * Endpunkt: `/model/update`
   * Erfoderliche Daten werden im Requst - Body als JSON - Objekt mitgeschickt.
     `{"name": "Apple Probook M2", "price": 13.5, "brand": 1}`
   * Im Erfolgsfall wird der Statuscode `200` zurückgegeben.
   * Bei Misserfolg wird der Statuscode `400` / `401` zurückgebeben.

### Model löschen
   * `DELETE` - Request
   * Endpunkt: `/model/delete/{id}`
   * Im Erfolgsfall wird der Statuscode `200` zurückgegeben.
   * Bei Misserfolg wird der Statuscode `400` / `401` zurückgebeben.

### Modelle laden
   * `GET` - Request
   * Endpunkt: `/model/load`
   * Erforderlicher Token wird als Bearer - Token im Autorizationheader mitgeschickt.
   * Im Erfolgsfall wird der Statuscode `200` zurückgegeben sowie die Daten im Requestbody zurückgegeben: `[{"id": 1, "name": "Apple Probook M2", "price": 13.5, "brand": 1}, {"id": 2, "name": "Lenovo Yoga 7 14 arb", "price": 7.5, "brand": 2}, ...]`
   * Bei Misserfolg wird der Statuscode `400` / `401` zurückgebeben.

### Modell laden
   * `GET` - Request
   * Endpunkt: `/model/load/{id}`
   * Erforderlicher Token wird als Bearer - Token im Autorizationheader mitgeschickt.
   * Im Erfolgsfall wird der Statuscode `200` zurückgegeben sowie die Daten im Requestbody zurückgegeben: `{"id": 1, "name": "Apple Probook M2", "price": 13.5, "brand": 1}`
   * Bei Misserfolg wird der Statuscode `400` / `401` zurückgebeben.

### Modelle für Marke laden
   * `GET` - Request
   * Endpunkt: `/model/brand/{brandId}`
   * Erforderlicher Token wird als Bearer - Token im Autorizationheader mitgeschickt.
   * Im Erfolgsfall wird der Statuscode `200` zurückgegeben sowie die Daten im Requestbody zurückgegeben: `[{"id": 2, "name": "Lenovo Yoga 7 14 arb", "price": 7.5, "brand": 2}, {"id": 3, "name": "Lenovo Thinkpad", "price": 6.5, "brand": 2}, ...]`
   * Bei Misserfolg wird der Statuscode `400` / `401` zurückgebeben.

### Freie Modelle im Zeitraum laden
   * `GET` - Request
   * Endpunkt: `/rental/period/{from}/{to}`
   * Erforderlicher Token wird als Bearer - Token im Autorizationheader mitgeschickt.
   * Im Erfolgsfall wird der Statuscode `200` zurückgegeben sowie die Daten im Requestbody zurückgegeben: `[{"id": 2, "name": "Lenovo Yoga 7 14 arb", "price": 7.5, "brand": 2}, {"id": 3, "name": "Lenovo Thinkpad", "price": 6.5, "brand": 2}, ...]`
   * Bei Misserfolg wird der Statuscode `400` / `401` zurückgebeben.

### Modell entleihen
   * `POST` - Request
   * Endpunkt: `/rental/rent`
   * Erforderlicher Token wird als Bearer - Token im Autorizationheader mitgeschickt.
   * Erfoderliche Daten werden im Requst - Body als JSON - Objekt mitgeschickt.
     `{"modelId": 1, "customerId": 1, "from": "2023-02-20", "to": "2023-02-25"}`
   * Im Erfolgsfall wird der Statuscode `200` zurückgegeben.
   * Bei Misserfolg wird der Statuscode `400` / `401` zurückgebeben.

### Offene Verleihungen für Kunden laden
   * `GET` - Request
   * Endpunkt: `/rental/customer`
   * Erforderlicher Token wird als Bearer - Token im Autorizationheader mitgeschickt.
   * Im Erfolgsfall wird der Statuscode `200` zurückgegeben sowie die Daten im Requestbody zurückgegeben: `[{"id": 1, "modelId": "1", "customerId": 1, "from": "2023-02-20", "to": "2023-02-25"}, ...]`
   * Bei Misserfolg wird der Statuscode `400` / `401` zurückgebeben.

### Verleihungen abschließen 
   * `POST` - Request
   * Endpunkt: `/rental/done/{rentalId}`
   * Erfoderliche Daten werden im Requst - Body als JSON - Objekt mitgeschickt.
     `{"loadings": 18}`
   * Erforderlicher Token wird als Bearer - Token im Autorizationheader mitgeschickt.
   * Im Erfolgsfall wird der Statuscode `200` zurückgegeben sowie die Daten im Requestbody zurückgegeben: `{"totalPrice": 392,40}`
   * Bei Misserfolg wird der Statuscode `400` / `401` zurückgebeben.


## Abgabe
Abgabe ist am 11.04.2023 am Ende der Stunde (um 08:00 Uhr).
Abgabe: Projektverzeichnis
Abgabeverzeichnis: `H:\Abgabe\ITP\Computerverleih`

Für verspätete Abgaben gilt: Pro Tag Verspätung -> ein Notengrad schlechter.