package org.gitools.ui.heatmap.header.wizard.heatmapheader;

import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.list.SelectionInList;
import org.gitools.heatmap.header.HeatmapDataHeatmapHeader;
import org.gitools.ui.heatmap.panel.settings.decorators.DecoratorPanel;
import org.gitools.ui.heatmap.panel.settings.decorators.DecoratorPanelContainer;
import org.gitools.ui.heatmap.panel.settings.decorators.DecoratorPanels;
import org.gitools.ui.platform.wizard.AbstractWizardPage;
import org.gitools.ui.settings.decorators.SaveDecoratorDialog;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class NewColorScalePage extends AbstractWizardPage {
    private JPanel mainPanel;
    private JComboBox decoratorPanelSelector;
    private JLabel colorScaleSave;
    private JLabel colorScaleOpen;
    private JPanel decoratorPanels;


    public NewColorScalePage(HeatmapDataHeatmapHeader header) {
        super();

        // Bind color scale controls
        DecoratorPanels decorators = new DecoratorPanels();
        DecoratorPanelContainer decoratorsPanels = (DecoratorPanelContainer) this.decoratorPanels;

        //TODO
        //decoratorsPanels.init(decorators, heatmap);
        Bindings.bind(decoratorPanelSelector, new SelectionInList<DecoratorPanel>(
                decorators,
                decoratorsPanels.getCurrentPanelModel()
        ));

        colorScaleSave.setCursor(new Cursor(Cursor.HAND_CURSOR));
        colorScaleSave.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //TODO
                SaveDecoratorDialog.actionSaveDecorator(null);
            }
        });

        colorScaleOpen.setCursor(new Cursor(Cursor.HAND_CURSOR));
        colorScaleOpen.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //TODO
                SaveDecoratorDialog.actionLoadDecorator(null);
            }
        });
    }

    @Nullable
    @Override
    public JComponent createControls() {
        return mainPanel;
    }

    private void createUIComponents() {
        this.decoratorPanels = new DecoratorPanelContainer();
    }
}
