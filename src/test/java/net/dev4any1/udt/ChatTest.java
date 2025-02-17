package net.dev4any1.udt;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.dev4any1.udt.TCPChat.TCPClient;
import net.dev4any1.udt.TCPChat.TCPServer;
import net.dev4any1.udt.utils.Log;

public class ChatTest {

	private static final String MSG = "TCP echo ";
	private static final int COUNT = 10000;
	private TCPServer server;
	private TCPClient client;

	private Runnable testTCPServer = new Runnable() {
		@Override
		public void run() {
			try {
				server.acceptConnection();
				int count = 0;
				do {
					assertEquals(MSG, server.receive());
					server.send(MSG + "reply");
				} while (++count < COUNT);
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
		long startTime = System.currentTimeMillis();
		for (int i = 0; i < COUNT; i++) {
			client.send(MSG);
			assertEquals(MSG + "reply", client.receive());
		}
		long took = System.currentTimeMillis() - startTime;
		Log.info(client, COUNT + " requests processed at " + took + " ms. Rate: " + COUNT / took + " msg./ms.");

	}

	@AfterEach
	public void close() throws Exception {
		server.close();
		client.close();
	}
}
