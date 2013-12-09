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
/*
 * Copyright 2000-2013 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gitools.ui.platform.idea;

import org.gitools.ui.platform.os.SystemInfo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;

/**
 * @author evgeny.zakrevsky
 */
public class JBTabbedPane extends JTabbedPane implements HierarchyListener {
  public static final java.lang.String LABEL_FROM_TABBED_PANE = "JBTabbedPane.labelFromTabbedPane";
  private int previousSelectedIndex = -1;
  
  public JBTabbedPane() {
  }

  public JBTabbedPane(int tabPlacement) {
    super(tabPlacement);
  }

  public JBTabbedPane(int tabPlacement, int tabLayoutPolicy) {
    super(tabPlacement, tabLayoutPolicy);
  }

  @Override
  public void setComponentAt(int index, Component component) {
    super.setComponentAt(index, component);
    component.addHierarchyListener(this);
    UIUtil.setNotOpaqueRecursively(component);
    setInsets(component);
    revalidate();
    repaint();
  }

  @Override
  public void insertTab(String title, Icon icon, Component component, String tip, int index) {
    super.insertTab(title, icon, component, tip, index);

    //set custom label for correct work spotlighting in settings
    JLabel label = new JLabel(title);
    label.setIcon(icon);
    label.setBorder(new EmptyBorder(1,1,1,1));
    setTabComponentAt(index, label);
    updateSelectedTabForeground();
    label.putClientProperty(LABEL_FROM_TABBED_PANE, Boolean.TRUE);

    component.addHierarchyListener(this);
    UIUtil.setNotOpaqueRecursively(component);
    setInsets(component);

    revalidate();
    repaint();
  }

  @Override
  public void setSelectedIndex(int index) {
    previousSelectedIndex = getSelectedIndex();
    super.setSelectedIndex(index);
    updateSelectedTabForeground();
    revalidate();
    repaint();
  }

  private void updateSelectedTabForeground() {
    if (UIUtil.isUnderAquaLookAndFeel() && SystemInfo.isMacOSLion) {
      if (getSelectedIndex() != -1 && getTabComponentAt(getSelectedIndex()) != null) {
        getTabComponentAt(getSelectedIndex()).setForeground(Color.WHITE);
      }
      if (previousSelectedIndex != -1 && getTabComponentAt(previousSelectedIndex) != null) {
        getTabComponentAt(previousSelectedIndex).setForeground(UIUtil.getLabelForeground());
      }
    }
  }

  private void setInsets(Component component) {
    if (component instanceof JComponent) {
      UIUtil.addInsets((JComponent)component, getInsetsForTabComponent());
    }
  }

  protected java.awt.Insets getInsetsForTabComponent() {
    return UIUtil.PANEL_SMALL_INSETS;
  }

  @Override
  public void hierarchyChanged(HierarchyEvent e) {
    UIUtil.setNotOpaqueRecursively(e.getComponent());
    repaint();
  }

  @Override
  public void removeNotify() {
    super.removeNotify();
    if (!ScreenUtil.isStandardAddRemoveNotify(this))
      return;
    for (int i=0; i<getTabCount(); i++) {
      getComponentAt(i).removeHierarchyListener(this);
    }
  }
}
