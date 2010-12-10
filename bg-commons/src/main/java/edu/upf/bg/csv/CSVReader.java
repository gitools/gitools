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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Deprecated
public class CSVReader {

	private static final CSVProcessor defaultProcessor = new CSVProcessorAdapter();

	private CharSequence seq;
	
	private Pattern pat;
	private Pattern patLineSep;
	private Pattern patRemoveQuotes;
	private Pattern patInternalQuotes;
	
	private int row;
	private Matcher matcher;
	
	public CSVReader(FileInputStream in, char sep, char quote) throws IOException {
		createCharSequence(in);
		createPattern(sep, quote);
		reset();
	}
	
	public CSVReader(File file, char sep, char quote) throws IOException {
		this(new FileInputStream(file), sep, quote);
	}
	
	private void createCharSequence(FileInputStream in) throws IOException {
		// Open a file channel
		FileChannel channel = in.getChannel();
    
		// Create a read-only CharBuffer on the file
		ByteBuffer bbuf = channel.map(FileChannel.MapMode.READ_ONLY, 0, (int)channel.size());
		seq = Charset.defaultCharset().newDecoder().decode(bbuf); //.forName("8859_1")
	}

	private void createPattern(char sep, char quote) {
		
		String regex = sep == ' ' ?
				"(q(?:[^q]|qq)*q|[^qs\\r\\n]*)(s+|s*\\r\\n?|s*\\n)?" :
				"( *q(?:[^q]|qq)*q *|[^qs\\r\\n]*)(s|\\r\\n?|\\n)?";
		
		String regexQuote = String.valueOf(quote);
		String regexSep = String.valueOf(sep);
		
		regex = regex.replaceAll("q", regexQuote);
		regex = regex.replaceAll("s", regexSep);
		
		pat = Pattern.compile(regex);
		
		regex = "\\r\\n?|\\n";
		patLineSep = Pattern.compile(regex);
		
		regex = "^q((?:[^q]|qq)*)q$";
		regex = regex.replaceAll("q", regexQuote);
		patRemoveQuotes = Pattern.compile(regex);
		
		regex = "(q)q";
		regex = regex.replaceAll("q", regexQuote);
		patInternalQuotes = Pattern.compile(regex);
	}

	public void scan(CSVProcessor proc) throws CSVException {

		if (proc == null)
			proc = defaultProcessor;
		
		if (row == 0)
			proc.start();
		
		while(internalScanLine(proc));
		
		proc.end();
	}

	public boolean scanLine(CSVProcessor proc) throws CSVException {
		if (proc == null)
			proc = defaultProcessor;
		
		if (row == 0)
			proc.start();
		
		boolean endOfFile = !internalScanLine(proc);
		
		if (endOfFile)
			proc.end();
		
		return !endOfFile;
	}
	
	private boolean internalScanLine(CSVProcessor proc) throws CSVException {
		
		int col = 0;
		
		boolean endOfFile = false;
		boolean lastField = false;
		
		proc.lineStart(row);
		
		while (!lastField && matcher.find()) {
			
			String field = matcher.group(1);
			String sep = matcher.group(2);
		
			//System.out.print("'"+matcher.group(1)+"' - ");
			//System.out.println("'"+matcher.group(2)+"'");
			
			if (sep == null) {
				/*if (field.length() > 0)
					throw new CSVException(row, col, 
							CSVException.msgSeparatorExpected);
				else*/ if (matcher.end() < seq.length())
					throw new CSVException(row, col, 
							CSVException.msgEndOfFileExpected);
				else {
					endOfFile = true;
					if (field.length() == 0 && col == 0)
						return !endOfFile;
					else
						lastField = true;
				}
			} else
				lastField = patLineSep.matcher(sep).matches();
			
			Matcher mq = patRemoveQuotes.matcher(field.trim());
			field = mq.replaceAll("$1");
			mq = patInternalQuotes.matcher(field);
			field = mq.replaceAll("$1");
			proc.field(field, row, col++);
		}
		
		proc.lineEnd(row++);
		
		return !endOfFile;
	}
	
	public void reset() {
		row = 0;
		matcher = pat.matcher(seq);
	}

}
