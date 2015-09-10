package com.prism.mina.stream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.mina.core.session.IoSession;

public class Work extends Thread {
	private InputStream in;
	private OutputStream out;
	private IoSession session;

	public Work(IoSession session, InputStream in, OutputStream out) {
		this.in = in;
		this.out = out;
		this.session = session;
	}

	public void run() {
		try {
			// FileOutputStream fos = new FileOutputStream(new File(
			// "e:/jre(1).z01"));
			byte[] buf = new byte[640];
			int offset = 0;
			while ((offset = in.read(buf)) != -1) {
//				System.out.println(buf[0]);
				for (int i = 0; i < buf.length; i++) {
					out.write(buf[i]);	
				}
				
				// fos.write(buf, 0, offset);
				// fos.flush();
			}
			// fos.close();
			System.out.println("session over..");
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}
}
