/*
 * #%L
 * org.gitools.heatmap
 * %%
 * Copyright (C) 2013 - 2014 Universitat Pompeu Fabra - Biomedical Genomics group
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
package org.gitools.heatmap;


import org.gitools.utils.xml.adapter.StringArrayXmlAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Bookmark implements Serializable {

    private String name;

    @XmlElement(name = "layer")
    private String layerId;
    @XmlJavaTypeAdapter(StringArrayXmlAdapter.class)
    private List<String> rows;
    @XmlJavaTypeAdapter(StringArrayXmlAdapter.class)
    private List<String> columns;

    private String description;

    public Bookmark(String name, List<String> rows, List<String> columns, String layerId) {
        this.name = name;
        setRows(rows);
        setColumns(columns);
        this.layerId = layerId;
    }


    public Bookmark() {
    }

    public String getName() {
        return name;
    }

    public List<String> getRows() {
        return rows;
    }

    public List<String> getColumns() {
        return columns;
    }

    public void setRows(List<String> rows) {
        this.rows = new ArrayList<>(rows);
    }

    public void setColumns(List<String> columns) {
        this.columns = new ArrayList<>(columns);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLayerId() {
        return layerId;
    }

    public void setLayerId(String layerId) {
        this.layerId = layerId;
    }

    public String getDescription() {
        if (description == null) {
            return "";
        }
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return name;
    }
}
