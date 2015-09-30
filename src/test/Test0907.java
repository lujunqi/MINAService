package test;

import org.apache.mina.core.buffer.IoBuffer;

public class Test0907 {
	public static void main(String[] args) {
		IoBuffer ib = IoBuffer.allocate(8).setAutoExpand(true);
		ib.putChar('W');
		ib.putInt(Integer.parseInt("1"));// form
		ib.flip();
System.out.println(ib.limit());
		
	}
}
