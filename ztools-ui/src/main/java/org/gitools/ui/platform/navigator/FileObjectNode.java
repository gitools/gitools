package org.gitools.ui.platform.navigator;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;

public class FileObjectNode extends AbstractNode {

	private static final long serialVersionUID = 1168962294490262027L;

	public FileObjectNode(FileObject fileObject) {
		super(fileObject);
	}
	
	protected FileObject getFileObject() {
		return (FileObject) getUserObject();
	}
	
	@Override
	public void expand() {
		super.expand();
		
		try {
			FileObject[] files = getFileObject().getChildren();
			for (FileObject file : files)
				add(new FileObjectNode(file));
		} catch (FileSystemException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean isLeaf() {
		try {
			return !getFileObject().getType().hasChildren();
		} catch (FileSystemException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	@Override
	public String getLabel() {
		return getFileObject().getName().getBaseName();
	}
}
