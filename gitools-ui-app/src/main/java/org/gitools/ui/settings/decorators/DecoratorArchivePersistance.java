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
import edu.upf.bg.colorscale.ColorScaleFragment;
import edu.upf.bg.colorscale.ColorScalePoint;
import edu.upf.bg.colorscale.impl.CorrelationColorScale;
import edu.upf.bg.colorscale.impl.LinearColorScale;
import edu.upf.bg.colorscale.impl.LogColorScale;
import edu.upf.bg.colorscale.impl.UniformColorScale;
import java.io.*;
import org.gitools.model.decorator.ElementDecorator;
import org.gitools.model.decorator.impl.*;


/**
 *
 * @author michi
 */
public class DecoratorArchivePersistance {

 

    private transient static XStream xstream;


	/*public static DecoratorArchivePersistance getInstance() {
		if (instance == null) {
            instance = load();
        }
        return instance;
	}*/

    private static final String userPath =
		System.getProperty("user.home", ".");

	public static final String CONFIG_PATH =
		userPath + File.separator + ".gitools";

	private static final String configFileName = "scales.xml";

	private static final String configFile =
		CONFIG_PATH + File.separator + configFileName;

    /*	FormattedTextElementDecorator.class,
	LinearTwoSidedElementDecorator.class,
	PValueElementDecorator.class,
	ZScoreElementDecorator.class,
    CorrelationElementDecorator.class*/
    
    public DecoratorArchivePersistance() {
        xstream = new XStream();
        
        xstream.alias("decoratorArchive", DecoratorArchive.class);
        xstream.alias("colorScalePoint", ColorScalePoint.class);
        
        xstream.alias("colorScaleFragment", ColorScaleFragment.class);
        xstream.alias("linearColorScale", LinearColorScale.class);
        xstream.alias("uniformColorScale", UniformColorScale.class);
        xstream.alias("logColorScale", LogColorScale.class);
        xstream.alias("correlationColorScale", CorrelationColorScale.class);
        
        xstream.aliasPackage("cutoffComparison", "edu.upf.bg.cutoffcmp");
        
        xstream.alias("binaryDecorator", BinaryElementDecorator.class);
        xstream.omitField(BinaryElementDecorator.class, "valueIndex");     
        //xstream.omitField(BinaryElementDecorator.class, "fmt");  
        
        xstream.alias("linearTwoSidedDecorator", LinearTwoSidedElementDecorator.class);
        xstream.omitField(LinearTwoSidedElementDecorator.class, "valueIndex");
        //xstream.omitField(LinearTwoSidedElementDecorator.class, "fmt"); 
        
        xstream.alias("formattedTextDecorator", FormattedTextElementDecorator.class);
        xstream.omitField(FormattedTextElementDecorator.class, "valueIndex");
        //xstream.omitField(FormattedTextElementDecorator.class, "fmt"); 
        
        xstream.alias("pValueDecorator", PValueElementDecorator.class);
        xstream.omitField(PValueElementDecorator.class, "valueIndex");
        xstream.omitField(PValueElementDecorator.class, "correctedValueIndex");
        //xstream.omitField(PValueElementDecorator.class, "fmt"); 
        
        xstream.alias("zScoreDecorator", ZScoreElementDecorator.class);
        xstream.omitField(ZScoreElementDecorator.class, "valueIndex");
        xstream.omitField(ZScoreElementDecorator.class, "correctedValueIndex");
        //xstream.omitField(ZScoreElementDecorator.class, "fmt");
        
        xstream.alias("correlationDecorator", CorrelationElementDecorator.class);
        xstream.omitField(CorrelationElementDecorator.class, "valueIndex");
        //xstream.omitField(CorrelationElementDecorator.class, "fmt");
        
        xstream.omitField(ElementDecorator.class, "valueIndex");
        xstream.omitField(ElementDecorator.class, "adapter");
        xstream.useAttributeFor(ElementDecorator.class, "name");
        //xstream.omitField(ElementDecorator.class, "fmt");
        
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
            Reader reader = new FileReader(configFile);
            decoratorArchive = (DecoratorArchive) xstream.fromXML(reader, new DecoratorArchive());
            reader.close();
        } catch (FileNotFoundException e) {
            System.err.println("Settings file doesn't exist: " + configFile);
            System.err.println("Created one with defaults.");
            decoratorArchive = new DecoratorArchive();
            decoratorArchive.add(decoratorArchive.getDefaultElementDecoratros());
            save(decoratorArchive);
        }
        catch (Exception e) {
            decoratorArchive = new DecoratorArchive();
            e.printStackTrace();
        }
        return decoratorArchive;
    }
}
