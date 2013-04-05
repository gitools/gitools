/*
 * Copyright (c) 2007-2011 by The Broad Institute of MIT and Harvard.  All Rights Reserved.
 *
 * This software is licensed under the terms of the GNU Lesser General Public License (LGPL),
 * Version 2.1 which is available at http://www.opensource.org/licenses/lgpl-2.1.php.
 *
 * THE SOFTWARE IS PROVIDED "AS IS." THE BROAD AND MIT MAKE NO REPRESENTATIONS OR
 * WARRANTES OF ANY KIND CONCERNING THE SOFTWARE, EXPRESS OR IMPLIED, INCLUDING,
 * WITHOUT LIMITATION, WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, NONINFRINGEMENT, OR THE ABSENCE OF LATENT OR OTHER DEFECTS, WHETHER
 * OR NOT DISCOVERABLE.  IN NO EVENT SHALL THE BROAD OR MIT, OR THEIR RESPECTIVE
 * TRUSTEES, DIRECTORS, OFFICERS, EMPLOYEES, AND AFFILIATES BE LIABLE FOR ANY DAMAGES
 * OF ANY KIND, INCLUDING, WITHOUT LIMITATION, INCIDENTAL OR CONSEQUENTIAL DAMAGES,
 * ECONOMIC DAMAGES OR INJURY TO PROPERTY AND LOST PROFITS, REGARDLESS OF WHETHER
 * THE BROAD OR MIT SHALL BE ADVISED, SHALL HAVE OTHER REASON TO KNOW, OR IN FACT
 * SHALL KNOW OF THE POSSIBILITY OF THE FOREGOING.
 */

/*
 * Created by JFormDesigner on Sun Jun 05 19:25:45 EDT 2011
 */

package org.gitools.ui.genomespace;

import org.apache.log4j.Logger;
import org.gitools.ui.genomespace.dm.DMUtils;
import org.gitools.ui.genomespace.dm.GSDirectoryListing;
import org.gitools.ui.genomespace.dm.GSFileMetadata;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.dialog.MessageUtils;
import org.gitools.ui.settings.Settings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * @author Jim Robinson
 * @noinspection ALL
 */
public class GSFileBrowser extends JDialog
{

    private GSDirectoryListing dirListing;

    public enum Mode
    {
        OPEN, SAVE
    }

    private static final Logger log = Logger.getLogger(GSFileBrowser.class);

    private static ImageIcon folderIcon;
    private static ImageIcon fileIcon;
    @Nullable
    private static GSFileMetadata selectedFile;

    private Mode mode = Mode.OPEN;
    @Nullable
    private String userRootUrl = null;

    private GSFileBrowser(Frame owner) throws IOException, JSONException
    {
        this(owner, Mode.OPEN);
    }

    public GSFileBrowser(Frame owner, Mode mode) throws IOException, JSONException
    {
        super(owner);
        setModal(true);
        initComponents();
        init(mode);
        // For now single selection
        this.fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.mode = mode;
        savePanel.setVisible(mode == Mode.SAVE);
        newFolderButton.setVisible(mode == Mode.SAVE);
        openButton.setText(mode == Mode.OPEN ? "Open" : "Save");
        getRootPane().setDefaultButton(openButton);


    }

