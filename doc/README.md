SIFCore Entwickler-Quickstart-Guide
===================================

Willkommen zum SIFCore Entwickler-Guide, dieses Dokument gibt eine kurze Übersicht 
und Anleitung wo sich welche Sachen im Projektverzeichnis befinden und wie man bestimmte Dinge umsetzt.
Grundsätzlich befinden sich viele Informationen in meiner
Diplomarbeit <i>Architekturanalyse und Reengineering einer Prüfumgebung für Spreadsheets</i> und
ich empfehle jedem zumindest die Kapitel Anforderungen, Entwurf und Implementierung zu lesen.
Auch die __F.A.Q.__ am Ende dieses Dokuments sollten durchgelesen werden!

Projektverzeichnis
------------------

<dl>
    <dt>build/</dt>
    <dd>Dieses Verzeichnis wird von Gradle beim <i>build</i> erstellt.
    Im <i>classes/</i>-Verzeichnis befinden sich die kompilierten <i>class</i>-Dateien von SIFCore.
    Im <i>libs/</i>-Verzeichnis sind die erstellten <i>jar</i>-Files abgelegt, einmal ohne Abhängigkeiten
    und einmal mit allen benötigten Bibliotheken.
    Im <i>jacoco/</i>-Verzeichnis befinden sich die Ergebnisse zur Testabdeckung 
    die zum Beispiel von Sonarqube oder anderen Programmen weiter analysiert werden kann.
    Im <i>reports/</i>-Verzeichnis befindet sich ein für HTML-Report der Tests.</dd>
    <dt>doc/</dt>
    <dd>Hier befindet sich dieser Guide und einige Architekturbeschreibungen.
    Die Klassendiagramme (.uml-Dateien) können mit IntelliJ IDEA geöffnet werden.</dd>
    <dt>gradle/</dt>
    <dd>Hier befinden sich die vom Gradle-Weapper benötigten Dateien.</dd>
    <dt>src/</dt>
    <dd>In diesen Ordner befinden sich der Sourcecode, im Unterordner <i>test/</i> ist
    das <i>siftest</i>-Modul mit den Modul- und Integrationstests abgelegt.
    Im Unterordner <i>sif</i> befindet sich SIFCore im Package <i>sif</i>.</dd>
</dl>

SIFCore Java-Packages
---------------------

SIFCore ist in einzelne Module in Form von Java-Packages unterteilt.

<dl>
    <dt>api</dt>
    <dd>In diesem Modul befinden sich die drei bisher implementierten REST-Endpunkte in Form von Resourcen.
    Über Annotationen werden Jersey 2.0 <i>Root Resource Classes</i> definiert. Die Dokumentation für Jersey findet sich <a href="https://jersey.java.net/documentation/latest/index.html">hier</a>.
    Der <i>InspectionService</i> steuert den grundsätzlichen Ablauf innerhalb von SIFCore, er wird also von der OdsResource (LibreOffice) und der OoxmlResource (Excel xls. und .xlsx) verwendet.
    Die HeartbeatResource ist nur für Tests da und um zu testen ob der Server läuft.</dd>
    <dt>app</dt>
    <dd>Hier befindet sich die Konfiguration für Google Guice (das hier verwendete <i>Dependency Injection</i> Framework). Wenn man eine neue Klasse erstellt, oder neue Factories definieren will, muss man wahrscheinlich hier im entsprechenden Guice-Modul diese Klassen dann <i>binden</i>.</dd>
    <dt>testcenter</dt>
    <dd>Hier befinden sich die ganzen Module für die einzelnen Prüfungen. Weitere Details bitte in der Diplomarbeit nachlesen.</dd>
    <dt>io</dt>
    <dd>Hier befinden sich die Schnittstellen und Implementierungen, um die XML-Dateien mit den Policies einzulesen und zu verarbeiten (Modul <i>policy</i>) und um die Spreadsheets in das interne Modell zu überführen.
    Weitere Details bitte in der Diplomarbeit nachlesen.</dd>
    <dt>model</dt>
    <dd>Hier befinden sich die Klassen für die Datenhaltung. Das Grundelement ist das <i>Spreadsheet</i>.
    In diesem können sich beliebig viele <i>Worksheets</i> befinden, die jeweils <i>Rows</i>, <i>Columns</i> sowie eine Anzahl an <i>Cells</i> beinhalten. Alle die Objekte sind <i>Elements</i> und können über entsprechende <i>Factories</i> erstellt werden.
    Vorsicht, diese Factories existieren nur als Interface, weil die Implementierung implizit durch Google Guice erstellt wird. Dieser Vorgang wird <i>AssistedInject</i> genannt und Details können <a href="https://github.com/google/guice/wiki/AssistedInject">hier</a> nachgelesen werden.</dd>
    <dt>scanner</dt>
    <dd>Hier befinden sich die Scanner, mit denen Metainformationen des Spreadsheets erstellt werden können, damit diese in Prüfungen verwendet oder an SIFEI weitergegeben werden können.
    Die Scanner werden vor der Ausführung der Prüfungen aufgerufen.</dd>
    <dt>utility</dt>
    <dd>Hier befinden sich die Klassen für die Übersetzung und für die Konvertierung von Werten.</dd>
