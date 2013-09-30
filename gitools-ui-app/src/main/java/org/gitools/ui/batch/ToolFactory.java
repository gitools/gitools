package org.gitools.ui.batch;

import org.gitools.ui.batch.tools.*;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class ToolFactory {
    final static Map<String, Class<? extends ITool>> TOOLS = new HashMap<String, Class<? extends ITool>>();
    static {
        addTool(new LoadTool());
        addTool(new VersionTool());
        addTool(new AddHeaderTextLabelsTool());
        addTool(new AddHeaderColoredLabelsTool());
    }

    static void addTool(@NotNull ITool tool) {
        TOOLS.put(tool.getName().toLowerCase(), tool.getClass());
    }

    public static ITool get(String toolName) {
        try {
            return TOOLS.get(toolName).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}