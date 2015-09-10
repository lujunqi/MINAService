package com.prism.socket;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class SocketServer {
	public static final int PORT = 9001;// 监听的端口号

	public static void main(String[] args) {
		System.out.println("服务器启动...\n");
		SocketServer server = new SocketServer();
		server.init();
	}

	private Set<Socket> address = new HashSet<Socket>();

	public void init() {
		try {
			@SuppressWarnings("resource")
			ServerSocket serverSocket = new ServerSocket(PORT);
			while (true) {
				// 一旦有堵塞, 则表示服务器与客户端获得了连接
				Socket client = serverSocket.accept();
				System.out.println(client.getInetAddress().toString());
				System.out.println(client.getPort());
				address.add(client);
				System.out.println(address);
				// 处理这次连接
				new HandlerThread(client);
			}
		} catch (Exception e) {
			System.out.println("服务器异常: " + e.getMessage());
		}
	}

	private class HandlerThread implements Runnable {
		private Socket socket;

		public HandlerThread(Socket client) {
			socket = client;
			new Thread(this).start();
		}

		public void run() {
			try {
				InputStream in = socket.getInputStream();
				//
				while (true) {
					if (in.available() >= 1024*2) {
						byte[] b = new byte[1024*2];
						in.read(b);
//						int i = in.read();
						for (Socket socket2 : address) {
							String line1 = socket.getInetAddress() + ":"
									+ socket.getPort();
							String line2 = socket2.getInetAddress() + ":"
									+ socket2.getPort();
							
							if (!line1.equals(line2)) {
								try {
									OutputStream out = socket2
											.getOutputStream();
									
									out.write(b);
								} catch (Exception e) {
									address.remove(socket2);
									e.printStackTrace();
								}
							}
						}

					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("服务器 run 异常: " + e.getMessage());
			} finally {
				if (socket != null) {
					try {
						socket.close();
					} catch (Exception e) {
						socket = null;
						System.out.println("服务端 finally 异常:" + e.getMessage());
					}
				}
			}
		}
	}
}
