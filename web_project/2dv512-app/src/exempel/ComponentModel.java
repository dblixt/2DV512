package exempel;

import java.io.Serializable;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;

@Named
@SessionScoped
public class ComponentModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4137025607278505114L;
	private String secret;

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}
}
