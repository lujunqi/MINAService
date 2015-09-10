package test;

import org.apache.mina.core.buffer.IoBuffer;

public class Test0907 {
	public static void main(String[] args) {
		IoBuffer buf = IoBuffer.allocate(8);
		buf.setAutoExpand(true);
		buf.putChar('D');
		buf.putChar('s');
		buf.putFloat(0.0f);
		buf.putFloat(0.0f);
		buf.putChar('m');
		buf.putFloat(1.0f);
		buf.putFloat(1.0f);
		buf.putChar('m');
		buf.putFloat(2.0f);
		buf.putFloat(2.0f);
		buf.putChar('u');
		buf.putFloat(1.0f);
		buf.putFloat(1.0f);
		buf.flip();
		byte[] b = new byte[buf.limit()];
		buf.get(b);
		String s = new String(b);
		System.out.println(buf.limit());
		
		IoBuffer buf2 = IoBuffer.allocate(8);
		buf2.setAutoExpand(true);
		buf2.put(s.getBytes());
		buf2.flip();
		System.out.println(buf2.limit());
		System.out.println(buf2.getChar());
		
		System.out.println(buf2.getChar());
		System.out.println(buf2.getFloat());
		System.out.println(buf2.getFloat());
		
		System.out.println(buf2.getChar());
		System.out.println(buf2.getFloat());
		System.out.println(buf2.getFloat());

		System.out.println(buf2.getChar());
		System.out.println(buf2.getFloat());
		System.out.println(buf2.getFloat());

		
	}
}
