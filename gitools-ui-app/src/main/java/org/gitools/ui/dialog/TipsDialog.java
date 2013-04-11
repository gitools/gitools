package org.gitools.ui.dialog;

import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.settings.Settings;
import org.jdesktop.swingx.JXTipOfTheDay;
import org.jdesktop.swingx.tips.TipLoader;
import org.jdesktop.swingx.tips.TipOfTheDayModel;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;

public class TipsDialog {
    public TipsDialog() {
    }


    public void show(boolean forceShow) {

        Properties tipProperties = new Properties();
        InputStream inputStream = this.getClass().getResourceAsStream("/help/gitooltips.properties");
        try {
            tipProperties.load(inputStream);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

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