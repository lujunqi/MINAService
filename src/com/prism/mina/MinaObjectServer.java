package com.prism.mina;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

public class MinaObjectServer extends IoHandlerAdapter {
	public static void main(String[] args) throws IOException {
		int bindPort = 3456;

		IoAcceptor acceptor = new NioSocketAcceptor();

		acceptor.getSessionConfig().setReadBufferSize(2048);
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);

		// 设定服务器解析消息的规则是以Object对象为单位进行传输，注意此时该对象需实现Serializable接口
		acceptor.getFilterChain().addLast("codec",
				new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));

		acceptor.setHandler(new MinaObjectServer());

		acceptor.bind(new InetSocketAddress(bindPort));

		System.out.println("MinaServer is startup, and it`s listing on := "
				+ bindPort);
	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		System.out.println("收到客户机发来的类型：" + message.getClass());
//		session.write(new UserInfo(ui.getName() + "==>>是个神秘的人"));
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		System.out.println("InComing Client：" + session.getRemoteAddress());
	}
}
