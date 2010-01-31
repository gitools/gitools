package org.gitools.persistence;

import java.util.HashMap;
import java.util.Map;

import org.gitools.matrix.model.AnnotationMatrix;
import org.gitools.matrix.model.DoubleMatrix;
import org.gitools.matrix.model.ObjectMatrix;
import org.gitools.matrix.model.StringMatrix;

public class FileSuffixes {

	public static final String WORKSPACE = "workspace.xml";

	public static final String PROJECT = "project.xml";
	public static final String CONTENT = "contents.xml";
	
	public static final String ENRICHMENT_ANALYSIS = "enrichment";
	public static final String ONCOZ_ANALYSIS = "oncodriver";
	
	public static final String HEATMAP_FIGURE = "heatmap";
	public static final String TABLE_FIGURE = "table";

	public static final String OBJECT_MATRIX = "objectmatrix.gz";
	public static final String DOUBLE_MATRIX = "doublematrix.gz";
	public static final String BINARY_MATRIX = "binarymatrix.gz";
	public static final String STRING_MATRIX = "stringmatrix.gz";
	public static final String ANNOTATION_MATRIX = "annotationmatrix.gz";

	public static final String MODULES_INDEXED_MAP = "modules.gz";
	public static final String MODULES_2C_MAP = "modules2c.gz";

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
