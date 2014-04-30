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

import org.gitools.ui.app.actions.help.*;
import org.gitools.ui.core.actions.BaseAction;

final class HelpActions {

    public static final BaseAction aboutAction = new AboutAction();

    public static final BaseAction showTipsAction = new ShowTipsAction();

    public static final BaseAction welcomeAction = new WelcomeAction();

    public static final BaseAction shortcutsAction = new ShortcutsAction();

    public static final BaseAction settingsAction = new SettingsAction();

}
