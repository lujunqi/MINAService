package com.prism.socket.nat;

public class UDPServer extends UDPAgent {
	public static void main(String[] args) throws Exception {
		new UDPServer(2008).start();
	}

	public UDPServer(int port) {
		super(port);
	}

}
