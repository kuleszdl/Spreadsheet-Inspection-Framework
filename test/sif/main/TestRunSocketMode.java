package sif.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class TestRunSocketMode {
	private static final String POLICYFILE = "test/sif/testdata/TestSanityPolicy.xml";
	private ServerSocket serverSocket;
	/**
	 * Needed for synchronization purposes, as JUnit doesn't wait for created threads
	 */
	private Object synchro = new Object();
	private volatile boolean finished = false;
	@Before
	public void setupSever(){
		try {
			serverSocket = new ServerSocket(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testSocket(){
		String[] params = new String [2];
		params[0] = "socket";
		params[1] = serverSocket.getLocalPort() + "";
		startTransmission();
		Application.main(params);
		try {
			synchronized(synchro){
				synchro.wait(5000);
				Assert.assertTrue("Server communication did not finish in 5 seconds", finished);
			}
		} catch (InterruptedException e) {
			Assert.fail("JUnit thread was interruped while waiting for the server communication");
		}
	}

	private void startTransmission(){
		new Thread(){
			@Override
			public void run() {
				networkTransmission();
			}
		}.start();
	}

	private void networkTransmission(){
		FileInputStream polStream = null;
		Socket s = null;
		try {
			s = serverSocket.accept();

			File polFile = new File(POLICYFILE);
			// Getting the length in reverse byte ordering as expected by SIF
			long length = polFile.length();
			byte[] sendBuffer = new byte[(int) length];
			length = Long.reverseBytes(length);
			ByteBuffer buffer = ByteBuffer.allocate((Long.SIZE / 8));
			buffer.putLong(length);
			byte[] sendingArr = buffer.array();

			// sending the length of the policy
			s.getOutputStream().write(sendingArr);
			polStream = new FileInputStream(polFile);
			int read = 0;
			while ((read = polStream.read(sendBuffer)) > 0){
				s.getOutputStream().write(sendBuffer, 0, read);
			}
			s.getOutputStream().flush();

			buffer.rewind();
			read = -1;
			for (int i = 0; i < (Long.SIZE / 8); ){
				read = s.getInputStream().read(sendingArr, 0, (Long.SIZE / 8) - i);
				Assert.assertTrue(read >= 0);
				buffer.put(sendingArr, 0, read);
				i += read;
			}
			buffer.rewind();
			length = buffer.getLong();
			length = Long.reverseBytes(length);

			buffer = ByteBuffer.allocate((int) length);
			read = -1;
			for (int i = 0; i < length;){
				read = s.getInputStream().read(sendBuffer);
				Assert.assertTrue(read >= 0);
				buffer.put(sendBuffer, 0, read);
				i += read;
			}
			buffer.rewind();
			String answerString = new String(buffer.array(), "UTF8");
			Assert.assertTrue("Answer contained no findings", answerString.contains("iolation"));

		} catch (IOException e) {
			Assert.fail(e.getMessage());
			e.printStackTrace();
		} finally {
			if (polStream != null){
				try {
					polStream.close();
				} catch (IOException e) {}
			}
			if (s != null){
				try {
					s.close();
				} catch (IOException e) {}
			}
			try {
				serverSocket.close();
			} catch (IOException e) {}
			finished = true;
			synchronized(synchro){
				synchro.notify();
			}

		}
		
	}
	
}
