package dv512.controller.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class PropUtils {

	public static Properties read(String file) {		
		Properties prop = new Properties();

		InputStream is = PropUtils.class.getClassLoader()
				.getResourceAsStream(file);

		if (is != null) {
			try {
				prop.load(is);
				return prop;
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		} 
	
		return null;		
	}
	
}
