package org.gitools.ui.platform.help;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class GitoolsTips {

    Properties tipProperties = new Properties();

    public GitoolsTips() {
    }

    public Properties getTips() {
        InputStream inputStream = this.getClass().getResourceAsStream("/gitooltips.properties");
        try {
            this.tipProperties.load(inputStream);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        String leadingHTML = "<html><body style='margin: 5px; font-size= 14px;'>";
        String closingHTML = "</body></html>";

        for (String propKey : this.tipProperties.stringPropertyNames()) {
            String tipProp = this.tipProperties.getProperty(propKey);

            this.tipProperties.put(propKey, leadingHTML.concat(tipProp).concat(closingHTML));
        }
        return this.tipProperties;
    }


    public String getRandomTip() {
        if (this.tipProperties.size() == 0) {
            this.tipProperties = getTips();
        }

        int random = (int) (Math.random() * tipProperties.size());
        String key = (String) tipProperties.stringPropertyNames().toArray()[random];
        return (key.endsWith(".name")) ?
                getRandomTip() :
                tipProperties.getProperty(key);


    }
}