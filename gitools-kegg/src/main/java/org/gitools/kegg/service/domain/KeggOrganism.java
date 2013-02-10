package org.gitools.kegg.service.domain;


public class KeggOrganism {

    private String id;
    private String description;

    public KeggOrganism(String[] fields) {
        this.id = fields[1];
        this.description = fields[2];
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

}
