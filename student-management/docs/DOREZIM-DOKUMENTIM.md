# Student Management System — Dokumentim për Dorëzim

**Studenti:** Irida  
**Repository GitHub:** https://github.com/irida22/ProjektCloud  
**Teknologji:** Java 17, Spring Boot 3.2, Maven multi-module, Jenkins, OpenShift  

---

## Hyrje dhe përmbledhje e dorëzimit

Ky dokument përshkron projektin **Student Management System**, një aplikacion për menaxhimin e studentëve i zhvilluar në kuadër të kursit, me metodologji Agile, integrim të vazhdueshëm përmes Jenkins dhe deploy në OpenShift. Dokumentimi është shkruar në formë narrative, me qëllim që lexuesi të kuptojë qartë çfarë bën sistemi, si është ndërtuar dhe si funksionon cikli i plotë i zhvillimit deri në produksion.

Sipas kërkesës së kursit, dorëzimi përbëhet nga tre pjesë kryesore. Së pari, i gjithë kodi burim duhet të jetë i disponueshëm në GitHub, në adresën https://github.com/irida22/ProjektCloud, ku çdo modul Maven, konfigurimet dhe skedarët e nevojshëm për build dhe deploy janë versionuar dhe të aksesueshëm. Së dyti, studenti duhet të paraqesë screenshot-e që vërtetojnë punën në mjetet e menaxhimit dhe automatizimit: një pamje nga bordi i Jira-s ku detyrat e sprintit janë përfunduar, një pamje nga Jenkins që tregon një build të suksesshëm me teste dhe artefakt, si dhe një pamje nga OpenShift ku aplikacioni është deployuar dhe i aksesueshëm përmes një URL publike. Së treti, kërkohet ky dokumentim në format PDF, i cili përfshin përshkrimin e projektit, arkitekturën, përdorimin e librarive, përshkrimin e pipeline-it dhe, ku është e nevojshme, referencën ndaj deploy-it në OpenShift.

Për pjesën e OpenShift, aplikacioni është deployuar në projektin **anonymous14-dev** dhe është i aksesueshëm përmes adresës https://projekt-cloud-anonymous14-dev.apps.rm2.thpm.p1.openshiftapps.com/. Përmes kësaj adrese mund të hapet faqja kryesore e aplikacionit, të kontrollohet gjendja e shëndetit në /actuator/health dhe të përdoret API-ja REST në /api/students. Në versionin final të PDF-së rekomandohet të vendosen screenshot-et e Jira-s, Jenkins-it dhe OpenShift-it pas seksioneve përkatëse, ose në fund të dokumentit si shtojca vizuale.

---

## 1. Përshkrimi i projektit

### Qëllimi dhe funksionaliteti

Student Management System është një aplikacion web i bazuar në REST, i krijuar për të menaxhuar të dhënat e studentëve në një kontekst universitar ose arsimor. Aplikacioni lejon regjistrimin e studentëve të rinj, shikimin e listës së plotë, marrjen e të dhënave për një student specifik sipas identifikuesit numerik, përditësimin e informacionit ekzistues dhe fshirjen e një regjistrimi. Përveç operacioneve standarde CRUD, sistemi ofron edhe mundësinë e eksportimit të listës së studentëve në një skedar Excel me formatin .xlsx, funksion i cili implementohet me librarinë Apache POI dhe është i dobishëm për raportim ose përpunim të mëtejshëm jashtë aplikacionit.

Përveç API-së, aplikacioni përfshin një faqe web statike të thjeshtë, e vendosur në index.html, e cila shërben për testim manual përmes shfletuesit. Kjo faqe nuk është thelbësore për logjikën e biznesit, por e bën më të lehtë demonstrimin e projektit gjatë prezantimit ose vlerësimit, pa pasur nevojë për mjete të jashtme si Postman, edhe pse API-ja mund të testohet edhe me to.

