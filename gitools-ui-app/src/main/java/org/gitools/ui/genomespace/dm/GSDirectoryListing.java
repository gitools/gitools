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

package org.gitools.ui.genomespace.dm;

import java.io.File;
import java.net.URL;
import java.util.List;

/**
 * @author Jim Robinson
 * @date Aug 2, 2011
 */
public class GSDirectoryListing
{

    private GSFileMetadata directory;

    private List<GSFileMetadata> contents;

    public GSDirectoryListing(String url, List<GSFileMetadata> contents)
    {

        // Parse URL to get components.
        //URL u = new URL(url);
        String path = null;
        try
        {
            path = (new URL(url)).getPath();
        } catch (Exception e)
        {

        }
        String name = (new File(path)).getName();
        String format = "";
        String size = "";
        boolean isDirectory = true;
        directory = new GSFileMetadata(name, path, url, format, size, isDirectory);
        this.contents = contents;
    }

    public GSFileMetadata getDirectory()
    {
        return directory;
    }

    public List<GSFileMetadata> getContents()
    {
        return contents;
    }
}
