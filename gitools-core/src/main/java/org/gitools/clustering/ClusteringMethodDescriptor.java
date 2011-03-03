package org.gitools.clustering;

public class ClusteringMethodDescriptor {

	private String title;
	private String description;
	
	private Class<? extends ClusteringMethod> methodClass;

	public ClusteringMethodDescriptor(
			String title, String description,
			Class<? extends ClusteringMethod> methodClass) {

		this.title = title;
		this.description = description;
		this.methodClass = methodClass;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public Class<? extends ClusteringMethod> getMethodClass() {
		return methodClass;
	}

	@Override
	public String toString() {
		return title;
	}
}
