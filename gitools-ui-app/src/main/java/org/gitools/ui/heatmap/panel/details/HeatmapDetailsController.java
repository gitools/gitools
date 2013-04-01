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
package org.gitools.ui.heatmap.panel.details;

import org.apache.velocity.VelocityContext;
import org.gitools.analysis.combination.CombinationResult;
import org.gitools.analysis.correlation.CorrelationResult;
import org.gitools.analysis.overlapping.OverlappingResult;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapDim;
import org.gitools.idtype.IdType;
import org.gitools.idtype.IdTypeManager;
import org.gitools.idtype.UrlLink;
import org.gitools.matrix.model.AnnotationMatrix;
import org.gitools.matrix.model.AnnotationResolver;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.element.IElementAdapter;
import org.gitools.matrix.model.element.IElementAttribute;
import org.gitools.stats.test.results.BinomialResult;
import org.gitools.stats.test.results.FisherResult;
import org.gitools.stats.test.results.ZScoreResult;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.panel.TemplatePanel;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.settings.Settings;
import org.gitools.ui.view.entity.EntityController;
import org.gitools.utils.colorscale.impl.PValueColorScale;
import org.gitools.utils.colorscale.impl.ZScoreColorScale;
import org.gitools.utils.formatter.GenericFormatter;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.gitools.utils.textpatt.TextPattern;

import javax.swing.*;
import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.*;

public class HeatmapDetailsController implements EntityController
{

    private static final String defaultTemplateName = "/vm/details/noselection.vm";
    private static final String headerTemplateName = "/vm/details/header.vm";
    private static final String heatmapTemplateName = "/vm/details/heatmap.vm";

    private TemplatePanel templatePanel;

    protected TemplatePanel getTemplatePanel()
    {
        if (templatePanel == null)
        {
            Properties props = new Properties();
            //props.put(VelocityEngine.VM_LIBRARY, "/vm/details/common.vm");
            templatePanel = new TemplatePanel(props)
            {
                @Override
                protected void performUrlAction(String name, Map<String, String> params)
                {
                    HeatmapDetailsController.this.performUrlAction(name, params);
                }
            };
        }
        return templatePanel;
    }

    private void performUrlAction(String name, Map<String, String> params)
    {

        // Open IGV
        if ("action:igv".equals(name))
        {
            IgvCommand igv = new IgvCommand(params);
            JobThread.execute(AppFrame.get(), igv);
        }

    }

