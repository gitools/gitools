package org.gitools.persistence;

public class FileFormat {

	private String title;
	private String extension;
	private String mime;

	public FileFormat(String title, String extension) {
		this(title, extension, null);
	}

	public FileFormat(String title, String extension, String mime) {
		this.title = title;
		this.extension = extension;
		this.mime = mime;
	}

	public String getTitle() {
		return title;
	}

	public String getExtension() {
		return extension;
	}

	public String getMime() {
		return mime;
	}

	@Override
	public String toString() {
		return title;
	}
}