Çdo student në sistem përfaqësohet nga fusha si emri, mbiemri, adresa e email-it, programi i studimit dhe viti i regjistrimit. Email-i trajtohet si unik: nëse përdoruesi përpiqet të regjistrojë dy studentë me të njëjtin email, aplikacioni kthen një përgjigje të qartë gabimi, duke respektuar një rregull të rëndësishëm biznesi. Në të njëjtën mënyrë, kur kërkohet një student me një identifikues që nuk ekziston, sistemi nuk kthen thjesht një përgjigje bosh, por një mesazh i strukturuar gabimi që tregon se regjistrimi nuk u gjet.

### Organizimi i punës me Agile dhe Jira

Zhvillimi i projektit është planifikuar dhe ndjekur sipas praktikave Agile. Puna është organizuar në Jira, ku detyrat janë grupuar në epike dhe user stories. Epic-i i parë, i etiketuar në kontekstin e projektit si SMS-1, mbulon API-në CRUD për studentët dhe përfshin të gjitha detyrat që lidhen me modelimin e të dhënave, shtresën e shërbimit dhe ekspozimin përmes kontrollerëve REST. Epic-i i dytë, SMS-2, fokusohet në eksportin Excel dhe modulin e veçantë që përdor Apache POI. Epic-i i tretë, SMS-3, lidhet me infrastrukturën e zhvillimit dhe deploy-it: pipeline Jenkins, ndërtimi i imazhit Docker dhe vendosja e aplikacionit në OpenShift.

Puna është ndarë në dy sprint-e. Në sprint-in e parë u realizuan funksionalitetet bazë: regjistrimi i studentit, listimi, përditësimi dhe fshirja. Në sprint-in e dytë u shtua eksporti në Excel, u konfigurua pipeline-i i Jenkins-it dhe u krye deploy-i në OpenShift. Çdo user story në Jira përshkruan nevojën nga këndvështrimi i përdoruesit ose zhvilluesit, p.sh. «Si përdorues, dua të regjistroj student të ri» ose «Si zhvillues, dua deploy në OpenShift», dhe story-t e tilla janë ndarë në detyra më të vogla teknike. Definition of Done për çdo detyrë përfshin kodin në GitHub, kalimin e testeve, dokumentimin përkatës dhe, ku aplikohet, deploy-in e funksionimit.

Për dorëzimin e kursit, në PDF duhet të përfshihet një screenshot i bordit të Jira-s ku shihen story-t e sprint-it në kolonën Done, si provë vizuale e menaxhimit Agile të projektit.

### Teknologjitë kryesore

Aplikacioni është shkruar në Java 17 dhe ndërtuar me Spring Boot në versionin 3.2. Për organizimin e kodit përdoret Maven me strukturë multi-module, që lejon ndarjen e përgjegjësive në module të pavarura por të lidhura. Si bazë të dhënash përdoret H2 në memorie, e cila është e përshtatshme për zhvillim lokal dhe për demonstrim në OpenShift pa konfigurim të komplikuar të një serveri të jashtëm baze të dhënash. Integrimi i vazhdueshëm realizohet me Jenkins, ndërsa deploy-i në mjedisin cloud bëhet në OpenShift, ku imazhi Docker ndërtohet direkt nga kodi në GitHub. Testet automatike shkruhen me JUnit 5, Mockito për izolimin e varësive në testet e shërbimit, dhe MockMvc për testet e shtresës web.

---

## 2. Arkitektura e sistemit

### Parimi i ndarjes në shtresa

Arkitektura e projektit bazohet në parimin e shtresave, ku çdo shtresë ka një përgjegjësi të qartë dhe komunikon vetëm me shtresat fqinje, pa kapërcyer logjikën e biznesit drejt kontrollerëve ose anasjelltas. Kjo qasje e bën kodin më të lexueshëm, më të lehtë për mirëmbajtje dhe më të përshtatshëm për testim të njëëshem. Në praktikë, kur një klient dërgon një kërkesë HTTP, ajo mbërrin së pari te shtresa web, ku kontrolleri REST e pranon, e validon nëse është e nevojshme, dhe e kalon te shtresa e shërbimit. Shërbimi aplikon rregullat e biznesit, përdor mapper-in për të kthyer objektet e transferimit në entitete ose anasjelltas, dhe komunikon me repository-n për ruajtjen ose leximin nga baza e të dhënave. Për eksportin Excel, shërbimi ose kontrolleri përdor modulin excel, i cili nuk varet nga detajet e HTTP por vetëm nga lista e objekteve DTO.