    @Override
    public JComponent getComponent(Object ctx)
    {
        TemplatePanel panel = getTemplatePanel();

        final Heatmap heatmap = (Heatmap) ctx;
        final IMatrixView matrixView = heatmap.getMatrixView();
        int row = matrixView.getLeadSelectionRow();
        int[] rows = matrixView.getSelectedRows();
        int rowCount = matrixView.getRowCount();
        int column = matrixView.getLeadSelectionColumn();
        int columns[] = matrixView.getSelectedColumns();
        int columnCount = matrixView.getColumnCount();

        if (rows.length == 0 && row >= 0)
        {
            rows = new int[]{row};
        }

        if (columns.length == 0 && column >= 0)
        {
            columns = new int[]{column};
        }

        VelocityContext context = new VelocityContext();
        context.put("fmt", new GenericFormatter());

        String templateName = heatmapTemplateName;

        context.put("rowIndex", row + 1);
        context.put("columnIndex", column + 1);
        context.put("title", heatmap.getTitle());
        context.put("notes", heatmap.getDescription());
        context.put("attributes", heatmap.getAttributes());
        context.put("numColumns", matrixView.getColumnCount());
        context.put("numRows", matrixView.getRowCount());

        // Cell related properties
        String igvColumnLabel = null;
        String igvRowLabel = null;

        if (column >= 0 && column < columnCount && row >= 0 && row < rowCount)
        {

            final Object columnId = matrixView.getColumnLabel(column);
            final Object columnLabel = heatmap.getColumnLabel(column);

            final Object rowId = matrixView.getRowLabel(row);
            final Object rowLabel = heatmap.getRowLabel(row);

            final IElementAdapter cellAdapter = matrixView.getCellAdapter();
            final Object cellElement = matrixView.getCell(row, column);

            templateName = getTemplateNameFromObject(cellElement);

            if (templateName != null)
            {
                context.put("zscoreScale", new ZScoreColorScale()); //FIXME
                context.put("pvalueScale", new PValueColorScale()); //FIXME

                context.put("columnId", columnId);
                context.put("columnLabel", columnLabel);
                context.put("columnElement", columnLabel); //FIXME deprecated

                context.put("rowId", rowId);
                context.put("rowLabel", rowLabel);
                context.put("rowElement", rowLabel); //FIXME deprecated

                context.put("cellAdapter", cellAdapter);
                context.put("cellElement", cellElement);

                final List<IElementAttribute> properties =
                        cellAdapter.getProperties();

                final Map<String, Object> cellMap = new HashMap<String, Object>();

                final Map<String, IElementAttribute> attrMap =
                        new HashMap<String, IElementAttribute>();

                for (int index = 0; index < properties.size(); index++)
                {
                    final IElementAttribute prop = properties.get(index);
                    cellMap.put(prop.getId(),
                            cellAdapter.getValue(cellElement, index));

                    attrMap.put(prop.getId(), prop);
                }

                context.put("cell", cellMap);
                context.put("attr", attrMap);
            }
        }


        // Annotations properties
        String name = "";
        List<AnnotationMatrix.Annotation> annotations = new ArrayList<AnnotationMatrix.Annotation>(0);
        Map<String, String> links = new LinkedHashMap<String, String>();

        // Add IGV link
        if (Settings.getDefault().isShowIGVLink())
        {
            if (rows.length > 0 || columns.length > 0)
            {

                igvRowLabel = "";
                igvColumnLabel = "";

                for (int i : rows)
                    igvRowLabel = igvRowLabel + heatmap.getMatrixView().getRowLabel(i) + " ";

                for (int i : columns)
                    igvColumnLabel = igvColumnLabel + heatmap.getMatrixView().getColumnLabel(i) + " ";

                String id = (rows.length > 1 || columns.length > 1) ? "Ids" : "Id";

                final String rowLink = "action:igv?" + (row >= 0 ? "row=" + igvRowLabel + "&" : "");
                final String completeLink = rowLink; /* + (column>=0 ? "column=" + igvColumnLabel : "");*/
                if (row >= 0)
                {
                    links.put("Locate " + id + " in genomic viewer (IGV)", completeLink);
                }
            }
        }

        if (row >= 0 && row < rowCount)
        {
            HeatmapDim rowDim = heatmap.getRowDim();

            name = heatmap.getRowLabel(row);
            String label = heatmap.getMatrixView().getRowLabel(row);


            AnnotationMatrix annMatrix = rowDim.getAnnotations();
            if (annMatrix != null)
            {
                annotations = annMatrix.getAnnotations(label);
            }

            IdType idType = rowDim.getIdType();
            if (idType != null)
            {
                String idTypeKey = idType.getKey();
                List<UrlLink> tlinks = IdTypeManager.getDefault().getLinks(idTypeKey);
                AnnotationResolver resolver = new AnnotationResolver(annMatrix, label);
                for (UrlLink link : tlinks)
                {
                    TextPattern pat = link.getPattern();
                    String url = pat.generate(resolver);
                    links.put(link.getName() + " (" + label + ")", url);
                }
            }
        }

        if (column >= 0 && column < columnCount)
        {
            HeatmapDim colDim = heatmap.getColumnDim();

            name = heatmap.getColumnLabel(column);
            String label = heatmap.getMatrixView().getColumnLabel(column);
            AnnotationMatrix annMatrix = colDim.getAnnotations();
            if (annMatrix != null)
            {
                annotations = annMatrix.getAnnotations(label);
            }

            IdType idType = colDim.getIdType();
            if (idType != null)
            {
                String idTypeKey = idType.getKey();
                List<UrlLink> tlinks = IdTypeManager.getDefault().getLinks(idTypeKey);
                AnnotationResolver resolver = new AnnotationResolver(annMatrix, label);
                for (UrlLink link : tlinks)
                {
                    TextPattern pat = link.getPattern();
                    String url = pat.generate(resolver);
                    links.put(link.getName() + " (" + label + ")", url);
                }
            }
        }


        context.put("name", name);
        context.put("annotations", annotations);
        context.put("links", links);


        try
        {
            panel.setTemplateFromResource(templateName);
            panel.setContext(context);
            panel.render();


        } catch (Exception ex)
        {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            pw.close();
            return new JLabel(sw.toString());
        }

        return panel;
    }

