package org.gitools.persistence;

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
