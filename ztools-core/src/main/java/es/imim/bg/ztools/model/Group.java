package es.imim.bg.ztools.model;

import java.io.Serializable;

public class Group implements Serializable {

    private String name;
    private String source;
    private String organism;
    private String version;
    private String descr;

    public Group() {
    }

    public Group(String name) {
	this.name = name;
    }

    public Group(String name, String descr) {
	this.name = name;
	this.descr = descr;
    }

    protected String getName() {
	return name;
    }

    protected void setName(String name) {
	this.name = name;
    }

    protected String getSource() {
	return source;
    }

    protected void setSource(String source) {
	this.source = source;
    }

    protected String getOrganism() {
	return organism;
    }

    protected void setOrganism(String organism) {
	this.organism = organism;
    }

    protected String getVersion() {
	return version;
    }

    protected void setVersion(String version) {
	this.version = version;
    }

    protected String getDescr() {
	return descr;
    }

    protected void setDescr(String descr) {
	this.descr = descr;
    }

}