</dl>

F.A.Q.
------

<dl>
    <dt>Welche Entwicklungsumgebungen werden empfohlen?</dt>
    <dd>Ich empfehle IntelliJ IDEA (für Studenten ist auch die Ultimate-Edition kostenlos, ansonsten die Community Edition) oder natürlich Eclipse, wobei jeweils die entsprechenden Plugins für Git- und Gradle-Unterstützung installiert werden sollten.</dd>
</dl>

<dl>
    <dt>Was bedeuten diese @Inject unnd @AssistedInject Annotationen und warum findet man keine Implementierung für viele Factories?</dt>
    <dd>In SIFCore wird das Entwurfsmuster <i><a href="https://de.wikipedia.org/wiki/Dependency_Injection">Dependency Injection</a></i> (DI) eingesetzt.
    Dabei wird das Google Guice Framework in der Version 4.1 eingesetzt. Das <a href="https://github.com/google/guice/wiki">Wiki</a> von Google Guice beinhaltet alle wichtigen Informationen wie und warum man DI einsetzt.
    Ich empfehle <b>dringend</b> dass zumindest die Punkte <i>Motivation</i> und <i>Getting started</i> gründlich durchgelesen und verstanden werden.
    In SIFCore werden die folgenden DI-Features verwendet:
    <ul>
    <li><a href="https://github.com/google/guice/wiki/CustomScopes">Custom Scopes</a> um einige Objekte mit @Request einzigartig für jeden HTTP-Request zu machen.</li>
    <li><a href="https://github.com/google/guice/wiki/Multibindings">Multibindings</a> um automatische alle Scanner und Prüfungen zu laden und auszuführen.</li>
    <li><a href="https://github.com/google/guice/wiki/AssistedInject">AssistedInject</a> um Spreadsheet-Elemente mit Factories erstellen zu können.</li>
    </ul>
    </dd>
</dl>

<dl>
    <dt>Wie kann man eine neue Prüfung erstellen?</dt>
    <dd>Um eine neue Prüfung zu erstellen müssen die folgenden Schritte durchgeführt werden:
    <ol>
    <li>Ein neues Java-Package im <i>testcenter</i>-Package erstellen.</li>
    <li>Eine Facility, eine Policy und eine Violation in diesem Package erstellen, die jeweils von den Basisklassen im <i>testcenter</i> abgeleitet werden.</li>
    <li>In der Facility-Klasse die Methoden <i>getPolicyClass()</i> und <i>setPolicy()</i> mit der korrekten Policy überladen.
    <li>Die Facility im <i>app.TestcenterModule</i> mit einem <i>facilityBinder</i> binden.</li>
    <li>Erstellen des Namens, der Beschreibung und weiterer Details der Policy durch den Aufruf der enstprechenden Setter im Kontruktor.
    <li>Erstellen von Feldern in der Policy-Klasse für eventuell gewünschte Konfigurationsmöglichkeiten der Prüfungen.</li>
    <li>Registrierung eines neues <i>@XmlElement</i> in der InspectionRequest-Klasse für die Policy. Nun kann mit einem solchem XML-Element die Ausführung der Prüfung getriggert werden.</li>
    <li>Implementierung der Prüfung innerhalb der <i>run()</i>-Methode der Facility-Klasse.<br />
    Dabei sollte bei einem Verstoß eine neue <i>Violation</i> mit <i>validationReport.add(new MyNewViolation(cell))</i> an den <i>validationReport</i> der <i>Facility</i> angehängt werden.
    Am Ende der Prüfung wird dieser dann mit <i>spreadsheetInventory.getInspectionResponse().add(validationReport)</i> an die HTTP-Response angehängt.</li>
    </ol>
    Man kann als Beispielimplementierung auch die NonConsideredValues-Prüfung verwenden, diese Prüfung ist sehr einfach aufgebaut,
    nutzt aber bereits die Variable <i>checkStrings<i> zur Konfiguration der Prüfung.
    </dd>
