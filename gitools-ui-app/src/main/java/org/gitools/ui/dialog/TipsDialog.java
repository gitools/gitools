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
package org.gitools.ui.dialog;

import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.help.GitoolsTips;
import org.gitools.ui.settings.Settings;
import org.jdesktop.swingx.JXTipOfTheDay;
import org.jdesktop.swingx.tips.TipLoader;
import org.jdesktop.swingx.tips.TipOfTheDayModel;

import java.util.Properties;
import java.util.Random;

public class TipsDialog {
    private final GitoolsTips gitoolsTips = new GitoolsTips();

    public TipsDialog() {
    }


    public void show(boolean forceShow) {

        Properties tipProperties = gitoolsTips.getTips();

        TipOfTheDayModel loadedTips = TipLoader.load(tipProperties);

        final boolean settingsShowDialog = Settings.getDefault().isShowTipsAtStartup();

        JXTipOfTheDay tipOfTheDay = new JXTipOfTheDay(loadedTips);

        Random generator = new Random();
        int i = generator.nextInt(loadedTips.getTipCount());
        tipOfTheDay.setCurrentTip(i);

        tipOfTheDay.showDialog(AppFrame.get(), new JXTipOfTheDay.ShowOnStartupChoice() {
            @Override
            public void setShowingOnStartup(boolean showOnStartup) {
                Settings.getDefault().setShowTipsAtStartup(showOnStartup);
            }

            @Override
            public boolean isShowingOnStartup() {
                return settingsShowDialog;
            }


        }, forceShow);
    }

    public void show() {
        show(false);
    }

}