Projekti është ndarë edhe në shtatë module Maven, secili me një rol specifik. Moduli model përmban entitetin Student me anotacionet JPA dhe Lombok. Moduli dto përmban klasat që përdoren për komunikim me klientin, si StudentDto për përgjigje, CreateStudentRequest për krijim dhe UpdateStudentRequest për përditësim. Moduli mapper përmban logjikën e konvertimit midis entitetit dhe DTO-ve. Moduli repository ofron aksesin në bazën e të dhënave përmes Spring Data JPA. Moduli service përmban logjikën e biznesit. Moduli excel përmban eksportuesin e skedarit Excel. Moduli web është moduli i ekzekutueshëm: përmban aplikacionin Spring Boot, kontrollerët, trajtimin e gabimeve, konfigurimet dhe faqen statike. Varësitë midis moduleve janë të kontrolluara: web varet nga service, excel, repository dhe mapper; service varet nga repository, mapper, dto dhe model; dhe kështu me radhë, duke garantuar që shtresa e ulët nuk varet kurrë nga shtresa e lartë.

### Përshkrimi i moduleve

Moduli model është themeli i të dhënave. Klasa Student përfaqëson tabelën në bazën e të dhënave dhe përdor anotacione si Entity, Id dhe GeneratedValue për identifikimin automatik. Lombok përdoret për të gjeneruar konstruktorë, getterë dhe setterë, duke e mbajtur klasën të shkurtër dhe të lexueshme.

Moduli dto përmban objekte të thjeshta pa logjikë biznesi, të përshtatshme për serializim në JSON. Kjo ndarje është e rëndësishme sepse entiteti i brendshëm nuk ekspozohet drejtpërdrejt jashtë API-së, gjë që lejon ndryshime në strukturën e bazës së të dhënave pa prekur kontratën me klientin, për sa kohë mapper-i dhe DTO-t përditësohen në mënyrë të koordinuar.

Moduli mapper përmban interfacën StudentMapper, e shënuar me anotacionin Mapper të MapStruct, si dhe implementimin StudentMapperImpl, i cili kryen konvertimin manual të fushave. MapStruct në kombinim me Lombok lejon mapim të sigurt dhe të shpejtë; në këtë projekt implementimi është shkruar qartë në kod burim për transparencë dhe për lehtësi debugimi gjatë zhvillimit.

Moduli repository përmban StudentRepository, një interfejs që zgjat JpaRepository dhe ofron metoda të gatshme si findAll, save dhe delete, si dhe metodën existsByEmail për validimin e unicitetit të email-it. Spring Data JPA gjeneron automatikisht implementimin e këtij interfejsi gjatë ekzekutimit.

Moduli service përmban StudentService, ku implementohen operacionet createStudent, getAllStudents, getStudentById, updateStudent dhe deleteStudent. Këtu kontrollohet nëse email-i ekziston tashmë, nëse studenti me id të dhënë gjendet, dhe këtu hidhen përjashtimet DuplicateEmailException dhe StudentNotFoundException, të cilat më vonë kapen nga GlobalExceptionHandler në modulin web dhe kthehen si përgjigje HTTP të kuptueshme.

Moduli excel përmban StudentExcelExporter, i cili merr një listë StudentDto dhe ndërton një skedar Excel me rresht header dhe një rresht për çdo student. Skedari kthehet si varg bajtësh te kontrolleri, i cili e dërgon klientit me tipin e përmbajtjes së duhur dhe me header Content-Disposition që sugjeron shkarkimin e skedarit students.xlsx.

Moduli web lidh të gjitha këto së bashku. StudentController ekspozon endpoint-et REST. StudentManagementApplication është klasa kryesore Spring Boot. GlobalExceptionHandler trajton gabimet e biznesit dhe gabimet e validimit. Konfigurimet application.yml dhe application-openshift.yml përcaktojnë portin, datasource-in H2 dhe profilin aktiv në mjedise të ndryshme.

