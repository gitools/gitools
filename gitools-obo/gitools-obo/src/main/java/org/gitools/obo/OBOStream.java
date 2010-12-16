package org.gitools.obo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import org.apache.commons.net.ftp.FTPClient;

class OBOStream {

	private URL baseUrl;

	private BufferedReader reader;

	private int linePos;

	public OBOStream(BufferedReader reader) {
		this.reader = reader;
		this.linePos = 0;
	}

	public OBOStream(URL baseUrl) throws IOException {
		this.baseUrl = baseUrl;

		// Workaround for FTP problem connecting ftp.geneontology.org with URL.openStream()
		if (baseUrl.getProtocol().equalsIgnoreCase("ftp")) {
			FTPClient ftp = new FTPClient();
			if (baseUrl.getPort() != -1)
				ftp.connect(baseUrl.getHost(), baseUrl.getPort());
			else
				ftp.connect(baseUrl.getHost());
			ftp.login("anonymous", "");
			ftp.enterLocalPassiveMode();
			InputStream is = ftp.retrieveFileStream(baseUrl.getPath());
			this.reader = new BufferedReader(new InputStreamReader(is));
		}
		else
			this.reader = new BufferedReader(new InputStreamReader(baseUrl.openStream()));

		this.linePos = 0;
	}

	public URL getBaseUrl() {
		return baseUrl;
	}
	
	public BufferedReader getReader() {
		return reader;
	}

	public int getLinePos() {
		return linePos;
	}

	/** returns a non empty line or null if EOF */
	public String nextLine() throws IOException {
		String line = readLine();
		while (line != null && isEmptyLine(line))
			line = readLine();

		return line;
	}

	/** returns the next line or null if EOF */
	private String readLine() throws IOException {
		StringBuilder completeLine = new StringBuilder();

		String line = reader.readLine();
		linePos++;

		if (line != null)
			line = line.trim();

		while (line != null && line.endsWith("\\")) {
			line = line.substring(0, line.length() - 1);
			completeLine.append(line);
			//escapeCharsAndRemoveComments(line, completeLine);
			line = reader.readLine();
			linePos++;
			if (line != null)
				line = line.trim();
		}

		if (line != null)
			completeLine.append(line);
			//escapeCharsAndRemoveComments(line, completeLine);

		if (line == null && completeLine.length() == 0)
			return null;

		return completeLine.toString();
	}

	private boolean isEmptyLine(String line) {
		return line.replaceAll("\\s", "").isEmpty();
	}
}
