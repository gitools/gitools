package org.gitools.ui;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import org.gitools.analysis.correlation.CorrelationAnalysis;
import org.gitools.analysis.htest.enrichment.EnrichmentAnalysis;
import org.gitools.heatmap.model.Heatmap;
import org.gitools.matrix.model.AnnotationMatrix;
import org.gitools.matrix.model.DoubleBinaryMatrix;
import org.gitools.matrix.model.DoubleMatrix;
import org.gitools.matrix.model.ObjectMatrix;
import org.gitools.model.Container;
import org.gitools.model.ModuleMap;
import org.gitools.model.Project;
import org.gitools.persistence.FileSuffixes;
import org.gitools.persistence.MimeTypes;
import org.gitools.persistence.PersistenceManager;
import org.gitools.ui.actions.Actions;

import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.help.Help;

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
import org.gitools.persistence.xml.ProjectXmlPersistence;
import org.gitools.persistence.xml.TableFigureXmlPersistence;
import org.gitools.table.model.TableFigure;

public class Main {

	public static void main(String[] args) {

		// Initialize look and feel
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.err.println("Error loading Look&Feel:");
			e.printStackTrace();
		}

		// Initialize loggers
		Logger.getLogger("org.lobobrowser").setLevel(Level.WARNING);

		// Initialize help system
		try {
			Help.getDefault().loadProperties(Main.class.getResourceAsStream("/help/help.properties"));
			Help.getDefault().loadUrlMap(Main.class.getResourceAsStream("/help/help.mappings"));
		}
		catch (Exception ex) {
			System.err.println("Error loading help system:");
			ex.printStackTrace();
		}

		// Initialize file formats
		registerFormats();
		
		// Initialize actions
		Actions.init();

		// Launch frame
		AppFrame.instance().start();
	}

	private static void registerFormats() {
		PersistenceManager pm = PersistenceManager.getDefault();

		pm.registerFormat(MimeTypes.PROJECT, FileSuffixes.PROJECT, Project.class, ProjectXmlPersistence.class);
		pm.registerFormat(MimeTypes.CONTENT, FileSuffixes.CONTENT, Container.class, ContainerXmlPersistence.class);
		pm.registerFormat(MimeTypes.TABLE_FIGURE, FileSuffixes.TABLE_FIGURE, TableFigure.class, TableFigureXmlPersistence.class);

		pm.registerFormat(MimeTypes.ENRICHMENT_ANALYSIS, FileSuffixes.ENRICHMENT, EnrichmentAnalysis.class, EnrichmentAnalysisXmlPersistence.class);
		pm.registerFormat(MimeTypes.CORRELATION_ANALYSIS, FileSuffixes.CORRELATION, CorrelationAnalysis.class, CorrelationAnalysisXmlPersistence.class);

		pm.registerFormat(MimeTypes.HEATMAP_FIGURE, FileSuffixes.HEATMAP, Heatmap.class, HeatmapXmlPersistence.class);

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
