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
package org.gitools.persistence;

import org.gitools.persistence.formats.analysis.*;
import org.gitools.persistence.formats.matrix.*;
import org.gitools.persistence.formats.modulemap.IndexedModuleMapFormat;
import org.gitools.persistence.formats.modulemap.TwoColumnModuleMapFormat;
import org.gitools.persistence.formats.text.GeneSetFormat;
import org.jetbrains.annotations.NotNull;

public class PersistenceInitialization
{

    public static void registerFormats()
    {
        registerFormats(
                PersistenceManager.get());
    }

    public static void registerFormats(@NotNull PersistenceManager pm)
    {
        pm.registerFormat(new EnrichmentAnalysisXmlFormat());
        pm.registerFormat(new OncodriveAnalysisXmlFormat());
        pm.registerFormat(new CorrelationAnalysisXmlFormat());
        pm.registerFormat(new CombinationAnalysisXmlFormat());
        pm.registerFormat(new OverlappingAnalysisXmlFormat());
        pm.registerFormat(new GroupComparisonAnalysisXmlFormat());
        pm.registerFormat(new HeatmapXmlFormat());
        pm.registerFormat(new GeneSetFormat());
        pm.registerFormat(new GeneMatrixFormat());
        pm.registerFormat(new GeneMatrixTransposedFormat());
        pm.registerFormat(new MultiValueMatrixFormat());
        pm.registerFormat(new GeneClusterTextMatrixFormat());
        pm.registerFormat(new DoubleMatrixFormat());
        pm.registerFormat(new DoubleBinaryMatrixFormat());
        pm.registerFormat(new AnnotationMatrixFormat());
        pm.registerFormat(new TwoColumnModuleMapFormat());
        pm.registerFormat(new IndexedModuleMapFormat());
    }


}
