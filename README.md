# e_case

### Hvordan skal applikasjonen brukes?
Jeg har utviklet to applikasjoner. En server og en klient. I klienten skal man laste opp datasettet ved å klikke på "velg fil" knappen øverst.
Da man har lastet denne opp vil den sendes til serveren og lagres i databasen. Innholdet i fila vil også leses av klienten, og innholdet vil dukke opp
i boksen nederst. Statistikken fylles også ut så snart filen er opplastet.
I utgangspunktet skulle klienten fått tilsendt innholdet i fila fra serveren. Dette fikk jeg ikke til å implementere. Jeg slet med å finne ut av hvordan jeg
skulle håndtere CORS på server-siden. Jeg klarte ikke å få preflight-requesten fra klienten til å gå gjennom, og dermed kunne jeg ikke lese response body fra serveren.

### Hva løser applikasjonen?
Applikasjonen gjør det lettere å håndtere brukerdataene. Gjennom klienten får man nyttig statistikk, samtidig som man kan søke på dataene. Serveren gjør også lagring 
av dataene i en database. Statistikk som prosentandel brukere innenfor aldersgrupper, stater med flest og færrest brukere og totalt antall brukere gir et godt bilde
av situasjonen. Utover dette ser jeg også at det kunne vært nyttig å bruke gps-koordinatene. Med lengre tid hadde jeg implementert et heatmap, med oversikt over hvor
brukerne befinner seg.

### Hvordan bygges applikasjonen?
Applikasjonen er delt opp i to deler. Serveren har jeg utviklet i Java, mens klienten er utviklet med React. Serveren benytter seg av DAO-patternet for håndtering av databasen.
Jeg bruker en .properties fil med informasjon som trengs for å connecte til databasen. Serveren har også en klasse for å lese fila som sendes inn fra klienten. Denne klassen tar i mot filen og parser dataen til en liste med "User" objekter. HttpServer klassen tar seg av kommunikasjon med klienten. Serveren tar i bruk Sockets for håndtering av kommunikasjon.
Serveren er laget med Maven, som genererer en executable .jar fil.
Klienten har kun en side hvor all informasjon ligger. Siden jeg ikke fikk til å håndtere CORS på server siden, må jeg lese inn dataen fra fila direkte i klienten. Dette gjøres ved hjelp av en "fileReader" som leser fila med en gang den er lastet opp. Etter fila er lest oppdaterer jeg state variabelen "setContents", med parset innhold fra fila. Da "setContents er oppdatert kalles en "useEffect" funksjon automatisk. Denne funksjonen kaller på alle funksjonene som trengs for å fylle ut statistikken. Datasettet har 100000 rader med data, noe som resulterte i frysing av applikasjonen og ekstremt hakkete scrolling i begynnelsen. Dette løste jeg ved å implementere "infinite scroll". Dette gjør slik at elementene i listen lastes inn mens du scroller, slik at den ikke prøver å laste inn alle på en gang. Gjennom klienten kan du også søke i datasettet. Du søker da på alt som er String, og returnerer et resultat av alle rader i datasettet som inneholder søkeordet. 

### Hvordan kjører man opp applikasjonen?
Serveren er bygget med Maven og kan generere en executable .jar fil. Det første man må gjøre er å laste ned prosjektet og gjøre nødvendige endringer i "postgresql.properties" fila(endre datbase url, endre brukernavn, skrive passord). Etter man har har gjort dette kan man åpne terminalen og skrive inn "mvn clean install". Da dette er ferdig kjører man kommandoen "mvn package". Etter dette navigarer man seg inn i "target" mappa, hvor man kjører kommandoen: "java -cp etjenesten-case-1.0-SNAPSHOT.jar HttpServer" som starter serveren.
For å kjøre opp klienten må man først laste ned prosjektet. Applikasjonen bygger på npm, og bruker også dette for å kjøre. Etter dette åpner du terminalen og navigerer deg til prosjekt mappa. Herfra kjører man kommandoen "npm install" for  å installer alle packages. Da denne kommandoen er ferdig kjører man "npm start". Dette vil starte en "Development server" som kjører på localhost.
Dersom man ønsker at fila skal lagres i databasen er det viktig å kjøre opp serveren først.

Jeg synes oppgaven var spennende og ser flere ting jeg kunne gjort annerledes med mer tid. Jeg hadde gjort CORS håndtering på server siden, og definitivt lagt på en del styling.
Hadde jeg hatt mer tid kunne jeg også tenkt meg å anvende GPS koordinatene i samsvar med google maps api.
