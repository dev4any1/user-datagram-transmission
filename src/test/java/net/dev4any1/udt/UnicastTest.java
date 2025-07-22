package net.dev4any1.udt;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.dev4any1.udt.UDPUnicast.UnicastConsumer;
import net.dev4any1.udt.UDPUnicast.UnicastProducer;
import net.dev4any1.udt.utils.TestOpts;

public class UnicastTest {

	private UnicastConsumer receiver;
	private UnicastProducer sender;
	private long startTime = 0;

	private Runnable testReceiver = new Runnable() {
		@Override
		public void run() {
			try {
				int count = 0;
				do {
					assertEquals(TestOpts.MSG, receiver.receive());
				} while (++count != TestOpts.COUNT);
				long took = System.currentTimeMillis() - startTime;
				TestOpts.info(receiver, "messages received", took, count);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	@BeforeEach
	public void init() throws Exception {
		receiver = new UnicastConsumer(4446, 512);
		sender = new UnicastProducer("localhost", 4446);
	}

	@Test
	public void test() throws Exception {
		new Thread(testReceiver).start();
		Thread.sleep(100);
		startTime = System.currentTimeMillis();
		for (int i = 0; i < TestOpts.COUNT; i++) {
			sender.send(TestOpts.MSG);
		}
	}

	@AfterEach
	public void close() throws InterruptedException {
		receiver.close();
		sender.close();
	}
}
