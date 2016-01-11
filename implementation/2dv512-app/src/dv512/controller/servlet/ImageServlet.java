package dv512.controller.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;

import dv512.controller.util.ImgUtils;

@WebServlet({"/img/profile/*", "/img/dog/*"})
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
		
		String pathInfo = req.getPathInfo();		
		String id = null;
		if(pathInfo != null && pathInfo.length() > 0) {
			id = pathInfo.substring(1);
		}
		
		// cache for 1 month
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, 1);		
		resp.setDateHeader("Expires", c.getTimeInMillis());
		
		System.out.println("ImageServlet: id=" + id );			
		if(id == null || id.length() != 32) {
			String reqPath = req.getServletPath();			
			if("/img/profile".equals(reqPath)) {
				handleNoProfile(req, resp);
			}
			else if("/img/dog".equals(reqPath)) {
				handleNoDog(req, resp);
			}			
		}
		else {			
			resp.setHeader("Content-Type", ImgUtils.IMAGE_MIME_TYPE);	
	        resp.setHeader("Content-Disposition", "inline; filename=\"" + id + ".jpg" + "\"");
	               
	        InputStream is = null;
	        OutputStream os = null;
	        try {
	    		is = dbc.getAttachment(id, "img");
	            os = resp.getOutputStream();	    		
	            IOUtils.copy(is, os);
	        }
	        catch(Exception e) {
	        	e.printStackTrace();
	        }
	        finally {
	        	try {
	        		is.close();
	        		os.close();
	        	}
	        	catch(Exception e) {}
	        }       
		}	
	}
	
	private void handleNoProfile(HttpServletRequest req, HttpServletResponse resp) {		
		resp.setHeader("Content-Type", "image/png");	
		resp.setHeader("Content-Disposition", "inline; filename=\"no_profile.png" + "\"");
		HttpSession s = req.getSession();			
		
		InputStream is = null;
		OutputStream os = null;
		try {
			is = s.getServletContext()
					.getResourceAsStream("/resources/img/no_profile.png");
			os = resp.getOutputStream();
			IOUtils.copy(is, os);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				is.close();
				os.close();
			}
			catch(Exception e) {}
		}
	}
	
	private void handleNoDog(HttpServletRequest req, HttpServletResponse resp) {
		resp.setHeader("Content-Type", "image/png");	
		resp.setHeader("Content-Disposition", "inline; filename=\"no_dog.png" + "\"");
		HttpSession s = req.getSession();			
		
		InputStream is = null;
		OutputStream os = null;
		try {
			is = s.getServletContext()
					.getResourceAsStream("/resources/img/no_dog.png");
			os = resp.getOutputStream();
			IOUtils.copy(is, os);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				is.close();
				os.close();
			}
			catch(Exception e) {}
		}
	}

}
