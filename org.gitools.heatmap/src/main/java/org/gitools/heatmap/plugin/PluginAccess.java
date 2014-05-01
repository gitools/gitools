/*
 * #%L
 * org.gitools.heatmap
 * %%
 * Copyright (C) 2013 - 2014 Universitat Pompeu Fabra - Biomedical Genomics group
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
        availableAccesses.add(HEATMAP);
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
