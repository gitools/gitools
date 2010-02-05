/*
 *  Copyright 2010 chris.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.gitools.ui.actions;

import org.gitools.ui.platform.actions.ActionSet;
import org.gitools.stats.mtc.BenjaminiHochbergFdr;
import org.gitools.stats.mtc.Bonferroni;
import org.gitools.ui.actions.data.FilterByLabelAction;
import org.gitools.ui.actions.data.FilterByValueAction;
import org.gitools.ui.actions.data.HideSelectionAction;
import org.gitools.ui.actions.data.MoveSelectionAction;
import org.gitools.ui.actions.data.MtcAction;
import org.gitools.ui.actions.data.ShowAllAction;
import org.gitools.ui.actions.data.SortByValueAction;
import org.gitools.ui.actions.edit.InvertSelectionAction;
import org.gitools.ui.actions.edit.SelectAllAction;
import org.gitools.ui.actions.edit.UnselectAllAction;
import org.gitools.ui.actions.file.CloseAction;
import org.gitools.ui.actions.file.ExitAction;
import org.gitools.ui.actions.file.ExportHeatmapHtmlAction;
import org.gitools.ui.actions.file.ExportHeatmapImageAction;
import org.gitools.ui.actions.file.ExportLabelNamesAction;
import org.gitools.ui.actions.file.ExportMatrixAction;
import org.gitools.ui.actions.file.ExportTableAction;
import org.gitools.ui.actions.file.ImportBioMartTableAction;
import org.gitools.ui.actions.file.ImportBiomartModulesAction;
import org.gitools.ui.actions.file.ImportIntogenHeatmapAction;
import org.gitools.ui.actions.file.ImportIntogenOncomodulesAction;
import org.gitools.ui.actions.file.ImportIntogenTableAction;
import org.gitools.ui.actions.file.NewCombinationAnalysisAction;
import org.gitools.ui.actions.file.NewCorrelationAnalysisAction;
import org.gitools.ui.actions.file.NewEnrichmentAnalysisAction;
import org.gitools.ui.actions.file.NewOncozAnalysisAction;
import org.gitools.ui.actions.file.NewProjectAction;
import org.gitools.ui.actions.file.OpenDataAction;
import org.gitools.ui.actions.file.OpenEnrichmentAnalysisAction;
import org.gitools.ui.actions.file.OpenHeatmapAction;
import org.gitools.ui.actions.file.SaveAction;
import org.gitools.ui.actions.file.SaveAsAction;
import org.gitools.ui.actions.help.AboutAction;
import org.gitools.ui.actions.help.WelcomeAction;
import org.gitools.ui.platform.actions.ActionManager;
import org.gitools.ui.platform.actions.BaseAction;


// TODO: No tengo muy claro que acabe usandolo
public class ActionManagerInitializer {

	public static final BaseAction showAllRows = new ShowAllAction(ShowAllAction.ElementType.ROWS);
	public static final BaseAction showAllColumns = new ShowAllAction(ShowAllAction.ElementType.COLUMNS);
	public static final BaseAction hideSelectedColumns = new HideSelectionAction(HideSelectionAction.ElementType.COLUMNS);
	public static final BaseAction hideSelectedRows = new HideSelectionAction(HideSelectionAction.ElementType.ROWS);

	public static final BaseAction sortRows = new SortByValueAction(SortByValueAction.SortSubject.ROW);
	public static final BaseAction sortColumns = new SortByValueAction(SortByValueAction.SortSubject.COLUMN);
	public static final BaseAction sortRowsAndColumns = new SortByValueAction(SortByValueAction.SortSubject.BOTH);

	public static final BaseAction moveRowsUpAction = new MoveSelectionAction(MoveSelectionAction.MoveDirection.ROW_UP);
	public static final BaseAction moveRowsDownAction = new MoveSelectionAction(MoveSelectionAction.MoveDirection.ROW_DOWN);
	public static final BaseAction moveColsLeftAction = new MoveSelectionAction(MoveSelectionAction.MoveDirection.COL_LEFT);
	public static final BaseAction moveColsRightAction = new MoveSelectionAction(MoveSelectionAction.MoveDirection.COL_RIGHT);

	public static final BaseAction mtcBonferroniAction = new MtcAction(new Bonferroni());
	public static final BaseAction mtcBenjaminiHochbergFdrAction = new MtcAction(new BenjaminiHochbergFdr());
	public static final BaseAction mtcBenjaminiYekutieliFdrAction = new UnimplementedAction("Benjamini & Yekutieli FDR", false);

	private static ActionSet mainMenu = new ActionSet(new BaseAction[] {
		new ActionSet("File", new BaseAction[] {
			new ActionSet("New", new BaseAction[] {
				//new NewProjectAction(),
				new ActionSet("Analysis", new BaseAction[] {
					new NewEnrichmentAnalysisAction(),
					new NewOncozAnalysisAction(),
					new NewCombinationAnalysisAction(),
					new NewCorrelationAnalysisAction()
				})
			}),
			new ActionSet("Open", new BaseAction[] {
				//openProjectAction,
				new OpenDataAction(),
				new OpenEnrichmentAnalysisAction(),
				new OpenHeatmapAction()
			}),
			BaseAction.separator,
			new SaveAction(),
			new SaveAsAction(),
			BaseAction.separator,
			new CloseAction(),
			BaseAction.separator,
			new ActionSet("Import", new BaseAction[] {
				new ActionSet("IntOGen", new BaseAction[] {
					new ImportIntogenTableAction(),
					new ImportIntogenOncomodulesAction(),
					new ImportIntogenHeatmapAction()
				}),
				new ActionSet("BioMart", new BaseAction[] {
					new ImportBioMartTableAction(),
					new ImportBiomartModulesAction()
				})
			}),
			//exportWizardAction,
			new ActionSet("Export", new BaseAction[] {
				new ExportLabelNamesAction(),
				new ExportMatrixAction(),
				new ExportTableAction(),
				new ExportHeatmapImageAction(),
				new ExportHeatmapHtmlAction()
			}),
			BaseAction.separator,
			new ExitAction()
		}),
		new ActionSet("Edit", new BaseAction[] {
				new SelectAllAction(),
				new UnselectAllAction(),
				new InvertSelectionAction()
		}),
		new ActionSet("Data", new BaseAction[] {
			new ActionSet("Filter", new BaseAction[] {
				new FilterByLabelAction(),
				new FilterByValueAction()
			}),
			new ActionSet("Sort", new BaseAction[] {
				sortRows,
				sortColumns,
				sortRowsAndColumns
			}),
			new ActionSet("Move", new BaseAction[] {
				moveRowsUpAction,
				moveRowsDownAction,
				moveColsLeftAction,
				moveColsRightAction
			}),
			new ActionSet("Visibility", new BaseAction[] {
				showAllRows,
				hideSelectedRows,
				showAllColumns,
				hideSelectedColumns
			}),
			new ActionSet("MTC", new BaseAction[] {
				mtcBonferroniAction,
				mtcBenjaminiHochbergFdrAction,
				mtcBenjaminiYekutieliFdrAction
			})
		}),
		new ActionSet("Help", new BaseAction[] {
			new WelcomeAction(),
			BaseAction.separator,
			new AboutAction()
		})
	});

	public static final void init() {
		ActionManager am = ActionManager.getDefault();


	}
}
