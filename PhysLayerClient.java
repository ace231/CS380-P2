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

public class PhysLayerClient {
	public static void main(String[] args) {
		
		Socket socket;
		
		try {
			socket = new Socket("18.221.102.182", 38002);
			// Checking that connection went through
			String address = socket.getInetAddress().getHostAddress();
			System.out.printf("Connected to %s%n", address);
			
			// Client input stream to receive data from server	
			InputStream is = socket.getInputStream();
			//InputStreamReader isr = new InputStreamReader(is,"UTF-8");
			//BufferedReader br = new BufferedReader(isr);
			
			// Client output stream to send data to server
			OutputStream os = socket.getOutputStream();
			
			// Server is sending 64 unsigned byte values that simulate
			// actual signals as the preamble. These have to be averaged
			// out and that value is the baseline.
			int total = 0;
			for(int i = 0; i < 64; i++) {
					int temp = is.read();
					total += temp;
			}
			double baseLine = total / 64.0;
			System.out.printf("Baseline established from preamble: %f%n", baseLine);
			
			// Once baseline is found, next 32 bytes are actual data
			int[] data = new int[32];
			for(int i = 0; i < 32; i++) {
				data[i] = is.read();
			}
			// Since data was encoded using 4b/5b, it has to be decoded
			
			data = convertData(data);
			
		} catch(Exception e) {
			System.out.println("Something broke...");
		}
	}
	
	
	private static int[] convertData(int[] dat) {
		byte emptyByte = 0;
		System.out.println("Received 32 bytes: ");
		for(int i = 0; i < 32; i++) {
			System.out.printf("0x%08X  %s%n", dat[i], Integer.toHexString(dat[i]));
			//System.out.print(Integer.toHexString(dat[i]).toUpperCase());
		}
		System.out.println();
		return null; //placeholder
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
		} else if(fiveBits.equals("")) {
			return 16;
		}
		return -1;	// Acts as an error code
	}
}