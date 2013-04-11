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
package org.gitools.ui.actions;

import org.gitools.ui.platform.actions.ActionSet;
import org.gitools.ui.platform.actions.ActionSetUtils;
import org.gitools.ui.platform.actions.BaseAction;

import javax.swing.*;

public class MenuActionSet extends ActionSet {

    private static final long serialVersionUID = -7702905459240675073L;

    public MenuActionSet() {
        super(new BaseAction[]{
                new ActionSet("File",
                        new BaseAction[]{
                                new ActionSet("New",
                                        new BaseAction[]{
                                                FileActions.newEnrichmentAnalysisAction,
                                                FileActions.newOncozAnalysisAction,
                                                FileActions.newCorrelationAnalysisAction,
                                                FileActions.newOverlapAnalysisAction,
                                                FileActions.newCombinationAnalysisAction
                                        }),
                                new ActionSet("Open",
                                        new BaseAction[]{
                                                FileActions.openAction,
                                                FileActions.openGenomeSpaceAction
                                        }),
                                BaseAction.separator,
                                FileActions.saveAction,
                                FileActions.saveAsAction,
                                BaseAction.separator,
                                new ActionSet("Import",
                                        new BaseAction[]{
                                                new ActionSet("Matrix",
                                                        new BaseAction[]{
                                                                FileActions.importExcelMatrixAction,
                                                                FileActions.importIntogenTableAction
                                                        }),
                                                new ActionSet("Modules",
                                                        new BaseAction[]{
                                                                FileActions.importIntogenOncomodulesAction,
                                                                FileActions.importKeggModulesAction,
                                                                FileActions.importGoModulesAction,
                                                                FileActions.importBioMartModulesAction
                                                        }),
                                                new ActionSet("Annotations",
                                                        new BaseAction[]{
                                                                FileActions.importBioMartTableAction
                                                        })}),
                                new ActionSet("Export",
                                        new BaseAction[]{
                                                FileActions.exportLabelNamesAction,
                                                FileActions.exportMatrixAction,
                                                FileActions.exportTableAction,
                                                FileActions.exportHeatmapImageAction,
                                                FileActions.exportScaleImageAction
                                        }),
                                BaseAction.separator,
                                FileActions.exitAction
                        }),
                new ActionSet("Edit",
                        new BaseAction[]{
                                EditActions.selectAllAction,
                                EditActions.unselectAllAction,
                                EditActions.invertSelectionAction
                        }),
                new ActionSet("Data",
                        new BaseAction[]{
                                new ActionSet("Filter",
                                        new BaseAction[]{
                                                DataActions.filterByLabelAction,
                                                DataActions.filterByValueAction
                                        }),
                                new ActionSet("Sort",
                                        new BaseAction[]{
                                                DataActions.sortByLabelAction,
                                                DataActions.sortByValueAction,
                                                DataActions.sortByMutualExclusionAction
                                        }),
                                new ActionSet("Move",
                                        new BaseAction[]{
                                                DataActions.moveRowsUpAction,
                                                DataActions.moveRowsDownAction,
                                                DataActions.moveColsLeftAction,
                                                DataActions.moveColsRightAction
                                        }),
                                new ActionSet("Visibility",
                                        new BaseAction[]{
                                                DataActions.showAllRowsAction,
                                                DataActions.hideSelectedRowsAction,
                                                DataActions.showAllColumnsAction,
                                                DataActions.hideSelectedColumnsAction
                                        }),
                                DataActions.clusteringByValueAction,
                        }),
                new ActionSet("Analysis",
                        new BaseAction[]{
                                AnalysisActions.correlations,
                                AnalysisActions.combinations,
                                AnalysisActions.overlapping,
                                AnalysisActions.groupComparison,
                                new ActionSet("MTC",
                                        new BaseAction[]{
                                                AnalysisActions.mtcBonferroniAction,
                                                AnalysisActions.mtcBenjaminiHochbergFdrAction
                                        }),
                        }),
                new ActionSet("Help",
                        new BaseAction[]{
                                HelpActions.welcomeAction,
                                BaseAction.separator,
                                HelpActions.showTipsAction,
                                HelpActions.aboutAction
                        })
        });
    }

    public JMenuBar createMenuBar() {
        return ActionSetUtils.createMenuBar(this);
    }
}
