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
import org.gitools.plugins.mutex.ui.MutualExclusiveBox;
import org.gitools.ui.core.components.boxes.Box;
import org.gitools.ui.core.plugins.IActionPlugin;
import org.gitools.ui.core.plugins.IBoxPlugin;

import javax.enterprise.context.ApplicationScoped;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.*;

@ApplicationScoped
@XmlRootElement(name = "mutual-exclusive")
@XmlAccessorType(XmlAccessType.FIELD)
public class MutualExclusivePlugin extends AbstractPlugin implements IBoxPlugin, IActionPlugin {

    @XmlTransient
    public static final String PROPERTY_NAME = "mutex";

    @XmlTransient
    public static String NAME = "mutual-exclusive";

    private List<MutualExclusiveBookmark> bookmarks;

    @XmlTransient
    private Map<String, Integer> keysMap;

    public MutualExclusivePlugin() {
        super(NAME);
        bookmarks = new ArrayList<>();
        keysMap = new HashMap<>();
        updateKeys();
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


    public void add(MutualExclusiveBookmark bookmark) {
        bookmarks.add(bookmark);
        updateKeys();
    }

    private void updateKeys() {
        keysMap.clear();
        for (Bookmark b : bookmarks)
            keysMap.put(b.getName(), bookmarks.indexOf(b));
        firePropertyChange(PROPERTY_NAME, null, bookmarks);

    }

    public String[] getKeys() {
        String[] keys = keysMap.keySet().toArray(new String[keysMap.size()]);
        Arrays.sort(keys);
        return keys;
    }

    public MutualExclusiveBookmark getBookmark(String name) {
        return bookmarks.get(keysMap.get(name));
    }

    public void remove(String name) {
        Bookmark b = getBookmark(name);
        bookmarks.remove(b);
        updateKeys();
    }

    public boolean isNotEmpty() {
        return bookmarks.size() > 0;
    }

    @Override
    public Box[] getBoxes(Heatmap heatmap) {
        return new Box[]{new MutualExclusiveBox("Mutual exclusion results", heatmap, this)};
    }

    public void forceUpdate() {
        updateKeys();
    }
}
