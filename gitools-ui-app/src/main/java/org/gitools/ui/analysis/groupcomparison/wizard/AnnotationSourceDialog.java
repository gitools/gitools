package org.gitools.ui.analysis.groupcomparison.wizard;

import org.gitools.analysis.groupcomparison.DimensionGroups.DimensionGroupAnnotation;
import org.gitools.core.heatmap.HeatmapDimension;
import org.gitools.ui.platform.dialog.AbstractDialog;
import org.gitools.ui.platform.dialog.MessageStatus;
import org.gitools.ui.wizard.common.PatternSourcePage;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mschroeder on 12/11/13.
 */
public class AnnotationSourceDialog extends AbstractDialog {

    private final PatternSourcePage page;

    protected AnnotationSourceDialog(Window owner, String title, Icon icon, HeatmapDimension hdim) {
        super(owner, title, icon);
        page = new PatternSourcePage(hdim, true);
        setModalityType(ModalityType.APPLICATION_MODAL);
        setMinimumSize(new Dimension(700, 500));
        setContainer(this.page);
        createComponents("Choose annotation source", "", MessageStatus.INFO, null);
        page.updateControls();

    }

    public List<DimensionGroupAnnotation> getGroups() {
        for (String s : page.getSelectedValues()) {
            System.out.println(s);
        }

        //TODO: get groups!
        return null;
    }

    @Override
    protected JComponent createContainer() {
        return page;
    }

    @Override
    protected List<JButton> createButtons() {
        List<JButton> buttons = new ArrayList<>();

        JButton okButton = new JButton("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doClose(RET_OK);
            }
        });
        buttons.add(okButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doClose(RET_CANCEL);
            }
        });
        buttons.add(cancelButton);

        return buttons;
    }
}
