package es.imim.bg.ztools.model;

public class Attribute {

	/** Annotation for items, adds a dynamic attribute to the desired item **/

	/** Name **/
	private String name;

	/** Value **/
	private String value;

	public Attribute() {
	}

	public Attribute(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
