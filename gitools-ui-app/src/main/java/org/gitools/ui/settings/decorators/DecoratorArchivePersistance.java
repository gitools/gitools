/*
 *  Copyright 2012 michi.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.gitools.ui.settings.decorators;

import com.thoughtworks.xstream.XStream;
import edu.upf.bg.colorscale.NumericColorScale;
import edu.upf.bg.colorscale.impl.CorrelationColorScale;
import org.apache.commons.io.FileUtils;
import org.gitools.model.decorator.ElementDecorator;
import org.gitools.model.decorator.impl.*;

import java.awt.*;
import java.io.*;
import java.util.regex.Pattern;


/**
 * @author michi
 */
public class DecoratorArchivePersistance {

    private transient static XStream xstream;

    private static final String userPath =
            System.getProperty("user.home", ".");

    public static final String CONFIG_PATH =
            userPath + File.separator + ".gitools";

    private static final String configFileName = "scales.xml";

    private static final String configFile =
            CONFIG_PATH + File.separator + configFileName;

    public DecoratorArchivePersistance() {
        xstream = new XStream();

        xstream.alias("decoratorArchive", DecoratorArchive.class);

        xstream.alias("correlationColorScale", CorrelationColorScale.class);

        xstream.alias("binaryDecorator", BinaryElementDecorator.class);
        xstream.omitField(BinaryElementDecorator.class, "valueIndex");

        xstream.alias("linearTwoSidedDecorator", LinearTwoSidedElementDecorator.class);
        xstream.omitField(LinearTwoSidedElementDecorator.class, "valueIndex");

        xstream.alias("formattedTextDecorator", FormattedTextElementDecorator.class);
        xstream.omitField(FormattedTextElementDecorator.class, "valueIndex");

        xstream.alias("pValueDecorator", PValueElementDecorator.class);
        xstream.omitField(PValueElementDecorator.class, "valueIndex");
        xstream.omitField(PValueElementDecorator.class, "correctedValueIndex");

        xstream.alias("zScoreDecorator", ZScoreElementDecorator.class);
        xstream.omitField(ZScoreElementDecorator.class, "valueIndex");
        xstream.omitField(ZScoreElementDecorator.class, "correctedValueIndex");

        xstream.alias("correlationDecorator", CorrelationElementDecorator.class);
        xstream.omitField(CorrelationElementDecorator.class, "valueIndex");

        xstream.omitField(ElementDecorator.class, "valueIndex");
        xstream.omitField(ElementDecorator.class, "adapter");
        xstream.useAttributeFor(ElementDecorator.class, "name");

        xstream.omitField(NumericColorScale.class, "rangesList");

        xstream.omitField(Color.class, "alpha");

        xstream.setMode(XStream.NO_REFERENCES);
    }

    public void save(DecoratorArchive archive) {
        File path = new File(CONFIG_PATH);
        if (!path.exists())
            path.mkdirs();

        try {
            FileWriter writer = new FileWriter(configFile);

            writer.write("<!-- scales.xml version " + DecoratorArchive.VERSION + " -->\n");
            xstream.toXML(archive, writer);
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DecoratorArchive load() {
        DecoratorArchive decoratorArchive;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(configFile));

            String firstLine = reader.readLine();

            if (!firstLine.matches("(.*)" + Pattern.quote("version " + DecoratorArchive.VERSION) + "(.*)")) {
                System.err.println("Current scales.xml file not compatible with current version.");
                reader.close();
                FileUtils.copyFile(new File(configFile), new File(configFile + ".backup"));
                decoratorArchive = new DecoratorArchive();
                decoratorArchive.add(decoratorArchive.getDefaultElementDecoratros());
                save(decoratorArchive);
                System.err.println("Created scales file with defaults and there is a backup copy on " + configFileName + ".backup");

            } else {
                decoratorArchive = (DecoratorArchive) xstream.fromXML(reader, new DecoratorArchive());
                reader.close();
            }


        } catch (FileNotFoundException e) {
            System.err.println("Settings file doesn't exist: " + configFile);
            System.err.println("Created scales file with defaults.");
            decoratorArchive = new DecoratorArchive();
            decoratorArchive.add(decoratorArchive.getDefaultElementDecoratros());
            save(decoratorArchive);
        } catch (Exception e) {
            decoratorArchive = new DecoratorArchive();
            e.printStackTrace();
        }
        return decoratorArchive;
    }
}
