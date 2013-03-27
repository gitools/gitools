/*
 *  Copyright 2010 Universitat Pompeu Fabra.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.gitools.persistence;

import org.gitools.persistence.formats.text.*;
import org.gitools.persistence.formats.xml.*;

public class PersistenceInitialization {

    public static void registerFormats() {
        registerFormats(
                PersistenceManager.get());
    }

    public static void registerFormats(PersistenceManager pm) {
        pm.registerFormat(new ProjectXmlFormat());
        pm.registerFormat(new TableFigureXmlFormat());
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