    void init(Mode mode) throws JSONException, IOException
    {

        if (folderIcon == null)
        {
            folderIcon = new ImageIcon(getClass().getResource("/img/Folder-icon.png"));
            fileIcon = new ImageIcon(getClass().getResource("/img/file-document.png"));
        }
        fileList.setCellRenderer(new CellRenderer());

        MouseListener mouseListener = new MouseAdapter()
        {
            public void mouseClicked(@NotNull MouseEvent e)
            {
                int index = fileList.locationToIndex(e.getPoint());
                GSFileMetadata md = (GSFileMetadata) fileList.getModel().getElementAt(index);
                setSelectedFile(md);
                if (e.getClickCount() == 2)
                {
                    if (md.isDirectory())
                    {
                        try
                        {
                            fetchContents(new URL(md.getUrl()));
                        } catch (IOException e1)
                        {
                            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        } catch (JSONException e1)
                        {
                            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
                    }
                    else
                    {
                        setVisible(false);
                    }
                }
            }
        };
        fileList.addMouseListener(mouseListener);

        String rootdirectory = mode == Mode.OPEN ? DMUtils.DEFAULT_DIRECTORY : DMUtils.PERSONAL_DIRECTORY;
        URL defaultURL = new URL(GSUtils.DEFAULT_GS_DM_SERVER + rootdirectory);
        fetchContents(defaultURL);
    }

    private void setSelectedFile(@NotNull GSFileMetadata md)
    {
        selectedFile = md;
        if (md.isDirectory())
        {
            selectedFileTextField.setText(null);
        }
        else
        {
            selectedFileTextField.setText(md.getName());
        }
    }

    @Nullable
    public String getFileURL()
    {
        return selectedFile == null ? null : selectedFile.getUrl();
    }

    @Nullable
    public String getPath()
    {
        if (selectedFile == null)
        {
            return null;
        }
        if (selectedFile.isDirectory())
        {
            if (mode == Mode.OPEN)
            {
                return null;
            }
            else
            {
                return selectedFile.getPath() + "/" + selectedFileTextField.getText();
            }
        }
        else
        {
            return selectedFile.getPath();
        }
    }


    private void fetchContents(URL url) throws IOException, JSONException
    {

        dirListing = DMUtils.getDirectoryListing(url);
        String dirUrlString = dirListing.getDirectory().getUrl();

        setTitle(dirUrlString);
        if (userRootUrl == null)
        {
            userRootUrl = dirUrlString;
            // A little trick to get the root meta data
            int idx = userRootUrl.lastIndexOf("/");
            String user = userRootUrl.substring(idx).replace("/", "");
            GSFileMetadata rootMD = new GSFileMetadata(".", "/users/" + user, userRootUrl, "", "", true);
            setSelectedFile(rootMD);
        }

        List<GSFileMetadata> elements = dirListing.getContents();
        //Unless this is the root directory create a "up-one-level" entry
        if (!dirUrlString.equals(userRootUrl))
        {
            int lastSlashIdx = dirUrlString.lastIndexOf("/");
            String parentURL = dirUrlString.substring(0, lastSlashIdx);
            elements.add(0, new GSFileMetadata("Parent Directory", "", parentURL, "", "", true));
        }

        ListModel model = new ListModel(dirListing.getContents());
        fileList.setModel(model);

    }

    private void cancelButtonActionPerformed(ActionEvent e)
    {
        selectedFile = null;
        setVisible(false);
        dispose();
    }

    private void logoutButtonActionPerformed(ActionEvent e)
    {
        selectedFile = null;
        setVisible(false);
        dispose();
        GSUtils.logout();
        if (MessageUtils.confirm(AppFrame.get(), "You must shutdown Gitools to complete the GenomeSpace logout. Shutdown now?"))
        {
            Settings.getDefault().save();
            System.exit(0);
        }
    }

    private void loadButtonActionPerformed(ActionEvent e)
    {

        try
        {
            Object[] selections = fileList.getSelectedValues();

            // If there is a single selection, and it is a directory,  load that directory
            if (selections.length == 1)
            {
                GSFileMetadata md = (GSFileMetadata) selections[0];
                if (md.isDirectory())
                {
                    fetchContents(new URL(md.getUrl()));
                    return;
                }
            }

            for (Object obj : selections)
            {
                if (obj instanceof GSFileMetadata)
                {
                    GSFileMetadata md = (GSFileMetadata) obj;
                    if (mode == Mode.OPEN)
                    {
                        if (!md.isDirectory())
                        {
                            selectedFile = md;
                        }
                    }
                }
            }

            setVisible(false);
            dispose();

        } catch (Exception e1)
        {
            log.error("Error loading GS files", e1);
            MessageUtils.showMessage(AppFrame.get(), "Error: " + e1.toString());
        }
    }

    private void newFolderButtonActionPerformed(ActionEvent e)
    {
        String folderName = MessageUtils.showInputDialog(AppFrame.get(), "Name of new folder:");
        if (folderName != null && folderName.length() > 0)
        {
            String dirurl = selectedFile.getUrl();
            if (!selectedFile.isDirectory())
            {
                // Strip off file part
                int idx = dirurl.lastIndexOf("/");
                dirurl = dirurl.substring(0, idx);
            }
            String putURL = dirurl + "/" + folderName;
            try
            {
                GSFileMetadata metaData = DMUtils.createDirectory(putURL);
                if (metaData != null)
                {
                    setSelectedFile(metaData);
                }
                // Refresh
                fetchContents(new URL(putURL));
            } catch (IOException e1)
            {
                log.error("Error creating directory: " + putURL, e1);
                MessageUtils.showMessage(AppFrame.get(), "<html>Error creating directory: " + e1 + "<br>" + e1.getMessage());
            } catch (JSONException e1)
            {
                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

    }


    static class ListModel extends AbstractListModel
    {

        final List<GSFileMetadata> elements;

        ListModel(List<GSFileMetadata> elements)
        {
            this.elements = elements;
        }

        public int getSize()
        {
            return elements.size();
        }

        public Object getElementAt(int i)
        {
            return elements.get(i);
        }

    }

    private static class CellRenderer extends JLabel implements ListCellRenderer
    {
        // This is the only method defined by ListCellRenderer.
        // We just reconfigure the JLabel each time we're called.

        @NotNull
        public Component getListCellRendererComponent(@NotNull JList list, @NotNull Object value,            // value to display
                                                      int index,               // cell index
                                                      boolean isSelected,      // is the cell selected
                                                      boolean cellHasFocus)    // the list and the cell have the focus
        {
            GSFileMetadata fileElement = (GSFileMetadata) value;

            String s = value.toString();
            setText(s);
            setIcon(fileElement.isDirectory() ? folderIcon : fileIcon);
            if (isSelected)
            {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            }
            else
            {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            setEnabled(list.isEnabled());
            setFont(list.getFont());
            setOpaque(true);
            return this;
        }
    }

    private void initComponents()
    {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        dialogPane = new JPanel();
        buttonBar = new JPanel();
        hSpacer2 = new JPanel(null);
        newFolderButton = new JButton();
        hSpacer1 = new JPanel(null);
        cancelButton = new JButton();
        openButton = new JButton();
        logoutButton = new JButton();
        savePanel = new JPanel();
        label2 = new JLabel();
        selectedFileTextField = new JTextField();
        splitPane1 = new JPanel();
        scrollPane1 = new JScrollPane();
        fileList = new JList();
        label1 = new JLabel();

        //======== this ========
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setLayout(new BorderLayout());

            //======== buttonBar ========
            {
                buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                buttonBar.setLayout(new BoxLayout(buttonBar, BoxLayout.X_AXIS));
                buttonBar.add(hSpacer2);

                //---- newFolderButton ----
                newFolderButton.setText("New Folder");
                newFolderButton.addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        newFolderButtonActionPerformed(e);
                    }
                });
                buttonBar.add(newFolderButton);
                buttonBar.add(hSpacer1);

                //---- cancelButton ----
                cancelButton.setText("Cancel");
                cancelButton.addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        cancelButtonActionPerformed(e);
                    }
                });
                buttonBar.add(cancelButton);

