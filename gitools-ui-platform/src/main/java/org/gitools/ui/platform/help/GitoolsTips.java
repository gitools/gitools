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