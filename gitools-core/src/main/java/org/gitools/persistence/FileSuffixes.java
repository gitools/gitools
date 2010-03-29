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
	
	public static final String ENRICHMENT = "enrichment";
	public static final String ONCODRIVER = "oncodriver";
	public static final String CORRELATION = "correlation";
	
	public static final String HEATMAP = "heatmap";
	public static final String TABLE_FIGURE = "table";

	public static final String GENE_SET = "grp";

	public static final String GENE_MATRIX = "gmx";
	public static final String GENE_MATRIX_TRANSPOSED = "gmt";

	public static final String OBJECT_MATRIX = "tsv";
	public static final String DOUBLE_MATRIX = "cdm";
	public static final String DOUBLE_BINARY_MATRIX = "bdm";
	public static final String STRING_MATRIX = "tsv";
	public static final String ANNOTATION_MATRIX = "tsv";

	public static final String MODULES_INDEXED_MAP = "ixm";
	public static final String MODULES_2C_MAP = "tcm";

	private static final Map<Class<?>, String> suffixesMap = new HashMap<Class<?>, String>();

	// Deprecated
	static {
		suffixesMap.put(ObjectMatrix.class, FileSuffixes.OBJECT_MATRIX);
		suffixesMap.put(DoubleMatrix.class, FileSuffixes.DOUBLE_MATRIX);
		suffixesMap.put(StringMatrix.class, FileSuffixes.STRING_MATRIX);
		suffixesMap.put(AnnotationMatrix.class, FileSuffixes.ANNOTATION_MATRIX);
	}

	/*@Deprecated
	public static String getEntityExtension(Class<?> entityClass) {
		return (String) suffixesMap.get(entityClass);
	}*/
}
