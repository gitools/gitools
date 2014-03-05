/*
 * #%L
 * org.gitools.ui.app
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
/*
 * Copyright (c) 2007-2011 by The Broad Institute of MIT and Harvard.  All Rights Reserved.
 *
 * This software is licensed under the terms of the GNU Lesser General Public License (LGPL),
 * Version 2.1 which is available at http://www.opensource.org/licenses/lgpl-2.1.php.
 *
 * THE SOFTWARE IS PROVIDED "AS IS." THE BROAD AND MIT MAKE NO REPRESENTATIONS OR
 * WARRANTES OF ANY KIND CONCERNING THE SOFTWARE, EXPRESS OR IMPLIED, INCLUDING,
 * WITHOUT LIMITATION, WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, NONINFRINGEMENT, OR THE ABSENCE OF LATENT OR OTHER DEFECTS, WHETHER
 * OR NOT DISCOVERABLE.  IN NO EVENT SHALL THE BROAD OR MIT, OR THEIR RESPECTIVE
 * TRUSTEES, DIRECTORS, OFFICERS, EMPLOYEES, AND AFFILIATES BE LIABLE FOR ANY DAMAGES
 * OF ANY KIND, INCLUDING, WITHOUT LIMITATION, INCIDENTAL OR CONSEQUENTIAL DAMAGES,
 * ECONOMIC DAMAGES OR INJURY TO PROPERTY AND LOST PROFITS, REGARDLESS OF WHETHER
 * THE BROAD OR MIT SHALL BE ADVISED, SHALL HAVE OTHER REASON TO KNOW, OR IN FACT
 * SHALL KNOW OF THE POSSIBILITY OF THE FOREGOING.
 */

package org.gitools.ui.app.genomespace.dm;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Represents a file or directory in GS storage.
 *
 * @author Jim Robinson
 * @date Aug 2, 2011
 * @noinspection ALL
 */
public class GSFileMetadata {
    private boolean isDirectory;
    private String name;
    private String path;
    private String url;
    private String format;
    private String size;

    public GSFileMetadata(String name, String path, String url, String format, String size, boolean isDirectory) {
        this.isDirectory = isDirectory;
        this.name = name;
        this.path = path;
        this.url = url;
        this.format = format;
        this.size = size;
    }

    public GSFileMetadata(JSONObject o) throws JSONException {
        name = (String) o.get("name");
        path = (String) o.get("path");
        url = (String) o.get("url");
        isDirectory = (Boolean) o.get("isDirectory");
        if (o.has("dataFormat")) {
            JSONObject dataFormat = o.has("dataFormat") ? (JSONObject) o.get("dataFormat") : null;
            format = dataFormat == null ? "" : dataFormat.getString("name");
            size = o.get("size").toString();
        }

    }

    public String toString() {
        return getName();
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }


    public String getUrl() {
        return url;
    }

    public String getFormat() {
        return format;
    }

    public String getSize() {
        return size;
    }
}
