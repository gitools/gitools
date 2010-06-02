package org.gitools.ui.utils;

import java.io.File;
import javax.swing.filechooser.FileFilter;
import org.gitools.persistence.FileFormat;

public class FileFormatFilter extends FileFilter {

	private String description;
	private String mime;

	private FileFormat format;

	public FileFormatFilter(String description, String mime) {
		this.description = description;
		this.mime = mime;
	}

	public FileFormatFilter(FileFormat format) {
		this(format.getTitleWithExtension(), format.getMime());
		this.format = format;
	}

	@Override
	public boolean accept(File f) {
		return format == null ? true :
			f.isDirectory() || format.checkExtension(f.getName());
	}

	@Override
	public String getDescription() {
		return description;
	}

	public String getMime() {
		return mime;
	}

	public FileFormat getFormat() {
		return format;
	}
}
