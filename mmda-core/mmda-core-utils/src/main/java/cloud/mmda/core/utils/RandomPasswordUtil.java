package cloud.mmda.core.utils;

import java.util.Random;

public class RandomPasswordUtil {
	private static char[] chars = { '0', '1', '2', '3', '4', '5', '6', '7',
		     '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
		     'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
		     'y', 'z', 'A', 'B','C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
		     'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y',
		     'Z','!','@','#','$','%','^','&','*','~'};
	
	public static String getRandomPassword(int length){
		
		return getRandomLetterPassword(length/2)+getRandomDigitPassword(length-length/2);
	}
	
	private static String getRandomDigitPassword(int length){
		Random random = new Random();
		StringBuilder password = new StringBuilder("");
		for (int m = 1; m <= length; m++) {
			password.append(chars[random.nextInt(10)]);
		}
		return password.toString();
	}
	
	private static String getRandomLetterPassword(int length){
		Random random = new Random();
		StringBuilder password = new StringBuilder("");
		for (int m = 1; m <= length; m++) {
			password.append(chars[10+random.nextInt(51)]);
		}
		return password.toString();
	}
}
