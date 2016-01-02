package dv512.controller;


import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import dv512.controller.util.PropUtils;
import dv512.model.User;
import dv512.model.dao.UsersDAO;


@Named
@ApplicationScoped
public class LoginController implements Serializable {
	private static final long serialVersionUID = 1L;

	
	public static final int STATE_LOGIN = 0;
	public static final int STATE_RESET_PASSWORD = 1;
	
	public static final String ACTION_LOGIN_SUCCESS = "success";
	public static final String ACTION_LOGIN_FAIL = "fail";	
	public static final String ACTION_LOGOUT = "logout";
	
	@Inject
	private UserController thisUser;
	
	@Inject
	private UsersDAO userDAO;
	
	
	private int currentState = STATE_LOGIN;
	
	private User user;
	private int retryCount = 0;
		
	
	public LoginController() {
		user = new User();
	}
	
	public User getUser(){
		return user;
	}
	
	public int getRetryCount() {
		return retryCount;
	}
	
	public boolean isVerified() {
	    return user.getId() != User.UNKNOWN_ID;
	}
	
	public int getUserId() {
		return user.getId();
	}
	
	
	public String login() {	
		if(userDAO.verify(user)) {
			retryCount = 0;
			thisUser.setUserId(user.getId());
			return ACTION_LOGIN_SUCCESS;
		}
		else {
			retryCount++;
			return ACTION_LOGIN_FAIL;
		}
	}
	
	public String logout() {
		user.setId(User.UNKNOWN_ID);
		thisUser.setUserId(User.UNKNOWN_ID);
		retryCount = 0;
		user.setEmail(null);
		user.setPassword(null);		
		return ACTION_LOGOUT;
	}
	
	
	public String sendResetEmail() throws IOException, AddressException, MessagingException {		
		String token = userDAO.requestResetPassword(user);
		
		if(token != null) {			
			HttpServletRequest origRequest = (HttpServletRequest)FacesContext
					.getCurrentInstance().getExternalContext().getRequest();
			
			String url = origRequest.getRequestURL().toString();
			url = url.substring(0, url.lastIndexOf("/") + 1) + "reset.xhtml?token=" + token;
			
			Properties smtp = System.getProperties();
			smtp.put("mail.smtp.auth", "true");
			smtp.put("mail.smtp.starttls.enable", "true");
			smtp.put("mail.smtp.host", "smtp.gmail.com");
			smtp.put("mail.smtp.port", "587");
								
			Session session = Session.getInstance(smtp, new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					Properties prop = PropUtils.read("mailconfig.properties");		
					if(prop == null) {
						throw new RuntimeException("Mail properties could not be loaded, check files.");
					}
								
					return new PasswordAuthentication(
							prop.getProperty("mail.address"), 
							prop.getProperty("mail.password"));
				}
			});
			
			
			MimeMessage msg = new MimeMessage(session);
			msg.setRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
			msg.setSubject("Password Reset");
			
			String body = "<p>Someone has requested a password reset for your account on <b>Internet of Dogs</b>. " + 
					"If you are not responsable, feel free to ignore this email.</p>" +
					"<p>If you want to reset you password you do that by clicking this link: </p>" +
					"<a href=\"" + url + "\">" + url + "</a>";
			
			msg.setContent(body, "text/html");
					
			Transport.send(msg);
		}
		
		currentState = STATE_LOGIN;
		return null;
	}
	
	
	public boolean isStateLogin() {
		return currentState == STATE_LOGIN;
	}
	
	public boolean isStateReset() {
		return currentState == STATE_RESET_PASSWORD;
	}
	
	public void switchStateToReset() {
		currentState = STATE_RESET_PASSWORD;
	}
	
	public void switchStateToLogin() {
		currentState = STATE_LOGIN;
	}
	
}