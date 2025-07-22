package net.dev4any1.udt;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Reliable TCP Client / Server show case
 * 
 * @see <a href=
 *      "https://www.enterpriseintegrationpatterns.com/patterns/messaging/RequestReply.html">Request-Reply</a>
 * @see <a href=
 *      "https://www.enterpriseintegrationpatterns.com/patterns/messaging/GuaranteedMessaging.html">Guaranteed
 *      Delivery</a>
 * 
 * @author soulaway
 */
public class TCPChat {

	public static class ChatClient implements Closeable {

		private Socket socket;
		private PrintWriter out;
		private BufferedReader in;

		public ChatClient(String address, int port) throws Exception {
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

	public static class ChatServer implements Closeable {

		private ServerSocket serverSocket;
		private Socket socket;
		private PrintWriter out;
		private BufferedReader in;

		public ChatServer(int port) throws Exception {
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
