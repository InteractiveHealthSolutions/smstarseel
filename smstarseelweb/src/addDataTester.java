

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Properties;

import javax.management.InstanceAlreadyExistsException;

import org.irdresearch.smstarseel.SmsTarseelUtil;
import org.irdresearch.smstarseel.context.TarseelContext;
import org.irdresearch.smstarseel.context.TarseelServices;
import org.irdresearch.smstarseel.data.OutboundMessage.PeriodType;
import org.irdresearch.smstarseel.data.OutboundMessage.Priority;

import net.jmatrix.eproperties.EProperties;

public class addDataTester {

	public static void main(String[] args) throws IOException, InstanceAlreadyExistsException
	{
		System.out.println(">>>>LOADING SYSTEM PROPERTIES...");
		InputStream f = Thread.currentThread().getContextClassLoader().getResourceAsStream("smstarseel.properties");
		// Java Properties donot seem to support substitutions hence EProperties are used to accomplish the task
		EProperties root = new EProperties();
		root.load(f);

		// Java Properties to send to context and other APIs for configuration
		Properties prop = new Properties();
		prop.putAll(SmsTarseelUtil.convertEntrySetToMap(root.entrySet()));
		
		TarseelContext.instantiate(prop, null);
		TarseelServices sc = TarseelContext.getServices();
		
		Calendar c = Calendar.getInstance();
		c.add(Calendar.HOUR_OF_DAY, -1);
		c.add(Calendar.MINUTE, 1);

		for (int i = 0; i < 9; i++)
		{
			//System.out.println(i%20);
			//if(i%30==0)
				//c.add(Calendar.MINUTE, 1);
		//o.setSystemProcessingEndDate(systemProcessingEndDate);
		//o.setSystemProcessingStartDate(systemProcessingStartDate);
		String text = new String(("huuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuugeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee").getBytes(), Charset.forName("UTF-8"));
		System.out.println(text);
		String cellNum = "03343872951";
		
		sc.getSmsService().createNewOutboundSms(cellNum, text, c.getTime(), Priority.HIGH, 24, PeriodType.HOUR, 1, null);

		}
		sc.commitTransaction();
		sc.closeSession();
		
		

	}
}
