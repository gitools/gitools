package org.gitools.cli.convert;

class Conversion {

	public String src;
	public String dst;

	public ConversionDelegate delegate;

	public Conversion(String src, String dst) {
		this(src, dst, null);
	}

	public Conversion(String src, String dst, ConversionDelegate delegate) {
		this.src = src;
		this.dst = dst;
		this.delegate = delegate;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		} else if (!(obj instanceof Conversion)) {
			return false;
		}
		Conversion o = (Conversion) obj;
		return o.src.equals(this.src) && o.dst.equals(this.dst);
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 89 * hash + (this.src != null ? this.src.hashCode() : 0);
		hash = 89 * hash + (this.dst != null ? this.dst.hashCode() : 0);
		return hash;
	}
}
