package dv512.filter;

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

import dv512.LoginController;

@WebFilter("/user/*")
public class AuthenticationFilter implements Filter {

	@Inject
	private LoginController loginController;
	

	@Override
	public void init(FilterConfig config) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;

		String servPath = request.getServletPath();
		System.out.println("AuthenticationFilter: " + servPath);
		
		if (!loginController.isVerified()) {
			// redirect to login page, user not verified.
			response.sendRedirect(request.getContextPath() + "/index.xhtml");
		}
		else {
			chain.doFilter(req, resp);
		}	
	}

	@Override
	public void destroy() {
		
	}

}
