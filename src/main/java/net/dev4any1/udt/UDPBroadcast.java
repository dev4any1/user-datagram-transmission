package net.dev4any1.udt;

import java.io.Closeable;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class UDPBroadcast {

	public static class MulticastPublisher implements Closeable {

		private MulticastSocket socket;
		private InetAddress group;
		private int port;

		public MulticastPublisher(String groupAddress, int port) throws Exception {
			this.group = InetAddress.getByName(groupAddress);
			this.port = port;
			this.socket = new MulticastSocket();
		}

		public void send(String text) throws Exception {
			DatagramPacket packet = new DatagramPacket(text.getBytes(), text.length(), group, port);
			socket.send(packet);
		}

		public void close() {
			socket.close();
		}
	}

	public static class MulticastSubscriber implements Closeable {
		private MulticastSocket socket;
		private InetAddress group;
		private int port;
		private NetworkInterface networkInterface;
		private int bufferSize;
	
		public MulticastSubscriber(String groupAddress, int port, int bufferSize) throws Exception {
			this.group = InetAddress.getByName(groupAddress);
			this.port = port;
			this.socket = new MulticastSocket(port);
			this.networkInterface = getNetworkInterface();
			this.bufferSize = bufferSize;
			socket.joinGroup(new InetSocketAddress(group, port), networkInterface);
		}

		public String receive() throws Exception {
			byte[] buffer = new byte[bufferSize];
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			socket.receive(packet);
			return new String(packet.getData(), 0, packet.getLength());
		}

		public void close() {
			try {
				socket.leaveGroup(new InetSocketAddress(group, port), networkInterface);
			} catch (IOException e) {
				System.err.print("IOException while leaving group " + e.getMessage());
			}
			socket.close();
		}

		private NetworkInterface getNetworkInterface() throws SocketException {
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements()) {
				NetworkInterface netIf = interfaces.nextElement();
				if (netIf.isUp() && !netIf.isLoopback()) {
					return netIf;
				}
			}
			throw new RuntimeException("No suitable network interface found.");
		}
	}
}
