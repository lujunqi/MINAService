package com.prism.mina.iobuffer.client;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.DatagramSessionConfig;
import org.apache.mina.transport.socket.nio.NioDatagramConnector;

import com.prism.mina.iobuffer.coder.CmccSipcCodecFactory;
import com.prism.mina.iobuffer.dto.SmsObject;

public class Client {

	public static ConnectFuture future;
	public static boolean flag = true;

	public static void main(String args[]) throws Throwable {
		NioDatagramConnector connector = new NioDatagramConnector();
		connector.setHandler(new IoHandlerAdapter() {
			@Override
			public void exceptionCaught(IoSession session, Throwable cause)
					throws Exception {
				System.out.println("服务器发生异常： {}" + cause.getMessage());
			}

			@Override
			public void sessionOpened(final IoSession session) {
				new Thread() {
					public void run() {
						while (true) {
							SmsObject sms = new SmsObject();
							sms.setCmd("play");
							sms.setInfo(new byte[] { 1, 2, 3 });
							session.write(sms);
							try {
								Thread.sleep(10000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				}.start();

			}

			@Override
			public void messageReceived(IoSession session, Object message)
					throws Exception {
				SmsObject sms = (SmsObject) message;
				System.out.println(sms.getCmd() + "===" + sms.getInfo().length);
			}

		});
		connector.setConnectTimeoutMillis(30000);
		DefaultIoFilterChainBuilder filterChain = connector.getFilterChain();
		filterChain.addLast("codec", new ProtocolCodecFilter(
				new CmccSipcCodecFactory(Charset.forName("UTF-8"))));
		// filterChain.addLast("keep-alive", new HachiKeepAliveFilterInMina());
		// // 心跳
		DatagramSessionConfig dcfg = connector.getSessionConfig();
		dcfg.setReadBufferSize(1024 * 70);// 设置接收最大字节默认2048
		dcfg.setMaxReadBufferSize(1024 * 70);
		dcfg.setReceiveBufferSize(1024 * 70);// 设置输入缓冲区的大小
		dcfg.setSendBufferSize(1024 * 70);// 设置输出缓冲区的大小
		dcfg.setReuseAddress(true);// 设置每一个非主监听连接的端口可以重用
//		future = connector.connect(new InetSocketAddress("120.24.76.197", 9999));
		future = connector.connect(new InetSocketAddress("10.80.1.212", 9999));
	}
}
