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
public interface CSVProcessor {
	public boolean start() throws CSVException;
	public boolean end() throws CSVException;
	
	public boolean lineStart(int row) throws CSVException;
	public boolean lineEnd(int row) throws CSVException;
	
	public boolean field(String field, int row, int col) throws CSVException;
}
