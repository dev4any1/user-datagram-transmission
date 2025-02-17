package net.dev4any1.udt;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.dev4any1.udt.Broadcast.MulticastReceiver;
import net.dev4any1.udt.Broadcast.MulticastSender;

public class MulticastTest {
	private static final String MSG = "Broadcast message";
	private static final int TIMES = 5;
	private static MulticastSender sender;
	private List<MulticastReceiver> receivers;

	private Runnable testMulticastReceiver = new Runnable() {
		@Override
		public void run() {
			try {
				MulticastReceiver receiver = new MulticastReceiver("230.0.0.0", 4446);
				receivers.add(receiver);
				int msgsCount = 0;
				while (msgsCount != TIMES) {
					assertTrue(receiver.receive().startsWith(MSG));
					msgsCount++;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	@BeforeEach
	public void init() throws Exception {
		sender = new MulticastSender("230.0.0.0", 4446);
		receivers = new LinkedList<MulticastReceiver>();
	}

	@Test
	public void testBroadcastMulticast() throws Exception {
		new Thread(testMulticastReceiver).start();
		new Thread(testMulticastReceiver).start();
		new Thread(testMulticastReceiver).start();
		Thread.sleep(200);
		for (int i = 1; i <= TIMES; i++) {
			sender.send(MSG + i);
		}
	}

	@AfterEach
	public void close() throws InterruptedException {
		sender.close();
		receivers.forEach(r -> r.close());
	}
}