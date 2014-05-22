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
package org.gitools.ui.app.actions;

import org.gitools.ui.platform.icons.IconNames;
import org.gitools.ui.app.actions.edit.ColumnsActionSet;
import org.gitools.ui.app.actions.edit.LayersActionSet;
import org.gitools.ui.app.actions.edit.RowsActionSet;
import org.gitools.ui.app.actions.help.OpenURLAction;
import org.gitools.ui.core.actions.ActionSet;
import org.gitools.ui.core.actions.ActionSetUtils;
import org.gitools.ui.core.actions.BaseAction;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class MenuActionSet extends ActionSet {

    public static final String ACION_SCOPE_FILE = "File";
    public static final String ACTION_SCOPE_EXPORT = "Export";
    public static final String ACTION_SCOPE_EDIT = "Edit";
    public static final String ACTION_SCOPE_ANALYSIS = "Analysis";
    public static final String ACTION_SCOPE_DOWNLOAD = "Download";
    public static final String ACTION_SCOPE_MODULES = "Modules";
    public static final String ACTION_SCOPE_ANNOTATIONS = "Annotations";
    public static final String ACTION_SCOPE_HELP = "Help";
    public static final String ACTION_SCOPE_ROWS = "Rows";
    public static final String ACTION_SCOPE_COLUMNS = "Columns";
    public static final String ACTION_SCOPE_LAYERS = "Layers";

    public static MenuActionSet INSTANCE = new MenuActionSet();

    public MenuActionSet() {
        super(new BaseAction[]{
                new ActionSet(ACION_SCOPE_FILE, KeyEvent.VK_F,
                        new BaseAction[]{
                                Actions.open,
                                Actions.openURL,
                                Actions.openGenomeSpace,
                                BaseAction.separator,
                                Actions.saveAction,
                                Actions.saveAsAction,
                                BaseAction.separator,
                                new ActionSet(ACTION_SCOPE_EXPORT, KeyEvent.VK_E,
                                        new BaseAction[]{
                                                Actions.exportAnnotationAction,
                                                Actions.exportMatrixAction,
                                                Actions.exportTableAction,
                                                Actions.exportHeatmapImageAction,
                                                Actions.exportHeatmapSVGAction,
                                                Actions.exportScaleImageAction,
                                                Actions.exportHierarchicalTreeImageAction
                                        }
                                ),
                                BaseAction.separator,
                                Actions.exitAction
                        }
                ),
                new ActionSet(ACTION_SCOPE_EDIT, KeyEvent.VK_E,
                        new BaseAction[]{
                                new ColumnsActionSet(ACTION_SCOPE_COLUMNS),
                                new RowsActionSet(ACTION_SCOPE_ROWS),
                                new LayersActionSet(ACTION_SCOPE_LAYERS),
                                Actions.heatmapSettings,
                                BaseAction.separator,
                                Actions.searchRowsAction,
                                Actions.createBookmarkAction
                        }
                ),
                new ActionSet(ACTION_SCOPE_ANALYSIS, KeyEvent.VK_A,
                        new BaseAction[]{
                                Actions.enrichment,
                                Actions.oncodrive,
                                Actions.correlations,
                                Actions.combinations,
                                Actions.overlapping,
                                Actions.groupComparison,
                                Actions.clusteringAction,
                        }
                ),
                new ActionSet(ACTION_SCOPE_DOWNLOAD, KeyEvent.VK_D,
                        new BaseAction[]{
                                new ActionSet(ACTION_SCOPE_MODULES, KeyEvent.VK_M, IconNames.empty16,
                                        new BaseAction[]{
                                                Actions.importKeggModulesAction,
                                                Actions.importGoModulesAction,
                                                Actions.importBioMartModulesAction
                                        }
                                ),
                                new ActionSet(ACTION_SCOPE_ANNOTATIONS, KeyEvent.VK_A,
                                        new BaseAction[]{
                                                Actions.importBioMartTableAction
                                        }
                                )
                        }
                ),
                new ActionSet(ACTION_SCOPE_HELP, KeyEvent.VK_H,
                        new BaseAction[]{
                                HelpActions.welcomeAction,
                                BaseAction.separator,
                                HelpActions.shortcutsAction,
                                HelpActions.showTipsAction,
                                HelpActions.settingsAction,
                                BaseAction.separator,
                                new OpenURLAction("Users mail list", "https://groups.google.com/forum/#!forum/gitools-users", KeyEvent.VK_M),
                                new OpenURLAction("Submit an issue", "https://github.com/gitools/gitools/issues", KeyEvent.VK_S),
                                BaseAction.separator,
                                new OpenURLAction("User guide", "http://www.gitools.org/documentation/UserGuide.html", KeyEvent.VK_U, KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0)),
                                new OpenURLAction("Tutorials", "http://www.gitools.org/documentation/Tutorials.html", KeyEvent.VK_U),
                                new OpenURLAction("Examples", "http://www.gitools.org/documentation/Examples.html", KeyEvent.VK_E),
                                new OpenURLAction("Website", "http://www.gitools.org", KeyEvent.VK_S),
                                BaseAction.separator,
                                HelpActions.aboutAction
                        }
                )
        }
        );
    }

    public JMenuBar createMenuBar() {
        return ActionSetUtils.createMenuBar(this);
    }
}
