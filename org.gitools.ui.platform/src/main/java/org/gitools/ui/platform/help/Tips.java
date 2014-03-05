/*
 * #%L
 * gitools-ui-platform
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
package org.gitools.ui.platform.help;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Tips {

    private static Tips INSTANCE = new Tips();

    public static Tips get() {
        return INSTANCE;
    }

    private Properties tips = new Properties();

    private Tips() {
    }

    public void load(InputStream inputStream) {
        try {
            this.tips.load(inputStream);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        String leadingHTML = "<html><body style='margin: 5px; font-size= 14px;'>";
        String closingHTML = "</body></html>";

        for (String propKey : this.tips.stringPropertyNames()) {
            String tipProp = this.tips.getProperty(propKey);

            this.tips.put(propKey, leadingHTML.concat(tipProp).concat(closingHTML));
        }
    }

    public Properties getTips() {
        return this.tips;
    }


    public String getRandomTip() {
        if (this.tips.size() == 0) {
            this.tips = getTips();
        }

        int random = (int) (Math.random() * tips.size());
        String key = (String) tips.stringPropertyNames().toArray()[random];
        return (key.endsWith(".name")) ?
                getRandomTip() :
                tips.getProperty(key);
    }
}