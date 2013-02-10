package org.gitools.kegg.service.domain;


public class KeggPathway {

    private String id;
    private String description;

    public KeggPathway(String[] fields) {
        this.id = fields[0];
        this.description = fields[1];
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

}