                //---- openButton ----
                openButton.setText("Open");
                openButton.addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        loadButtonActionPerformed(e);
                    }
                });
                buttonBar.add(openButton);

                //---- logoutButton ----
                logoutButton.setText("Logout");
                logoutButton.addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        logoutButtonActionPerformed(e);
                    }
                });
                buttonBar.add(logoutButton);
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);

            //======== savePanel ========
            {
                savePanel.setVisible(false);
                savePanel.setLayout(new BoxLayout(savePanel, BoxLayout.X_AXIS));

                //---- label2 ----
                label2.setText("Save As: ");
                savePanel.add(label2);
                savePanel.add(selectedFileTextField);
            }
            dialogPane.add(savePanel, BorderLayout.NORTH);

            //======== splitPane1 ========
            {
                splitPane1.setLayout(new BoxLayout(splitPane1, BoxLayout.Y_AXIS));

                //======== scrollPane1 ========
                {
                    scrollPane1.setViewportView(fileList);
                }
                splitPane1.add(scrollPane1);

                //---- label1 ----
                label1.setHorizontalAlignment(SwingConstants.CENTER);
                label1.setIcon(new ImageIcon(getClass().getResource("/img/genomespacelogo.png")));
                splitPane1.add(label1);
            }
            dialogPane.add(splitPane1, BorderLayout.CENTER);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        setSize(490, 530);
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel dialogPane;
    private JPanel buttonBar;
    @Nullable
    private JPanel hSpacer2;
    private JButton newFolderButton;
    @Nullable
    private JPanel hSpacer1;
    private JButton cancelButton;
    private JButton openButton;
    private JButton logoutButton;
    private JPanel savePanel;
    private JLabel label2;
    private JTextField selectedFileTextField;
    private JPanel splitPane1;
    private JScrollPane scrollPane1;
    private JList fileList;
    private JLabel label1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
