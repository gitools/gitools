
package org.gitools.biomart.restful;

import org.gitools.biomart.cxf.*;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="mart" type="{http://www.biomart.org:80/MartServiceSoap}mart" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "mart"
})
@XmlRootElement(name = "MartRegistry")
public class MartRegistry {

	@XmlElement(name = "MartURLLocation")
    protected List<Mart> mart;

    /**
     * Gets the value of the mart property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the mart property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMart().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Mart }
     * 
     * 
     */
    public List<Mart> getMart() {
        if (mart == null) {
            mart = new ArrayList<Mart>();
        }
        return this.mart;
    }

}
