package dv512.controller.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;

public class ImgUtils {

	public static final int TYPE_PROFILE_PIC = 0;
	public static final int TYPE_DOG_PIC = 1;
	
	private final static File PATH_PROFILE = new File("images/profile");
	private final static File PATH_DOG = new File("images/dog");
	
	public final static String IMAGE_FORMAT = "jpg";
	public final static String IAMGE_MIME_TYPE = "image/jpeg";
	private final static int SAVE_SIZE = 500;
	
	
	public static File getFolder(int type) {
		if(type == TYPE_PROFILE_PIC) {
			if(!PATH_PROFILE.exists()) {
				PATH_PROFILE.mkdirs();
			}
			
			return PATH_PROFILE;
		}
		else if(type == TYPE_DOG_PIC) {
			if(!PATH_DOG.exists()) {
				PATH_DOG.mkdirs();
			}
			
			return PATH_DOG;
		}

		return null;
	}
	
	public static File createPath(int type, int id) {
		return null; //new File(getFolder(type), id + "-" 
					//+ System.currentTimeMillis() + "." + SAVE_FORMAT);
					
	}
	
	/*
	public static boolean saveImage(File path, InputStream is) {
		try {
			BufferedImage img = ImageIO.read(is);
			
			BufferedImage small = Scalr.resize(img, Scalr.Method.SPEED, 
					Scalr.Mode.AUTOMATIC, SAVE_SIZE, SAVE_SIZE, Scalr.OP_ANTIALIAS);
			
			ImageIO.write(small, SAVE_FORMAT, path);	
			return true;
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
				
		return false;		
	} */
	
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
	
	
	
	public static boolean delete(String name, int type) {
		if(name == null) 
			return false;
		
		File f = new File(getFolder(type), name);
		if(f.exists()) {
			return f.delete();			
		}
	
		return false;
	}
	
}
