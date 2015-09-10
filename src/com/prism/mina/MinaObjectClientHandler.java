package com.prism.mina;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

public class MinaObjectClientHandler extends IoHandlerAdapter{
	private final InfoBean ui;

	public MinaObjectClientHandler(InfoBean ui) {
		this.ui = ui;
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		session.write(ui);
	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		InfoBean ui = (InfoBean) message;
		System.out.println("收到服务机发来的用户名：" + ui);
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		System.out.println("与" + session.getRemoteAddress() + "通信过程中出现错误:["
				+ cause.getMessage() + "]..连接即将关闭....");
		session.close(false);
		session.getService().dispose();
	}
}
