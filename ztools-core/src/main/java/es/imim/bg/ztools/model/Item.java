package es.imim.bg.ztools.model;

import java.util.ArrayList;
import java.util.List;

public class Item {

	/** The name of the item. */
	private String name;

	/** The description of the item. **/
	private String descr;

	/** The group this item belongs to. */
	private Group group;

	/** Extra attributes **/
	private List<Attribute> attributes = new ArrayList<Attribute>(0);

	public Item() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public List<Attribute> getAnnotations() {
		return attributes;
	}

	public void setAnnotations(List<Attribute> annotations) {
		this.attributes = annotations;
	}

}
