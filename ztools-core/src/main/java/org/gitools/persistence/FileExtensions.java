package org.gitools.persistence;

import java.util.HashMap;
import java.util.Map;

import org.gitools.model.Project;
import org.gitools.model.ResourceContainer;
import org.gitools.model.figure.MatrixFigure;
import org.gitools.model.matrix.AnnotationMatrix;
import org.gitools.model.matrix.DoubleMatrix;
import org.gitools.model.matrix.ObjectMatrix;
import org.gitools.model.matrix.StringMatrix;

public class FileExtensions {

	public static final String extensionSeparator = ".";

	public static final String PROJECT = "project";
	public static final String RESOURCE_CONTAINER = "folder";
	public static final String MATRIX_FIGURE = "figure";

	public static final String OBJECT_MATRIX = "objectMatrix";
	public static final String ANNOTATION_MATRIX = "annotationMatrix";
	public static final String DOUBLE_MATRIX = "doubleMatrix";
	public static final String STRING_MATRIX = "stringMatrix";

	private static final Map <Class<?>, String> extensionsMap = new HashMap<Class<?>, String>();

	private static final Map<String, Class<?>> classesMap = new HashMap<String, Class<?>>();

	static {
		
		extensionsMap.put(ObjectMatrix.class,
				FileExtensions.OBJECT_MATRIX);
		extensionsMap.put(DoubleMatrix.class,
				FileExtensions.DOUBLE_MATRIX);
		extensionsMap.put(StringMatrix.class,
				FileExtensions.STRING_MATRIX);
		extensionsMap.put(AnnotationMatrix.class,
				FileExtensions.ANNOTATION_MATRIX);

		
		classesMap.put(FileExtensions.PROJECT, Project.class);
		classesMap.put(FileExtensions.RESOURCE_CONTAINER, ResourceContainer.class);
		classesMap.put(FileExtensions.MATRIX_FIGURE, MatrixFigure.class);
		
		
		classesMap.put(FileExtensions.OBJECT_MATRIX, ObjectMatrix.class);
		classesMap.put(FileExtensions.DOUBLE_MATRIX, DoubleMatrix.class);
		classesMap.put(FileExtensions.STRING_MATRIX, StringMatrix.class);
		classesMap.put(FileExtensions.ANNOTATION_MATRIX,AnnotationMatrix.class);

	}

	private FileExtensions() {
		
	}

	public static String getEntityExtension(Class<?> entityClass) {
		return (String) extensionsMap.get(entityClass);
	}

	public static Class<?> getEntityClass(String extension) {
		return (Class<?>) classesMap.get(extension);
	}

}
