package net.dev4any1.udt;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.dev4any1.udt.TCPChat.ChatClient;
import net.dev4any1.udt.TCPChat.ChatServer;
import net.dev4any1.udt.utils.TestOpts;

public class ChatTest {

	private ChatServer server;
	private ChatClient client;

	private Runnable testTCPServer = new Runnable() {
		@Override
		public void run() {
			try {
				server.acceptConnection();
				int count = 0;
				do {
					assertEquals(TestOpts.MSG, server.receive());
					server.send(TestOpts.MSG + "reply");
				} while (++count < TestOpts.COUNT);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	@BeforeEach
	public void init() throws Exception {
		server = new ChatServer(6789);
		client = new ChatClient("localhost", 6789);
	}
	@Test
	public void test() throws Exception {
		new Thread(testTCPServer).start();
		long startTime = System.currentTimeMillis();
		for (int i = 0; i < TestOpts.COUNT; i++) {
			client.send(TestOpts.MSG);
			assertEquals(TestOpts.MSG + "reply", client.receive());
		}
		long took = System.currentTimeMillis() - startTime;
		TestOpts.info(client, "requests processed", took, TestOpts.COUNT);

	}
	@AfterEach
	public void close() throws Exception {
		server.close();
		client.close();
	}
}
