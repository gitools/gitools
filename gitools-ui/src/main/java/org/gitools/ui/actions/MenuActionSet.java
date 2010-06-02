package org.gitools.ui.actions;

import org.gitools.ui.platform.actions.ActionSet;
import org.gitools.ui.platform.actions.BaseAction;

import javax.swing.JMenuBar;
import org.gitools.ui.IconNames;
import org.gitools.ui.platform.IconUtils;

import org.gitools.ui.platform.actions.ActionSetUtils;

public class MenuActionSet extends ActionSet {

	private static final long serialVersionUID = -7702905459240675073L;

	public MenuActionSet() {
		super(new BaseAction[] {
			new ActionSet("File", new BaseAction[] {
				new ActionSet("New", new BaseAction[] {
					//FileActionSet.newProjectAction,
					new ActionSet("Analysis", new BaseAction[] {
						FileActions.newEnrichmentAnalysisAction,
						FileActions.newOncozAnalysisAction,
						FileActions.newCorrelationAnalysisAction,
						FileActions.newCombinationAnalysisAction
					})
				}),
				new ActionSet("Open", new BaseAction[] {
					//FileActions.openProjectAction,
					FileActions.openEnrichmentAnalysisAction,
					FileActions.openHeatmapFromMatrixAction
				}),
				BaseAction.separator,
				FileActions.saveAction,
				FileActions.saveAsAction,
				BaseAction.separator,
				FileActions.closeAction,
				BaseAction.separator,
				new ActionSet("Import", new BaseAction[] {
					new ActionSet("IntOGen", IconUtils.getImageIconResource(IconNames.intogen16), new BaseAction[] {
						FileActions.importIntogenTableAction,
						FileActions.importIntogenOncomodulesAction,
						//FileActions.importIntogenHeatmapAction
					}),
					new ActionSet("BioMart", IconUtils.getImageIconResource(IconNames.biomart16), new BaseAction[] {
						FileActions.importBioMartTableAction,
						FileActions.importBioMartModulesAction
						//FileActions.importBioMartMatrixAction
					})
				}),
				//FileActionSet.exportWizardAction,
				new ActionSet("Export", new BaseAction[] {
					FileActions.exportLabelNamesAction,
					FileActions.exportMatrixAction,
					FileActions.exportTableAction,
					FileActions.exportHeatmapImageAction,
					FileActions.exportScaleImageAction,
					FileActions.exportHeatmapHtmlAction,
					FileActions.exportPdfReportAction
				}),
				BaseAction.separator,
				FileActions.exitAction
			}),
			new ActionSet("Edit", new BaseAction[] {
					EditActions.selectAllAction,
					EditActions.unselectAllAction,
					EditActions.invertSelectionAction
			}),
			new ActionSet("Data", new BaseAction[] {
				new ActionSet("Filter", new BaseAction[] {
					DataActions.filterByLabelAction,
					DataActions.filterByValueAction
				}),
				new ActionSet("Sort", new BaseAction[] {
					DataActions.sortByLabelAction,
					DataActions.sortByValueAction
				}),
				new ActionSet("Move", new BaseAction[] {
					DataActions.moveRowsUpAction,
					DataActions.moveRowsDownAction,
					DataActions.moveColsLeftAction,
					DataActions.moveColsRightAction
				}),
				new ActionSet("Visibility", new BaseAction[] {
					DataActions.showAllRowsAction,
					DataActions.hideSelectedRowsAction,
					DataActions.showAllColumnsAction,
					DataActions.hideSelectedColumnsAction
				}),
				DataActions.clusteringAction
			}),
			new ActionSet("Analysis", new BaseAction[] {
				AnalysisActions.correlations,
				AnalysisActions.combinations,
//				new ActionSet("Clustering", new BaseAction[] {
//					AnalysisActions.clusteringAction
//				}),
				new ActionSet("MTC", new BaseAction[] {
					AnalysisActions.mtcBonferroniAction,
					AnalysisActions.mtcBenjaminiHochbergFdrAction,
					AnalysisActions.mtcBenjaminiYekutieliFdrAction
				}),
			}),
			new ActionSet("Help", new BaseAction[] {
				HelpActions.welcomeAction,
				BaseAction.separator,
				HelpActions.aboutAction
			})
		});
	}
	
	public JMenuBar createMenuBar() {
		return ActionSetUtils.createMenuBar(this);
	}
}
