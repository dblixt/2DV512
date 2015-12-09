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

@WebServlet("/attachment")
public class AttachmentServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	@Resource(name = "couchdb/nosql-app-db")
	protected CouchDbInstance db;
	
	CouchDbConnector dbc = null;
	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {		
		if(dbc == null) {
			dbc = db.createConnector("app-db", true);		
		}

		String id = req.getParameter("id");
		String name = req.getParameter("name");
	
		System.out.println("id=" + id + " name=" + name);
		
		resp.setHeader("Content-Type", getServletContext().getMimeType(name));
        resp.setHeader("Content-Length", String.valueOf(req.getContentLength()));
        resp.setHeader("Content-Disposition", "inline; filename=\"" + name + "\"");
        
        
        InputStream is = null;
        OutputStream os = null;
        try {
    		is = dbc.getAttachment(id, name);
            os = resp.getOutputStream();
    		
    		byte[] buffer = new byte[1024000];
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
