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
