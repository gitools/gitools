package es.imim.bg.ztools.model;

public class Item {

    /** The group this item belogs to. */

    protected Group group;

    /** The name of the item. */

    protected String name;

    /** The description of the item. **/

    protected String descr;

    public Item() {
    }

    public Item(String name) {
	this.name = name;
    }

    public Item(String name, String descr) {
	this.name = name;
	this.descr = descr;
    }

    public Item(Group group) {
	this.group = group;
    }

    public Item(Group group, String name) {
	this.group = group;
	this.name = name;
    }

    public Item(Group group, String name, String sym, String descr) {
	this.group = group;
	this.name = name;
	this.descr = descr;
    }

}
