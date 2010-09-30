package org.gitools.kegg.modules;

public abstract class RefImpl implements Ref {

	private String id;
	private String name;

	public RefImpl(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	@Override
	public String getRef() {
		return getId();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}
}
