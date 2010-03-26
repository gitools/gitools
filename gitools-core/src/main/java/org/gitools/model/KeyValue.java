package org.gitools.model;

import java.io.Serializable;

/** Key-Value pair **/
public class KeyValue implements Serializable {

	private static final long serialVersionUID = -391476933832883165L;

	private String key;

	private String value;

	public KeyValue() {
	}

	public KeyValue(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setKeyAndValue(String name, String value) {
		setKey(name);
		setValue(value);
	}
}
