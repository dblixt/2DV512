package dv512.model.service;

import java.io.InputStream;
import java.io.Serializable;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.ektorp.AttachmentInputStream;
import org.ektorp.CouchDbConnector;

import dv512.controller.util.ImgUtils;
import dv512.controller.util.NosqlManager;

@Named
@ApplicationScoped
public class ImageService implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject 
	private NosqlManager mgr;
	
	
	public String create(InputStream is) {
		CouchDbConnector c = mgr.getImgConnection();
		
		AttachmentInputStream ais = new AttachmentInputStream(
				"img", is, ImgUtils.IMAGE_MIME_TYPE);
				
		String id = UUID.randomUUID().toString().replace("-", "");
		c.createAttachment(id, ais);
		System.out.println(id);

		return id;
	}

}