### Rrjedha e një kërkese tipike

Kur përdoruesi dërgon një kërkesë për të krijuar një student të ri, kontrolleri merr trupin JSON dhe e kalon te shërbimi pas validimit. Shërbimi kontrollon në repository nëse ekziston tashmë një student me atë email. Nëse po, hidhet përjashtimi për email të dyfishtë. Nëse jo, kërkesa e krijimit konvertohet në entitet Student, ruhet në bazë, entiteti i ruajtur konvertohet në StudentDto dhe kthehet te klienti me statusin HTTP 201 Created. E njëjta logjikë e ndarjes në shtresa vlen për leximin, përditësimin, fshirjen dhe eksportin: kontrolleri nuk di si funksionon SQL, repository nuk di rregullat e email-it, dhe mapper nuk di asgjë për HTTP.

### API REST

API-ja është e organizuar nën prefiksin /api/students. Metoda POST në këtë path krijon student të ri. Metoda GET pa identifikues kthen listën e të gjithë studentëve. Metoda GET me identifikues në path, për shembull /api/students/1, kthen studentin me atë id. Metoda PUT përditëson studentin ekzistues. Metoda DELETE e fshin. Path-i /api/students/export/excel, i aksesuar me GET, kthen skedarin Excel. Për monitorimin në OpenShift përdoret endpoint-i /actuator/health i Spring Boot Actuator, i cili tregon nëse aplikacioni është gati të marrë trafik.

Një shembull trupi JSON për krijim studenti përfshin firstName, lastName, email, program dhe enrollmentYear, ku fushat përkatëse plotësohen me vlera të vlefshme, si emri Ana, mbiemri Hoxha, email ana@university.edu, programi Computer Science dhe viti i regjistrimit 2024.

---

## 3. Përdorimi i librarive

Kërkesa e kursit përfshin përdorimin e librarive moderne për reduktimin e kodit, mapimin e objekteve dhe eksportin Excel. Më poshtë shpjegohet roli i secilës librari në këtë projekt, ku përdoret dhe pse është zgjedhur.

### Lombok

Lombok përdoret kryesisht në modulin model dhe në modulet që përmbajnë klasa të thjeshta transferimi. Në vend që të shkruhen manualisht konstruktorë, getterë, setterë dhe metoda të tjera të përsëritura, klasa Student dhe DTO-t shënohen me anotacione si Getter, Setter, Builder, NoArgsConstructor dhe AllArgsConstructor. Kompilatori dhe procesori i anotacioneve gjenerojnë kodin e nevojshëm gjatë kompilimit. Kjo e bën kodin burim më të lexueshëm dhe më të lehtë për ndryshim, duke ulur rrezikun e gabimeve kur shtohet një fushë e re, pasi nuk duhet përditësuar dhjetëra metoda manualisht.

### MapStruct

MapStruct përdoret në modulin mapper për konvertimin midis entitetit Student dhe objekteve DTO. Interfejsi StudentMapper deklaron metodat toDto, toEntity dhe updateEntity. Implementimi StudentMapperImpl përmban logjikën konkrete të mapimit të fushave. MapStruct ofron mapim të tipizuar në kohë kompilimi, që redukton gabimet e runtime-it që shpesh hasen kur mapimi bëhet me duart ose me reflexion. Në këtë projekt, përdorimi i MapStruct është i integruar me Spring përmes konfigurimit MapperConfiguration, në mënyrë që implementimi të injektohet si bean dhe të përdoret nga StudentService.

### Apache POI

Apache POI përdoret në modulin excel për krijimin e skedarëve Excel në formatin OOXML, pra skedarëve .xlsx. Klasa StudentExcelExporter krijon një XSSFWorkbook, shton një fletë me emrin Students, shkruan rreshtin e header-it me kolonat ID, First Name, Last Name, Email, Program dhe Enrollment Year, pastaj iteron mbi listën e StudentDto dhe shkruan një rresht për çdo student. Rezultati kthehet si byte array, i cili nga kontrolleri dërgohet te klienti si përgjigje binare me tipin e duhur MIME. Kjo librari është zgjedhur sepse është standardi i përdorur në ekosistemin Java për manipulim skedarësh Office dhe ofron kontroll të mjaftueshëm mbi strukturën e fletës pa varur nga Excel i instaluar në server.

