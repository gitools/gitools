package org.gitools.fileutils;

//FIXME This class has to be moved to gitools-core
public class FileFormat {

	private String title;
	private String extension;
	private Object userObject;

	public FileFormat(String title, String extension) {
		this(title, extension, null);
	}

	public FileFormat(String title, String extension, Object userObject) {
		this.title = title;
		this.extension = extension;
		this.userObject = userObject;
	}

	public String getTitle() {
		return title;
	}

	public String getExtension() {
		return extension;
	}

	public Object getUserObject() {
		return userObject;
	}

	@Override
	public String toString() {
		return title;
	}
}
