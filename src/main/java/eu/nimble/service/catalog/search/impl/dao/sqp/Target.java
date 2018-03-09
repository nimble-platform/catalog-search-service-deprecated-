//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
//Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2018.01.11 um 01:06:26 PM CET 
//


package eu.nimble.service.catalog.search.impl.dao.sqp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse fuer Target complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="Target">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TargetProperty" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="TargetPathFromSource" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DirectionSourceOriented" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Target", propOrder = {
    "targetProperty",
    "targetPathFromSource",
    "directionSourceOriented"
})
public class Target {

    @XmlElement(name = "TargetProperty", required = true)
    protected String targetProperty;
    @XmlElement(name = "TargetPathFromSource", required = true)
    protected String targetPathFromSource;
    @XmlElement(name = "DirectionSourceOriented")
    protected boolean directionSourceOriented;

    /**
     * Ruft den Wert der targetProperty-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTargetProperty() {
        return targetProperty;
    }

    /**
     * Legt den Wert der targetProperty-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTargetProperty(String value) {
        this.targetProperty = value;
    }

    /**
     * Ruft den Wert der targetPathFromSource-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTargetPathFromSource() {
        return targetPathFromSource;
    }

    /**
     * Legt den Wert der targetPathFromSource-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTargetPathFromSource(String value) {
        this.targetPathFromSource = value;
    }

    /**
     * Ruft den Wert der directionSourceOriented-Eigenschaft ab.
     * 
     */
    public boolean isDirectionSourceOriented() {
        return directionSourceOriented;
    }

    /**
     * Legt den Wert der directionSourceOriented-Eigenschaft fest.
     * 
     */
    public void setDirectionSourceOriented(boolean value) {
        this.directionSourceOriented = value;
    }

}
