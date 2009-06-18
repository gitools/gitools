package org.gitools.model.REVIEW;

import java.io.Serializable;
import java.util.HashMap;

import org.gitools.model.Artifact;

@Deprecated
public class Group extends Artifact implements Serializable {

	private String source;
	private String organism;
	private String version;

	/** Group items */
	private HashMap<Long, Item> items = new HashMap<Long, Item>();

	public Group() {
		super();
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getOrganism() {
		return organism;
	}

	public void setOrganism(String organism) {
		this.organism = organism;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public HashMap<Long, Item> getItems() {
		return items;
	}

	public void setItems(HashMap<Long, Item> items) {
		this.items = items;
	}
}
