package com.prism.mina.ioudp.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.Collection;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.DatagramSessionConfig;
import org.apache.mina.transport.socket.nio.NioDatagramAcceptor;

import com.prism.mina.iobuffer.coder.CmccSipcCodecFactory;
import com.prism.mina.iobuffer.dto.SmsObject;

public class MinaUdpServer {

	public static void main(String[] args) throws IOException {
		int PORT = 9999;
		NioDatagramAcceptor acceptor = new NioDatagramAcceptor();
		acceptor.setHandler(new IoHandlerAdapter() {
			@Override
			public void exceptionCaught(IoSession session, Throwable cause)
					throws Exception {
				System.out.println("服务器发生异常： {}" + cause.getMessage());
			}
			@Override
			public void sessionClosed(IoSession session) {
				System.out.println(session.getRemoteAddress()
						+ "================close==========" + session.getId());
			}
			@Override
			public void sessionOpened(IoSession session) {
				System.out.println(session.getRemoteAddress()
						+ "========open==================" + session.getId());
			}
			@Override
			public void messageReceived(IoSession session, Object message)
					throws Exception {
				SmsObject sms = (SmsObject) message;
				byte[] b = sms.getInfo();
//				 if (sms.getCmd().trim().equals("draw")) {
//				 System.out.println(sms.getCmd()+"==========="+sms.getInfo().length);
				// }
				// System.out.println(sms.getCmd()+"============"+b.length);
				Collection<IoSession> sessions = session.getService()
						.getManagedSessions().values();
				for (IoSession sess : sessions) {
					// System.out.println("=============="+sess.getId());
					if (sess.getId() != session.getId()) {
						sess.write(message);
					}
				}
			}
		});

//		 Executor threadPool = Executors.newCachedThreadPool();
		DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
		chain.addLast("logger", new LoggingFilter());
		acceptor.getFilterChain().addLast(
				"codec",
				new ProtocolCodecFilter(new CmccSipcCodecFactory(Charset
						.forName("UTF-8"))));
		// chain.addLast("keep-alive", new HachiKeepAliveFilterInMina()); // 心跳
		// chain.addLast("threadPool", new ExecutorFilter(threadPool));

		DatagramSessionConfig dcfg = acceptor.getSessionConfig();
		dcfg.setReadBufferSize(1024 * 100);// 设置接收最大字节默认2048
		dcfg.setMaxReadBufferSize(1024 * 100);
		dcfg.setReceiveBufferSize(1024 * 100);// 设置输入缓冲区的大小
		dcfg.setSendBufferSize(1024 * 100);// 设置输出缓冲区的大小
		dcfg.setReuseAddress(true);// 设置每一个非主监听连接的端口可以重用

		acceptor.bind(new InetSocketAddress(PORT));
		System.out.println("UDPServer listening on port " + PORT);
	}
}
