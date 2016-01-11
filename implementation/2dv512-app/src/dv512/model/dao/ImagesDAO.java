package dv512.model.dao;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.ektorp.AttachmentInputStream;
import org.ektorp.BulkDeleteDocument;
import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;

import dv512.controller.util.DbManager;
import dv512.controller.util.ImgUtils;
import dv512.model.Image;

@Named
@ApplicationScoped
public class ImagesDAO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Inject 
	private DbManager mgr;
	
	
	public String create(InputStream is) {
		CouchDbConnector c = mgr.getImgConnection();
		
		AttachmentInputStream ais = new AttachmentInputStream(
				"img", is, ImgUtils.IMAGE_MIME_TYPE);
				
		String id = UUID.randomUUID().toString().replace("-", "");
		c.createAttachment(id, ais);

		return id;
	}
	
	/**
	 * Delete multiple image documents from the 
	 * database. The database will be queried for
	 * the documents of the given id:s. All
	 * will then be deleted in a bulk operation
	 * resulting in a total of two database calls.
	 * @param ids
	 * @return
	 */
	public boolean delete(List<String> ids) {
		try {
			CouchDbConnector c = mgr.getImgConnection();
			
			ViewQuery q = new ViewQuery()
					.dbPath(c.getDatabaseName())
					.viewName("find")
					.designDocId("_design/image")
					.keys(ids)
					.reduce(false);
		
			List<Image> r = c.queryView(q, Image.class);
			
			List<Object> bulkDocs = new ArrayList<>();
			for(Image i : r) {
				bulkDocs.add(BulkDeleteDocument.of(i));				
			}
			
			c.executeBulk(bulkDocs);	
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
			
		return false;		
	}
	
}
