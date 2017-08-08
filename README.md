# Catalog Search Service
Search functionalities are provided by this service. It communicates with the ontological data source, which stores products and user services persistently.

The service can use  a local ontology (file) or a Marmotta instance (url) as data source. In each of the cases, a different property must be set.

In the case of Marmotta, you need the Spring property: nimble.shared.property.marmottauri
In the case of local file, you need the Spring property: nimble.shared.property.ontologyfile


you can run this service as a Docker with the following command (just an example): docker run –p 8050:8080  -e NIMBLE_SHARED_PROPERTY_MARMOTTAURI:http://134.168.33.237:8080/marmotta search-service:0.0.1 


-------------------------------------------------
1. Swagger problem  
(1) Das Maven-Plugin kann nicht integriert werden, was nicht weiter schlimm ist. Hier einfach den Fehler in eine Warnung umwandeln.
In Eclipse: "Preferences -> Maven -> Error/Warnings" und Fehler in Warnung umwandeln: "Plugin execution not converted by lifecycle configuration”
(2) Um den von Swagger autogenerierten Quellcode zu Eclipse hinzuzufügen bitte
	(a) "mvn clean package” in der Konsole ausführen, um den Quellcode zu generieren. Um "mvn package" richtig auszuführen, die pfad zu owl Datei in "MediatorSPARQLDerivationTest" angepasst werden muss.
	
	(b) Im “Project Explorer” in Eclipse den Ordner “target/generated-sources/swagger/src/gen/java/main” als Source Folder hinzufügen.
-------------------------------------------
2. Debug program
0) update the path within "start.bat"
1) Start "start.bat"
2) You will still need to attach the debugger in Eclipse by making a new Debug Configuration for a "Remote Java Application" on the relevant port.
	
