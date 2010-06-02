/*
 *  Copyright 2010 cperez.
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

import org.gitools.analysis.correlation.CorrelationAnalysis;
import org.gitools.analysis.htest.enrichment.EnrichmentAnalysis;
import org.gitools.analysis.htest.oncozet.OncodriveAnalysis;
import org.gitools.heatmap.model.Heatmap;
import org.gitools.matrix.model.AnnotationMatrix;
import org.gitools.matrix.model.DoubleBinaryMatrix;
import org.gitools.matrix.model.DoubleMatrix;
import org.gitools.matrix.model.ObjectMatrix;
import org.gitools.model.Container;
import org.gitools.model.ModuleMap;
import org.gitools.model.Project;

import org.gitools.persistence.text.DoubleBinaryMatrixTextPersistence;
import org.gitools.persistence.text.GeneMatrixPersistence;
import org.gitools.persistence.text.GeneMatrixTransposedPersistence;
import org.gitools.persistence.text.GeneSetPersistence;
import org.gitools.persistence.text.ModuleMapText2CPersistence;
import org.gitools.persistence.text.AnnotationMatrixTextPersistence;
import org.gitools.persistence.text.DoubleMatrixTextPersistence;
import org.gitools.persistence.text.ModuleMapTextIndicesPersistence;
import org.gitools.persistence.text.ObjectMatrixTextPersistence;
import org.gitools.persistence.xml.ContainerXmlPersistence;
import org.gitools.persistence.xml.CorrelationAnalysisXmlPersistence;
import org.gitools.persistence.xml.EnrichmentAnalysisXmlPersistence;
import org.gitools.persistence.xml.HeatmapXmlPersistence;
import org.gitools.persistence.xml.OncodriveAnalysisXmlPersistence;
import org.gitools.persistence.xml.ProjectXmlPersistence;
import org.gitools.persistence.xml.TableFigureXmlPersistence;
import org.gitools.table.model.TableFigure;

public class PersistenceInitialization {

	public static void registerFormats() {
		registerFormats(
				PersistenceManager.getDefault());
	}

	public static void registerFormats(PersistenceManager pm) {
		
		pm.registerFormat(MimeTypes.PROJECT, FileSuffixes.PROJECT, Project.class, ProjectXmlPersistence.class);
		pm.registerFormat(MimeTypes.CONTENT, FileSuffixes.CONTENT, Container.class, ContainerXmlPersistence.class);
		pm.registerFormat(MimeTypes.TABLE, FileSuffixes.TABLE_FIGURE, TableFigure.class, TableFigureXmlPersistence.class);

		pm.registerFormat(MimeTypes.ENRICHMENT_ANALYSIS, FileSuffixes.ENRICHMENT, EnrichmentAnalysis.class, EnrichmentAnalysisXmlPersistence.class);
		pm.registerFormat(MimeTypes.ONCODRIVE_ANALYSIS, FileSuffixes.ONCODRIVE, OncodriveAnalysis.class, OncodriveAnalysisXmlPersistence.class);
		pm.registerFormat(MimeTypes.CORRELATIONS_ANALYSIS, FileSuffixes.CORRELATIONS, CorrelationAnalysis.class, CorrelationAnalysisXmlPersistence.class);

		pm.registerFormat(MimeTypes.HEATMAP, FileSuffixes.HEATMAP, Heatmap.class, HeatmapXmlPersistence.class);

		pm.registerFormat(MimeTypes.GENE_SET, FileSuffixes.GENE_SET, GeneSetPersistence.class);

		pm.registerFormat(MimeTypes.GENE_MATRIX, FileSuffixes.GENE_MATRIX, GeneMatrixPersistence.class);
		pm.registerFormat(MimeTypes.GENE_MATRIX_TRANSPOSED, FileSuffixes.GENE_MATRIX_TRANSPOSED, GeneMatrixTransposedPersistence.class);
		pm.registerFormat(MimeTypes.OBJECT_MATRIX, FileSuffixes.OBJECT_MATRIX, ObjectMatrix.class, ObjectMatrixTextPersistence.class);
		pm.registerFormat(MimeTypes.DOUBLE_MATRIX, FileSuffixes.DOUBLE_MATRIX, DoubleMatrix.class, DoubleMatrixTextPersistence.class);
		pm.registerFormat(MimeTypes.DOUBLE_BINARY_MATRIX, FileSuffixes.DOUBLE_BINARY_MATRIX, DoubleBinaryMatrix.class, DoubleBinaryMatrixTextPersistence.class);

		pm.registerFormat(MimeTypes.ANNOTATION_MATRIX, FileSuffixes.ANNOTATION_MATRIX, AnnotationMatrix.class, AnnotationMatrixTextPersistence.class);
		pm.registerFormat(MimeTypes.MODULES_2C_MAP, FileSuffixes.MODULES_2C_MAP, ModuleMapText2CPersistence.class);
		pm.registerFormat(MimeTypes.MODULES_INDEXED_MAP, FileSuffixes.MODULES_INDEXED_MAP, ModuleMap.class, ModuleMapTextIndicesPersistence.class);
	}
}
