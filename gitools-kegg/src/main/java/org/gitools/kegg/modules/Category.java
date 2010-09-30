package org.gitools.kegg.modules;

public abstract class Category extends RefImpl {

	private String db;

	public Category(String db, String id, String name) {
		super(id, name);
		this.db = db;
	}

	public String getDb() {
		return db;
	}

	@Override
	public String getRef() {
		return db + ":" + super.getRef();
	}
}
