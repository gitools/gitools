/*
 * #%L
 * org.gitools.ui.core
 * %%
 * Copyright (C) 2013 - 2014 Universitat Pompeu Fabra - Biomedical Genomics group
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
package org.gitools.ui.core.components.boxes;


import com.alee.extended.label.WebLinkLabel;
import org.gitools.ui.core.Application;
import org.gitools.utils.HttpUtils;

import java.io.File;

public class DetailsWebLinkLabel extends WebLinkLabel {

    public DetailsWebLinkLabel(String s, int trailing) {
        super(s, trailing);
    }

    public DetailsWebLinkLabel(String abbreviatedValue) {
        super(abbreviatedValue);
    }

    @Override
    protected Runnable createAddressLink(final String address) {
        return new Runnable() {
            @Override
            public void run() {
                HttpUtils.openURLInBrowser(address, Application.get(), true);
            }
        };
    }

    @Override
    protected Runnable createEmailLink(String email) {
        return super.createEmailLink(email);
    }

    @Override
    protected Runnable createFileLink(File file) {
        return super.createFileLink(file);
    }
}
