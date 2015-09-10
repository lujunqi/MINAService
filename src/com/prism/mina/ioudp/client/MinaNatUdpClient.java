package com.prism.mina.ioudp.client;

import java.net.InetSocketAddress;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.DatagramSessionConfig;
import org.apache.mina.transport.socket.nio.NioDatagramConnector;

import com.prism.mina.ioudp.coder.HachiKeepAliveFilterInMina;

public class MinaNatUdpClient {
	private static final String IoHandlerAdapter = null;

	public static void main(String[] args) {
		final NioDatagramConnector connector = new NioDatagramConnector();
		connector.setHandler(new IoHandlerAdapter() {
			@Override
			public void exceptionCaught(IoSession session, Throwable cause)
					throws Exception {
				System.out.println("服务器发生异常： {}" + cause.getMessage());
			}

			@Override
			public void sessionClosed(IoSession session) {
				System.out.println(session.getRemoteAddress()
						+ "==========================" + session.getId());
			}

			@Override
			public void sessionOpened(IoSession session) {
				System.out.println(session.getRemoteAddress()
						+ "========[open]==================" + session.getId());
				IoBuffer buf = IoBuffer.allocate(8);
				buf.setAutoExpand(true);
				buf.putChar('T');
				buf.putChar('W');
				buf.putInt(1);
				buf.flip();
				session.write(buf);

			}

			@Override
			public void messageReceived(IoSession session, Object message)
					throws Exception {

				IoBuffer buff = (IoBuffer) message;
				System.out.println(buff.limit());
				byte[] b = new byte[buff.limit()];
				buff.get(b);
				buff.flip();
				for (int i = 0; i < b.length; i++) {
					System.out.print(b[i]+",");
				}
				if (buff.limit() == 8) {
					char type = buff.getChar(); // 老师 或 学生
					char io = buff.getChar(); // 开通或关闭
					int id = buff.getInt(); // id
					System.out
							.println("[51]====" + type + "==" + io + "=" + id);
				} else {
					
					
						char type = buff.getChar();
						
						if(type=='D'){
							while(buff.position()<buff.limit()){
								char cmd = buff.getChar();
								float x = buff.getFloat();
								float y = buff.getFloat();
							}
						}
					
					
						if (type == 'A') { // audio
							int len = buff.getInt();
							byte[] audio = new byte[len];
							buff.get(audio);
							for (int i = 0; i < audio.length; i++) {
								System.out.print(audio[i]+",");	
							}
							
						
						}
					
				}
			}

		});
		connector.setConnectTimeoutMillis(30000);
		connector.setConnectTimeoutCheckInterval(10000);
		DefaultIoFilterChainBuilder filterChain = connector.getFilterChain();
		// filterChain.addLast("codec", new ProtocolCodecFilter(
		// new CmccSipcCodecFactory(Charset.forName("UTF-8"))));
		// 心跳
		filterChain.addLast("keep-alive", new HachiKeepAliveFilterInMina());

		DatagramSessionConfig dcfg = connector.getSessionConfig();
		dcfg.setReadBufferSize(1024 * 70);// 设置接收最大字节默认2048
		dcfg.setMaxReadBufferSize(1024 * 70);
		dcfg.setReceiveBufferSize(1024 * 70);// 设置输入缓冲区的大小
		dcfg.setSendBufferSize(1024 * 70);// 设置输出缓冲区的大小
		dcfg.setReuseAddress(true);// 设置每一个非主监听连接的端口可以重用
//		ConnectFuture connFuture = connector.connect(new InetSocketAddress("120.24.76.197",
//		 9999));
		ConnectFuture connFuture = connector.connect(new InetSocketAddress(
				"10.80.1.212", 9999));
		connFuture.awaitUninterruptibly();
		IoSession session = connFuture.getSession();
		try {
			Thread.sleep(200);
			IoBuffer buf2 = IoBuffer.allocate(10);
			buf2.setAutoExpand(true);
			buf2.putChar('T');
			buf2.putChar('A');
			buf2.putInt(1);
			buf2.putInt(2);
			buf2.putInt(3);
			buf2.putInt(4);
			buf2.putInt(5);
			
			buf2.flip();
			session.write(buf2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// 关闭连接
		session.getCloseFuture().awaitUninterruptibly();
		connector.dispose();

	}

}
