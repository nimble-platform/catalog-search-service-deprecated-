//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-520 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.03.11 at 09:46:29 PM MEZ 
//


package eu.nimble.service.catalog.search.mediator.datatypes;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DataSource complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DataSource">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="pathToConfigFile" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="javaClassForWrapper" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DataSource", propOrder = {
    "pathToConfigFile",
    "javaClassForWrapper"
})
public class DataSource {

    @XmlElement(required = true)
    protected String pathToConfigFile;
    @XmlElement(required = true)
    protected String javaClassForWrapper;

    /**
     * Gets the value of the pathToConfigFile property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPathToConfigFile() {
        return pathToConfigFile;
    }

    /**
     * Sets the value of the pathToConfigFile property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPathToConfigFile(String value) {
        this.pathToConfigFile = value;
    }

    /**
     * Gets the value of the javaClassForWrapper property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJavaClassForWrapper() {
        return javaClassForWrapper;
    }

    /**
     * Sets the value of the javaClassForWrapper property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJavaClassForWrapper(String value) {
        this.javaClassForWrapper = value;
    }

}
