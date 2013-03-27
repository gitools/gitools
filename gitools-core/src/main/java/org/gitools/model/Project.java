/*
 *  Copyright 2010 Universitat Pompeu Fabra.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.gitools.model;

import org.gitools.persistence.IResource;
import org.gitools.persistence.IResourceLocator;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "project")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType( propOrder={"publications", "laboratories" } )

public class Project extends Artifact implements IResource {

	private static final long serialVersionUID = 7978328129043692524L;

	/** List of publications associated with the project, if any **/
	
	@XmlElementWrapper(name = "publications")
    @XmlElement(name = "publication")
	private List<Publication> publications = new ArrayList<Publication>();

	/** List of the laboratories involved in the project, if any **/

	@XmlElementWrapper(name = "laboratories")
    @XmlElement(name = "laboratory")
	private List<Laboratory> laboratories = new ArrayList<Laboratory>();

    private IResourceLocator locator;

	public Project() {
	}

    public IResourceLocator getLocator() {
        return locator;
    }

    public void setLocator(IResourceLocator locator) {
        this.locator = locator;
    }

    public List<Publication> getPublications() {
		return publications;
	}

	public void setPublications(List<Publication> publications) {
		this.publications = publications;
	}

	public List<Laboratory> getLaboratories() {
		return laboratories;
	}

	public void setLaboratories(List<Laboratory> laboratories) {
		this.laboratories = laboratories;
	}

}
