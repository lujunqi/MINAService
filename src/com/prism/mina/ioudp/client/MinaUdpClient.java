package com.prism.mina.ioudp.client;

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

public class MinaUdpClient {
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
			public void sessionOpened(final IoSession session) {
				new Thread() {
					public void run() {

						SmsObject sms = new SmsObject();
						sms.setCmd("PLAY");
						sms.setInfo(new byte[] { 1, 2, 3 });
						session.write(sms);
						try {
							Thread.sleep(10000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

				}.start();

			}

			@Override
			public void messageReceived(IoSession session, Object message)
					throws Exception {
				SmsObject sms = (SmsObject) message;
				System.out.println(sms.getCmd()+"==============");
//				if(sms.getCmd().trim().equals("NAT")){
					String addr = new String(sms.getInfo());
					String host = addr.split(":")[0];
					int port = Integer.parseInt(addr.split(":")[1]);
					
					SmsObject sms1 = new SmsObject();
					sms1.setCmd("XX");
					sms1.setInfo(new byte[]{1,2});
//					session.write(sms1, new InetSocketAddress(host,port));
					ConnectFuture future = connector.connect(new InetSocketAddress(host, port));
					future.getSession().write(sms1);
					System.out.println(host+"=="+port);
//				}
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
		// future = connector.connect(new InetSocketAddress("120.24.76.197",
		// 9999));
		connector.connect(new InetSocketAddress("10.80.1.212", 9999));
		connector.dispose(true);
	}
}
