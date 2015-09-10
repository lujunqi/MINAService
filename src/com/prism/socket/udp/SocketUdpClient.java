package com.prism.socket.udp;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

public class SocketUdpClient {
	public static DatagramSocket socket;
	public static boolean flag = true;

	public static void main(String[] args) throws Exception {
		socket = new DatagramSocket();


		new Thread() {
			public void run() {
				while (flag) {
					byte[] buffer = new byte[640  + 4];
					buffer[0] = 'p';
					buffer[1] = 'l';
					buffer[2] = 'a';
					buffer[3] = 'y';
					
					
					try {
						DatagramPacket packet = new DatagramPacket(buffer,
								buffer.length,
								InetAddress.getByName("127.0.0.1"), 9000);
						socket.send(packet);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();

		while (flag) {
			byte[] receiveByte = new byte[3];
			DatagramPacket dataPacket = new DatagramPacket(receiveByte,
					receiveByte.length);
			
			socket.receive(dataPacket);
			
				System.out.println(receiveByte[0]+";"+receiveByte[1]+";"+receiveByte[2]);
			
			
			
		}

		socket.close();

	}
}
