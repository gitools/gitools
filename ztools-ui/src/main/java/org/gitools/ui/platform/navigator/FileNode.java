package org.gitools.ui.platform.navigator;

import java.io.File;

public class FileNode extends AbstractNode {

	private static final long serialVersionUID = 1168962294490262027L;

	public FileNode(File fileObject) {
		super(fileObject);
	}
	
	protected File getFileObject() {
		return (File) getUserObject();
	}
	
	@Override
	public void expand() {
		super.expand();
		
		File[] files = getFileObject().listFiles();
		for (File file : files)
			add(new FileNode(file));
	}
	
	@Override
	public boolean isLeaf() {
		return !getFileObject().isDirectory();
	}
	
	@Override
	public String getLabel() {
		return getFileObject().getName();
	}
}
