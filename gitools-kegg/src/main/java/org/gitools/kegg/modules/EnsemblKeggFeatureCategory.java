package org.gitools.kegg.modules;

public class EnsemblKeggFeatureCategory extends Category implements FeatureCategory {

	private String section;

	public EnsemblKeggFeatureCategory(String db, String id, String name, String section) {
		super(db, id, name);

		this.section = section;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}
}
