public class PhysLayerClient {
	public static void main(String[] args) {
		
	}
	
	private static int 5bitsTo4bits(String 5bits) {
		if(5bits.equals("11110")) {
			return 0;
		} else if(5bits.equals("01001")) {
			return 1;
		} else if(5bits.equals("10100")) {
			return 2;
		} else if(5bits.equals("10101")) {
			return 3;
		} else if(5bits.equals("01010")) {
			return 4;
		} else if(5bits.equals("01011")) {
			return 5;
		} else if(5bits.equals("01110")) {
			return 6;
		} else if(5bits.equals("01111")) {
			return 7;
		} else if(5bits.equals("10010")) {
			return 8;
		} else if(5bits.equals("10011")) {
			return 9;
		} else if(5bits.equals("10110")) {
			return 10;
		} else if(5bits.equals("10111")) {
			return 11;
		} else if(5bits.equals("11010")) {
			return 12;
		} else if(5bits.equals("11011")) {
			return 13;
		} else if(5bits.equals("11100")) {
			return 14;
		} else if(5bits.equals("11101")) {
			return 15;
		} else if(5bits.equals("")) {
			return 16;
		}
	}
}