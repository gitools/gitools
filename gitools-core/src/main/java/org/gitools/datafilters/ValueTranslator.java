package org.gitools.datafilters;

public interface ValueTranslator<T> {

	T stringToValue(String str);

	String valueToString(T value);
}
