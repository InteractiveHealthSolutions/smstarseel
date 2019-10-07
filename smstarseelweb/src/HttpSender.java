

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

public class HttpSender
{
	public static void main(String[] args) throws JSONException, IOException {
		JSONObject jo = new JSONObject();
		jo.put("text", "my text");
		jo.put("recipient", "79878979");
		jo.put("priority", "HIGH");
		jo.put("periodType", "HOUR");
		jo.put("projectId", 1);
		jo.put("dueDate", System.currentTimeMillis());
		
		System.out.println(HttpUtil.post("http://localhost:8080/smstarseelweb/rest/outbound", "", jo.toString(), "admin", "admin123").body());
	}
}
