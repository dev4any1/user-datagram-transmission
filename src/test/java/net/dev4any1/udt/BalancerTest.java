package net.dev4any1.udt;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.dev4any1.udt.UDPBalancer.UDPCompetingConsumer;
import net.dev4any1.udt.UDPBalancer.UDPProducer;
import net.dev4any1.udt.utils.Log;

public class BalancerTest {
	private static final String BROADCAST_ADDR = "255.255.255.255";
	private static final String MSG = "Competition message";
	private static final int COUNT = 10000;
	private UDPProducer sender;
	private UDPCompetingConsumer receiver;
	private long startTime = 0;

	@BeforeEach
	public void init() throws Exception {
		sender = new UDPProducer(4446, BROADCAST_ADDR);
		receiver = new UDPCompetingConsumer(4446, 512);
	}

	private Runnable testBroadcastReceiver = new Runnable() {
		@Override
		public void run() {
			int count = 0;
			try {
				do {
					assertEquals(MSG, receiver.receive());
				} while (++count != COUNT);
			} catch (Exception e) {
				e.printStackTrace();
			}
			long took = System.currentTimeMillis() - startTime;
			Log.info(receiver, "messages received" , took, COUNT);
		}
	};

	@Test
	public void testLoadBalancedBroadcast() throws Exception {
		new Thread(testBroadcastReceiver).start();
		new Thread(testBroadcastReceiver).start();
		new Thread(testBroadcastReceiver).start();
		Thread.sleep(100);
		startTime = System.currentTimeMillis();
		for (int i = 1; i <= COUNT * 3; i++) {
			sender.send(MSG);
		}
	}

	@AfterEach
	public void close() throws InterruptedException {
		sender.close();
		receiver.close();
	}
}