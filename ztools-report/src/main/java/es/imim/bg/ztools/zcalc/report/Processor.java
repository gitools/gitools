package org.gitools.zcalc.report;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateTransformModel;

public class Processor {
	
	private File templatePath;
	private Configuration cfg;
	private Template temp;
	
	public Processor(File templatePath, String templateName) throws IOException {
		
		this.templatePath = templatePath;
		
		cfg = new Configuration();
		
		cfg.setDirectoryForTemplateLoading(templatePath);
		
		cfg.setObjectWrapper(new DefaultObjectWrapper());	

		temp = cfg.getTemplate(templateName);
	}

	public void process( 
			Map<String, Object> model,
			File outputPath,
			String outputFileName) 
				throws TemplateException, IOException {
		
		Writer out = new FileWriter(
				new File(outputPath, outputFileName));
		
		temp.process(model, out);
		
		out.flush();
		out.close();
	}
	
	public void copyTemplateContents(File outputPath) throws IOException {
		copy(templatePath, outputPath);
	}

	public void addDirectives(final Map<String, Object> model, final File outputPath) {
		
		model.put("process", new TemplateTransformModel() {
			@Override
			public Writer getWriter(final Writer out, final Map args)
					throws TemplateModelException, IOException {

				Map<String, Object> params = new HashMap<String, Object>();
				
				String templateName = null;
				String outputName = null;
				
				for (Object key : args.keySet()) {
					String name = key.toString();
					if (name.equals("template"))
						templateName = (String) args.get("template").toString();
					else if (name.equals("output"))
						outputName = (String) args.get("output").toString();
					else
						params.put(name, args.get(key));
				}
				
				if (templateName == null)
					throw new TemplateModelException("Missing required directive parameter: template");
				
				if (outputName == null)
					throw new TemplateModelException("Missing required directive parameter: output");
				
				model.put("params", params);
				
				try {
					new Processor(templatePath, templateName)
						.process(model, outputPath, outputName);
				} catch (TemplateException e) {
					throw new TemplateModelException("Error processing nested template '" + templateName + "' to file '" + outputName + "'", e);
				}
				
				final StringBuffer buf = new StringBuffer();
				return new Writer() {
					public void write(char cbuf[], int off, int len) {
						buf.append(cbuf, off, len);
					}
					public void flush() throws IOException {
						out.flush();
					}
					public void close() throws IOException {
						out.write(buf.toString());
					}
				};
			}
		});
	}

	private void copy(File src, File dst) throws IOException {
		File[] list = src.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return !name.endsWith(".ftl");
			}
		});
		
		for (File file : list) {
			File dstFile = new File(dst, file.getName());
			//System.out.println(file.getAbsolutePath() + "\n\t-> " + dstFile.getAbsolutePath());
			if (file.isFile())
				copyFile(file, dstFile);
			else if (file.isDirectory()) {
				dstFile.mkdir();
				copy(file, dstFile);
			}
		}
	}

	private void copyFile(File src, File dst) throws IOException {
		FileChannel in = new FileInputStream(src).getChannel();
		FileChannel out = new FileOutputStream(dst).getChannel();
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		while (in.read(buffer) != -1) {
			buffer.flip(); // Prepare for writing
			out.write(buffer);
			buffer.clear(); // Prepare for reading
		}
	}
}
