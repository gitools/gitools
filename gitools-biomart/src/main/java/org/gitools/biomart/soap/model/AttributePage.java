
package org.gitools.biomart.soap.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for attributePage complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="attributePage">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="displayName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="maxSelect" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="formatters" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="attributeGroup" type="{http://www.biomart.org:80/MartServiceSoap}attributeGroup" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "attributePage", propOrder = {
    "name",
    "displayName",
    "maxSelect",
    "formatters",
    "attributeGroup"
})
public class AttributePage {

    @XmlElement(required = true)
    protected String name;
    @XmlElement(required = true)
    protected String displayName;
    protected int maxSelect;
    @XmlElement(required = true)
    protected String formatters;
    protected List<AttributeGroup> attributeGroup;

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the displayName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Sets the value of the displayName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDisplayName(String value) {
        this.displayName = value;
    }

    /**
     * Gets the value of the maxSelect property.
     * 
     */
    public int getMaxSelect() {
        return maxSelect;
    }

    /**
     * Sets the value of the maxSelect property.
     * 
     */
    public void setMaxSelect(int value) {
        this.maxSelect = value;
    }

    /**
     * Gets the value of the formatters property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFormatters() {
        return formatters;
    }

    /**
     * Sets the value of the formatters property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFormatters(String value) {
        this.formatters = value;
    }

    /**
     * Gets the value of the attributeGroup property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the attributeGroup property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAttributeGroup().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AttributeGroup }
     * 
     * 
     */
    public List<AttributeGroup> getAttributeGroup() {
        if (attributeGroup == null) {
            attributeGroup = new ArrayList<AttributeGroup>();
        }
        return this.attributeGroup;
    }

}
