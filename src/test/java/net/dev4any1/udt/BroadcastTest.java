package net.dev4any1.udt;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.dev4any1.udt.Balancer.BroadcastReceiver;
import net.dev4any1.udt.Balancer.BroadcastSender;

public class BroadcastTest {

	private static final String MSG = "Broadcast message";
	private BroadcastSender sender;
	private BroadcastReceiver receiver;

	@BeforeEach
	public void init() throws Exception {
		sender = new BroadcastSender(4446);
		receiver = new BroadcastReceiver(4446);
	}

	private Runnable testBroadcastReceiver = new Runnable() {
		@Override
		public void run() {
			long now = System.currentTimeMillis();
			 while (System.currentTimeMillis() - now < 200) {
				try {
					assertTrue(receiver.receive().startsWith(MSG));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	};

	@Test
	public void testLoadBalancedBroadcast() throws Exception {
		new Thread(testBroadcastReceiver).start();
		new Thread(testBroadcastReceiver).start();
		new Thread(testBroadcastReceiver).start();
		Thread.sleep(200);
		for (int i = 1; i <= 5; i++) {
			sender.send(MSG + i);
		}
	}

	@AfterEach
	public void close() throws InterruptedException {
		sender.close();
		receiver.close();
	}
}