### Spring Data JPA

Spring Data JPA përdoret në modulin repository dhe abstrakton aksesin në bazën e të dhënave. Duke zgjatur JpaRepository me tipin Student dhe Long për çelësin, projekti merr falas metoda si save, findById, findAll dhe deleteById. Metoda existsByEmail deklarohet në interfejs dhe Spring e implementon automatikisht sipas emrit të metodës. Kjo ul ndjeshëm sasinë e kodit SQL ose JPQL të shkruar manualisht dhe e bën shtresën e repository-t të fokusuar vetëm në kontratën e aksesit të të dhënave.

### Spring Boot dhe varësitë e tjera

Moduli web përdor spring-boot-starter-web për REST dhe embedded Tomcat, spring-boot-starter-data-jpa për ORM dhe lidhjen me H2, spring-boot-starter-validation për validimin e fushave të kërkesave me anotacione si NotBlank dhe Email, dhe spring-boot-starter-actuator për endpoint-in e shëndetit. Baza H2 në memorie konfigurohet në application.yml për zhvillim lokal dhe në application-openshift.yml për deploy, ku porti lexohet nga variabla e mjedisit PORT dhe profili aktiv është openshift. Kjo kombinim e lejon të njëjtin JAR të ekzekutohet lokalisht dhe në container pa ndryshime në kod burim.

---

## 4. Pipeline-i Jenkins

### Qëllimi dhe konfigurimi

Pipeline-i Jenkins automatizon procesin që pas çdo ndryshimi në kod ose pas një build-i manual verifikon që projekti kompilohet, testet kalojnë dhe artefakti i ekzekutueshëm ruhet. Job-i në Jenkins është i tipit Pipeline dhe lidhet me repository-n GitHub të ProjektCloud, degën main, ndërsa skripti i pipeline-it lexohet nga skedari Jenkinsfile që ndodhet në rrënjën e repository-t, jo brenda dosjes student-management. Kjo strukturë është e rëndësishme sepse Jenkins bën checkout të gjithë repo-së dhe pastaj ekzekuton komandat Maven brenda nëndrejtësisë student-management.

### Fazat e pipeline-it

Faza e parë quhet Checkout dhe ka për qëllim të tërheqë kodin më të fundit nga GitHub. Kjo korrespondon me kërkesën e kursit për pull nga Git dhe siguron që çdo build të bazohet në versionin e saktë të skedarëve.

Faza e dytë verifikon Java-n në makinën e Jenkins-it duke ekzekutuar java -version. Projekti kërkon Java 17 ose më të lartë, në përputhje me versionin e përdorur në Spring Boot 3.2.

Faza e tretë është Build dhe përbën thelbin e pipeline-it. Brenda dosjes student-management ekzekutohet mvnw clean install, që kompilon të gjitha modulet, ekzekuton testet dhe paketon aplikacionin. Komanda clean install është ajo që kërkon kursi dhe njëkohësisht garanton që testet të mos anashkalohen. Në fund të kësaj faze, nëse gjithçka shkon mirë, krijohet skedari student-management.jar në dosjen web/target.

Faza e katërt mbledh rezultatet e testeve JUnit duke lexuar raportet XML nga dosjet surefire-reports. Jenkins i paraqet këto në ndërfaqen Test Result, ku shihet numri i testeve të ekzekutuara, të kaluara dhe të dështuara.

Faza e pestë arkivon artefaktin JAR si build artifact, në mënyrë që të mund të shkarkohet nga ndërfaqja e Jenkins-it pas një build-i të suksesshëm. Kjo plotëson kërkesën për gjenerimin e artefaktit të ekzekutueshëm.

### Rezultatet e testeve

