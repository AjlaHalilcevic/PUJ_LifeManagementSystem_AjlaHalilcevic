### Life Management System (PUJ_2)

#### Opis projekta

Life Management System je desktop aplikacija razvijena u Java Swing tehnologiji koja omogućava korisniku da organizuje 
i prati svakodnevne aktivnosti kroz više modula: finansije, spavanje, učenje, navike i kalendar.

Aplikacija podržava prijavu korisnika i čuva podatke trajno u MongoDB bazi, tako da su  unosi dostupni i nakon ponovnog
pokretanja aplikacije.

#### Cilj projekta je demonstracija:

- rada sa GUI aplikacijom (Swing);
- rada sa bazom podataka (MongoDB);
- organizacije koda kroz slojeve (UI / Service / DB).

#### Tehnologije i alati:

- Java (JDK 11);
- Java Swing (GUI);
- MongoDB (perzistencija podataka);
- Maven (upravljanje zavisnostima);
- IntelliJ IDEA;
- Git & GitHub.

#### Struktura i arhitektura aplikacije

Projekat je organizovan u više slojeva:
1. UI sloj (Swing Frames) - Prikaz korisničkog interfejsa i obrada akcija (dugmad, forme, liste);
2. Service sloj (logika aplikacije) - Operacije nad podacima: dodavanje, prikaz, ažuriranje i brisanje (CRUD). Svaki tracker ima svoj Service ("SleepService", "StudyService",...);
3. DB sloj (MongoDBConnection) - Centralizovana konekcija na bazu i kolekcije.

Podaci su vezani za trenutno prijavljenog korisnika putem "Session.username", tako da svaki korisnik vidi samo svoje unose.

##### Funkcionalnosti (moduli)

1) Authentication (Prijava)
- Prijava korisnika i pamćenje aktivnog korisnika u "Session";
- Nakon prijave korisnik dobija glavni meni sa modulima.
2) Finance Tracker
- Evidencija finansijskih unosa (prihodi, troškovi);
- Pregled i upravljanje unosima po korisniku.
3) Sleep Tracker 
- Dodavanje unosa spavanja: datum, broj sati, kvalitet sna (good/bad);
- Prikaz liste unosa za prijavljenog korisnika;
- Ažuriranje i brisanje unosa;
- Čuvanje u MongoDB kolekciji ("sleep_entries").
4) Study Tracker
- Dodavanje unosa učenja: datum, minute, predmet, bilješke;
- Prikaz, update i delete unosa;
- Čuvanje u MongoDB kolekciji ("study_records").
5) Habit Tracker
- Kreiranje navike: naziv, učestalost, status aktivno/neaktivno, datum kreiranja;
- Prikaz, izmjena i brisanje navika;
- Čuvanje u MongoDB kolekciji ("habits").
6) Calendar Tracker
- Dodavanje događaja: naslov, datum, vrijeme i opis;
- Prikaz, update i delete događaja;
- Čuvanje u MongoDB kolekciji ("calendar_events").
7) Account Details
- Unos i čuvanje dodatnih informacija o korisniku (npr. puno ime i prezime, email, godine, visina, težina, ciljevi);
- Podaci su vezani za korisnika i čuvaju se u bazi.
8) Analytics

Modul prikazuje statistiku za trackere (na osnovu podataka iz baze), npr:
- Sleep: broj unosa, prosjek sati, procenat kvalitetnog sna, prosjek zadnjih 7 dana;
- Study: ukupan broj unosa, ukupno minuta, prosjek minuta/dan (i sl.);
- Habit: broj aktivnih navika, ukupno navika;
- Calendar: broj događaja, događaji u narednim danima (zavisno od implementacije).

##### Kako pokrenuti projekat (Setup)

1) Preduslovi:
- Instaliran JDK 11;
- Instaliran i pokrenut MongoDB (lokalno);
- (Opcionalno) MongoDB Compass za pregled kolekcija.
2) MongoDB podešavanje:

Aplikacija koristi konekciju: 
- URI: "mongodb://localhost:27017"
- Database: "LifeManagementDB"
3) Pokretanje aplikacije:
- Klonirati repo: "git clone (repo_link)"; 
- Otvoriti projekat u IntelliJ IDEA; 
- Maven će automatski povući dependencije (ili pokrenuti "mv clean install"); 
- Pokrenuti glavnu klasu: "SwingApp" (ili odgovarajuću main klasu u projektu).
##### Način korištenja (kratko uputstvo):
- Pokrenuti aplikaciju i prijaviti se sa korisničkim nalogom;
- Iz glavnog menija otvoriti željeni modul (Sleep/Study/Habit/Calendar/Finance);
- Dodati unose kroz formu;
- Unosi se automatski čuvaju u bazi i ostaju dostupni i nakon ponovnog pokretanja aplikacije;
- U modulu Analytics pregledati statistiku za različite trackere.
##### Napomena o Git historiji

- Projekat je razvijen kroz više commitova kako bi se pratila historija razvoja. Commit poruke opisuju implementirane funkcionalnosti.
##### Autor

- Ime i prezime: Ajla Halilčević
- IDE: IntelliJ IDEA Community Edition 2025.2.5
- Fakultet/smijer/godina: Internacionalna poslovno-informaciona akademija Tuzla / Informatika i računarstvo / treća godina studija.
