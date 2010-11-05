package org.gitools.workspace;

import java.io.File;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlAccessorType(XmlAccessType.FIELD)
public class WorkspaceProjectRef {

	@XmlTransient
	private Workspace workspace;
	
	@XmlElement
	private String url;

	public WorkspaceProjectRef() {
	}
	
	public WorkspaceProjectRef(Workspace workspace) {
		this.workspace = workspace;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public File getPath() {
		if (url.startsWith(File.pathSeparator))
			return new File(url);
		else
			return new File(workspace.getBasePath(), url);
	}

}
