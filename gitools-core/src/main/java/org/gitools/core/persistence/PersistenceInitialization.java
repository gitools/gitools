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
package org.gitools.core.persistence;

import org.gitools.core.persistence.formats.analysis.*;
import org.gitools.core.persistence.formats.annotations.TsvAnnotationMatrixFormat;
import org.gitools.core.persistence.formats.matrix.CmatrixMatrixFormat;
import org.gitools.core.persistence.formats.matrix.*;
import org.gitools.core.persistence.formats.modulemap.IxmModuleMapFormat;
import org.gitools.core.persistence.formats.modulemap.TcmModuleMapFormat;
import org.gitools.core.persistence.formats.geneset.GeneSetFormat;
import org.gitools.core.persistence.locators.filters.gz.GzResourceFilter;
import org.gitools.core.persistence.locators.filters.zip.ZipResourceFilter;
import org.jetbrains.annotations.NotNull;

public class PersistenceInitialization {

    public static void registerFormats() {
        registerFormats(PersistenceManager.get());
    }

    private static void registerFormats(@NotNull PersistenceManager pm) {
        pm.registerFormat(new EnrichmentAnalysisFormat());
        pm.registerFormat(new OncodriveAnalysisFormat());
        pm.registerFormat(new CorrelationAnalysisFormat());
        pm.registerFormat(new CombinationAnalysisFormat());
        pm.registerFormat(new OverlappingAnalysisFormat());
        pm.registerFormat(new GroupComparisonAnalysisFormat());
        pm.registerFormat(new HeatmapFormat());
        pm.registerFormat(new GeneSetFormat());
        pm.registerFormat(new GmxMatrixFormat());
        pm.registerFormat(new GmtMatrixFormat());
        pm.registerFormat(new TdmMatrixFormat());
        pm.registerFormat(new GctMatrixFormat());
        pm.registerFormat(new CdmMatrixFormat());
        pm.registerFormat(new BdmMatrixFormat());
        pm.registerFormat(new TsvAnnotationMatrixFormat());
        pm.registerFormat(new TcmModuleMapFormat());
        pm.registerFormat(new IxmModuleMapFormat());
        pm.registerFormat(new CmatrixMatrixFormat());

        pm.registerResourceFilter(new GzResourceFilter());
        pm.registerResourceFilter(new ZipResourceFilter());
    }


}
