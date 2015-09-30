package com.prism.mina.ioudp.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.DatagramSessionConfig;
import org.apache.mina.transport.socket.nio.NioDatagramAcceptor;

import com.prism.mina.ioudp.coder.HachiKeepAliveFilterInMina;

public class MinaNatUdpServer {
	public static final char OPEN = 'O';
	final static char CLOSE = 'C';

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
			public void sessionOpened(IoSession session) {
				Collection<IoSession> sessions = session.getService()
						.getManagedSessions().values();

				System.out.println(session.getRemoteAddress() + "[ID:"
						+ session.getId() + "]=============[open]=");

				for (IoSession sess : sessions) {
					if (sess.getId() == session.getId()) {
						continue;
					}
					if (sess.getAttribute("TYPE") != null) {

						IoBuffer buf = IoBuffer.allocate(8);
						buf.setAutoExpand(true);
						char type = (Character) sess.getAttribute("TYPE");
						int id = (Integer) sess.getAttribute("ID");
						buf.putChar(type);
						buf.putChar(OPEN);
						buf.putInt(id);
						buf.flip();
						session.write(buf);
					}
				}

			}

			@Override
			public void sessionClosed(IoSession session) {
				Collection<IoSession> sessions = session.getService()
						.getManagedSessions().values();
				char type = (Character) session.getAttribute("TYPE");
				int id = (Integer) session.getAttribute("ID");
				for (IoSession sess : sessions) {
					if (sess.getId() == session.getId()) {
						continue;
					}
					IoBuffer buf = IoBuffer.allocate(8);
					buf.setAutoExpand(true);
					buf.putChar(type);
					buf.putChar(CLOSE);
					buf.putInt(id);
					buf.flip();
					sess.write(buf);
				}
			}

			@Override
			public void messageReceived(IoSession session, Object message)
					throws Exception {
				IoBuffer buff = (IoBuffer) message;

				if (buff.limit() == 8) {// 状态
					char type = buff.getChar(); // 老师 或 学生
					char io = buff.getChar(); // 开通 关闭 唤醒
					int id = buff.getInt(); // user_id

					session.setAttribute("TYPE", type);
					session.setAttribute("ID", id);
					// Collection<IoSession> sessions = session.getService()
					// .getManagedSessions().values();

					// session.setAttribute("file_name",
					// (UUID.randomUUID().toString()).replaceAll("-", "_"));
					// session.setAttribute("start_mark", new Date().getTime());
					//
					// for (IoSession sess : sessions) {
					// if (sess.getId() == session.getId()) {
					// continue;
					// }
					// if (io == 'W') {
					// int id2 = Integer.parseInt(sess.getAttribute("ID")
					// + "");
					// if (id2 != id) {
					// continue;
					// }
					// }
					//
					// IoBuffer buf = IoBuffer.allocate(8);
					// buf.setAutoExpand(true);
					// buf.putChar(type);
					// buf.putChar(io);
					// buf.putInt(id);
					// buf.flip();
					// sess.write(buf);
					//
					// }
				} else if (buff.limit() > 8) {
					Collection<IoSession> sessions = session.getService()
							.getManagedSessions().values();
					char type = buff.getChar(); // 老师 或 学生
					char io = buff.getChar(); // 开通 关闭 唤醒
					int from = buff.getInt(); // from
					int to = buff.getInt(); // to

					for (IoSession sess : sessions) {
						if (Integer.parseInt(sess.getAttribute("ID") + "") == to) {
							sess.write(message);
						}

					}
					session.setAttribute("start_mark", new Date().getTime());
				}
			}
		});

		Executor threadPool = Executors.newCachedThreadPool();
		DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
		chain.addLast("logger", new LoggingFilter());
		chain.addLast("keep-alive", new HachiKeepAliveFilterInMina()); // 心跳
		chain.addLast("threadPool", new ExecutorFilter(threadPool));

		DatagramSessionConfig dcfg = acceptor.getSessionConfig();
		dcfg.setReadBufferSize(1024 * 70);// 设置接收最大字节默认2048
		dcfg.setMaxReadBufferSize(1024 * 70);
		dcfg.setReceiveBufferSize(1024 * 70);// 设置输入缓冲区的大小
		dcfg.setSendBufferSize(1024 * 70);// 设置输出缓冲区的大小
		dcfg.setReuseAddress(true);// 设置每一个非主监听连接的端口可以重用
		acceptor.bind(new InetSocketAddress(PORT));
	}
}
