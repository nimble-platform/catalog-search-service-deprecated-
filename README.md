# Catalog Search Service
Search functionalities are provided by this service. It communicates with the ontological data source, which stores products and user services persistently.

The service can use  a local ontology (file) or a Marmotta instance (url) as data source. In each of the cases, a different property must be set.

In the case of Marmotta, you need the Spring property: nimble.shared.property.marmottauri
In the case of local file, you need the Spring property: nimble.shared.property.ontologyfile


you can run this service as a Docker with the following command (just an example): docker run –p 8050:8080  -e NIMBLE_SHARED_PROPERTY_MARMOTTAURI:http://134.168.33.237:8080/marmotta search-service:0.0.1 
