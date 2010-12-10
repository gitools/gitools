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

package edu.upf.bg.csv;

@Deprecated
public class CSVException extends Exception {

	private static final long serialVersionUID = 8673467489532683989L;

	public static final String msgSeparatorExpected = "Separator expected.";
	public static final String msgEndOfFileExpected = "End of file expected.";

	private int line;
	private int col;
	private String csvMessage;
	
	public CSVException(int line, int col, String msg) {
		super("line " + (line + 1) + ", col " + (col + 1) + " : " + msg);
		this.line = line + 1;
		this.col = col + 1;
		this.csvMessage = msg;
	}
	
	public CSVException(int line, int col, String msg, Exception e) {
		super("line " + (line + 1) + ", col " + (col + 1) + " : " + msg, e);
		this.line = line + 1;
		this.col = col + 1;
		this.csvMessage = msg;
	}

	public CSVException() {
		this(0,0,"");
	}
	
	public int getLine() {
		return line;
	}
	
	public int getCol() {
		return col;
	}
	
	public String getCsvMessage() {
		return csvMessage;
	}
}
