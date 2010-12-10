package org.gitools.utils;

import com.thoughtworks.xstream.XStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.Queue;

/** Code obtained from http://weblogs.java.net/blog/emcmanus/archive/2007/04/cloning_java_ob.html */
public class SerialClone {

	public static <T> T clone(T x) {
		try {
			return cloneX(x);
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException(e);
		}
	}

	private static <T> T cloneX(T x) throws IOException, ClassNotFoundException {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		CloneOutput cout = new CloneOutput(bout);
		cout.writeObject(x);
		byte[] bytes = bout.toByteArray();

		ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
		CloneInput cin = new CloneInput(bin, cout);
		@SuppressWarnings("unchecked")  // thanks to Bas de Bakker for the tip!
		T clone = (T) cin.readObject();
		return clone;
	}

	private static class CloneOutput extends ObjectOutputStream {

		Queue<Class<?>> classQueue = new LinkedList<Class<?>>();

		CloneOutput(OutputStream out) throws IOException {
			super(out);
		}

		@Override
		protected void annotateClass(Class<?> c) {
			classQueue.add(c);
		}

		@Override
		protected void annotateProxyClass(Class<?> c) {
			classQueue.add(c);
		}
	}

	private static class CloneInput extends ObjectInputStream {

		private final CloneOutput output;

		CloneInput(InputStream in, CloneOutput output) throws IOException {
			super(in);
			this.output = output;
		}

		@Override
		protected Class<?> resolveClass(ObjectStreamClass osc)
				throws IOException, ClassNotFoundException {
			Class<?> c = output.classQueue.poll();
			String expected = osc.getName();
			String found = (c == null) ? null : c.getName();
			if (!expected.equals(found)) {
				throw new InvalidClassException("Classes desynchronized: "
						+ "found " + found + " when expecting " + expected);
			}
			return c;
		}

		@Override
		protected Class<?> resolveProxyClass(String[] interfaceNames)
				throws IOException, ClassNotFoundException {
			return output.classQueue.poll();
		}
	}

	public static <T> T xclone(T x) {
		XStream xstream = new XStream();
		String xml = xstream.toXML(x);
		return (T) xstream.fromXML(xml);
	}
}
