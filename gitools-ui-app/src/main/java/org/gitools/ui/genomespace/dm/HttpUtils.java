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

package org.gitools.ui.genomespace.dm;

import org.apache.log4j.Logger;
import org.gitools.ui.genomespace.GSUtils;
import org.gitools.ui.platform.AppFrame;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

/**
 * Wrapper utility class... for interacting with HttpURLConnection.
 *
 * @author Jim Robinson
 * @date 9/22/11
 */
public class HttpUtils
{

    private static Logger log = Logger.getLogger(HttpUtils.class);

    private static HttpUtils instance;

    public static int CONNECT_TIMEOUT = 20000;        // 20 seconds
    public static int READ_TIMEOUT = 1000 * 3 * 60;   // 3 minutes

    private Map<String, Boolean> byteRangeTestMap;

    private final int MAX_REDIRECTS = 5;

    private String defaultUserName = null;
    private char[] defaultPassword = null;
    private static Pattern URLmatcher = Pattern.compile(".{1,8}://.*");

    // static provided to support unit testing
    private static boolean BYTE_RANGE_DISABLED = false;

    /**
     * @return the single instance
     */
    public static HttpUtils getInstance()
    {
        if (instance == null)
        {
            instance = new HttpUtils();
        }
        return instance;
    }

    /**
     * Constructor
     */
    private HttpUtils()
    {

        disableCertificateValidation();
        CookieHandler.setDefault(new IGVCookieManager());
        Authenticator.setDefault(new IGVAuthenticator());

        byteRangeTestMap = Collections.synchronizedMap(new HashMap());
    }


    public static boolean isRemoteURL(String string)
    {
        String lcString = string.toLowerCase();
        return lcString.startsWith("http://") || lcString.startsWith("https://") || lcString.startsWith("ftp://");
    }

    /**
     * Provided to support unit testing (force disable byte range requests)
     *
     * @return
     */
    public static void disableByteRange(boolean b)
    {
        BYTE_RANGE_DISABLED = b;
    }


    /**
     * Join the {@code elements} with the character {@code joiner},
     * URLencoding the {@code elements} along the way. {@code joiner}
     * is NOT URLEncoded
     * Example:
     * String[] parm_list = new String[]{"app les", "oranges", "bananas"};
     * String formatted = buildURLString(Arrays.asList(parm_list), "+");
     * <p/>
     * formatted will be "app%20les+oranges+bananas"
     *
     * @param elements
     * @param joiner
     * @return
     */
    public static String buildURLString(Iterable<String> elements, String joiner)
    {

        Iterator<String> iter = elements.iterator();
        if (!iter.hasNext())
        {
            return "";
        }
        String wholequery = iter.next();
        try
        {
            while (iter.hasNext())
            {
                wholequery += joiner + URLEncoder.encode(iter.next(), "UTF-8");
            }
            return wholequery;
        } catch (UnsupportedEncodingException e)
        {
            throw new IllegalArgumentException("Bad argument in genelist: " + e.getMessage());
        }
    }


    /**
     * Return the contents of the url as a String.  This method should only be used for queries expected to return
     * a small amount of data.
     *
     * @param url
     * @return
     * @throws IOException
     */
    public String getContentsAsString(URL url) throws IOException
    {

        InputStream is = null;
        HttpURLConnection conn = openConnection(url, null);
        try
        {
            is = conn.getInputStream();
            return readContents(is);

        } finally
        {
            if (is != null)
            {
                is.close();
            }
        }
    }

    public String getContentsAsJSON(URL url) throws IOException
    {

        InputStream is = null;
        Map<String, String> reqProperties = new HashMap();
        reqProperties.put("Accept", "application/json,text/plain");
        HttpURLConnection conn = openConnection(url, reqProperties);
        try
        {
            is = conn.getInputStream();
            return readContents(is);

        } finally
        {
            if (is != null)
            {
                is.close();
            }
        }
    }

    /**
     * Open a connection stream for the URL.
     *
     * @param url
     * @return
     * @throws IOException
     */
    public InputStream openConnectionStream(URL url) throws IOException
    {
        return openConnectionStream(url, null);
    }

