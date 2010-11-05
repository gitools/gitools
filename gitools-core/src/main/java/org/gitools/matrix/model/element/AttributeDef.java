package org.gitools.matrix.model.element;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface AttributeDef {

	String id();
	String name();
	String description();
}
