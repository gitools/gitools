package org.gitools.ui.utils;

import java.io.File;

public class FileChoose {

    private File file;
    private FileFormatFilter filter;

    public FileChoose(File file, FileFormatFilter filter) {
        this.file = file;
        this.filter = filter;
    }

    public File getFile() {
        return file;
    }

    public FileFormatFilter getFilter() {
        return filter;
    }
}
