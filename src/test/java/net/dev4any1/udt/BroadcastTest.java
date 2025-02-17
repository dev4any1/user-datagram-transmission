package net.dev4any1.udt;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.dev4any1.udt.UDPBroadcast.MulticastPublisher;
import net.dev4any1.udt.UDPBroadcast.MulticastSubscriber;
import net.dev4any1.udt.utils.Log;

public class BroadcastTest {
	private static final String MULTICAST_ADDR = "230.0.0.0";
	private static final int PORT = 4446;
	private static final String MSG = "Broadcast ";
	private static final int COUNT = 10000;
	private MulticastPublisher sender;
	private List<MulticastSubscriber> receivers;
	private long startTime = 0;

	private Runnable testMulticastReceiver = new Runnable() {
		@Override
		public void run() {
			try {
				MulticastSubscriber receiver = new MulticastSubscriber(MULTICAST_ADDR, PORT, 512);
				receivers.add(receiver);
				int msgsCount = 0;
				do {
					assertEquals(MSG, receiver.receive());
				} while (++msgsCount != COUNT);
				long took = System.currentTimeMillis() - startTime;
				Log.info(receiver, COUNT + " messages received in " + took + " ms. Rate: " + COUNT / took + " msg./ms.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	@BeforeEach
	public void init() throws Exception {
		sender = new MulticastPublisher(MULTICAST_ADDR, PORT);
		receivers = new LinkedList<MulticastSubscriber>();
	}

	@Test
	public void testBroadcastMulticast() throws Exception {
		new Thread(testMulticastReceiver).start();
		new Thread(testMulticastReceiver).start();
		new Thread(testMulticastReceiver).start();
		Thread.sleep(100);
		startTime = System.currentTimeMillis();
		for (int i = 1; i <= COUNT; i++) {
			sender.send(MSG);
		}
	}

	@AfterEach
	public void close() throws InterruptedException {
		sender.close();
		receivers.forEach(r -> r.close());
	}
}