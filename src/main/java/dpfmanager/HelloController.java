package dpfmanager;



import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.*;
import java.awt.font.*;
import java.util.concurrent.locks.*;
import java.util.concurrent.locks.*;

/**
 * @author Easy asdfasd
 *
 */
public class HelloController {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory
	    .getLogger(HelloController.class);

    /** The first name field. */
    @FXML
    private TextField firstNameField;

    /** The last name field. */
    @FXML
    private TextField lastNameField;

    /** The message label. */
    @FXML
    private Label messageLabel;

    /**
     * 
     */
    public final void sayHello() {

	String firstName = firstNameField.getText();
	String lastName = lastNameField.getText();

	StringBuilder builder = new StringBuilder();

	if (!StringUtils.isEmpty(firstName)) {
	    builder.append(firstName);
	}

	if (!StringUtils.isEmpty(lastName)) {
	    if (builder.length() > 0) {
		builder.append(" ");
	    }
	    builder.append(lastName);
	}

	if (builder.length() > 0) {
	    String name = builder.toString();
	    LOG.debug("Saying hello to " + name);
	    messageLabel.setText("Hello " + name);
	    
	} else {
	    LOG.debug("Neither first name nor last "
	    	+ "name was set, saying hello to anonymous person asdfasd");
	    messageLabel.setText("Hello mysterious person");
	}
    }

    /**
     * @param hello
     *            asdf asdf
     */
    public final void doingnothing(final String hello) {
	String doing;
	String nothing;

	nothing = "asdfasf";
	String merda = "merda";
	String holla = "asd";
    }

    /**
     * @param hello
     *            asdfa sdf
     * @return asdfas df
     */
    public final String doingnothing2(final String hello) {
	String doing;
	String nothing;

	nothing = "asdfasf";
	return nothing;
    }
    
    /**
     * @param tePermisEdicio asdf
     * @return asdf
     */
    public boolean noTePermisEdicio(Integer tePermisEdicio) {
	
	  if (tePermisEdicio.equals(0)) {
	   return tePermisEdicio.equals(0);
	  } else if (tePermisEdicio.equals(1)) {
	   return !tePermisEdicio.equals(1);
	  } else {
	   return false;
	  }
    } 
    
    public boolean noTePermisEdicioaltre(int tePermisEdicio) {
	
	  if (tePermisEdicio == 0) {
	   return (tePermisEdicio == 0);
	  } else if (tePermisEdicio == 1) {
	   return !(tePermisEdicio == 1);
	  } else {
	   return false;
	  }
  } 
	
}
