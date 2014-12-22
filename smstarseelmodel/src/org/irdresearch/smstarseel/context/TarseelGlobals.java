package org.irdresearch.smstarseel.context;

import java.util.Random;

public class TarseelGlobals {

	static Random ran = new Random();
	public static synchronized String getUniqueSmsId(int projectId)
	{
		try
		{
			Thread.sleep(1);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		StringBuilder sb = new StringBuilder(projectId+""+Long.toString(System.currentTimeMillis()));
		sb.append(ran.nextInt(9)+""+ran.nextInt(9));
		//sb.setLength(17);
		return sb.toString();
	}
}