    public InputStream openConnectionStream(URL url, Map<String, String> requestProperties) throws IOException
    {

        HttpURLConnection conn = openConnection(url, requestProperties);
        if (conn == null)
        {
            return null;
        }
        InputStream input = conn.getInputStream();
        if ("gzip".equals(conn.getContentEncoding()))
        {
            input = new GZIPInputStream(input);
        }
        return input;
    }


    public boolean resourceAvailable(URL url)
    {
        try
        {
            HttpURLConnection conn = openConnection(url, null, "HEAD");
            int code = conn.getResponseCode();
            return code == 200;
        } catch (IOException e)
        {
            return false;
        }
    }

    public String getHeaderField(URL url, String key) throws IOException
    {
        HttpURLConnection conn = openConnection(url, null, "HEAD");
        if (conn == null)
        {
            return null;
        }
        return conn.getHeaderField(key);
    }

    public long getContentLength(URL url) throws IOException
    {

        String contentLengthString = getHeaderField(url, "Content-Length");
        if (contentLengthString == null)
        {
            return -1;
        }
        else
        {
            return Long.parseLong(contentLengthString);
        }
    }

    public boolean downloadFile(String url, File outputFile) throws IOException
    {

        log.info("Downloading " + url + " to " + outputFile.getAbsolutePath());

        HttpURLConnection conn = openConnection(new URL(url), null);

        long contentLength = -1;
        String contentLengthString = conn.getHeaderField("Content-Length");
        if (contentLengthString != null)
        {
            contentLength = Long.parseLong(contentLengthString);
        }


        log.info("Content length = " + contentLength);

        InputStream is = null;
        OutputStream out = null;

        try
        {
            is = conn.getInputStream();
            out = new FileOutputStream(outputFile);

            byte[] buf = new byte[64 * 1024];
            int downloaded = 0;
            int bytesRead = 0;
            while ((bytesRead = is.read(buf)) != -1)
            {
                out.write(buf, 0, bytesRead);
                downloaded += bytesRead;
            }
            log.info("Download complete.  Total bytes downloaded = " + downloaded);
        } finally
        {
            if (is != null)
            {
                is.close();
            }
            if (out != null)
            {
                out.flush();
                out.close();
            }
        }
        long fileLength = outputFile.length();

        return contentLength <= 0 || contentLength == fileLength;
    }


    public void uploadGenomeSpaceFile(String uri, File file, Map<String, String> headers) throws IOException
    {

        HttpURLConnection urlconnection = null;
        OutputStream bos = null;

        URL url = new URL(uri);
        urlconnection = openConnection(url, headers, "PUT");
        urlconnection.setDoOutput(true);
        urlconnection.setDoInput(true);

        bos = new BufferedOutputStream(urlconnection.getOutputStream());
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        int i;
        // read byte by byte until end of stream
        while ((i = bis.read()) > 0)
        {
            bos.write(i);
        }
        bos.close();
        int responseCode = urlconnection.getResponseCode();

        // Error messages below.
        if (responseCode >= 400)
        {
            String message = readErrorStream(urlconnection);
            throw new IOException("Error uploading " + file.getName() + " : " + message);
        }
    }


