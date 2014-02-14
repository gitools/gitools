package org.gitools.ui.app.fileimport.wizard.text;

import javax.activation.ActivationDataFlavor;
import javax.activation.DataHandler;
import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

class ListItemTransferHandler extends TransferHandler {
    private final DataFlavor localObjectFlavor;
    private JList source = null;
    private int[] indices = null;
    private int addIndex = -1; //Location where items were added
    private int addCount = 0;  //Number of items added.

    public ListItemTransferHandler() {
        super();
        localObjectFlavor = new ActivationDataFlavor(Object[].class, DataFlavor.javaJVMLocalObjectMimeType, "Array of items");
    }

    @Override
    protected Transferable createTransferable(JComponent c) {
        source = (JList) c;
        indices = source.getSelectedIndices();
        @SuppressWarnings("deprecation") Object[] transferedObjects = source.getSelectedValues();
        return new DataHandler(transferedObjects, localObjectFlavor.getMimeType());
    }

    @Override
    public boolean canImport(TransferSupport info) {
        return info.isDrop() && info.isDataFlavorSupported(localObjectFlavor);
    }

    @Override
    public int getSourceActions(JComponent c) {
        return MOVE; //TransferHandler.COPY_OR_MOVE;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean importData(TransferSupport info) {
        if (!canImport(info)) {
            return false;
        }
        JList target = (JList) info.getComponent();
        JList.DropLocation dl = (JList.DropLocation) info.getDropLocation();
        DefaultListModel listModel = (DefaultListModel) target.getModel();
        int index = dl.getIndex();
        //boolean insert = dl.isInsert();
        int max = listModel.getSize();
        if (index < 0 || index > max) {
            index = max;
        }
        addIndex = index;

        try {
            Object[] values = (Object[]) info.getTransferable().getTransferData(localObjectFlavor);
            target.setSelectedIndices(new int[0]);
            for (int i = 0; i < values.length; i++) {
                int idx = index++;
                listModel.add(idx, values[i]);
                target.addSelectionInterval(idx, idx);
            }
            addCount = source.equals(target) ? values.length : 0;
            return true;
        } catch (UnsupportedFlavorException ufe) {
            ufe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return false;
    }

    @Override
    protected void exportDone(JComponent c, Transferable data, int action) {
        cleanup(c, action == MOVE);
    }

    private void cleanup(JComponent c, boolean remove) {
        if (remove && indices != null) {
            //If we are moving items around in the same list, we
            //need to adjust the indices accordingly, since those
            //after the insertion point have moved.
            if (addCount > 0) {
                for (int i = 0; i < indices.length; i++) {
                    if (indices[i] >= addIndex) {
                        indices[i] += addCount;
                    }
                }
            }
            JList source = (JList) c;
            DefaultListModel model = (DefaultListModel) source.getModel();
            for (int i = indices.length - 1; i >= 0; i--) {
                model.remove(indices[i]);
            }
        }
        indices = null;
        addCount = 0;
        addIndex = -1;
    }
}