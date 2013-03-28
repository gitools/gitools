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

@Deprecated
public class FileFormat {

    private String title;
    private String extension;
    private String mime;
    private boolean titleWithExtension;
    private boolean allowGzExtension;

    public FileFormat(String title, String extension) {
        this(title, extension, null);
    }

    public FileFormat(String title, String extension, String mime) {
        this(title, extension, mime, true, true);
    }

    public FileFormat(String title, String extension, String mime, boolean titleWithExtension, boolean allowGzExtension) {
        this.title = title;
        this.extension = extension;
        this.mime = mime;
        this.titleWithExtension = titleWithExtension;
        this.allowGzExtension = allowGzExtension;
    }

    public String getTitle() {
        return title;
    }

    public String getTitleWithExtension() {
        StringBuilder sb = new StringBuilder();
        sb.append(title).append(" (").append(extension);
        if (allowGzExtension)
            sb.append(", ").append(extension).append(".gz");
        sb.append(")");
        return sb.toString();
    }

    public String getExtension() {
        return extension;
    }

    public String getMime() {
        return mime;
    }

    public boolean checkExtension(String fileName) {
        fileName = fileName.toLowerCase();
        String ext = extension.toLowerCase();
        return fileName.endsWith(ext)
                || (allowGzExtension && fileName.endsWith(ext + ".gz"));
    }

    @Override
    public String toString() {
        return titleWithExtension ? getTitleWithExtension() : getTitle();
    }
}
