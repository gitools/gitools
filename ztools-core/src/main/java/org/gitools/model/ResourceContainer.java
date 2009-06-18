package org.gitools.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.gitools.model.xml.ResourceReferenceXmlAdapter;

@XmlRootElement
public class ResourceContainer extends Artifact {

	private static final long serialVersionUID = 9028366098418461333L;

	protected boolean limitedExploration;

	@XmlElementWrapper(name = "references")
	@XmlJavaTypeAdapter(ResourceReferenceXmlAdapter.class)
	@XmlElement(name = "reference")
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
