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


import com.jgoodies.binding.beans.Model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@XmlAccessorType(XmlAccessType.FIELD)
public class Bookmarks extends Model {

    public static String PROPERTY_CONTENTS = "contents";

    @XmlElement(name = "bookmark")
    private List<Bookmark> bookmarks;

    @XmlTransient
    private Map<String, Integer> nameMap;

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
        bookmarks.remove(b);
        nameMap.remove(b.getName());
        firePropertyChange(PROPERTY_CONTENTS, null, this);
    }


    public Bookmark get(String name) {
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


}
