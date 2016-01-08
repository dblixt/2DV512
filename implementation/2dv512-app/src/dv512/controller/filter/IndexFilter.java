package dv512.controller.filter;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dv512.UserSession;

@WebFilter("/*")
public class IndexFilter implements Filter {

	@Inject
	private UserSession session;
	
	
	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}
	
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;

		String servPath = request.getServletPath();		
		System.out.println("IndexFilter: " + servPath);
		
		if (servPath.equals("")) {
			response.sendRedirect(request.getContextPath() + "/index.xhtml");
			return;
		}
		else if(servPath.equals("/index.xhtml")) {
			if(session.isValid()) {
				response.sendRedirect(request.getContextPath() + "/user/feed.xhtml");
				return;
			}
		}
				
		chain.doFilter(req, resp);		
	}

	@Override
	public void destroy() {


	}

}