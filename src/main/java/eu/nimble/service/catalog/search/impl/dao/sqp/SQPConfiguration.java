//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2018.01.11 um 01:06:26 PM CET 
//


package eu.nimble.service.catalog.search.impl.dao.sqp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse fuer SQPConfiguration complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="SQPConfiguration">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SQPName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="UserContext" type="{https://www.w3schools.com}UserContext" minOccurs="0"/>
 *         &lt;element name="SQPMapping" type="{https://www.w3schools.com}SQPMapping"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SQPConfiguration", propOrder = {
    "sqpName",
    "userContext",
    "sqpMapping"
})
public class SQPConfiguration {

    @XmlElement(name = "SQPName", required = true)
    protected String sqpName;
    @XmlElement(name = "UserContext")
    protected UserContext userContext;
    @XmlElement(name = "SQPMapping", required = true)
    protected SQPMapping sqpMapping;

    /**
     * Ruft den Wert der sqpName-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSQPName() {
        return sqpName;
    }

    /**
     * Legt den Wert der sqpName-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSQPName(String value) {
        this.sqpName = value;
    }

    /**
     * Ruft den Wert der userContext-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link UserContext }
     *     
     */
    public UserContext getUserContext() {
        return userContext;
    }

    /**
     * Legt den Wert der userContext-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link UserContext }
     *     
     */
    public void setUserContext(UserContext value) {
        this.userContext = value;
    }

    /**
     * Ruft den Wert der sqpMapping-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SQPMapping }
     *     
     */
    public SQPMapping getSQPMapping() {
        return sqpMapping;
    }

    /**
     * Legt den Wert der sqpMapping-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SQPMapping }
     *     
     */
    public void setSQPMapping(SQPMapping value) {
        this.sqpMapping = value;
    }

}
