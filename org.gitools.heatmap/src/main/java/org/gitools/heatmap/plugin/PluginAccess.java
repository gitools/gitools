package org.gitools.heatmap.plugin;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PluginAccess {

    public static String DETAILS_PANEL = "details-panel";
    public static String GITOOLS_MENU = "gitools-menu";
    public static String HEATMAP = "heatmap";
    private List<String> accessList;


    private static List availableAccesses = new ArrayList();

    static {
        availableAccesses.add(DETAILS_PANEL);
        availableAccesses.add(GITOOLS_MENU);
    }

    public PluginAccess(String... access) {

        accessList = new ArrayList();

        for (String a : access) {
            if (availableAccesses.contains(a)) {
                accessList.add(a);
            } else {
                try {
                    throw new Exception("Invalid access string: " + a);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public PluginAccess merge(PluginAccess otherAccesses) {
        Set<String> set = new HashSet<String>();
        set.addAll(accessList);
        set.addAll(otherAccesses.getAccessList());
        String[] merged = set.toArray(new String[0]);
        return new PluginAccess(merged);
    }

    public List<String> getAccessList() {
        return accessList;
    }
}
