package com.prism.socket.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SocketUdpServer {
	public static int size = 1024 * 2;

	public static void main(String[] args) throws IOException {

		final DatagramSocket ds = new DatagramSocket(9000);
		final Set<String> address = new HashSet<String>();
		boolean flag = true;
		while (flag) {// 无数据，则循环
			byte[] receiveByte = new byte[640 + 4];
			DatagramPacket dataPacket = new DatagramPacket(receiveByte,
					receiveByte.length);
			ds.receive(dataPacket);
			byte[] tmpBuf = null;

			String line = dataPacket.getSocketAddress().toString();
			String head = new String(Arrays.copyOf(receiveByte, 4));

			if ("play".equals(head)) {// 播放
				address.add(line);
			} else {
				tmpBuf = Arrays.copyOf(receiveByte, size);
			}
			if ("audi".equals(head) || "draw".equals(head)) {// 音频 绘图
				for (String a : address) {
					String host = a.split(":")[0].substring(1);
					int port = Integer.parseInt(a.split(":")[1]);
					try {
						if (tmpBuf != null) {
							DatagramPacket packet = new DatagramPacket(tmpBuf,
									tmpBuf.length, InetAddress.getByName(host),
									port);
							ds.send(packet);
						}
					} catch (UnknownHostException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}

				}
			}
		}
		ds.close();
	}
}
