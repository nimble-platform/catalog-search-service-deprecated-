//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2018.01.10 um 03:35:01 PM CET 
//


package eu.nimble.service.catalog.search.impl.dao.sqp;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the eu.nimble.service.catalog.search.impl.dao.sqp package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: eu.nimble.service.catalog.search.impl.dao.sqp
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SQPConfigurations }
     * 
     */
    public SQPConfigurations createSQPConfigurations() {
        return new SQPConfigurations();
    }

    /**
     * Create an instance of {@link SQPConfiguration }
     * 
     */
    public SQPConfiguration createSQPConfiguration() {
        return new SQPConfiguration();
    }

    /**
     * Create an instance of {@link Target }
     * 
     */
    public Target createTarget() {
        return new Target();
    }

    /**
     * Create an instance of {@link SQPMapping }
     * 
     */
    public SQPMapping createSQPMapping() {
        return new SQPMapping();
    }

    /**
     * Create an instance of {@link UserContext }
     * 
     */
    public UserContext createUserContext() {
        return new UserContext();
    }

    /**
     * Create an instance of {@link Source }
     * 
     */
    public Source createSource() {
        return new Source();
    }

}
