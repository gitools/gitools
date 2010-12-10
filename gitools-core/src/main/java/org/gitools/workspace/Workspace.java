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

package org.gitools.workspace;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "workspace")
@XmlAccessorType(XmlAccessType.FIELD)
public class Workspace {

	@XmlTransient
	protected File xmlPath;

	@XmlElementWrapper(name = "projects")
	@XmlElement(name = "project")
	protected List<WorkspaceProjectRef> projectRefs = new ArrayList<WorkspaceProjectRef>();

	public Workspace() {
	}
	
	public Workspace(File xmlPath) {
		this.xmlPath = xmlPath;
	}

	public File getXmlPath() {
		return xmlPath;
	}
	
	public void setXmlPath(File xmlPath) {
		this.xmlPath = xmlPath;
	}

	public File getBasePath() {
		return xmlPath.getParentFile();
	}
	
	public List<WorkspaceProjectRef> getProjectReferences() {
		return Collections.unmodifiableList(projectRefs);
	}

	/*public void setProjectReferences(List<WorkspaceProjectRef> projects) {
		this.projectRefs = projects;
	}*/
	
	public void addProjectRef(WorkspaceProjectRef projectRef) {
		projectRefs.add(projectRef);
	}
}
