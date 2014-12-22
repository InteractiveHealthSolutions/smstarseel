

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class jsontest {

	public static void main(String[] args)
	{
		System.out.println("543324".matches("\\+?[0-9]+"));
		try
		{
			JSONObject reqOrResp = new JSONObject();
			
			JSONArray jar = new JSONArray();
			for (int i = 0 ; i <9 ;i++)
			{
				JSONObject j = new JSONObject();

				j.put("firstname", "fname-"+i);
				j.put("nickname", "nname-"+i);
				jar.put(j);
				
			}
			reqOrResp.put("arr1", jar);
			
			JSONArray jar2 = new JSONArray();

			for (int i = 0 ; i <9 ;i++)
			{
				JSONObject j = new JSONObject();
				j.put("name", "fnamelname-"+i);
				j.put("cell", "0000cell-"+i);
				
				jar2.put(j);
			}
			
			reqOrResp.put("arr2", jar2);
			
			System.out.println(reqOrResp.toString(0));
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
	}
}