</dl>

<dl>
    <dt>Wie kann man zu einer Prüfung zusätzliche Konfigurationsmöglichkeiten hinzufügen?</dt>
    <dd>Über Felder (private Variablen mit Gettern und Settern) in der entsprechenden Policy-Klasse.
    Als Beispiel für eine komplexe Konfiguration mit verschachtelten Objekten kann die <i>dynamic_testing.DynamicTestingPolicy</i> herangezogen werden.</dd>
</dl>

<dl>
    <dt>Wie kann ich sicherstellen, dass meine Arbeit eine zufriedenstellende Qualität hat?</dt>
    <dd>Am besten die in der DA verwendeten Werkzeuge Sonarqube und Teamscale testen.
    Ich habe auch sehr gute Erfahrungen mit der "Code Inspection" von IntelliJ IDEA gemacht.
    Immer gleich Integrationstests für neue Prüfungen implementieren und schauen, dass die Anzahl der Probleme die von den Analysewerkzeugen gefunden wird gering bleibt und dass die Testabdeckung hoch ist.
<<<<<<< HEAD
    <br />Bitte vorsichtig mit <i>copy&paste<i> zu Werke gehen und immer prüfen, ob die gewünschte Funktionalität nicht ohne Kopieren des Codes möglich ist.
    <br />Bitte die Java <a href="http://www.oracle.com/technetwork/java/codeconvtoc-136057.html">Code-Conventions</a> beachten und den gesunden Menschenverstand einsetzen :) </dd>
=======
    <br />Bitte vorsichtig mit *copy&paste* zu Werke gehen und immer prüfen, ob die gewünschte Funktionalität nicht ohne Kopieren des Codes möglich ist.
    <br />Bitte die Java [Code-Conventions](http://www.oracle.com/technetwork/java/codeconvtoc-136057.html) beachten und den gesunden Menschenverstand einsetzen :) </dd>
>>>>>>> origin/master
</dl>

<dl>
    <dt>Wie kann ich meine Prüfung oder SIFCore "von Hand" testen?</dt>
<<<<<<< HEAD
    <dd>Zum manuellen Testen von SIFCore können die Policies der Integrationstests verwendet werden (<i>src/test/resources/policies/</i>) und diese können dem gestarteten SIFCore mit einem einfachen HTTP-Client übergeben werden.
    Ich empfehle <a href="https://chrome.google.com/webstore/detail/postman/fhbjgbiflinjbdggehcddcbncdddomop">Postman</a> (ein standalone Google Chrome Plugin) das sehr einfach zu bedienen ist.
    Im Ordner <i>doc/postman/<i> befindet sich ein Screenshot der einem bei der Konfiguration hilft.</dd>
=======
    <dd>Zum manuellen Testen von SIFCore können die Policies der Integrationstests verwendet werden (src/test/resources/policies/) und diese können dem gestarteten SIFCore mit einem einfachen HTTP-Client übergeben werden.
    Ich empfehle [Postman](https://chrome.google.com/webstore/detail/postman/fhbjgbiflinjbdggehcddcbncdddomop) (ein standalone Google Chrome Plugin) das sehr einfach zu bedienen ist.
    Im Ordner *doc/postman/* befindet sich ein Screenshot der einem bei der Konfiguration hilft.</dd>
>>>>>>> origin/master
</dl>