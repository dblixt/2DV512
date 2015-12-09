package dv512.controller.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;

import dv512.controller.util.ImgUtils;

@WebServlet("/img/*")
public class ImageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Resource(name = "couchdb/nosql-app-db")
	protected CouchDbInstance db;
	
	private CouchDbConnector dbc = null;
		
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {		
		if(dbc == null) {
			dbc = db.createConnector("app-img-db", true);		
		}

		String id = req.getPathInfo().substring(1);		
		System.out.println("id=" + id );
		
		resp.setHeader("Content-Type", ImgUtils.IMAGE_MIME_TYPE);
        resp.setHeader("Content-Length", String.valueOf(req.getContentLength()));
        resp.setHeader("Content-Disposition", "inline; filename=\"" + id + ".jpg" + "\"");
        
        
        InputStream is = null;
        OutputStream os = null;
        try {
    		is = dbc.getAttachment(id, "img");
            os = resp.getOutputStream();
    		
    		byte[] buffer = new byte[1024*100];
    		int len;
    		while ((len = is.read(buffer)) != -1) {
    		    os.write(buffer, 0, len);
    		}

    		is.close();
    		os.close();	
        }
        catch(Exception e) {
        	e.printStackTrace();
        }
        finally {
        	try {
        		is.close();
        		os.close();
        	}
        	catch(Exception e) {
        		
        	}
        }       
	}

}
