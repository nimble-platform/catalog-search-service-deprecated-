//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2018.01.11 um 01:06:26 PM CET 
//


package eu.nimble.service.catalog.search.impl.dao.sqp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für Source complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="Source">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SourceConcept" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ContextPathFromSource" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Source", propOrder = {
    "sourceConcept",
    "contextPathFromSource"
})
public class Source {

    @XmlElement(name = "SourceConcept", required = true)
    protected String sourceConcept;
    @XmlElement(name = "ContextPathFromSource", required = true)
    protected String contextPathFromSource;

    /**
     * Ruft den Wert der sourceConcept-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSourceConcept() {
        return sourceConcept;
    }

    /**
     * Legt den Wert der sourceConcept-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSourceConcept(String value) {
        this.sourceConcept = value;
    }

    /**
     * Ruft den Wert der contextPathFromSource-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContextPathFromSource() {
        return contextPathFromSource;
    }

    /**
     * Legt den Wert der contextPathFromSource-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContextPathFromSource(String value) {
        this.contextPathFromSource = value;
    }

}
