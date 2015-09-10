package com.prism.mina;

import java.io.Serializable;
@SuppressWarnings("serial")
public class InfoBean implements Serializable {
	private short[] buffer;

	public short[] getBuffer() {
		return buffer;
	}

	public void setBuffer(short[] buffer) {
		this.buffer = buffer;
	}
}
