package org.gitools.ui.app.fileimport.wizard.text;


import org.gitools.utils.text.ReaderProfileValidationException;

public interface IFileImportStep {

    void finish() throws ReaderProfileValidationException;

    FlatTextReader getReader();
}
