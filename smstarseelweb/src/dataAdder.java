import ird.xoutTB.emailer.exception.EmailException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.irdresearch.smstarseel.EmailEngine;
import org.irdresearch.smstarseel.context.TarseelContext;
import org.irdresearch.smstarseel.context.TarseelServices;
import org.irdresearch.smstarseel.data.User;
import org.irdresearch.smstarseel.data.User.UserStatus;
import org.irdresearch.smstarseel.service.UserServiceException;

public class dataAdder {

	public static void main(String[] args)
	{
		Properties prop;
		InputStream f;
		
		try {
			f = dataAdder.class.getResourceAsStream("/org/irdresearch/smstarseel/queries/"+"smstarseel.properties");
			prop=new Properties();
			prop.load(f);
			
			EmailEngine.instantiateEmailEngine(prop, prop.getProperty("admin.email-address"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (EmailException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		EmailEngine.getInstance().emailErrorReportToAdmin("xyz", "wxyz");
		//addUser();

	/*	TarseelServices sc = TarseelContext.getServices();

		Project p = new Project();
		p.setName("test");
		
		sc.getDeviceService().saveProject(p);
		sc.commitTransaction();
		sc.closeSession();*/
	}
	
	public static void addUser(){
		TarseelServices sc = TarseelContext.getServices();

		User user = new User("admin");
		user.setFirstName("Administrator");
		user.setClearTextPassword("admin123");
		user.setLastName("");
		user.setStatus(UserStatus.ACTIVE);
		user.addRole(sc.getUserService().getRole("admin"));
		
		try
		{
			sc.getUserService().createUser(user);
			sc.commitTransaction();
		}
		catch (UserServiceException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		sc.closeSession();
	}
}
