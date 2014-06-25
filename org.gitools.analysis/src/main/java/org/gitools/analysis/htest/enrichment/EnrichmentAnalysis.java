/*
 * #%L
 * gitools-core
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
package org.gitools.analysis.htest.enrichment;

import org.gitools.analysis.htest.HtestAnalysis;
import org.gitools.api.matrix.CacheKey;
import org.gitools.api.matrix.ICacheKey;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class EnrichmentAnalysis extends HtestAnalysis implements Serializable {

    private boolean discardNonMappedRows;

    public static ICacheKey<EnrichmentAnalysis> CACHE_KEY_ENHRICHMENT = new CacheKey<>();

    public EnrichmentAnalysis() {
    }

    public boolean isDiscardNonMappedRows() {
        return discardNonMappedRows;
    }

    public void setDiscardNonMappedRows(boolean discardNonMappedRows) {
        this.discardNonMappedRows = discardNonMappedRows;
    }

}
