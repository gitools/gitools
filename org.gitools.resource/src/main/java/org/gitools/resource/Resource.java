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
package org.gitools.resource;

import com.jgoodies.binding.beans.Model;
import org.gitools.api.resource.IResource;
import org.gitools.api.resource.IResourceLocator;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"title", "description", "properties", "gitoolsVersion"})
public class Resource extends Model implements IResource {
    public static final String PROPERTY_TITLE = "title";
    public static final String PROPERTY_DESCRIPTION = "description";
    public static final String PROPERTY_PROPERTIES = "properties";

    /**
     * short description *
     */
    protected String title;

    @XmlTransient
    protected boolean hasChanged = true;

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

    /**
     * Version of Gitools that saved resource
     * Field is set upon saving
     */
    private SemanticVersion gitoolsVersion;

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

    /**
     * Adds a property and overwrites if already exists
     *
     * @param property
     */
    public void addProperty(Property property) {
        if (property == null) {
            return;
        }

        for (Property p : properties) {
            if (property.getName().equals(p.getName())) {
                properties.remove(p);
            }
        }

        properties.add(property);

    }

    @Override
    public boolean isChanged() {
        return hasChanged;
    }

    public IResourceLocator getLocator() {
        return locator;
    }

    public void setLocator(IResourceLocator locator) {
        this.locator = locator;
    }

    public SemanticVersion getGitoolsVersion() {
        if (gitoolsVersion == null) {
            return new SemanticVersion(SemanticVersion.OLD_VERSION);
        }
        return gitoolsVersion;
    }

    public void setGitoolsVersion(SemanticVersion gitoolsVersion) {
        this.gitoolsVersion = gitoolsVersion;
    }
}
