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
package org.gitools.ui.app.analysis.editor;

import org.apache.velocity.VelocityContext;
import org.gitools.analysis.Analysis;
import org.gitools.api.persistence.FileFormat;
import org.gitools.ui.app.IconNames;
import org.gitools.ui.app.utils.LogUtils;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.ResourceEditor;
import org.gitools.ui.platform.panel.TemplatePanel;
import org.gitools.utils.formatter.HeatmapTextFormatter;
import org.lobobrowser.html.FormInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.net.URL;
import java.util.Map;

public abstract class AnalysisEditor<A extends Analysis> extends ResourceEditor<A> {
    private static final Logger log = LoggerFactory.getLogger(AnalysisEditor.class);

    private final String template;
    private TemplatePanel templatePanel;
    private String formatExtension;

    protected AnalysisEditor(A analysis, String template, String formatExtension) {
        super(analysis);

        this.formatExtension = formatExtension;
        this.template = template;

        setIcon(IconUtils.getIconResource(IconNames.LOGO_ANALYSIS_DETAILS16));

        createComponents();
    }

    @Override
    protected FileFormat[] getFileFormats() {
        return new FileFormat[]{
                new FileFormat("Single file (*." + formatExtension + ".zip)", formatExtension + ".zip", false, false),
                new FileFormat("Multiple files (*." + formatExtension + ")", formatExtension, false, false)
        };
    }

    protected String getFormatExtension() {
        return formatExtension;
    }

    private void createComponents() {
        templatePanel = new TemplatePanel() {
            @Override
            protected void submitForm(String method, URL action, String target, String enctype, FormInput[] formInputs) throws LinkVetoException {
                AnalysisEditor.this.submitForm(method, action, target, enctype, formInputs);
            }

            @Override
            protected void performAction(String name, Map<String, String> params) {
                AnalysisEditor.this.performUrlAction(name, params);
            }
        };
        try {
            URL url = getClass().getResource(template);
            templatePanel.setTemplateFromResource(template, url);

            VelocityContext context = new VelocityContext();
            context.put("fmt", HeatmapTextFormatter.TWO_DECIMALS);
            context.put("analysis", getModel());

            prepareContext(context);

            templatePanel.render(context);
        } catch (Exception e) {
            LogUtils.logException(e, log);
        }

        setLayout(new BorderLayout());

        add(templatePanel, BorderLayout.CENTER);
    }

    protected void prepareContext(VelocityContext context) {
    }

    @Override
    public void doVisible() {
        templatePanel.requestFocusInWindow();
    }

    void submitForm(String method, URL action, String target, String enctype, FormInput[] formInputs) {
    }

    protected abstract void performUrlAction(String name, Map<String, String> params);

}
