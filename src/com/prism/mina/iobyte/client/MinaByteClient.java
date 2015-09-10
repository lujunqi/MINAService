package com.prism.mina.iobyte.client;

import java.net.InetSocketAddress;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioDatagramConnector;

import com.prism.mina.iobyte.coder.ByteArrayCodecFactory;

public class MinaByteClient {
	public static void main(String args[]) throws Throwable {

		try {

			NioDatagramConnector connector = new NioDatagramConnector();

			// 设置链接超时时间
			connector.setConnectTimeoutMillis(3000);
			// 获取过滤器链
			DefaultIoFilterChainBuilder filterChain = connector
					.getFilterChain();
			//
			filterChain.addLast("codec", new ProtocolCodecFilter(
					new ByteArrayCodecFactory()));

			connector.setHandler(new IoHandlerAdapter() {
				@Override
				public void messageReceived(IoSession session, Object msg)
						throws Exception {
					byte[] b = (byte[])msg;
					for (int i = 0; i < b.length; i++) {
						System.out.print(b[i]+",");
					}
					System.out.println("\n"+b.length);

				}

				@Override
				public void sessionOpened(IoSession ssn) throws Exception {
					byte[] b = new byte[1025];
					for (int i = 0; i < 1024; i++) {
						b[i] = (byte)(i % 10);
					}
					b[1024] = 1;
					ssn.write(b);
					System.out.println("session open for "
							+ ssn.getRemoteAddress());

				}
			});
			ConnectFuture future = connector.connect(new InetSocketAddress(
					"10.80.1.212", 9000));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
