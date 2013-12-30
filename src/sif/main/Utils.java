package sif.main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;

/***
 * This class provides some helper methods for the application.
 * 
 * 
 * @author Ehssan Doust
 * 
 */
public class Utils {

	public static byte[] reverseByteArray(byte[] array) {

		byte[] inverted = new byte[array.length];
		for (int i = 0; i < array.length; i++) {
			inverted[i] = array[array.length - 1 - i];
		}
		return inverted;

	}

	public static long toLong(byte[] array) {

		return ByteBuffer.wrap(array).getLong();

	}

	public static byte[] toByteArray(long value) {

		return ByteBuffer.allocate(8).putLong(value).array();

	}

	public static long readLong(Socket socket) throws IOException {

		byte[] buffer = new byte[8];
		socket.getInputStream().read(buffer, 0, 8);
		return Utils.toLong(Utils.reverseByteArray(buffer));

	}

	public static String readString(Socket socket) throws IOException {

		// Get the length of the string
		int length = (int) Utils.readLong(socket);

		byte[] buffer = new byte[length];
		socket.getInputStream().read(buffer, 0, length);
		String string = new String(buffer, "UTF-8");
		return string;

	}

	public static byte[] readBytes(Socket socket) throws IOException {

		// Get the length of the string
		int length = (int) Utils.readLong(socket);

		byte[] buffer = new byte[length];
		socket.getInputStream().read(buffer, 0, length);
		return buffer;

	}

	public static void writeString(Socket socket, String value)
			throws Exception {

		byte[] bytes = value.getBytes("UTF-8");
		socket.getOutputStream().write(
				Utils.reverseByteArray(Utils.toByteArray(bytes.length)));
		socket.getOutputStream().write(bytes);
	}

	public static File writeToTempFile(byte[] value) throws Exception {

		File file = File.createTempFile("sif", "xls");
		file.deleteOnExit();

		FileOutputStream outputStream = new FileOutputStream(file);
		outputStream.write(value);
		outputStream.close();

		return file;

	}
}
