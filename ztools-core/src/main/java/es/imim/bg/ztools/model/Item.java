package es.imim.bg.ztools.model;

import java.util.HashMap;
import java.util.HashSet;

public class Item {

	/** The id of the item **/
	private long Id;

	/** The name of the item. */
	private String name;

	/** The description of the item. **/
	private String descr;

	/** The group this item belongs to. */
	private Group group;

	/** Extra attributes **/
	private HashSet<NameValueElement> annotations = new HashSet<NameValueElement>(
			0);

	public Item() {

	}

	public Item(long id, String name) {
		this(id, name, null, null);
	}

	public Item(long id, String name, String descr) {
		this(id, name, descr, null);
	}

	public Item(long id, String name, String descr, Group group) {
		this.Id = id;
		this.name = name;
		this.descr = descr;
		this.group = group;

	}

	public void setId(long id) {
		Id = id;
	}

	public long getId() {
		return Id;
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

	public HashSet<NameValueElement> getAnnotations() {
		return annotations;
	}

	public void setAnnotations(HashSet<NameValueElement> annotations) {
		this.annotations = annotations;
	}

}
