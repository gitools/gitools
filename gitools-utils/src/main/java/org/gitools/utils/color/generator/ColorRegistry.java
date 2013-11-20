/*
 * #%L
 * gitools-utils
 * %%
 * Copyright (C) 2013 Universitat Pompeu Fabra - Biomedical Genomics group
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
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
        } else {
            return null;
        }
    }

    public void registerId(String id, Color col) {
        registry.put(id, col);
    }


    public Color getColor(String id, Color alternative) {
        if (registry.containsKey(id)) {
            return registry.get(id);
        } else {
            return alternative;
        }
    }

}
