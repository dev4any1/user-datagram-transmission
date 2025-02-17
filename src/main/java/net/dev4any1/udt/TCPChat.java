package net.dev4any1.udt;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Request-Reply Guaranteed Delivery
 * https://www.enterpriseintegrationpatterns.com/patterns/messaging/RequestReply.html
 * https://www.enterpriseintegrationpatterns.com/patterns/messaging/GuaranteedMessaging.html
 */
public class TCPChat {
	public static class TCPClient implements Closeable{
		private Socket socket;
		private PrintWriter out;
		private BufferedReader in;

		public TCPClient(String address, int port) throws Exception {
			this.socket = new Socket(address, port);
			this.out = new PrintWriter(socket.getOutputStream(), true);
			this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		}

		public void send(String text) {
			out.println(text);
		}

		public String receive() throws Exception {
			return in.readLine();
		}

		public void close() {
			try {
			socket.close();
			} catch (IOException e) {
				System.err.print("IOException while closing TCPClient socket " + e.getMessage());
			}
		}
	}

	public static class TCPServer implements Closeable{
		private ServerSocket serverSocket;
		private Socket socket;
		private PrintWriter out;
		private BufferedReader in;

		public TCPServer(int port) throws Exception {
			this.serverSocket = new ServerSocket(port);
		}

		public void acceptConnection() throws Exception {
			this.socket = serverSocket.accept();
			this.out = new PrintWriter(socket.getOutputStream(), true);
			this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		}

		public String receive() throws Exception {
			return in.readLine();
		}

		public void send(String text) {
			out.println(text);
		}

		public void close() {
			try {
				socket.close();
				serverSocket.close();
			} catch (IOException e) {
				System.err.print("IOException while closing TCPServer sockets " + e.getMessage());
			}
		}
	}
}
