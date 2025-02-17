package net.dev4any1.udt;

import java.io.Closeable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPUnicast {
	public static class UDPSender implements Closeable {
		private DatagramSocket socket;
		private InetAddress address;
		private int port;

		public UDPSender(String address, int port) throws Exception {
			this.address = InetAddress.getByName(address);
			this.port = port;
			this.socket = new DatagramSocket();
		}

		public void send(String text) throws Exception {
			DatagramPacket packet = new DatagramPacket(text.getBytes(), text.length(), address, port);
			socket.send(packet);
		}

		public void close() {
			socket.close();
		}
	}

	public static class UDPReceiver implements Closeable {
		private DatagramSocket socket;

		public UDPReceiver(int port) throws Exception {
			this.socket = new DatagramSocket(port);
		}

		public String receive() throws Exception {
			byte[] buffer = new byte[256];
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			socket.receive(packet);
			return new String(packet.getData(), 0, packet.getLength());
		}

		public void close() {
			socket.close();
		}
	}
}
