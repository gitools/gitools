
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
 *         &lt;element name="resultsRow" type="{http://www.biomart.org:80/MartServiceSoap}resultsRow" maxOccurs="unbounded" minOccurs="0"/>
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
    "resultsRow"
})
@XmlRootElement(name = "queryResponse")
public class QueryResponse {

    protected List<ResultsRow> resultsRow;

    /**
     * Gets the value of the resultsRow property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the resultsRow property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getResultsRow().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ResultsRow }
     * 
     * 
     */
    public List<ResultsRow> getResultsRow() {
        if (resultsRow == null) {
            resultsRow = new ArrayList<ResultsRow>();
        }
        return this.resultsRow;
    }

}
