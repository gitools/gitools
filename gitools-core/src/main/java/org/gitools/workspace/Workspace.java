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
