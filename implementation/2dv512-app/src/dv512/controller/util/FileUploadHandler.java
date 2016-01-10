package dv512.controller.util;

import java.io.IOException;
import java.io.InputStream;

import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.Part;


public class FileUploadHandler {

	public interface FileUploadListener {
		void onUploadFile(String type, String filename, InputStream is);
	}
	
	private FileUploadListener listener;
	private Part file;

	public void setFileUploadListener(FileUploadListener listener) {
		this.listener = listener;
	}
		
	public Part getFile() {
		return null;
	}
	
	public void setFile(Part file) {
		this.file = file;
	}

	public void upload(AjaxBehaviorEvent event) throws IOException {				
		String type = (String) event.getComponent()
				.getAttributes().get("type");
		
		if(listener != null) {
			listener.onUploadFile(
					type,
					file.getSubmittedFileName(), 
					file.getInputStream()
			);
			file = null;
		}
	}
	
}
