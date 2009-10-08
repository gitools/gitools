package org.gitools.ui.platform.navigator.TMP;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;

public class FileNode extends NavigatorNode {

	protected FileObject fileObject;
	
	protected FileObject[] children;
	
	public FileNode(FileObject fileObject) {
		super(fileObject);
		
		this.fileObject = fileObject;
	}

	@Override
	public int getChildCount() {
		return getChildren().length;
	}
	
	@Override
	public NavigatorNode getChild(int index) {
		return new FileNode(getChildren()[index]);
	}
	
	public FileObject[] getChildren() {
		if (children == null) {
			try {
				children = fileObject.getChildren();
			} catch (FileSystemException e) {
				e.printStackTrace();
				children = new FileObject[0];
			}
		}
		return children;
	}
	
	@Override
	public boolean isLeaf() {
		return getChildren().length == 0;
	}
}
