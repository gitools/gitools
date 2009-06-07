package org.gitools.model.table.element;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Property {

	String id();
	String name();
	String description();
}
