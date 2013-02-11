package org.gitools.kegg.service.domain;

/**
 * A database identifier conversion
 */
public class IdConversion {

    private String sourceId;
    private String targetId;

    public IdConversion(String[] fields) {

        this.sourceId = fields[0];
        this.targetId = fields[1];

    }

    /**                                                *
     * @return The identifier of the source database entity
     */
    public String getSourceId() {
        return sourceId;
    }

    /**
     * @return The identifier of the target database entity
     */
    public String getTargetId() {
        return targetId;
    }
}
