package net.dev4any1.udt;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.dev4any1.udt.UDPUnicast.UDPReceiver;
import net.dev4any1.udt.UDPUnicast.UDPSender;

public class UDPUnicastTest {

	private static final String MSG = "UDP message";
	private UDPReceiver receiver;
	private UDPSender sender;

	private Runnable testReceiver = new Runnable() {
		@Override
		public void run() {
			try {
				assertEquals(MSG, receiver.receive());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	@BeforeEach
	public void init() throws Exception {
		receiver = new UDPReceiver(4446);
		sender = new UDPSender("localhost", 4446);
	}

	@Test
	public void testUDPUnicast() throws Exception {
		new Thread(testReceiver).start();
		Thread.sleep(200);
		sender.send(MSG);
	}

	@AfterEach
	public void close() throws InterruptedException {
		receiver.close();
		sender.close();
	}
}
