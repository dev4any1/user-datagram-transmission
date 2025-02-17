package net.dev4any1.udt;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.dev4any1.udt.TCPChat.TCPClient;
import net.dev4any1.udt.TCPChat.TCPServer;

public class TCPTest {

	private static final String MSG = "TCP message";
	private TCPServer server;
	private TCPClient client;

	private Runnable testTCPServer = new Runnable() {
		@Override
		public void run() {
			try {
				server.acceptConnection();
				assertEquals(MSG, server.receive());
				server.send(MSG + MSG);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	@BeforeEach
	public void init() throws Exception {
		server = new TCPServer(6789);
		client = new TCPClient("localhost", 6789);
	}

	@Test
	public void testTCPChat() throws Exception {
		new Thread(testTCPServer).start();
		client.send(MSG);
		assertEquals(client.receive().lastIndexOf(MSG), MSG.length());
	}

	@AfterEach
	public void close() throws Exception {
		server.close();
		client.close();
	}
}
