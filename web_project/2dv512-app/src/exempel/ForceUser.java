package exempel;

import java.io.Serializable;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;

@Named
@SessionScoped
public class ForceUser implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -548185428109151792L;
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String theName) {
		name = theName;
	}
}
