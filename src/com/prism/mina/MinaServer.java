package com.prism.mina;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.Collection;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

public class MinaServer {
	private SocketAcceptor acceptor;

	public MinaServer() {
		// 创建非阻塞的server端的Socket连接
		acceptor = new NioSocketAcceptor();
	}

	public boolean start() {
		DefaultIoFilterChainBuilder filterChain = acceptor.getFilterChain();
		// 添加编码过滤器 处理乱码、编码问题
		filterChain.addLast(
				"codec",
				new ProtocolCodecFilter(new TextLineCodecFactory(Charset
						.forName("UTF-8"), LineDelimiter.WINDOWS.getValue(),
						LineDelimiter.WINDOWS.getValue())));

		/*
		 * LoggingFilter loggingFilter = new LoggingFilter();
		 * loggingFilter.setMessageReceivedLogLevel(LogLevel.INFO);
		 * loggingFilter.setMessageSentLogLevel(LogLevel.INFO); // 添加日志过滤器
		 * filterChain.addLast("loger", loggingFilter);
		 */

		// 设置核心消息业务处理器
		acceptor.setHandler(new IoHandlerAdapter() {

			@Override
			public void exceptionCaught(IoSession session, Throwable arg1)
					throws Exception {
			}

			@Override
			public void inputClosed(IoSession session) throws Exception {

			}

			@Override
			public void messageReceived(IoSession session, Object message)
					throws Exception {
				System.out.println(message);
				Collection<IoSession> sessions = session.getService()
						.getManagedSessions().values();
				for (IoSession sess : sessions) {
					if (sess.getId() != session.getId()) {
						sess.write(message);
					}

				}

			}

		});
		// 设置session配置，30秒内无操作进入空闲状态
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 30);

		try {
			// 绑定端口3456
			acceptor.bind(new InetSocketAddress(9000));
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static void main(String[] args) {
		MinaServer server = new MinaServer();
		server.start();
	}

}