    private String getTemplateNameFromObject(Object object)
    {
        String templateName = "generic.vm";
        if (object != null)
        {
            if (object instanceof BinomialResult)
            {
                templateName = "binomial.vm";
            }
            else if (object instanceof FisherResult)
            {
                templateName = "fisher.vm";
            }
            else if (object instanceof ZScoreResult)
            {
                templateName = "zscore.vm";
            }
            else if (object instanceof CorrelationResult)
            {
                templateName = "correlation.vm";
            }
            else if (object instanceof CombinationResult)
            {
                templateName = "combination.vm";
            }
            else if (object instanceof OverlappingResult)
            {
                templateName = "overlapping.vm";
            }
        }
        return "/vm/details/" + templateName;
    }


    private class IgvCommand implements JobRunnable
    {

        private Map<String, String> params;

        private IgvCommand(Map<String, String> params)
        {
            this.params = params;
        }

        @Override
        public void run(IProgressMonitor monitor)
        {

            Socket socket = null;
            try
            {

                monitor.title("Connecting with Integrative Genomics Viewer");

                String igvUrl[] = Settings.getDefault().getIgvUrl().replace("http://", "").split(":");
                socket = new Socket(igvUrl[0], Integer.valueOf(igvUrl[1]));
                System.out.println();
                socket.setSoTimeout(30000);

                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                InputStream in = socket.getInputStream();

                String cmd;
                if (params.containsKey("column"))
                {
                    monitor.info("Locating " + params.get("column"));
                    cmd = "goto " + params.get("column");
                    out.println(cmd);
                    waitServerResponse(in, monitor);
                }

                if (params.containsKey("row"))
                {
                    monitor.info("Locating " + params.get("row"));
                    cmd = "goto " + params.get("row");
                    out.println(cmd);
                    waitServerResponse(in, monitor);
                }


            } catch (ConnectException e)
            {
                monitor.end();
                showMessage("Unable to connect with Integrative Genomics Viewer (IGV). " +
                        "\n It must be running on '" + Settings.getDefault().getIgvUrl() + "'. " +
                        "\n Install or launch it from 'http://www.broadinstitute.org/igv'.");
            } catch (SocketTimeoutException e)
            {
                monitor.end();
                showMessage("Timeout connecting with Integrative Genomics Viewer (IGV) on '" + Settings.getDefault().getIgvUrl() + "'. ");
            } catch (IOException e)
            {
                monitor.end();
                showMessage("Unknown problem 'e.getMessage()' connecting with Integrative Genomics Viewer (IGV). Check Gitools help.");
            } catch (UnsupportedOperationException e)
            {
            } finally
            {
                monitor.end();
                if (socket != null)
                {
                    try
                    {
                        socket.close();
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }


        }

        private void waitServerResponse(InputStream in, IProgressMonitor monitor) throws UnsupportedOperationException, IOException
        {
            while (in.available() == 0)
            {
                try
                {
                    Thread.sleep(1000);
                } catch (Exception ee)
                {
                }
                if (monitor.isCancelled())
                {
                    throw new UnsupportedOperationException("User cancel");
                }
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            reader.readLine();
        }

        private void showMessage(String msg)
        {
            AppFrame frame = AppFrame.get();
            JOptionPane.showMessageDialog(frame, msg);
        }
    }
}
