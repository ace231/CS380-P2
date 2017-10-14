/*****************************************
*	Alfredo Ceballos
*	CS 380 - Computer Networks
*	Project 2
*	Professor Nima Davarpanah
*****************************************/
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class PhysLayerClient {
	public static void main(String[] args) {
		Socket socket;
		
		try {
			socket = new Socket("18.221.102.182", 38002);
			// Checking that connection went through
			String address = socket.getInetAddress().getHostAddress();
			System.out.printf("Connected to server: %s%n", address);
			
			// Client input stream to receive data from server	
			InputStream is = socket.getInputStream();
			
			// Client output stream to send data to server
			OutputStream os = socket.getOutputStream();
			
			// Server is sending 64 unsigned byte values that simulate
			// actual signals as the preamble. These have to be averaged
			// out and that value is the baseline.
			int total = 0;
			for(int i = 0; i < 64; i++) {
					total += is.read();
			}
			double baseLine = total / 64.0;
			System.out.printf("Baseline established from preamble: %.2f%n", baseLine);
			
			// Once baseline is found, next 32 "bytes" are actual data
			// Since signals are being simulated and encoded using 4B/5B,
			// a total of 320 byte are being sent
			int[] data = new int[320];
			for(int i = 0; i < 320; i++) {
				data[i] = is.read();
			}
			
			/*
			 * Each incoming byte is a signal that has to be checked against the
			 * baseline. If it is greater than the baseline the it is logic 1,
			 * if not it is logic 0. This binary data is saved in a string for
			 * easy comparison and translation.
			 */
			String rawString = "";
			for(int i = 0; i < 320; i++) {
				if(data[i] >= baseLine) {
					rawString += "1";
				} else {
					rawString += "0";
				}
			}
			
			// Decoding NRZI
			String datString = "";
			if(rawString.charAt(0) == '0'){datString += "0";}
			else{datString += "1";}
			char curr, prev = rawString.charAt(0);
			for(int i = 1; i < 320; i++) {
				curr = rawString.charAt(i);
				if(curr == prev) {
					datString += "0";
				} else {
					datString += "1";
				}
				prev = curr;
			}
			
			// Since data was encoded using 4b/5b, it has to be decoded
		    byte[] bytesToServer = convertData(datString);
			os.write(bytesToServer);	// Sending byte array to server
			
			int response = is.read();
			if(response == 1) {
				System.out.println("Good response!");
			} else {
				System.out.println("Bad response...");
			}
			socket.close();
			System.out.println("Disconnected from server.");
			
		} catch(Exception e) {
			System.out.println("Something broke...");
			e.printStackTrace();
		} // End of try catch
	} // End of main
	
	
	/*
	 * Received 32 bytes of data from the server.
	 * Each bit is an unsigned byte, bits were encoded using 4B/5B. So 
	 * a total of 320 bytes were received. On each pass of the loop an 
	 * initial set of 5 bits are converted from 5 bits to 4 then a 
	 * second set of 5 bits are converted. Those first 4 and second set
	 * of 4 bits are then joined together to form a full 8 bits, a byte,
	 * of data. The end result is a byte array of 32 bytes
	 */
	private static byte[] convertData(String stringDat) {
		int temp, topFour, lowerFour, count = 0;
		byte[] convertedData = new byte[32];
		System.out.println("Received 32 bytes: ");
		for(int i = 0; i < 320; i += 10) {
			
			topFour = fiveBitsTo4bits(stringDat.substring(i, i + 5));
			lowerFour = fiveBitsTo4bits(stringDat.substring(i + 5, i + 10));
			
			temp = ((topFour << 4) | lowerFour);
			System.out.printf("%02X", temp);
			convertedData[count] = (byte)temp;
			count++;
		}
		System.out.println();
		return convertedData;
	}
	
	
	private static int fiveBitsTo4bits(String fiveBits) {
		if(fiveBits.equals("11110")) {
			return 0;
		} else if(fiveBits.equals("01001")) {
			return 1;
		} else if(fiveBits.equals("10100")) {
			return 2;
		} else if(fiveBits.equals("10101")) {
			return 3;
		} else if(fiveBits.equals("01010")) {
			return 4;
		} else if(fiveBits.equals("01011")) {
			return 5;
		} else if(fiveBits.equals("01110")) {
			return 6;
		} else if(fiveBits.equals("01111")) {
			return 7;
		} else if(fiveBits.equals("10010")) {
			return 8;
		} else if(fiveBits.equals("10011")) {
			return 9;
		} else if(fiveBits.equals("10110")) {
			return 10;
		} else if(fiveBits.equals("10111")) {
			return 11;
		} else if(fiveBits.equals("11010")) {
			return 12;
		} else if(fiveBits.equals("11011")) {
			return 13;
		} else if(fiveBits.equals("11100")) {
			return 14;
		} else if(fiveBits.equals("11101")) {
			return 15;
		} else {
			System.out.println("Unacceptable data inputted... ");
			return -1;	// Acts as an error code
		}
	} // End of fiveBitsTo4bits
	
}