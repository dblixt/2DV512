package dv512.controller.servlet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dv512.controller.util.ImgUtils;


@WebServlet("/images/profile/*")
public class ProfilePicServlet extends HttpServlet {
	private static final long serialVersionUID = -2451885364753755823L;
	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String filename = req.getPathInfo().substring(1);
		System.out.println("filename:" + filename);
		
        File file = new File(ImgUtils.getFolder(ImgUtils.TYPE_PROFILE_PIC), filename);
     
        resp.setHeader("Content-Type", getServletContext().getMimeType(filename));
        resp.setHeader("Content-Length", String.valueOf(file.length()));
        resp.setHeader("Content-Disposition", "inline; filename=\"" + filename + "\"");
        Files.copy(file.toPath(), resp.getOutputStream());
	}

}