Projekti përmban nëntëmbëdhjetë teste automatike, të shpërndara në katër klasa. Në modulin mapper, StudentMapperImplTest verifikon që konvertimet midis entitetit dhe DTO-ve prodhojnë rezultatin e pritur, me katër teste. Në modulin service, StudentServiceTest teston logjikën e biznesit me Mockito për të simuluar repository-n dhe mapper-in, përfshirë rastet e suksesit dhe gabimet për email të dyfishtë ose student të panjohur; kjo klasë përmban nëntë teste. Në modulin excel, StudentExcelExporterTest verifikon që eksportuesi prodhon skedar me përmbajtje të dukshme, me dy teste. Në modulin web, StudentControllerTest përdor MockMvc për të testuar endpoint-et pa ngritur server të plotë, me katër teste. Gjithsej, nëntëmbëdhjetë teste, të cilat në ekzekutimin e fundit kanë kaluar të gjitha pa dështime, siç tregohet në raportin e build-it Maven dhe në ndërfaqen e Jenkins-it.

Pas një build-i të suksesshëm, në konsolën e Jenkins-it duhet të shfaqet BUILD SUCCESS, në seksionin Test Result duhet të shfaqen nëntëmbëdhjetë teste të kaluara, dhe në Build Artifacts duhet të jetë i disponueshëm skedari student-management.jar. Për dorëzimin e kursit, screenshot-i i Jenkins-it duhet të kapë këto tre elemente në një pamje të qartë.

---

## 5. Deploy në OpenShift

### Qasja e përdorur

Deploy-i në OpenShift është realizuar duke përdorur qasjen Import from Git me strategjinë Docker. Kjo do të thotë që OpenShift tërheq kodin nga GitHub, lexon Dockerfile që ndodhet në rrënjën e repository-t ProjektCloud, dhe brenda procesit të build-it ekzekuton Maven për të kompiluar të gjitha modulet dhe për të krijuar JAR-in e ekzekutueshëm. Imazhi i ndërtuar ruhet në ImageStream të projektit OpenShift, i cili në këtë rast quhet projekt-cloud ose projekt-cloud-git, varësisht nga emërtimi i aplikacionit gjatë importit. Pas build-it, aplikacioni është deployuar si Deployment me emrin projekt-cloud, i lidhur me një Service që ekspozon portin 8080, dhe me një Route që jep URL publike në internet.

Profili Spring openshift aktivizon konfigurimin në application-openshift.yml, ku serveri dëgjon në portin e caktuar nga variabla e mjedisit PORT, zakonisht 8080, dhe datasource-i H2 mbetet në memorie, e përshtatshme për demonstrim pa konfigurim të jashtëm baze të dhënash. Spring Boot Actuator ekspozon /actuator/health, i cili përdoret për të verifikuar që container-i është gati.

### Skedarët që mbështesin deploy-in

Dockerfile në rrënjën e repository-t përmban dy faza. Në fazën e parë përdoret imazhi Maven me Java 17 për të kompiluar projektin. Në fazën e dytë kopjohet vetëm JAR-i i gatshëm në një imazh të lehtë JRE dhe aplikacioni nis me java -jar. Skedari .dockerignore përjashton dosjet target dhe .git për të shpejtuar build-in. Në modulin web, application-openshift.yml përcakton sjelljen në container. Skedari openshift/deployment.yaml përmban manifeste të gatshme për Deployment, Service dhe Route, të përdorshme me komandën oc apply nëse dikush preferon deploy manual; në këtë projekt deploy-i kryesor u bë përmes ndërfaqes dhe komandave oc new-app dhe oc expose.

### Verifikimi dhe URL

Pas deploy-it, në projektin anonymous14-dev pod-i i aplikacionit duhet të jetë në gjendjen Running me një nga një kontejnerë gati. Logjet e container-it tregojnë mesazhin Started StudentManagementApplication, që konfirmon se Spring Boot ka nisur me sukses. Route-i projekt-cloud jep URL-n publike, e cila fillon me https://projekt-cloud-anonymous14-dev.apps.rm2.thpm.p1.openshiftapps.com. Hapja e kësaj adrese në shfletues duhet të shfaqë faqen e aplikacionit; shtimi i path-it /actuator/health duhet të kthejë përgjigje me status UP; dhe /api/students duhet të kthejë listën e studentëve, inicialisht bosh ose me të dhëna test.

