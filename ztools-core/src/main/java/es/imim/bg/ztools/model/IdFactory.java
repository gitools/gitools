package es.imim.bg.ztools.model;

import java.util.HashMap;

public class IdFactory {

    static HashMap<Integer, String[]> artifactIdMap;

    public static String[] getUniqueIdentifier(Object object) {

	int key = object.hashCode();

	if (!artifactIdMap.containsKey(key)) {
	    String id[] = { Integer.toString(object.hashCode()), object.getClass().toString() };
	    artifactIdMap.put(key, id);
	    return id;
	}
	return artifactIdMap.get(key);

    }
}
