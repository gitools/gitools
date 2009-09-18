package edu.upf.bg;

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
	
	static {
		//defaultGenericFormatMap.put(Byte.class, "%d");
		//defaultGenericFormatMap.put(Short.class, "%d");
		//defaultGenericFormatMap.put(Integer.class, "%d");
		//defaultGenericFormatMap.put(Long.class, "%d");
		defaultGenericFormatMap.put(Float.class, "%.3g");
		defaultGenericFormatMap.put(Double.class, "%.3g");
		//defaultGenericFormatMap.put(String.class, "%s");
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
	
	public String pvalue(double value) {
		if (value < 1e-16)
			return ltString + " 1.0e-16";
		
		sb.setLength(0);
		fmt.format("%.3g", value);
		return sb.toString();
	}
	
	public String elapsedTime(Long elapsedTime) {
		/*long us = elapsedTime / oneMicrosecond;
		long ms = elapsedTime / oneMilisecond;
		long s = elapsedTime / oneSecond;
		long h = elapsedTime / oneHour;
		long m = h /
		String s = null;
		if (elapsedTime < oneMicrosecond) {
			s = "ns";
		}
		else if (elapsedTime < oneMilisecond) {
			s = "us";
		}
		else if (elapsedTime < oneSecond) {
			s = "ms";
		}
		else if (elapsedTime < oneMinute) {
			s = "s";
		}
		else if (elapsedTime < oneHour) {
			s = "m";
		}
		else if (elapsedTime < oneDay) {
			s = "h";
		}
		else {
			s = "d";
		}
		
		return s;*/
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
		
		String format = genericFormatMap.get(value.getClass());
		if (format == null)
			format = "%s";
		
		sb.setLength(0);
		fmt.format(format, value);
		return sb.toString();
	}
}
