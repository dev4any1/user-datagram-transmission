package net.dev4any1.udt;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.dev4any1.udt.UDPUnicast.UDPConsumer;
import net.dev4any1.udt.UDPUnicast.UDPProducer;
import net.dev4any1.udt.utils.Log;

public class UnicastTest {

	private static final String MSG = "UDP message";
	private static final int COUNT = 10000;
	private UDPConsumer receiver;
	private UDPProducer sender;
	private long startTime = 0;

	private Runnable testReceiver = new Runnable() {
		@Override
		public void run() {
			try {
				int count = 0;
				do {
					assertEquals(MSG, receiver.receive());
				} while (++count != COUNT);
				long took = System.currentTimeMillis() - startTime;
				Log.info(receiver, "messages received", took, count);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	@BeforeEach
	public void init() throws Exception {
		receiver = new UDPConsumer(4446, 512);
		sender = new UDPProducer("localhost", 4446);
	}

	@Test
	public void testUDPUnicast() throws Exception {
		new Thread(testReceiver).start();
		Thread.sleep(100);
		startTime = System.currentTimeMillis();
		for (int i = 0; i < COUNT; i++) {
			sender.send(MSG);
		}
	}

	@AfterEach
	public void close() throws InterruptedException {
		receiver.close();
		sender.close();
	}
}
