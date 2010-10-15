package org.gitools.ui.utils;

import java.io.File;
import javax.swing.filechooser.FileFilter;
import org.gitools.persistence.FileFormat;

public class FileFormatFilter extends FileFilter {

	private String description;
	private String mime;

	private FileFormat format;

	private FileFormat[] formats;

	public FileFormatFilter(String description, String mime) {
		this.description = description;
		this.mime = mime;
	}

	public FileFormatFilter(String description, String mime, FileFormat[] formats) {
		this(description, mime);
		this.formats = formats;
	}

	public FileFormatFilter(FileFormat format) {
		this(format.getTitleWithExtension(), format.getMime());
		this.format = format;
	}

	@Override
	public boolean accept(File f) {
		if (f.isDirectory())
			return true;

		if (format != null)
			return format.checkExtension(f.getName());
		else if (formats != null) {
			for (FileFormat ff : formats)
				if (ff.checkExtension(f.getName()))
					return true;
			return false;
		}

		return true;
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
