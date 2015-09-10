package com.prism.mina.stream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.handler.stream.StreamIoHandler;
import org.apache.mina.transport.socket.nio.NioDatagramAcceptor;

public class MinaStreamServer {

	public void createServerStream() {
		NioDatagramAcceptor acceptor = new NioDatagramAcceptor();
		acceptor.setHandler(new StreamIoHandler() {
			private ExecutorService pool = Executors.newCachedThreadPool();

			@Override
			protected void processStreamIo(IoSession session, InputStream in,
					OutputStream out) {
				System.out.println("process stream...");
				pool.execute(new Work(session, in, out));
			}
		});
		try {
			acceptor.bind(new InetSocketAddress(8889));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		MinaStreamServer server = new MinaStreamServer();
		server.createServerStream();
	}
}
