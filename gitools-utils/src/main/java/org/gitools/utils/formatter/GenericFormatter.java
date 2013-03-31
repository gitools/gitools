/*
 *  Copyright 2010 Universitat Pompeu Fabra.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.gitools.utils.formatter;

import java.io.Serializable;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;

public class GenericFormatter implements Serializable {

	private static final long oneMicrosecond = 1000;
	private static final long oneMilisecond = 1000 * oneMicrosecond;
	private static final long oneSecond = 1000 * oneMilisecond;
	private static final long oneMinute = 60 * oneSecond;
	private static final long oneHour = 60 * oneMinute;
	private static final long oneDay = 24 * oneHour;	
	
	private static Map<Class<?>, String> defaultGenericFormatMap = 
		new HashMap<Class<?>, String>();

    private Map<Class<?>, String> customFormatMap =
            new HashMap<Class<?>, String>();
	
	static {
		defaultGenericFormatMap.put(Float.class, "%.3g");
		defaultGenericFormatMap.put(Double.class, "%.3g");
	}
	
	private String ltString;
	private Map<Class<?>, String> genericFormatMap;
	
	private StringBuilder sb;
	private Formatter fmt;
	
	public GenericFormatter(String ltString) {
		this.ltString = ltString;
		genericFormatMap = defaultGenericFormatMap;
		sb = new StringBuilder(12);
		fmt = new Formatter(sb);
	}
	
	public GenericFormatter() {
		this("&lt;");
	}

    public void addCustomFormatter(Class<?> c , String s) {
        customFormatMap.put(c,s);
    }
	
	public String pvalue(double value) {
		if (value < 1e-16)
			return ltString + "1.0e-16";
		
		sb.setLength(0);
		fmt.format("%.3g", value);
		return sb.toString();
	}

	public String percentage(double value) {
		return format("%.2g%%", value * 100.0);
	}
	
	public String elapsedTime(Long elapsedTime) {
		return elapsedTime + " ns";
	}
	
	public String format(String format, Object... args) {
		sb.setLength(0);
		fmt.format(format, args);
		return sb.toString();
	}
	
	public String format(Object value) {
		if (value == null)
			return "null";
		
        String format = customFormatMap.get(value.getClass());
        if (format == null)
		    format = genericFormatMap.get(value.getClass());
		if (format == null)
			format = "%s";
		
		sb.setLength(0);
		fmt.format(format, value);
		return sb.toString();
	}
}
