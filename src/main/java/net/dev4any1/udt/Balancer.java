package net.dev4any1.udt;

import java.io.Closeable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Balancer {
	/**
	 * Broadcasts is using 255.255.255.255 or the network's broadcast address, e.g.,
	 * 192.168.1.255
	 */
	public static class BroadcastSender implements Closeable{
		private DatagramSocket socket;
		private InetAddress broadcastAddress;
		private int port;

		public BroadcastSender(int port) throws Exception {
			this.port = port;
			this.socket = new DatagramSocket();
			this.socket.setBroadcast(true);
			this.broadcastAddress = InetAddress.getByName("255.255.255.255");
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

	public static class BroadcastReceiver implements Closeable{

		private DatagramSocket socket;

		public BroadcastReceiver(int port) throws Exception {
			this.socket = new DatagramSocket(port);
		}

		public String receive() throws Exception {
			byte[] buffer = new byte[256];
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			socket.receive(packet); // Wait for a message
			return new String(packet.getData(), 0, packet.getLength());
		}

		public void close() {
	        socket.close();
		}
		
		public boolean isClosed(){
			return socket.isClosed();
		}
	}
}
