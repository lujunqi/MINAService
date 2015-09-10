package com.prism.datagrampacket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;

public class DatagramPacketServer {
	public static void main(String[] args) {
		try {

			DatagramPacket receive = new DatagramPacket(new byte[1024], 1024);
			DatagramSocket server = new DatagramSocket(8888);

			System.out.println("---------------------------------");
			System.out.println("Server current start......");
			System.out.println("---------------------------------");

			while (true) {
				server.receive(receive);
				System.out.println(receive.getLength());
				byte[] recvByte = Arrays.copyOfRange(receive.getData(), 0,
						receive.getLength());

				System.out
						.println("Server receive msg:" + new String(recvByte));
			}

		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
