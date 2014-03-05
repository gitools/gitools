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
package org.gitools.heatmap;


import com.google.common.collect.Iterables;
import com.google.common.primitives.Ints;
import com.jgoodies.binding.beans.Model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class Bookmarks extends Model {

    public static String PROPERTY_CONTENTS = "contents";

    @XmlElement(name = "bookmark")
    private List<Bookmark> bookmarks;

    @XmlTransient
    private Map<String, Integer> nameMap;

    @XmlTransient
    public static int ROWS = 1;

    @XmlTransient
    public static int COLUMNS = 2;

    @XmlTransient
    public static int LAYER = 3;

    public Bookmarks() {
        this.bookmarks = new ArrayList<>();
    }


    public void add(Bookmark b) {
        for (Bookmark existing : bookmarks) {
            if (existing.getName().equals(b.getName())) {
                bookmarks.remove(existing);
                break;
            }
        }
        bookmarks.add(b);
        Collections.sort(bookmarks, new Comparator<Bookmark>() {
            @Override
            public int compare(Bookmark o1, Bookmark o2) {
                return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
            }
        });
        firePropertyChange(PROPERTY_CONTENTS, null, this);
        updateMap();

    }

    private void updateMap() {
        if (nameMap == null) {
            nameMap = new HashMap<>();
        }
        nameMap.clear();
        Integer counter = 0;
        for (Bookmark b : bookmarks) {
            nameMap.put(b.getName(), counter++);
        }
    }

    public void removeBookmark(Bookmark b) {
        if (nameMap == null) {
            updateMap();
        }
        bookmarks.remove(b);
        nameMap.remove(b.getName());
        firePropertyChange(PROPERTY_CONTENTS, null, this);
    }


    public Bookmark get(String name) {
        if (nameMap == null) {
            updateMap();
        }
        if (!nameMap.containsKey(name)) {
            return null;
        }
        return bookmarks.get(nameMap.get(name));
    }

    public List<Bookmark> getAll() {
        return bookmarks;
    }

    public void setBookmarks(List<Bookmark> bookmarks) {
        this.bookmarks = bookmarks;
        updateMap();
    }


    public void createNew(Heatmap heatmap, String bookmarkName) {
        createNew(heatmap, bookmarkName, null);
    }

    public void createNew(Heatmap heatmap, String bookmarkName, int[] include) {
        String name = bookmarkName;
        int counter = 1;
        while (nameOccupied(name)) {
            name = bookmarkName + "-" + counter++;
        }

        if (include == null || include.length == 0) {
            include = new int[]{ROWS, COLUMNS, LAYER};
        }

        List<String> rows = null;
        List<String> cols = null;

        if (Ints.contains(include, ROWS)) {
            rows = new ArrayList<>();
            Iterables.addAll(rows, heatmap.getRows());
        }

        if (Ints.contains(include, COLUMNS)) {
            cols = new ArrayList<>();
            Iterables.addAll(cols, heatmap.getColumns());
        }

        String layerId = Ints.contains(include, LAYER) ? heatmap.getLayers().getTopLayer().getId() : null;

        add(new Bookmark(name, rows, cols, layerId));
    }

    private boolean nameOccupied(String name) {
        for (Bookmark b : bookmarks) {
            if (b.getName().toLowerCase().equals(name.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
