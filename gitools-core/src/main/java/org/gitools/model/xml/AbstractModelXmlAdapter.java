/*
 * #%L
 * gitools-core
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
package org.gitools.model.xml;

import org.gitools.model.AbstractModel;
import org.jetbrains.annotations.NotNull;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.List;

@Deprecated
public class AbstractModelXmlAdapter extends XmlAdapter<AbstractModelDecoratorElement, List<AbstractModel>>
{

    @NotNull
    @Override
    public AbstractModelDecoratorElement marshal(@NotNull List<AbstractModel> elems)
            throws Exception
    {
        return new AbstractModelDecoratorElement(elems);
    }

    @NotNull
    @Override
    public List<AbstractModel> unmarshal(@NotNull AbstractModelDecoratorElement v)
            throws Exception
    {
        return v.getList();
    }

}
