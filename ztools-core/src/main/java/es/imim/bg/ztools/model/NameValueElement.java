package es.imim.bg.ztools.model;

public class NameValueElement {
	
	/** Annotation for items, adds a dynamic attribute to the desired item  **/
	
	/** Name **/
	private String name;
	/** Value **/
	private String value;

	public NameValueElement(){
		
	}
	
	public NameValueElement(String name, String value) {
		super();
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
