//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2018.01.10 um 03:35:01 PM CET 
//


package eu.nimble.service.catalog.search.impl.dao.sqp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für UserContext complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="UserContext">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="BusinessProzessStep" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UserContext", propOrder = {
    "businessProzessStep"
})
public class UserContext {

    @XmlElement(name = "BusinessProzessStep")
    protected String businessProzessStep;

    /**
     * Ruft den Wert der businessProzessStep-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBusinessProzessStep() {
        return businessProzessStep;
    }

    /**
     * Legt den Wert der businessProzessStep-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBusinessProzessStep(String value) {
        this.businessProzessStep = value;
    }

}
