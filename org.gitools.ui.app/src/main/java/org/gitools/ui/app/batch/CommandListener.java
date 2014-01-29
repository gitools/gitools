/*
 * #%L
 * gitools-ui-app
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
package org.gitools.ui.app.batch;

import org.apache.log4j.Logger;
import org.gitools.ui.app.batch.tools.VersionTool;
import org.gitools.ui.platform.Application;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.channels.ClosedByInterruptException;
import java.util.HashMap;
import java.util.Map;

public class CommandListener implements Runnable {

    private static final Logger log = Logger.getLogger(CommandListener.class);


    private static CommandListener listener;

    private int port = -1;

    private ServerSocket serverSocket = null;

    private Socket clientSocket = null;
    private final Thread listenerThread;
    private boolean halt = false;

    public static synchronized void start(int port) {
        start(port, null);
    }

    public static synchronized void start(int port, String[] args) {
        if (args != null && args.length > 0) {
            if (checkSameVersion(port)) {

                // Pass the arguments to the other gitools instance
                Socket socket = null;
                try {
                    socket = new Socket("localhost", port);
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    InputStream in = socket.getInputStream();

                    StringBuilder cmd = new StringBuilder();
                    for (String arg : args) {
                        cmd.append(arg).append(' ');
                    }
                    out.println(cmd.toString());

                    // Wait server response
                    while (in.available() == 0) {
                        try {
                            Thread.sleep(1000);
                        } catch (Exception ee) {
                        }
                    }
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    reader.readLine();

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (socket != null) {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                // Close gitools
                System.exit(0);
            }
        }

        listener = new CommandListener(port);
        listener.listenerThread.start();
    }

    public static synchronized void halt() {
        if (listener != null) {
            listener.halt = true;
            listener.listenerThread.interrupt();
            listener.closeSockets();
            listener = null;
        }
    }

    private CommandListener(int port) {
        this.port = port;
        listenerThread = new Thread(this);
    }

    /**
     * Loop forever, processing client requests synchronously.  The server is single threaded.
     */
    public void run() {

        CommandExecutor cmdExe = new CommandExecutor();

        try {
            serverSocket = new ServerSocket(port);
            log.info("Listening on port " + port);

            while (!halt) {
                clientSocket = serverSocket.accept();
                processClientSession(cmdExe);
                if (clientSocket != null) {
                    try {
                        clientSocket.close();
                        clientSocket = null;
                    } catch (IOException e) {
                        log.error("Error in client socket loop", e);
                    }
                }
            }


        } catch (java.net.BindException e) {
            log.error(e);
        } catch (ClosedByInterruptException e) {
            log.error(e);

        } catch (IOException e) {
            if (!halt) {
                log.error("IO Error on port socket ", e);
            }
        }
    }

    /**
     * Process a client session.  Loop continuously until client sends the "halt" message, or closes the connection.
     *
     * @param cmdExe
     * @throws IOException
     */
    private void processClientSession(CommandExecutor cmdExe) throws IOException {
        PrintWriter out = null;
        BufferedReader in = null;

        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String inputLine;

            while (!halt && (inputLine = in.readLine()) != null) {

                String cmd = inputLine;
                if (cmd.startsWith("GET")) {
                    String command = null;
                    Map<String, String> params = null;
                    String[] tokens = inputLine.split(" ");
                    if (tokens.length < 2) {
                        sendHTTPResponse(out, "ERROR unexpected command line: " + inputLine);
                        return;
                    } else {
                        String[] parts = tokens[1].split("\\?");
                        if (parts.length < 2) {
                            sendHTTPResponse(out, "ERROR unexpected command line: " + inputLine);
                            return;
                        } else {
                            command = parts[0];
                            params = parseParameters(parts[1]);
                        }
                    }

                    // Consume the remainder of the request, if any.  This is important to free the connection.
                    String nextLine = in.readLine();
                    while (nextLine != null && nextLine.length() > 0) {
                        nextLine = in.readLine();
                    }

                    // If a callback (javascript) function is specified write it back immediately.  This function
                    // is used to cancel a timeout handler
                    String callback = params.get("callback");
                    if (callback != null) {
                        sendHTTPResponse(out, callback);
                    }

                    StringWriter response = new StringWriter();
                    processGet(command, params, cmdExe, new PrintWriter(response));

                    // If no callback was specified write back a "no response" header
                    if (callback == null) {

                        sendHTTPResponse(out, response.toString());
                    }

                    // http sockets are used for one request onle
                    return;

                } else {
                    // Port command
                    cmdExe.execute(inputLine.split(" "), out);
                }
            }
        } catch (IOException e) {
            log.error("Error processing client session", e);
        } finally {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
        }
    }


    private static final String CRNL = "\r\n";
    private static final String CONTENT_TYPE = "Content-Type: ";
    private static final String HTTP_RESPONSE = "HTTP/1.1 200 OK";
    private static final String HTTP_NO_RESPONSE = "HTTP/1.1 204 No Response";
    private static final String CONTENT_LENGTH = "Content-Length: ";
    private static final String CONTENT_TYPE_TEXT_HTML = "text/html";
    private static final String CONNECTION_CLOSE = "Connection: close";

    private static void sendHTTPResponse(PrintWriter out, String result) {

        out.println(result == null ? HTTP_NO_RESPONSE : HTTP_RESPONSE);
        if (result != null) {
            out.print(CONTENT_TYPE + CONTENT_TYPE_TEXT_HTML);
            out.print(CRNL);
            out.print(CONTENT_LENGTH + (result.length()));
            out.print(CRNL);
            out.print(CONNECTION_CLOSE);
            out.print(CRNL);
            out.print(CRNL);
            out.print(result);
            out.print(CRNL);
        }
        out.close();
    }

    private void closeSockets() {
        if (clientSocket != null) {
            try {
                clientSocket.close();
                clientSocket = null;
            } catch (IOException e) {
                log.error("Error closing clientSocket", e);
            }
        }

        if (serverSocket != null) {
            try {
                serverSocket.close();
                serverSocket = null;
            } catch (IOException e) {
                log.error("Error closing server socket", e);
            }
        }
    }


    /**
     * Process an http get request.
     */

    private void processGet(String command, Map<String, String> params, CommandExecutor cmdExe, PrintWriter out) throws IOException {

        final Frame mainFrame = Application.get();

        // Trick to force window to front, the setAlwaysOnTop works on a Mac,  toFront() does nothing.
        mainFrame.toFront();
        mainFrame.setAlwaysOnTop(true);
        mainFrame.setAlwaysOnTop(false);

        if (command.equals("/load")) {
            String file = params.get("file");

            if (file != null) {
                cmdExe.execute(new String[]{"load", file}, out);
            } else {
                out.println("ERROR Parameter \"file\" is required");
            }
        } else {
            out.println("ERROR Unknown command: " + command);
        }
        out.flush();
    }

    /**
     * Parse the html parameter string into a set of key-value pairs.  Parameter values are
     * url decoded with the exception of the "locus" parameter.
     *
     * @param parameterString
     * @return
     */

    private Map<String, String> parseParameters(String parameterString) {

        // Do a partial decoding now (ampersands only)
        parameterString = parameterString.replace("&amp;", "&");

        HashMap<String, String> params = new HashMap();
        String[] kvPairs = parameterString.split("&");
        for (String kvString : kvPairs) {
            // Split on the first "=",  all others are part of the parameter value
            String[] kv = kvString.split("=", 2);
            if (kv.length == 1) {
                params.put(kv[0], null);
            } else {
                String key = decodeURL(kv[0]);
                String value = decodeURL(kv[1]);
                params.put(kv[0], value);
            }
        }
        return params;

    }

    /**
     * Decode according to UTF-8. In the extremely unlikely
     * event that we are running on a platform which does not
     * support UTF-8 (it's part of the Java spec), URLDecoder.decode
     * is used.
     *
     * @param s
     * @return
     */

    private static String decodeURL(String s) {
        if (s == null) {
            return null;
        }
        try {
            return URLDecoder.decode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return URLDecoder.decode(s);
        }
    }

    /**
     * Check if the current open instance is the same version
     *
     * @return True if there are the same version, false if not the same version or if there is no other instance running.
     */
    private static boolean checkSameVersion(int port) {

        Socket socket = null;
        PrintWriter out = null;
        BufferedReader in = null;

        try {
            socket = new Socket("localhost", port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            VersionTool version = new VersionTool();

            out.println(version.getName());
            String otherInstanceVersion = in.readLine();

            in.close();
            out.close();

            if (otherInstanceVersion != null && otherInstanceVersion.equals(version.getVersion())) {
                return true;
            }

            return false;

        } catch (IOException e) {

            return false;

        } finally {

            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        }

    }
}
