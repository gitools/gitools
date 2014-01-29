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

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Utility class for accessing the GenomeSpace data manager web service
 *
 * @author Jim Robinson
 * @date Aug 2, 2011
 * @noinspection ALL
 */
public class DMUtils {

    private static Logger log = Logger.getLogger(DMUtils.class);
    private static final String UPLOAD_SERVICE = "uploadurl";
    public static final String DEFAULT_DIRECTORY = "defaultdirectory";
    public static final String PERSONAL_DIRECTORY = "personaldirectory";


    /**
     * Fetch the contents of the GenomeSpace directory.
     *
     * @param directoryURL
     * @return
     * @throws IOException
     * @throws JSONException
     */

    public static GSDirectoryListing getDirectoryListing(URL directoryURL) throws IOException, JSONException {

        String str = HttpUtils.getInstance().getContentsAsJSON(directoryURL);
        JSONTokener tk = new JSONTokener(str);
        JSONObject obj = new JSONObject(tk);

        JSONObject directory = (JSONObject) obj.get("directory");
        String dirUrlString = directory.get("url").toString();

        LinkedList<GSFileMetadata> elements = new LinkedList();
        if (obj.has("contents")) {
            Object c = obj.get("contents");
            List<JSONObject> contents = new ArrayList();
            if (c instanceof JSONObject) {
                contents.add((JSONObject) c);
            } else {
                JSONArray tmp = (JSONArray) c;
                int l = tmp.length();
                for (int i = 0; i < l; i++) {
                    contents.add((JSONObject) tmp.get(i));
                }
            }

            ArrayList<GSFileMetadata> dirElements = new ArrayList();
            ArrayList<GSFileMetadata> fileElements = new ArrayList();
            int contentsLength = contents.size();
            for (int i = 0; i < contentsLength; i++) {
                JSONObject o = contents.get(i);
                GSFileMetadata metaData = new GSFileMetadata(o);
                if (metaData.isDirectory()) {
                    dirElements.add(metaData);
                } else {
                    fileElements.add(metaData);
                }
            }

            elements.addAll(dirElements);
            elements.addAll(fileElements);
        }

        return new GSDirectoryListing(dirUrlString, elements);

    }


    public static GSFileMetadata createDirectory(String putURL) throws IOException, JSONException {

        JSONObject dirMeta = new JSONObject();
        try {
            dirMeta.put("isDirectory", true);
            System.out.println(dirMeta.toString());
        } catch (JSONException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        String body = "{\"isDirectory\":true}";
        String response = HttpUtils.getInstance().createGenomeSpaceDirectory(new URL(putURL), body);

        JSONTokener tk = new JSONTokener(response);
        JSONObject obj = new JSONObject(tk);
        return new GSFileMetadata(obj);

    }


}
