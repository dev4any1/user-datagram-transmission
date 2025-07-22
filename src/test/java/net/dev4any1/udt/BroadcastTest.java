package net.dev4any1.udt;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.dev4any1.udt.UDPBroadcast.MulticastPublisher;
import net.dev4any1.udt.UDPBroadcast.MulticastSubscriber;
import net.dev4any1.udt.utils.TestOpts;

public class BroadcastTest {

	static final String MULTICAST_ADDR = "230.0.0.0";

	private MulticastPublisher sender;
	private List<MulticastSubscriber> receivers;
	private long startTime = 0;

	private Runnable testMulticastReceiver = new Runnable() {
		@Override
		public void run() {
			try {
				MulticastSubscriber receiver = new MulticastSubscriber(MULTICAST_ADDR, 4446, 512);
				receivers.add(receiver);
				int msgsCount = 0;
				do {
					assertEquals(TestOpts.MSG, receiver.receive());
				} while (++msgsCount != TestOpts.COUNT);
				long took = System.currentTimeMillis() - startTime;
				TestOpts.info(receiver, "messages received", took, TestOpts.COUNT);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	@BeforeEach
	public void init() throws Exception {
		sender = new MulticastPublisher(MULTICAST_ADDR, 4446);
		receivers = new LinkedList<MulticastSubscriber>();
	}
	@Test
	public void test() throws Exception {
		new Thread(testMulticastReceiver).start();
		new Thread(testMulticastReceiver).start();
		new Thread(testMulticastReceiver).start();
		Thread.sleep(100);
		startTime = System.currentTimeMillis();
		for (int i = 1; i <= TestOpts.COUNT; i++) {
			sender.send(TestOpts.MSG);
		}
	}
	@AfterEach
	public void close() throws InterruptedException {
		sender.close();
		receivers.forEach(r -> r.close());
	}
}