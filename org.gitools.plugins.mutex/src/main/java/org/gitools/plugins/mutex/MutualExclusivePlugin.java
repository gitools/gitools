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


import org.gitools.api.plugins.PluginAccess;
import org.gitools.heatmap.Bookmark;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.plugins.AbstractPlugin;
import org.gitools.plugins.mutex.analysis.MutualExclusiveResult;
import org.gitools.plugins.mutex.ui.MutualExclusiveBox;
import org.gitools.ui.core.components.boxes.Box;
import org.gitools.ui.core.plugins.IActionPlugin;
import org.gitools.ui.core.plugins.IBoxPlugin;

import javax.enterprise.context.ApplicationScoped;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
@XmlRootElement(name = "mutual-exclusive")
@XmlAccessorType(XmlAccessType.FIELD)
public class MutualExclusivePlugin extends AbstractPlugin implements IBoxPlugin, IActionPlugin {

    @XmlTransient
    public static final String PROPERTY_NAME = "mutex";

    @XmlTransient
    public static String NAME = "mutual-exclusive";

    private Map<String, MutualExclusiveResult> results;
    private Map<String, Bookmark> bookmarks;
    private List<String> keys;

    public MutualExclusivePlugin() {
        super(NAME);
        results = new HashMap<>();
        bookmarks = new HashMap<>();
        keys = new ArrayList<>();
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public String getOldestCompatibleVersion() {
        return "0.0.99";
    }

    @Override
    public PluginAccess getPluginAccess() {
        return IBoxPlugin.ACCESSES
                .merge(IActionPlugin.ACCESSES);
    }


    public void addResult(MutualExclusiveResult result, Bookmark bookmark) {
        results.put(bookmark.getName(), result);
        bookmarks.put(bookmark.getName(), bookmark);
        keys.add(bookmark.getName());
        firePropertyChange(PROPERTY_NAME, null, result);
    }

    public List<String> getKeys() {
        return keys;
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
        keys.remove(name);
        firePropertyChange(PROPERTY_NAME, null, name);
    }

    public boolean isNotEmpty() {
        return results.size() > 0;
    }

    @Override
    public Box[] getBoxes(Heatmap heatmap) {
        return new Box[]{new MutualExclusiveBox("Mutual exclusion results", null, heatmap, this)};
    }
}
