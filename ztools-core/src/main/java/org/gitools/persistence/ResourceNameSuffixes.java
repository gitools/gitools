package org.gitools.persistence;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileObject;
import org.gitools.model.ModuleMap;
import org.gitools.model.Project;
import org.gitools.model.Container;
import org.gitools.model.analysis.EnrichmentAnalysis;
import org.gitools.model.figure.MatrixFigure;
import org.gitools.model.figure.TableFigure;
import org.gitools.model.matrix.AnnotationMatrix;
import org.gitools.model.matrix.DoubleMatrix;
import org.gitools.model.matrix.ObjectMatrix;
import org.gitools.model.matrix.StringMatrix;

public class ResourceNameSuffixes {

	public static final String extensionSeparator = ".";

	public static final String PROJECT = "project.xml";
	public static final String CONTENTS = "contents.xml";
	public static final String ENRICHMENT_ANALYSIS = "analysis";
	
	public static final String MATRIX_FIGURE = "figure";
	public static final String TABLE_FIGURE = "table";

	public static final String OBJECT_MATRIX = "objectMatrix";
	public static final String ANNOTATION_MATRIX = "annotationMatrix";
	public static final String DOUBLE_MATRIX = "doubleMatrix";
	public static final String STRING_MATRIX = "stringMatrix";

	private static final Map<Class<?>, String> suffixesMap = new HashMap<Class<?>, String>();

	private static final Map<String, Class<?>> classesMap = new HashMap<String, Class<?>>();

	public static final String MODULE_MAP = "moduleMap";

	static {

		suffixesMap.put(ObjectMatrix.class, ResourceNameSuffixes.OBJECT_MATRIX);
		suffixesMap.put(DoubleMatrix.class, ResourceNameSuffixes.DOUBLE_MATRIX);
		suffixesMap.put(StringMatrix.class, ResourceNameSuffixes.STRING_MATRIX);
		suffixesMap.put(AnnotationMatrix.class, ResourceNameSuffixes.ANNOTATION_MATRIX);

		classesMap.put(ResourceNameSuffixes.PROJECT, Project.class);
		classesMap.put(ResourceNameSuffixes.CONTENTS, Container.class);
		classesMap.put(ResourceNameSuffixes.ENRICHMENT_ANALYSIS,  EnrichmentAnalysis.class);
		
		classesMap.put(ResourceNameSuffixes.MATRIX_FIGURE, MatrixFigure.class);
		classesMap.put(ResourceNameSuffixes.TABLE_FIGURE, TableFigure.class);
		
		classesMap.put(ResourceNameSuffixes.OBJECT_MATRIX, ObjectMatrix.class);
		classesMap.put(ResourceNameSuffixes.DOUBLE_MATRIX, DoubleMatrix.class);
		classesMap.put(ResourceNameSuffixes.STRING_MATRIX, StringMatrix.class);
		classesMap.put(ResourceNameSuffixes.ANNOTATION_MATRIX, AnnotationMatrix.class);

		classesMap.put(ResourceNameSuffixes.MODULE_MAP, ModuleMap.class);		
	}

	private ResourceNameSuffixes() {

	}

	public static String getEntityExtension(Class<?> entityClass) {
		return (String) suffixesMap.get(entityClass);
	}

	public static Class<?> getEntityClass(String extension) {
		return (Class<?>) classesMap.get(extension);
	}

	public static Class<?> getEntityClass(FileObject resource) {

		FileName fileName = resource.getName();
		String baseName = fileName.getBaseName();
		String ext = fileName.getExtension();
		
		if (baseName.equals(PROJECT))
			return classesMap.get(ResourceNameSuffixes.PROJECT);
		if (baseName.equals(CONTENTS))
			return classesMap.get(ResourceNameSuffixes.CONTENTS);

		return classesMap.get(ext);
	}

}
