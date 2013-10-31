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
package org.gitools.core.persistence.formats.analysis;

import org.gitools.core.analysis.htest.enrichment.EnrichmentAnalysis;
import org.gitools.core.persistence.formats.FileFormat;

public class EnrichmentAnalysisFormat extends AbstractXmlFormat<EnrichmentAnalysis> {

    public static final String EXTENSION = "enrichment";
    public static final FileFormat FILE_FORMAT = new FileFormat("Enrichment analysis", EXTENSION, true, false, true);

    public EnrichmentAnalysisFormat() {
        super(EXTENSION, EnrichmentAnalysis.class);
    }

}
