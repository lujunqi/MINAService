package com.prism.mina;

import java.net.InetSocketAddress;

import org.apache.mina.core.service.IoConnector;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;


public class MinaObjectClient {
	public static void main(String... a) {
		System.err.println("===========");
		com.prism.mina.InfoBean info = new com.prism.mina.InfoBean();
		IoConnector connector = new NioSocketConnector();
		connector.setConnectTimeoutMillis(30000);
		connector.getFilterChain().addLast("codec",
				new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
		connector.setHandler(new MinaObjectClientHandler(info));
		connector.connect(new InetSocketAddress("10.80.1.212", 3456));
	}
}
