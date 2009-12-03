package org.gitools.persistence;

import java.util.HashMap;
import java.util.Map;

import org.gitools.model.matrix.AnnotationMatrix;
import org.gitools.model.matrix.DoubleMatrix;
import org.gitools.model.matrix.ObjectMatrix;
import org.gitools.model.matrix.StringMatrix;

public class FileSuffixes {

	public static final String WORKSPACE = "workspace.xml";

	public static final String PROJECT = "project.xml";
	public static final String CONTENT = "contents.xml";
	
	public static final String ENRICHMENT_ANALYSIS = "analysis";
	
	public static final String MATRIX_FIGURE = "figure";
	public static final String TABLE_FIGURE = "table";

	public static final String OBJECT_MATRIX = "objectMatrix";
	public static final String ANNOTATION_MATRIX = "annotationMatrix";
	public static final String DOUBLE_MATRIX = "doubleMatrix";
	public static final String STRING_MATRIX = "stringMatrix";

	public static final String MODULE_MAP = "moduleMap";

	private static final Map<Class<?>, String> suffixesMap = new HashMap<Class<?>, String>();
	
	static {
		suffixesMap.put(ObjectMatrix.class, FileSuffixes.OBJECT_MATRIX);
		suffixesMap.put(DoubleMatrix.class, FileSuffixes.DOUBLE_MATRIX);
		suffixesMap.put(StringMatrix.class, FileSuffixes.STRING_MATRIX);
		suffixesMap.put(AnnotationMatrix.class, FileSuffixes.ANNOTATION_MATRIX);	
	}

	public static String getEntityExtension(Class<?> entityClass) {
		return (String) suffixesMap.get(entityClass);
	}
}
