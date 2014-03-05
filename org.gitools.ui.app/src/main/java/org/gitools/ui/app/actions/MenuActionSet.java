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

import org.gitools.ui.app.IconNames;
import org.gitools.ui.app.actions.edit.ColumnsActionSet;
import org.gitools.ui.app.actions.edit.LayersActionSet;
import org.gitools.ui.app.actions.edit.RowsActionSet;
import org.gitools.ui.app.actions.help.OpenURLAction;
import org.gitools.ui.platform.actions.ActionSet;
import org.gitools.ui.platform.actions.ActionSetUtils;
import org.gitools.ui.platform.actions.BaseAction;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class MenuActionSet extends ActionSet {

    public static MenuActionSet INSTANCE = new MenuActionSet();

    public MenuActionSet() {
        super(new BaseAction[]{
                new ActionSet("File", KeyEvent.VK_F,
                        new BaseAction[]{
                                Actions.open,
                                Actions.openURL,
                                Actions.openGenomeSpace,
                                BaseAction.separator,
                                Actions.saveAction,
                                Actions.saveAsAction,
                                BaseAction.separator,
                                new ActionSet("Export", KeyEvent.VK_E,
                                        new BaseAction[]{
                                                Actions.exportAnnotationAction,
                                                Actions.exportMatrixAction,
                                                Actions.exportTableAction,
                                                Actions.exportHeatmapImageAction,
                                                Actions.exportScaleImageAction
                                        }
                                ),
                                BaseAction.separator,
                                Actions.exitAction
                        }
                ),
                new ActionSet("Edit", KeyEvent.VK_E,
                        new BaseAction[]{
                                new ColumnsActionSet(),
                                new RowsActionSet(),
                                new LayersActionSet(),
                                Actions.heatmapSettings,
                                BaseAction.separator,
                                Actions.searchRowsAction,
                                Actions.createBookmarkAction
                        }
                ),
                new ActionSet("Analysis", KeyEvent.VK_A,
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
                new ActionSet("Download", KeyEvent.VK_D,
                        new BaseAction[]{
                                new ActionSet("Modules", KeyEvent.VK_M, IconNames.empty16,
                                        new BaseAction[]{
                                                Actions.importKeggModulesAction,
                                                Actions.importGoModulesAction,
                                                Actions.importBioMartModulesAction
                                        }
                                ),
                                new ActionSet("Annotations", KeyEvent.VK_A,
                                        new BaseAction[]{
                                                Actions.importBioMartTableAction
                                        }
                                )
                        }
                ),
                new ActionSet("Help", KeyEvent.VK_H,
                        new BaseAction[]{
                                HelpActions.welcomeAction,
                                BaseAction.separator,
                                HelpActions.shortcutsAction,
                                HelpActions.showTipsAction,
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
