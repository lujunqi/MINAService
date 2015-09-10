package com.prism.mina.nat;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MinaNatClient {
	public static void main(String[] args) {
		try {
			
//			DatagramSocket socket = new DatagramSocket(1972);
//			InetAddress ia = InetAddress.getByName("58.20.37.144");
//			socket.connect(ia, 27714);
			
//			DatagramSocket socket = new DatagramSocket(1979);
//			InetAddress ia = InetAddress.getByName("211.149.195.78");
//			socket.connect(ia, 9000);

			DatagramSocket socket = new DatagramSocket();
			InetAddress ia = InetAddress.getByName("10.80.1.212");
			socket.connect(ia, 51150);
			
			byte[] buffer = new byte[1024];

			buffer = ("22").getBytes();
			DatagramPacket dp = new DatagramPacket(buffer, buffer.length);

			DatagramPacket dp1 = new DatagramPacket(new byte[22312], 22312);
			socket.send(dp);
			
			boolean flag = true;
			while (flag) {
				socket.receive(dp1);
				byte[] bb = dp1.getData();
				String info = "";
				for (int i = 0; i < dp1.getLength(); i++) {
					info+=(char) bb[i];
					System.out.print((char) bb[i]);
				}
				if(info.startsWith("NAT")){
					String[] nat = info.split(":");
					String host = nat[1];
					int port = Integer.parseInt(nat[2]);
					
					
					InetAddress ia2 = InetAddress.getByName(host);
					socket.connect(ia2, port);
					byte[] buffer2 = new byte[1024];

					buffer2 = ("Nat is Okey!").getBytes();
					DatagramPacket dp2 = new DatagramPacket(buffer2, buffer2.length);
					socket.send(dp2);
					
					
					
				}
			}

			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
