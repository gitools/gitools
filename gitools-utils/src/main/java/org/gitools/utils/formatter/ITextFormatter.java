package org.gitools.utils.formatter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: mschroeder
 * Date: 9/16/13
 * Time: 4:06 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ITextFormatter extends Serializable {
    @NotNull
    String format(@Nullable Object value);
}
