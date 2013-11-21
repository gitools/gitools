/*
 * #%L
 * gitools-core
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
package org.gitools.core.model;

import com.jgoodies.binding.beans.Model;
import org.gitools.api.resource.IResource;
import org.gitools.api.resource.IResourceLocator;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"title", "description", "properties"})
public class Resource extends Model implements IResource {
    public static final String PROPERTY_TITLE = "title";
    public static final String PROPERTY_DESCRIPTION = "description";
    public static final String PROPERTY_PROPERTIES = "properties";

    /**
     * short description *
     */
    protected String title;

    /**
     * long description *
     */
    private String description;

    /**
     * Extra attributes *
     */
    @XmlElementWrapper(name = "properties")
    @XmlElement(name = "property")
    private List<Property> properties = new ArrayList<>(0);

	/* constructors */

    @XmlTransient
    private IResourceLocator locator;


    protected Resource() {
    }

    public Resource(Resource artifact) {
        this.title = artifact.getTitle();
        this.description = artifact.getDescription();
        this.properties = (List<Property>) ((ArrayList<Property>) artifact.getProperties()).clone();
    }

	/* getters and setters */

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        String oldValue = this.title;
        this.title = title;
        firePropertyChange(PROPERTY_TITLE, oldValue, title);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        String oldValue = this.description;
        this.description = description;
        firePropertyChange(PROPERTY_DESCRIPTION, oldValue, description);
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        List<Property> oldValue = this.properties;
        this.properties = properties;
        firePropertyChange(PROPERTY_PROPERTIES, oldValue, properties);
    }

    public IResourceLocator getLocator() {
        return locator;
    }

    public void setLocator(IResourceLocator locator) {
        this.locator = locator;
    }
}
