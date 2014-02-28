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
package org.gitools.ui.app.actions.data;

import com.google.common.collect.Iterables;
import org.gitools.heatmap.Bookmark;
import org.gitools.heatmap.Heatmap;
import org.gitools.ui.app.IconNames;
import org.gitools.ui.app.actions.HeatmapAction;
import org.gitools.ui.app.dialog.CreateBookmarkPage;
import org.gitools.ui.platform.Application;
import org.gitools.ui.platform.wizard.PageDialog;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;


public class CreateBookmarkAction extends HeatmapAction {

    private ArrayList<Double> categories;

    public CreateBookmarkAction() {
        super("<html><i>Create</i> new bookmark</html>");
        setLargeIconFromResource(IconNames.bookmarkAdd24);
        setSmallIconFromResource(IconNames.bookmarkAdd16);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        run();
    }

    public void run() {
        Heatmap heatmap = getHeatmap();

        CreateBookmarkPage page = new CreateBookmarkPage(heatmap.getBookmarks().getAll(), heatmap.newBookmark("new Bookmark"));
        PageDialog dialog = new PageDialog(Application.get(), page);
        dialog.setVisible(true);

        if (dialog.isCancelled()) {
            heatmap.getBookmarks().removeBookmark(page.getBookmark());
            return;
        }

        Application.get().setStatusText("Bookmark created.");
    }

    public ArrayList<Double> getCategories() {
        return categories;
    }
}
