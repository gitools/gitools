package es.imim.bg;

import java.util.Map;

public interface MapConfigurable {

	public Map<String, Object> getConfig();
	public void setConfig(Map<String, Object> config);
}
