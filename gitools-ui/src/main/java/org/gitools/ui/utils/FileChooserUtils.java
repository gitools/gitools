/*
 *  Copyright 2009 chris.
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
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.settings.Settings;

public class FileChooserUtils {

	public static final int MODE_SAVE = 1;
	public static final int MODE_OPEN = 2;

	public final static String jpeg = "jpeg";
    public final static String jpg = "jpg";
    public final static String gif = "gif";
    public final static String tiff = "tiff";
    public final static String tif = "tif";
    public final static String png = "png";
	public final static String pdf = "pdf";

	private static FileFilter imageFileFilter = new FileFilter() {
		@Override
		public boolean accept(File f) {
			if (f.isDirectory())
				return true;

			String extension = getExtension(f);
			if (extension != null)
				return isImageExtension(extension);

			return false;
		}

		@Override
		public String getDescription() {
			return "Image files (*.png, *.jpg, *.jpeg, *.gif)";
		}
	};

	private static FileFilter pdfFileFilter = new FileFilter() {
		@Override
		public boolean accept(File f) {
			if (f.isDirectory())
				return true;

			String extension = getExtension(f);
			if (extension != null) {
				if (extension.equals(pdf))
					return true;
				else
					return false;
			}

			return false;
		}

		@Override
		public String getDescription() {
			return "Image files (*.pdf)";
		}
	};

	public static File selectFile(String title, int mode) {
		return selectFile(title, Settings.getDefault().getLastPath(), mode);
	}

	public static File selectFile(String title, String currentPath, int mode) {
		JFileChooser fileChooser = new JFileChooser(currentPath);

		fileChooser.setDialogTitle(title);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		int retval = JFileChooser.CANCEL_OPTION;

		if (mode == FileChooserUtils.MODE_SAVE)
			fileChooser.showSaveDialog(AppFrame.instance());
		else if (mode == FileChooserUtils.MODE_OPEN)
			fileChooser.showOpenDialog(AppFrame.instance());
		
		if (retval == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			return file;
		}

		return null;
	}

	public static File selectPath(String title) {
		return selectPath(title, Settings.getDefault().getLastPath());
	}

	public static File selectPath(String title, String currentPath) {
		JFileChooser fileChooser = new JFileChooser(currentPath);

		fileChooser.setDialogTitle(title);
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		int retval = fileChooser.showOpenDialog(AppFrame.instance());
		if (retval == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			return file;
		}

		return null;
	}

	public static File selectImageFile(String title, String currentPath, int mode) {
		JFileChooser fileChooser = new JFileChooser(currentPath);

		fileChooser.setDialogTitle(title);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.addChoosableFileFilter(imageFileFilter);

		int retval = JFileChooser.CANCEL_OPTION;

		if (mode == FileChooserUtils.MODE_SAVE)
			fileChooser.showSaveDialog(AppFrame.instance());
		else if (mode == FileChooserUtils.MODE_OPEN)
			fileChooser.showOpenDialog(AppFrame.instance());

		if (retval == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			return file;
		}

		return null;
	}

	public static String getExtension(File file) {
		return getExtension(file.getName());
	}

	public static String getExtension(String fileName) {
		String ext = null;
		int i = fileName.lastIndexOf('.');

		if (i > 0 && i < fileName.length() - 1)
			ext = fileName.substring(i + 1).toLowerCase();
		return ext;
	}

	public static boolean isImageExtension(String extension) {
		return extension.equals(tif)
				|| extension.equals(gif)
				|| extension.equals(jpeg)
				|| extension.equals(jpg)
				|| extension.equals(png);
	}
}