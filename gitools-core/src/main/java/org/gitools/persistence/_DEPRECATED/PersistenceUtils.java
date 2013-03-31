/*
 *  Copyright 2010 Universitat Pompeu Fabra.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.gitools.persistence._DEPRECATED;

import java.io.File;
import java.util.regex.Pattern;

@Deprecated
public class PersistenceUtils {

    // Copied from http://stackoverflow.com/questions/204784/how-to-construct-a-relative-path-in-java-from-two-absolute-paths-or-urls
    public static String getRelativePath(String basePath, String targetPath) {

        final String pathSeparator = File.separator;

        boolean isDir = false;
        {
            File f = new File(targetPath);
            isDir = f.isDirectory();
        }
        //  We need the -1 argument to split to make sure we get a trailing
        //  "" token if the base ends in the path separator and is therefore
        //  a directory. We require directory paths to end in the path
        //  separator -- otherwise they are indistinguishable from files.
        String[] base = basePath.split(Pattern.quote(pathSeparator), -1);
        String[] target = targetPath.split(Pattern.quote(pathSeparator), 0);

        //  First get all the common elements. Store them as a string,
        //  and also count how many of them there are.
        String common = "";
        int commonIndex = 0;
        for (int i = 0; i < target.length && i < base.length; i++) {
            if (target[i].equals(base[i])) {
                common += target[i] + pathSeparator;
                commonIndex++;
            } else {
                break;
            }
        }

        if (commonIndex == 0) {
            //  Whoops -- not even a single common path element. This most
            //  likely indicates differing drive letters, like C: and D:.
            //  These paths cannot be relativized. Return the target path.
            return targetPath;
            //  This should never happen when all absolute paths
            //  begin with / as in *nix.
        }

        String relative = "";
        if (base.length == commonIndex) {
            //  Comment this out if you prefer that a relative path not start with ./
            //relative = "." + pathSeparator;
        } else {
            int numDirsUp = base.length - commonIndex; /*- (isDir ? 0 : 1); /* only subtract 1 if it  is a file. */
            //  The number of directories we have to backtrack is the length of
            //  the base path MINUS the number of common path elements, minus
            //  one because the last element in the path isn't a directory.
            for (int i = 1; i <= (numDirsUp); i++) {
                relative += ".." + pathSeparator;
            }
        }
        //if we are comparing directories then we
        if (targetPath.length() > common.length()) {
            //it's OK, it isn't a directory
            relative += targetPath.substring(common.length());
        }

        return relative;
    }

    /**
     * Returns file name (including extension) without path
     */
    public static String getBaseName(String path) {
        int sep = path.lastIndexOf(File.separatorChar);
        return path.substring(sep + 1);
    }

    /**
     * Returns the file name without extension.
     * It takes into account composed extension for .gz
     */
    public static String getFileName(String path) {
        int dot = path.lastIndexOf('.');
        if (dot > 0 && path.substring(dot).equalsIgnoreCase(".gz"))
            dot = path.substring(0, dot - 1).lastIndexOf('.');

        int sep = path.lastIndexOf(File.separatorChar);
        return dot != -1 ? path.substring(sep + 1, dot) : path.substring(sep + 1);
    }

    /**
     * Returns only the extension of the file.
     * It takes into account composed extension for .gz
     */
    public static String getExtension(String path) {
        int dot = path.lastIndexOf('.');
        String ext = dot != -1 ? path.substring(dot + 1) : "";
        if (ext.equalsIgnoreCase("gz")) {
            String[] e = path.split("[.]");
            if (e.length >= 2)
                ext = e[e.length - 2] + "." + e[e.length - 1];
        }
        return ext;
    }





}
