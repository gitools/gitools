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
package org.gitools.ui.core.interaction;

import org.gitools.ui.core.Application;

public class InteractionStatus {

    private static Interaction interaction = Interaction.none;


    /**
     * returns true if there is an interaction going on
     *
     * @return
     */
    public static boolean isInteracting() {
        return interaction.equals(Interaction.none);
    }

    /**
     * returns if any of the passed is an interaction going on
     *
     * @return
     */
    public static boolean isInteracting(Interaction... interactions) {
        for (Interaction i : interactions) {
            if (interaction.equals(i)) {
                return true;
            }
        }

        return false;
    }

    public static Interaction getInteractionStatus() {
        return interaction;
    }

    public static void setInteractionStatus(Interaction interaction) {
        InteractionStatus.interaction = interaction;
        Application.get().setInteractionStatus(interaction.toString());

    }
}
