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
package org.gitools.plugins.mutex;


import org.gitools.heatmap.Bookmark;
import org.gitools.heatmap.plugin.AbstractPlugin;
import org.gitools.heatmap.plugin.IActionPlugin;
import org.gitools.heatmap.plugin.IBoxPlugin;
import org.gitools.heatmap.plugin.PluginAccess;
import org.gitools.plugins.mutex.analysis.MutualExclusiveResult;

import java.util.Map;

public class MutualExclusivePlugin extends AbstractPlugin implements IBoxPlugin, IActionPlugin {

    public static String NAME = "mutual-exclusive-plugin";

    private Map<String, MutualExclusiveResult> results;
    private Map<String, Bookmark> bookmarks;

    public MutualExclusivePlugin() {
        super(NAME);
    }

    @Override
    public PluginAccess getPluginAccess() {
        return IBoxPlugin.ACCESSES
                .merge(IActionPlugin.ACCESSES);
    }


    public void addResult(MutualExclusiveResult result, Bookmark bookmark) {
        results.put(bookmark.getName(), result);
        bookmarks.put(bookmark.getName(), bookmark);
    }

    public Bookmark getBookmark(String name) {
        return bookmarks.get(name);
    }

    public MutualExclusiveResult getResult(String name) {
        return results.get(name);
    }

    public void removeResult(String name) {
        bookmarks.remove(name);
        results.remove(name);
    }



}
