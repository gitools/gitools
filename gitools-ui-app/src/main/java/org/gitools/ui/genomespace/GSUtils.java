/*
 * Copyright (c) 2007-2012 The Broad Institute, Inc.
 * SOFTWARE COPYRIGHT NOTICE
 * This software and its documentation are the copyright of the Broad Institute, Inc. All rights are reserved.
 *
 * This software is supplied without any warranty or guaranteed support whatsoever. The Broad Institute is not responsible for its use, misuse, or functionality.
 *
 * This software is licensed under the terms of the GNU Lesser General Public License (LGPL),
 * Version 2.1 which is available at http://www.opensource.org/licenses/lgpl-2.1.php.
 */

package org.gitools.ui.genomespace;

import org.apache.log4j.Logger;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jrobinso
 * @date Jun 9, 2011
 * @noinspection ALL
 */
public class GSUtils {
    private static final Logger log = Logger.getLogger(GSUtils.class);


    /*
    * Directory and filenames to save the token and username to facilitate SSO
    */

    private static final String tokenSaveDir = ".gs";

    private static final String tokenSaveFileName = ".gstoken";

    private static final String usernameSaveFileName = ".gsusername";

    private static String gsUser = null;

    private static String gsToken = null;

    public static final String DEFAULT_GS_DM_SERVER = "https://dm.genomespace.org/datamanager/v1.0/";
    public static final String DEFAULT_GS_IDENTITY_SERVER = "https://identity.genomespace.org/identityServer/basic";


    private static File getTokenSaveDir() {
        String userDir = System.getProperty("user.home");
        File gsDir = new File(userDir, tokenSaveDir);
        if (!gsDir.exists()) {
            gsDir.mkdir();
        }
        return gsDir;
    }


    private static File getTokenFile() {
        File gsDir = getTokenSaveDir();
        return (gsDir != null && gsDir.exists()) ? new File(gsDir, tokenSaveFileName) : null;
    }


    private static File getUsernameFile() {
        File gsDir = getTokenSaveDir();
        return (gsDir != null && gsDir.exists()) ? new File(gsDir, usernameSaveFileName) : null;
    }

    public static void setGSToken(String newToken) {
        if (gsToken == null || !gsToken.equals(newToken)) {
            gsToken = newToken;
            BufferedWriter bw = null;

            File gsDir = getTokenSaveDir();
            if (!gsDir.isDirectory()) {
                log.error("Could not store token for SSO.  File " + gsDir.getAbsolutePath() + "exists and is not a directory.");
                return; // someone made a file with this name...
            }
            File tokenFile = getTokenFile();
            if (tokenFile.exists()) {
                tokenFile.delete();
            }
            writeToFile(gsToken, tokenFile);
        }
    }


    public static String getGSToken() {
        if (gsToken == null) {
            File file = GSUtils.getTokenFile();
            if (file.exists()) {
                BufferedReader br = null;
                try {
                    br = new BufferedReader(new FileReader(file));
                    gsToken = br.readLine();
                } catch (IOException e) {
                    log.error("Error reading GS cookie", e);
                } finally {
                    if (br != null) {
                        try {
                            br.close();
                        } catch (IOException e) {
                            // Ignore
                        }
                    }
                }
            }
        }
        return gsToken;
    }


    public static void setGSUser(String newUser) {
        if (gsUser == null || !gsUser.equals(newUser)) {
            gsUser = newUser;

            File gsDir = getTokenSaveDir();
            if (!gsDir.isDirectory()) {
                log.error("Could not store token for SSO.  File " + gsDir.getAbsolutePath() + "exists and is not a directory.");
                return; // someone made a file with this name...
            }
            File userFile = getUsernameFile();
            if (userFile.exists()) {
                userFile.delete();
            }
            writeToFile(gsUser, userFile);
        }
    }


    public static String getGSUser() throws IOException {
        if (gsUser == null) {
            BufferedReader br = null;
            try {
                File tokenFile = getUsernameFile();
                if (tokenFile.exists()) {
                    br = new BufferedReader(new FileReader(tokenFile));
                    gsUser = br.readLine().trim();
                }
            } finally {
                try {
                    if (br != null) {
                        br.close();
                    }
                } catch (Exception e) {
                }
            }
            return gsUser;
        }
        return gsUser;
    }


    public static void logout() {

        gsToken = null;
        gsUser = null;
        gsToken = null;
        File userfile = getUsernameFile();
        if (userfile.exists()) {
            userfile.delete();
        }
        File tokenFile = getTokenFile();
        if (tokenFile.exists()) {
            tokenFile.delete();
        }


        try {
            URI gsURI = new URI(DEFAULT_GS_DM_SERVER);
            CookieManager manager = (CookieManager) CookieManager.getDefault();
            if (manager == null) {
                return;
            }
            final CookieStore cookieStore = manager.getCookieStore();
            List<HttpCookie> cookies = new ArrayList<HttpCookie>(cookieStore.get(gsURI));
            if (cookies != null) {
                for (HttpCookie cookie : cookies) {
                    final String name = cookie.getName();
                    if (name.equals("gs-token") || name.equals("gs-username")) {
                        cookieStore.remove(gsURI, cookie);
                    }
                }
            }
        } catch (URISyntaxException e) {
            log.error("Error creating GS URI", e);
        }


    }

    private static void writeToFile(String line, File aFile) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(aFile));
            bw.write(line);

            bw.close();
        } catch (Exception e) {
            log.error("Failed to save the token for later Single Sign on", e);
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
            } catch (Exception e) {
            }
        }
    }


    public static boolean isGenomeSpace(URL url) {
        return url.getHost().contains("genomespace");
    }

}