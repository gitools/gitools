/*
 * #%L
 * gitools-kegg
 * %%
 * Copyright (C) 2013 Universitat Pompeu Fabra - Biomedical Genomics group
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.gitools.datasources.kegg.service.domain;

/**
 * A database identifier conversion
 */
public class IdConversion {

    private final String sourceId;
    private final String targetId;

    public IdConversion(String[] fields) {

        this.sourceId = fields[0];
        this.targetId = fields[1];

    }

    /**
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
