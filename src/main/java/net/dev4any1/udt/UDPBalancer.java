package net.dev4any1.udt;

import java.io.Closeable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPBalancer {

	public static class UDPProducer implements Closeable {

		private DatagramSocket socket;
		private InetAddress broadcastAddress;
		private int port;

		public UDPProducer(int port, String netAddressName) throws Exception {
			this.port = port;
			this.socket = new DatagramSocket();
			this.broadcastAddress = InetAddress.getByName(netAddressName);
		}

		public void send(String message) throws Exception {
			byte[] buffer = message.getBytes();
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length, broadcastAddress, port);
			socket.send(packet);
		}

		public void close() {
			socket.close();
		}
	}

	public static class UDPCompetingConsumer implements Closeable {

		private DatagramSocket socket;
		private int bufferSize;

		public UDPCompetingConsumer(int port, int bufferSize) throws Exception {
			this.socket = new DatagramSocket(port);
			this.bufferSize = bufferSize;
		}

		public String receive() throws Exception {
			byte[] buffer = new byte[bufferSize];
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			socket.receive(packet);
			return new String(packet.getData(), 0, packet.getLength());
		}

		public void close() {
	        socket.close();
		}
	}
}
