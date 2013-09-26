package org.gitools.utils.color.generator;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public final class ColorRegistry {


    private static ColorRegistry instance;


    public static ColorRegistry get() {
        if (instance == null) {
            instance = new ColorRegistry();
        }
        return instance;
    }

    private static final Map<String, Color> registry = new HashMap<String, Color>();

    public Color getColor(String id) {
        if (registry.containsKey(id)) {
            return registry.get(id);
        }
        else {
            return null;
        }
    }

    public void registerId(String id, Color col) {
        registry.put(id, col);
    }


    public Color getColor(String id, Color alternative) {
        if (registry.containsKey(id)) {
            return registry.get(id);
        }
        else {
            return alternative;
        }
    }

}
