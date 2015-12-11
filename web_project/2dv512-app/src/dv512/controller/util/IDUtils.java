package dv512.controller.util;

import java.util.Random;
import java.util.UUID;

public class IDUtils {

	public static final String generateUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}
	
	public static final int generateIntID() {
		return new Random().nextInt(Integer.MAX_VALUE);
	}
	
}