    public String createGenomeSpaceDirectory(URL url, String body) throws IOException
    {

        HttpURLConnection urlconnection = null;
        OutputStream bos = null;

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        headers.put("Content-Length", String.valueOf(body.getBytes().length));

        urlconnection = openConnection(url, headers, "PUT");
        urlconnection.setDoOutput(true);
        urlconnection.setDoInput(true);

        bos = new BufferedOutputStream(urlconnection.getOutputStream());
        bos.write(body.getBytes());
        bos.close();
        int responseCode = urlconnection.getResponseCode();

        // Error messages below.
        StringBuffer buf = new StringBuffer();
        InputStream inputStream;

        if (responseCode >= 200 && responseCode < 300)
        {
            inputStream = urlconnection.getInputStream();
        }
        else
        {
            inputStream = urlconnection.getErrorStream();
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String nextLine;
        while ((nextLine = br.readLine()) != null)
        {
            buf.append(nextLine);
            buf.append('\n');
        }
        inputStream.close();

        if (responseCode >= 200 && responseCode < 300)
        {
            return buf.toString();
        }
        else
        {
            throw new IOException("Error creating GS directory: " + buf.toString());
        }
    }

    /**
     * Code for disabling SSL certification
     */
    private void disableCertificateValidation()
    {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager()
                {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers()
                    {
                        return new java.security.cert.X509Certificate[0];
                    }

                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType)
                    {
                    }

                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType)
                    {
                    }
                }
        };

        // Install the all-trusting trust manager
        try
        {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, null);
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (NoSuchAlgorithmException e)
        {
        } catch (KeyManagementException e)
        {
        }

    }

    private String readContents(InputStream is) throws IOException
    {
        BufferedInputStream bis = new BufferedInputStream(is);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int b;
        while ((b = bis.read()) >= 0)
        {
            bos.write(b);
        }
        return new String(bos.toByteArray());
    }

    private String readErrorStream(HttpURLConnection connection) throws IOException
    {
        InputStream inputStream = null;

        try
        {
            inputStream = connection.getErrorStream();
            if (inputStream == null)
            {
                return null;
            }
            return readContents(inputStream);
        } finally
        {
            if (inputStream != null)
            {
                inputStream.close();
            }
        }


    }

    private HttpURLConnection openConnection(URL url, Map<String, String> requestProperties) throws IOException
    {
        return openConnection(url, requestProperties, "GET");
    }

    private HttpURLConnection openConnection(URL url, Map<String, String> requestProperties, String method) throws IOException
    {
        return openConnection(url, requestProperties, method, 0);
    }

    /**
     * The "real" connection method
     *
     * @param url
     * @param requestProperties
     * @param method
     * @return
     * @throws IOException
     */
    private HttpURLConnection openConnection(
            URL url, Map<String, String> requestProperties, String method, int redirectCount) throws IOException
    {

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();


        if (GSUtils.isGenomeSpace(url))
        {
            String token = GSUtils.getGSToken();
            if (token != null)
            {
                conn.setRequestProperty("Cookie", "gs-token=" + token);
            }
            conn.setRequestProperty("Accept", "application/json,text/plain");
        }
        else
        {
            conn.setRequestProperty("Accept", "text/plain");
        }

        conn.setUseCaches(false);  // <= very important!
        conn.setConnectTimeout(CONNECT_TIMEOUT);
        conn.setReadTimeout(READ_TIMEOUT);
        conn.setRequestMethod(method);
        conn.setRequestProperty("Connection", "Keep-Alive");
        if (requestProperties != null)
        {
            for (Map.Entry<String, String> prop : requestProperties.entrySet())
            {
                conn.setRequestProperty(prop.getKey(), prop.getValue());
            }
        }
        conn.setRequestProperty("User-Agent", "Gitools");

        if (method.equals("PUT"))
        {
            return conn;
        }
        else
        {

            int code = conn.getResponseCode();

            // Redirects.  These can occur even if followRedirects == true if there is a change in protocol,
            // for example http -> https.
            if (code >= 300 && code < 400)
            {

                if (redirectCount > MAX_REDIRECTS)
                {
                    throw new IOException("Too many redirects");
                }

                String newLocation = conn.getHeaderField("Location");
                log.debug("Redirecting to " + newLocation);

                return openConnection(new URL(newLocation), requestProperties, method, redirectCount++);
            }

            // TODO -- handle other response codes.
            else if (code >= 400)
            {

                String message;
                if (code == 404)
                {
                    message = "File not found: " + url.toString();
                    throw new FileNotFoundException(message);
                }
                else if (code == 401)
                {
                    // Looks like this only happens when user hits "Cancel".
                    // message = "Not authorized to view this file";
                    // JOptionPane.showMessageDialog(null, message, "HTTP error", JOptionPane.ERROR_MESSAGE);
                    redirectCount = MAX_REDIRECTS + 1;
                    return null;
                }
                else
                {
                    message = conn.getResponseMessage();
                }
                String details = readErrorStream(conn);
                log.debug("error stream: " + details);
                log.debug(message);
                HttpResponseException exc = new HttpResponseException(code);

                throw exc;
            }
        }
        return conn;
    }

    public void setDefaultPassword(String defaultPassword)
    {
        this.defaultPassword = defaultPassword.toCharArray();
    }

    public void setDefaultUserName(String defaultUserName)
    {
        this.defaultUserName = defaultUserName;
    }

    public void clearDefaultCredentials()
    {
        this.defaultPassword = null;
        this.defaultUserName = null;
    }

    /**
     * The default authenticator
     */
    public class IGVAuthenticator extends Authenticator
    {

        Hashtable<String, PasswordAuthentication> pwCache = new Hashtable<String, PasswordAuthentication>();
        HashSet<String> cacheAttempts = new HashSet<String>();

        /**
         * Called when password authentication is needed.
         *
         * @return
         */
        @Override
        protected synchronized PasswordAuthentication getPasswordAuthentication()
        {

            RequestorType type = getRequestorType();
            String urlString = getRequestingURL().toString();

            // Cache user entered PWs.  In normal use this shouldn't be necessary as credentials are cached upstream,
            // but if loading many files in parallel (e.g. from sessions) calls to this method can queue up before the
            // user enters their credentials, causing needless reentry.
            String pKey = type.toString() + getRequestingProtocol() + getRequestingHost();
            PasswordAuthentication pw = pwCache.get(pKey);
            if (pw != null)
            {
                // Prevents infinite loop if credentials are incorrect
                if (cacheAttempts.contains(urlString))
                {
                    cacheAttempts.remove(urlString);
                }
                else
                {
                    cacheAttempts.add(urlString);
                    return pw;
                }
            }

            if (defaultUserName != null && defaultPassword != null)
            {
                return new PasswordAuthentication(defaultUserName, defaultPassword);
            }

            Frame owner = AppFrame.get();

            boolean isGenomeSpace = GSUtils.isGenomeSpace(getRequestingURL());
            if (isGenomeSpace)
            {
                // If we are being challenged by GS the token must be bad/expired
                GSUtils.logout();
            }

            LoginDialog dlg = new LoginDialog(owner, isGenomeSpace, urlString, false);
            dlg.setVisible(true);
            if (dlg.isCanceled())
            {
                return null;
            }
            else
            {
                final String userString = dlg.getUsername();
                final char[] userPass = dlg.getPassword();

                pw = new PasswordAuthentication(userString, userPass);
                pwCache.put(pKey, pw);
                return pw;
            }
        }
    }


    /**
     * Provide override for unit tests
     */
    public void setAuthenticator(Authenticator authenticator)
    {
        Authenticator.setDefault(authenticator);
    }

    /**
     * For unit tests
     */
    public void resetAuthenticator()
    {
        Authenticator.setDefault(new IGVAuthenticator());

    }


    /**
     * Extension of CookieManager that grabs cookies from the GenomeSpace identity server to store locally.
     * This is to support the GenomeSpace "single sign-on". Examples ...
     * gs-username=igvtest; Domain=.genomespace.org; Expires=Mon, 21-Jul-2031 03:27:23 GMT; Path=/
     * gs-token=HnR9rBShNO4dTXk8cKXVJT98Oe0jWVY+; Domain=.genomespace.org; Expires=Mon, 21-Jul-2031 03:27:23 GMT; Path=/
     */

    final static public Pattern equalPattern = Pattern.compile("=");
    final static public Pattern semicolonPattern = Pattern.compile(";");

    static class IGVCookieManager extends CookieManager
    {


        @Override
        public void put(URI uri, Map<String, List<String>> stringListMap) throws IOException
        {
            if (uri.toString().startsWith(GSUtils.DEFAULT_GS_IDENTITY_SERVER))
            {
                List<String> cookies = stringListMap.get("Set-Cookie");
                if (cookies != null)
                {
                    for (String cstring : cookies)
                    {
                        String[] tokens = equalPattern.split(cstring);
                        if (tokens.length >= 2)
                        {
                            String cookieName = tokens[0];
                            String[] vTokens = semicolonPattern.split(tokens[1]);
                            String value = vTokens[0];
                            if (cookieName.equals("gs-token"))
                            {
                                GSUtils.setGSToken(value);
                            }
                            else if (cookieName.equals("gs-username"))
                            {
                                GSUtils.setGSUser(value);
                            }
                        }
                    }
                }
            }
            super.put(uri, stringListMap);
        }
    }

}
