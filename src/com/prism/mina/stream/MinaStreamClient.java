package com.prism.mina.stream;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.handler.stream.StreamIoHandler;
import org.apache.mina.transport.socket.nio.NioDatagramConnector;

public class MinaStreamClient {
	public static void main(String[] args) {
		try {
			NioDatagramConnector connector = new NioDatagramConnector();
			connector.setHandler(new StreamIoHandler() {
				@Override
				protected void processStreamIo(IoSession session,
						final InputStream in, OutputStream out) {
					// pool.execute(new Work(session, in, out));

					try {
						new Thread() {
							public void run() {
								try {
									System.out
											.println("client in process stream.."
													+ in.read());
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}.start();

						String fileName = "e:/xx.log";
						File f = new File(fileName);
						byte[] buf = new byte[2048];
						FileInputStream fis = new FileInputStream(f);
						int offset = 0;
						while (true) {
							offset = fis.read(buf);
							if (offset == -1) {
								break;
							}
							System.out.println(offset);
							out.write(buf, 0, offset);
						}
						fis.close();
						System.out.println("over..");
					} catch (IOException ex) {
					}
				}
			});

			ConnectFuture future1 = connector.connect(new InetSocketAddress(
					"127.0.0.1", 8889));
			future1.awaitUninterruptibly();
			if (!future1.isConnected()) {
				System.out.println("no connect...");
			}
//			IoSession session = future1.getSession();
			System.out.println("session==>start");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
