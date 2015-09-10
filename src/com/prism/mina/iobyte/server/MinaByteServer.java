package com.prism.mina.iobyte.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioDatagramAcceptor;

import com.prism.mina.iobyte.coder.ByteArrayCodecFactory;

public class MinaByteServer {
	private static final int PORT = 9000;

	public static void main(String[] args) throws IOException {
		
		NioDatagramAcceptor acceptor = new NioDatagramAcceptor();
		DefaultIoFilterChainBuilder filterChain = acceptor.getFilterChain();
		// 添加编码过滤器 处理乱码、编码问题
		filterChain.addLast("codec", new ProtocolCodecFilter(
				new ByteArrayCodecFactory()));
		// 设置核心消息业务处理器
		acceptor.setHandler(new MinaByteHandler());
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 30);

		try {
			acceptor.bind(new InetSocketAddress(PORT));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
