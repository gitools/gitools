package es.imim.bg.ztools.model;

import java.io.Serializable;

public class Group extends Artifact implements Serializable{

    protected final static String artifactType = "group";
   
    private String source;
    private String organism;
    private String version;
    private String descr;

    public Group(){
	super();
    }
    
    public Group(String id, String name) {
	super(id,artifactType);
    }

    public Group(String id, String name, String descr) {
	super(name,artifactType,name, descr);
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
