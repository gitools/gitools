
package org.gitools.biomart.restful.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Query")
@XmlAccessorType(XmlAccessType.FIELD)

public class Query {

    @XmlAttribute(required = true)
    protected String virtualSchemaName;

	@XmlAttribute
    protected int header;

	@XmlAttribute
    protected int count;

	@XmlAttribute
    protected int uniqueRows;

	@XmlAttribute
	protected String formatter;

	@XmlAttribute
	protected String limitStart;

	@XmlAttribute
	protected String limitSize;

	@XmlAttribute
	protected String requestId;

    @XmlElement(name = "Dataset", required = true)
    protected List<Dataset> datasets;

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
	 * Get the formatter (TSV, ...)
	 */
	public String getFormatter() {
		return formatter;
	}

	/**
	 * Set the formatter (TSV, ...)
	 */
	public void setFormatter(String formatter) {
		this.formatter = formatter;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
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
    public List<Dataset> getDatasets() {
        if (datasets == null) {
            datasets = new ArrayList<Dataset>();
        }
        return this.datasets;
    }

}
