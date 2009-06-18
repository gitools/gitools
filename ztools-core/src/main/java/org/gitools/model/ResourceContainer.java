package org.gitools.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ResourceContainer extends Artifact {

	private static final long serialVersionUID = 9028366098418461333L;

	protected boolean limitedExploration;

	protected List<ResourceReference> references = new ArrayList<ResourceReference>(0);

	public ResourceContainer() {
	}

	public List<ResourceReference> getReferences() {
		return references;
	}

	public void setReferences(List<ResourceReference> references) {
		this.references = references;
	}

	public boolean isLimitedExploration() {
		return limitedExploration;
	}

	public void setLimitedExploration(boolean limitedExploration) {
		this.limitedExploration = limitedExploration;
	}

}