Për dorëzimin e kursit, screenshot-i i OpenShift duhet të tregojë pod-in në gjendje Running, Route-in me host-in e plotë, dhe nëse është e mundur edhe faqen e hapur në shfletues që konfirmon aksesin.

---

## 6. Testimi

Testimi i projektit është pjesë integrale e ciklit të zhvillimit dhe nuk është i ndarë nga build-i. Çdo ekzekutim i mvn clean install ose i pipeline-it Jenkins përfshin automatikisht testet e shkruara me JUnit 5. Testet e shërbimit përdorin Mockito për të mos vënë në varësi testet nga baza e të dhënave reale. Testet e kontrollerit përdorin MockMvc për të simuluar kërkesa HTTP. Testet e mapper-it dhe excel-it fokusohen në rezultatin e konvertimit dhe të gjenerimit të skedarit. Kjo qasje siguron që ndryshimet e ardhshme në kod të mos prishin pa u vënë re funksionalitetet ekzistuese.

Për testim lokal, mjafton të ekzekutohet nga dosja student-management komanda mvnw clean install, pastaj java -jar web/target/student-management.jar, dhe të hapet http://localhost:8080 në shfletues.

---

## 7. Struktura e repository-t

Repository GitHub ProjektCloud përmban në rrënjë skedarët Jenkinsfile, Dockerfile dhe README, të nevojshëm për CI/CD dhe deploy. Brenda dosjes student-management ndodhet projekti Maven me pom.xml prind dhe shtatë nën-module: model, dto, mapper, repository, service, excel dhe web. Gjithashtu ka Maven Wrapper për ekzekutim pa Maven global, dosjen openshift me deployment.yaml, dhe dosjen docs me këtë dokumentim. Kodi burim i aplikacionit ndodhet në src/main/java të çdo moduli, ndërsa testet në src/test/java. Kjo strukturë është e qëllimshme: ajo pasqyron arkitekturën logjike dhe e bën projektin të kuptueshëm për një lexues të ri.

---

## 8. Konkluzion

Student Management System është një projekt i plotë që lidh zhvillimin e aplikacionit Java me praktikat moderne të inxhinierisë së softuerit. Aplikacioni ofron funksionalitet të qartë biznesi për menaxhimin e studentëve dhe eksportin Excel. Arkitektura multi-module me shtresa të ndara e bën kodin të organizuar dhe të testueshëm. Libraritë Lombok, MapStruct, Apache POI dhe Spring Data JPA plotësojnë kërkesat teknike të kursit dhe reduktojnë kompleksitetin e implementimit. Pipeline-i Jenkins automatizon build-in, testet dhe arkivimin e JAR-it. OpenShift hoston aplikacionin dhe e bën atë të aksesueshëm përmes një URL publike, duke plotësuar kërkesën e deploy-it në cloud.

Për dorëzimin përfundimtar, studentja duhet të sigurojë që repository GitHub është i përditësuar, të bashkojë në PDF këtë dokumentim pas konvertimit, dhe të shtojë screenshot-et e Jira-s, Jenkins-it dhe OpenShift-it si dëshmi vizuale të punës së kryer gjatë gjithë ciklit Agile, nga planifikimi deri te aplikacioni live.

---

## Shtojca: Si ta konvertosh në PDF

Hap këtë skedar në Microsoft Word ose në një redaktues që eksporton në PDF. Vendos screenshot-et e Jira-s, Jenkins-it dhe OpenShift-it pas seksioneve përkatëse ose në fund si shtojca. Ruaj dokumentin me emrin e sugjeruar Dorezim-Student-Management-Irida.pdf dhe dorëzoje sipas udhëzimeve të kursit.

---

*Dokumentim për dorëzimin e projektit — Kurs Agile / Cloud.*
