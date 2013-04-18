/*
 * #%L
 * gitools-biomart
 * %%
 * Copyright (C) 2013 Universitat Pompeu Fabra - Biomedical Genomics group
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.gitools.biomart.restful.model;

import org.gitools.biomart.restful.model.xml.IntegerXmlAdapter;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * @noinspection ALL
 */
@XmlRootElement(name = "Query")
@XmlAccessorType(XmlAccessType.FIELD)

public class Query {

    @XmlAttribute(required = true)
    private String virtualSchemaName;

    @XmlAttribute
    private int header;

    @XmlAttribute
    private int count;

    @XmlAttribute
    private int uniqueRows;

    @XmlAttribute
    private String formatter;

    @XmlAttribute
    @XmlJavaTypeAdapter(IntegerXmlAdapter.class)
    private Integer limitStart;

    @XmlAttribute
    @XmlJavaTypeAdapter(IntegerXmlAdapter.class)
    private Integer limitSize;

    @XmlAttribute
    private String requestId;

    @XmlElement(name = "Dataset", required = true)
    private List<Dataset> datasets;

    /**
     * Gets the value of the virtualSchemaName property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getVirtualSchemaName() {
        return virtualSchemaName;
    }

    /**
     * Sets the value of the virtualSchemaName property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setVirtualSchemaName(String value) {
        this.virtualSchemaName = value;
    }

    /**
     * Gets the value of the header property.
     */
    public int getHeader() {
        return header;
    }

    /**
     * Sets the value of the header property.
     */
    public void setHeader(int value) {
        this.header = value;
    }

    /**
     * Gets the value of the count property.
     */
    public int getCount() {
        return count;
    }

    /**
     * Sets the value of the count property.
     */
    public void setCount(int value) {
        this.count = value;
    }

    /**
     * Gets the value of the uniqueRows property.
     */
    public int getUniqueRows() {
        return uniqueRows;
    }

    /**
     * Sets the value of the uniqueRows property.
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

    public Integer getLimitStart() {
        return limitStart;
    }

    /**
     * @noinspection UnusedDeclaration
     */
    public void setLimitStart(Integer limitStart) {
        this.limitStart = limitStart;
    }

    public Integer getLimitSize() {
        return limitSize;
    }

    public void setLimitSize(Integer limitSize) {
        this.limitSize = limitSize;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    /**
     * Gets the value of the dataset property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dataset property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDataset().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link Dataset }
     */
    public List<Dataset> getDatasets() {
        if (datasets == null) {
            datasets = new ArrayList<Dataset>();
        }
        return this.datasets;
    }

}
