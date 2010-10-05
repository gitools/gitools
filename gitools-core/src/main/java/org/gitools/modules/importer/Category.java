package org.gitools.modules.importer;

public abstract class Category extends RefImpl {

	private String section;

	public Category(String section, String id, String name) {
		super(id, name);
		this.section = section;
	}

	public String getSection() {
		return section;
	}

	@Override
	public String getRef() {
		return section + ":" + super.getRef();
	}
}
