
package org.gitools.biomart.restful;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "virtualSchemaName",
    "header",
    "count",
    "uniqueRows",
    "dataset"
})
@XmlRootElement(name = "query")
public class Query {

    @XmlAttribute(required = true)
    protected String virtualSchemaName;
    protected int header;
    protected int count;
    protected int uniqueRows;
    @XmlAttribute(name = "Dataset", required = true)
    protected List<Dataset> dataset;

    /**
     * Gets the value of the virtualSchemaName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVirtualSchemaName() {
        return virtualSchemaName;
    }

    /**
     * Sets the value of the virtualSchemaName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVirtualSchemaName(String value) {
        this.virtualSchemaName = value;
    }

    /**
     * Gets the value of the header property.
     * 
     */
    public int getHeader() {
        return header;
    }

    /**
     * Sets the value of the header property.
     * 
     */
    public void setHeader(int value) {
        this.header = value;
    }

    /**
     * Gets the value of the count property.
     * 
     */
    public int getCount() {
        return count;
    }

    /**
     * Sets the value of the count property.
     * 
     */
    public void setCount(int value) {
        this.count = value;
    }

    /**
     * Gets the value of the uniqueRows property.
     * 
     */
    public int getUniqueRows() {
        return uniqueRows;
    }

    /**
     * Sets the value of the uniqueRows property.
     * 
     */
    public void setUniqueRows(int value) {
        this.uniqueRows = value;
    }

    /**
     * Gets the value of the dataset property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dataset property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDataset().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Dataset }
     * 
     * 
     */
    public List<Dataset> getDataset() {
        if (dataset == null) {
            dataset = new ArrayList<Dataset>();
        }
        return this.dataset;
    }

}
