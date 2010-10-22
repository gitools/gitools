package org.gitools.obo;

public class OBOEvent {

	private int id; //TODO rename to type
	private String stanzaName; //TODO remove from here
	private String tagName; //TODO remove from here
	//TODO file line and column
	protected String line;

	public OBOEvent(int id) {
		this.id = id;
	}

	public OBOEvent(int id, String stanzaName) {
		this.id = id;
		this.stanzaName = stanzaName;
	}

	public OBOEvent(int id, String stanzaName, String tagName) {
		this.id = id;
		this.stanzaName = stanzaName;
		this.tagName = tagName;
	}

	public int getId() {
		return id;
	}

	public String getStanzaName() {
		return stanzaName;
	}

	public String getTagName() {
		return tagName;
	}

	public String getLine() {
		return line;
	}
}
