package org.gitools.persistence;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.gitools.model.Container;
import org.gitools.model.ModuleMap;
import org.gitools.model.Project;
import org.gitools.model.analysis.EnrichmentAnalysis;
import org.gitools.model.figure.heatmap.Heatmap;
import org.gitools.model.figure.TableFigure;
import org.gitools.model.matrix.AnnotationMatrix;
import org.gitools.model.matrix.DoubleMatrix;
import org.gitools.model.matrix.ObjectMatrix;
import org.gitools.model.matrix.StringMatrix;

public class MimeTypeManager {

	private static MimeTypeManager resolver;
	
	public static final MimeTypeManager getDefault() {
		if (resolver == null)
			resolver = new MimeTypeManager();
		return resolver;
	}
	
	private Map<String, String> extMap = new HashMap<String, String>();
	private Map<Class<?>, String> classMap = new HashMap<Class<?>, String>();
	
	public MimeTypeManager() {
		
		// extension mapping
		
		extMap.put(FileSuffixes.PROJECT, MimeTypes.PROJECT);
		extMap.put(FileSuffixes.CONTENT, MimeTypes.CONTENT);
		
		extMap.put(FileSuffixes.ENRICHMENT_ANALYSIS, MimeTypes.ENRICHMENT_ANALYSIS);
		
		extMap.put(FileSuffixes.MATRIX_FIGURE, MimeTypes.MATRIX_FIGURE);
		extMap.put(FileSuffixes.TABLE_FIGURE, MimeTypes.TABLE_FIGURE);
		
		extMap.put(FileSuffixes.OBJECT_MATRIX, MimeTypes.OBJECT_MATRIX);
		extMap.put(FileSuffixes.DOUBLE_MATRIX, MimeTypes.DOUBLE_MATRIX);
		extMap.put(FileSuffixes.STRING_MATRIX, MimeTypes.STRING_MATRIX);
		extMap.put(FileSuffixes.ANNOTATION_MATRIX, MimeTypes.ANNOTATION_MATRIX);

		extMap.put(FileSuffixes.MODULE_MAP, MimeTypes.MODULE_MAP);
		
		// class mapping
		
		classMap.put(Project.class, MimeTypes.PROJECT);
		classMap.put(Container.class, MimeTypes.CONTENT);
		
		classMap.put(EnrichmentAnalysis.class, MimeTypes.ENRICHMENT_ANALYSIS);
		
		classMap.put(Heatmap.class, MimeTypes.MATRIX_FIGURE);
		classMap.put(TableFigure.class, MimeTypes.TABLE_FIGURE);
		
		classMap.put(ObjectMatrix.class, MimeTypes.OBJECT_MATRIX);
		classMap.put(DoubleMatrix.class, MimeTypes.DOUBLE_MATRIX);
		classMap.put(StringMatrix.class, MimeTypes.STRING_MATRIX);
		classMap.put(AnnotationMatrix.class, MimeTypes.ANNOTATION_MATRIX);

		classMap.put(ModuleMap.class, MimeTypes.MODULE_MAP);
	}
	
	public String fromFile(File file) {
		String name = file.getName();
		String mimeType = null;
		int pos = 0;
		while (pos >= 0 && mimeType == null) {
			mimeType = extMap.get(name);
			pos = name.indexOf('.', pos);
			name = name.substring(pos + 1, name.length());
		}
		
		return mimeType;
	}
	
	public String fromClass(Class<?> cls) {
		return classMap.get(cls);
	}
}
