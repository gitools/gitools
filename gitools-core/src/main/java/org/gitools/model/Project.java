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
package org.gitools.model;

import org.gitools.persistence.IResource;
import org.gitools.persistence.IResourceLocator;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "project")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"publications", "laboratories"})

public class Project extends Artifact implements IResource
{

    private static final long serialVersionUID = 7978328129043692524L;

    /**
     * List of publications associated with the project, if any *
     */

    @XmlElementWrapper(name = "publications")
    @XmlElement(name = "publication")
    private List<Publication> publications = new ArrayList<Publication>();

    /**
     * List of the laboratories involved in the project, if any *
     */

    @XmlElementWrapper(name = "laboratories")
    @XmlElement(name = "laboratory")
    private List<Laboratory> laboratories = new ArrayList<Laboratory>();

    private IResourceLocator locator;

    public Project()
    {
    }

    public IResourceLocator getLocator()
    {
        return locator;
    }

    public void setLocator(IResourceLocator locator)
    {
        this.locator = locator;
    }

    public List<Publication> getPublications()
    {
        return publications;
    }

    public void setPublications(List<Publication> publications)
    {
        this.publications = publications;
    }

    public List<Laboratory> getLaboratories()
    {
        return laboratories;
    }

    public void setLaboratories(List<Laboratory> laboratories)
    {
        this.laboratories = laboratories;
    }

}
