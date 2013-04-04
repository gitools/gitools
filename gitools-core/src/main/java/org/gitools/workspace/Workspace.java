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
package org.gitools.workspace;

import org.jetbrains.annotations.NotNull;

import javax.xml.bind.annotation.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@XmlRootElement(name = "workspace")
@XmlAccessorType(XmlAccessType.FIELD)
public class Workspace
{

    @XmlTransient
    protected File xmlPath;

    @NotNull
    @XmlElementWrapper(name = "projects")
    @XmlElement(name = "project")
    protected List<WorkspaceProjectRef> projectRefs = new ArrayList<WorkspaceProjectRef>();

    public Workspace()
    {
    }

    public Workspace(File xmlPath)
    {
        this.xmlPath = xmlPath;
    }

    public File getXmlPath()
    {
        return xmlPath;
    }

    public void setXmlPath(File xmlPath)
    {
        this.xmlPath = xmlPath;
    }

    public File getBasePath()
    {
        return xmlPath.getParentFile();
    }

    public List<WorkspaceProjectRef> getProjectReferences()
    {
        return Collections.unmodifiableList(projectRefs);
    }

	/*public void setProjectReferences(List<WorkspaceProjectRef> projects) {
        this.projectRefs = projects;
	}*/

    public void addProjectRef(WorkspaceProjectRef projectRef)
    {
        projectRefs.add(projectRef);
    }
}
