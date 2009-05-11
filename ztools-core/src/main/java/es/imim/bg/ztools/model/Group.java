package es.imim.bg.ztools.model;

import java.io.Serializable;
import java.util.HashMap;

public class Group extends Artifact implements Serializable {

	private String source;
	private String organism;
	private String version;

	/** Group items */
	private HashMap<Long, Item> items = new HashMap<Long, Item>();

	public Group() {
		super();
	}

	public Group(String id, String artifactType) {
		super(id, artifactType);
	}

	public Group(String id, String artifactType, String title) {
		super(id, artifactType, title);
	}

	public Group(String id, String artifactType, String title,
			String description) {
		super(id, artifactType, title, description);
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
