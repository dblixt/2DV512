package dv512;

import java.io.File;
import java.io.FilenameFilter;

public class ProfilePicUtils {

	private final static File PATH = new File("images/profile");
	public final static String[] SUPPORTED_EXTENSIONS
						= new String[]{".png", ".jpg", ".jpeg"};
	
	
	public static File getFolder() {
		if(!PATH.exists()) {
			PATH.mkdirs();
		}
		
		return PATH;		
	}
	
	public static File createPath(int userId, String ext, boolean unsaved) {
		return new File(getFolder(), userId + (unsaved ? "-" + System.currentTimeMillis() : "") + "." + ext);
	}
	

	public static File findPicture(int userId, boolean unsaved) {
		File[] pics = findPictures(userId, unsaved);
		
		if(pics != null && pics.length > 0) {
			return pics[0];
		}
		
		return null;
	}
	
	public static File[] findPictures(int userId, boolean unsaved) {	
		
		File[] pics = getFolder().listFiles(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {				
				return name.matches(userId + (unsaved ? "-\\d+" : "") + "\\..+");
			}
		});
		
				
		return pics;	
	}
	
	public static boolean hasPicture(int userId, boolean unsaved) {
		return findPicture(userId, unsaved) != null;
	}
	
	
	
	/**
	 * Save an uploaded but currently unsaved picture.
	 * This will remove currrent profile picture and
	 * set the newly uploaded one as profile picture.
	 * @param userId
	 */
	public static void saveProfilePicture(int userId) {		
		File unsaved = findPicture(userId, true);
		File current = findPicture(userId, false);
		
		if(unsaved != null) {
			if(current != null) {
				current.delete();
			}
			
			File renameTo = createPath(userId, getFileExtension(unsaved.getName()), false);
			unsaved.renameTo(renameTo);			
		}			
	}
	
	public static void cleanUnsaved(int userId) {
		for(File pic : findPictures(userId, true)) {
			pic.delete();
		}
	}
	
	
	public static String getFileExtension(String name) {
		String extension = "";
		int i = name.lastIndexOf('.');
		if (i > 0) {
		    extension = name.substring(i+1);
		}
		
		return extension;
	}
}
