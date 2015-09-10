package com.prism.socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

public class MulticastSender {
	public static void main(String[] args) throws IOException {
		byte[] msg = new String("sender2").getBytes();
		/*
		 * 在Java UDP中单播与广播的代码是相同的,要实现具有广播功能的程序只需要使用广播地址即可, 例如：这里使用了本地的广播地址
		 */
		InetAddress inetAddr = InetAddress.getByName("255.255.255.255");
		DatagramSocket client = new DatagramSocket();
		DatagramPacket receive = new DatagramPacket(new byte[1024], 1024);
		DatagramPacket sendPack = new DatagramPacket(msg, msg.length, inetAddr,
				8888);
		while (true) {
			client.send(sendPack);
			client.receive(receive);
			byte[] recvByte = Arrays.copyOfRange(receive.getData(), 0,
					receive.getLength());
			System.out.println("Sender receive msg:" + new String(recvByte));
		}
		// System.out.println("Client send msg complete");
		// client.close();
	}
}
