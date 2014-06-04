/*
 * #%L
 * gitools-core
 * %%
 * Copyright (C) 2013 Universitat Pompeu Fabra - Biomedical Genomics group
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.gitools.utils;

import com.google.common.net.HttpHeaders;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class HttpUtils {

    public static long getContentLength(URL url) {

        if (url == null) {
            return -1;
        }

        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("HEAD");
        } catch (IOException e) {
            return -1;
        }

        String contentLengthString = conn.getHeaderField(HttpHeaders.CONTENT_LENGTH);
        conn.disconnect();

        if (contentLengthString == null) {
            return -1;
        } else {
            return Long.parseLong(contentLengthString);
        }

    }

    public static void openURLInBrowser(String href, Component component) {
        openURLInBrowser(href, component, false);
    }

    public static void openURLInBrowser(String href, Component component, boolean forcePreview) {
        try {
            URI uri = new URI(href);
            if (!forcePreview && Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(uri);
            } else {
                JOptionPane.showInputDialog(component, "Copy the URL into your browser",
                        "Copy & Open URL", JOptionPane.INFORMATION_MESSAGE, null, null, uri.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
