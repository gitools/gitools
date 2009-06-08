package org.gitools.model.matrix.element;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Property {

	String id();
	String name();
	String description();
}
