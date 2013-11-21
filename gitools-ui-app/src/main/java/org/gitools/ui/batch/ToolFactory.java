/*
 * #%L
 * gitools-ui-app
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
package org.gitools.ui.batch;

import org.gitools.ui.batch.tools.*;

import java.util.HashMap;
import java.util.Map;

public class ToolFactory {
    final static Map<String, Class<? extends ITool>> TOOLS = new HashMap<>();

    static {
        addTool(new LoadTool());
        addTool(new VersionTool());
        addTool(new AddHeaderTextLabelsTool());
        addTool(new AddHeaderColoredLabelsTool());
    }

    static void addTool(ITool tool) {
        TOOLS.put(tool.getName().toLowerCase(), tool.getClass());
    }

    public static ITool get(String toolName) {
        try {
            return TOOLS.get(toolName).newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}