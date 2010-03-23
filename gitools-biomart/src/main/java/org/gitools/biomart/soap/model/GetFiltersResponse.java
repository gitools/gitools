
package org.gitools.biomart.soap.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *         &lt;element name="filterPage" type="{http://www.biomart.org:80/MartServiceSoap}filterPage" maxOccurs="unbounded" minOccurs="0"/>
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
    "filterPage"
})
@XmlRootElement(name = "getFiltersResponse")
public class GetFiltersResponse {

    protected List<FilterPage> filterPage;

    /**
     * Gets the value of the filterPage property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the filterPage property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFilterPage().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FilterPage }
     * 
     * 
     */
    public List<FilterPage> getFilterPage() {
        if (filterPage == null) {
            filterPage = new ArrayList<FilterPage>();
        }
        return this.filterPage;
    }

}
