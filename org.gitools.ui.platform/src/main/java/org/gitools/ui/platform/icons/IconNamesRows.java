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
package org.gitools.ui.platform.icons;

public class IconNamesRows implements DimensionIcons {

    public static final String showAll16 = "/img/RowShowAll16.gif";
    public static final String showAll24 = "/img/RowShowAll24.gif";

    public static final String hide16 = "/img/RowHide16.gif";
    public static final String hide24 = "/img/RowHide24.gif";

    public static final String moveUp16 = "/img/MoveRowsUp16.gif";
    public static final String moveUp24 = "/img/MoveRowsUp24.gif";

    public static final String moveDown16 = "/img/MoveRowsDown16.gif";
    public static final String moveDown24 = "/img/MoveRowsDown24.gif";

    @Override
    public String getHide16() {
        return hide16;
    }

    @Override
    public String getHide24() {
        return hide24;
    }

    @Override
    public String getMoveBackward16() {
        return moveUp16;
    }

    @Override
    public String getMoveBackward24() {
        return moveUp24;
    }

    @Override
    public String getMoveForward16() {
        return moveDown16;
    }

    @Override
    public String getMoveForward24() {
        return moveDown24;
    }

    @Override
    public String getShowAll16() {
        return showAll16;
    }

    @Override
    public String getShowAll24() {
        return showAll24;
    }


}
