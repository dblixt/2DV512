package dv512.controller.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.Part;

@Named
@ViewScoped
public class FileUploadHandler implements Serializable {

	private static final long serialVersionUID = -1851387365563543581L;
	
	public interface FileUploadListener {
		void onUploadFile(String filename, InputStream is);
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
		System.out.println("Upload file");
		if(listener != null) {
			listener.onUploadFile(
					file.getSubmittedFileName(), 
					file.getInputStream()
			);
		}
	}
	
}
