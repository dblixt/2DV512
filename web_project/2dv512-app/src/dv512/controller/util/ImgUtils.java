package dv512.controller.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;

public class ImgUtils {

	public final static String IMAGE_FORMAT = "jpg";
	public final static String IMAGE_MIME_TYPE = "image/jpeg";
	private final static int SAVE_SIZE = 500;
	

	public static InputStream scaleImage(InputStream is) {	
		try {
			BufferedImage img = ImageIO.read(is);
			
			BufferedImage small = Scalr.resize(img, Scalr.Method.SPEED, 
					Scalr.Mode.AUTOMATIC, SAVE_SIZE, SAVE_SIZE, Scalr.OP_ANTIALIAS);
			
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(small, IMAGE_FORMAT, os);
			
			return new ByteArrayInputStream(os.toByteArray());
		} 
		catch (IOException e) {
			e.printStackTrace();
		}	

		return null;
	}
		
}
