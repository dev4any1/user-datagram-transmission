package net.dev4any1.udt;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.dev4any1.udt.UDPBalancer.CompetingConsumer;
import net.dev4any1.udt.UDPBalancer.BalanceProducer;
import net.dev4any1.udt.utils.TestOpts;

public class BalancerTest {

	static final String BROADCAST_ADDR = "255.255.255.255";

	private BalanceProducer sender;
	private CompetingConsumer receiver;
	private long startTime = 0;

	@BeforeEach
	public void init() throws Exception {
		sender = new BalanceProducer(4446, BROADCAST_ADDR);
		receiver = new CompetingConsumer(4446, 512);
	}

	private Runnable sharedBroadcastReceiver = new Runnable() {
		@Override
		public void run() {
			int count = 0;
			try {
				do {
					assertEquals(TestOpts.MSG, receiver.receive());
				} while (++count != TestOpts.COUNT);
			} catch (Exception e) {
				e.printStackTrace();
			}
			long took = System.currentTimeMillis() - startTime;
			TestOpts.info(receiver, "messages received", took, TestOpts.COUNT);
		}
	};

	@Test
	public void test() throws Exception {
		new Thread(sharedBroadcastReceiver).start();
		new Thread(sharedBroadcastReceiver).start();
		new Thread(sharedBroadcastReceiver).start();
		Thread.sleep(100);
		startTime = System.currentTimeMillis();
		for (int i = 1; i <= TestOpts.COUNT * 3; i++) {
			sender.send(TestOpts.MSG);
		}
	}

	@AfterEach
	public void close() throws InterruptedException {
		sender.close();
		receiver.close();
	}
}