package com.prism.mina.iobyte.server;

import java.util.Collection;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

public class MinaByteHandler extends IoHandlerAdapter {

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {

	}

	@Override
	public void sessionOpened(IoSession ssn) throws Exception {
		// ssn.write(new byte[]{1,2,3});
		System.out.println("session open for " + ssn.getRemoteAddress());

	}

	/**
	 * 收到来自客户端的消息
	 */
	byte i = 0;

	public void messageReceived(IoSession session, Object message)
			throws Exception {
		
		if (message != null) {
			byte[] b = (byte[])message;
			for (int i = 0; i < b.length; i++) {
				System.out.print(b[i]+",");
			}
			System.out.println("\n"+b.length);
			
			Collection<IoSession> sessions = session.getService()
					.getManagedSessions().values();
			for (IoSession sess : sessions) {
				// if (session.getId() != sess.getId()) {
				sess.write(message);
				// }
			}
		}
	}
}
