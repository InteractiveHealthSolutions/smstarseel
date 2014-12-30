import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;




public class buffertest {

	static StringBuffer b = new StringBuffer();
	public static void main(String[] args)
	{
		String str = "{"+
			  "\"Demo\": {"+
				"\"CONTENT\": ["+
			     " {"+
				"\"ID\": \" 283 \","+
				"\"UID\": \" 87897bc8-ae9b-11e1-bdcf-123141042154 \","+
				"\"DURATION\": \"Full\""+
			     " },"+
			      "{"+
				"\"ID\": \" 283 \","+
				"\"UID\": \" 87897bc8-ae9b-11e1-bdcf-123141042154 \","+
				"\"DURATION\": \"Full\""+
			     " }"+
			    "]"+
			  "}"+
			"}";
		
		try {
			JSONObject jsr = new JSONObject(str); // JSON object with above data
			JSONObject demo = jsr.getJSONObject("Demo"); // get Demo which is a JSON object inside jsr.
			JSONArray content = demo.getJSONArray("CONTENT");// get CONTENT which is Json array inside Demo
			for (int i = 0; i < content.length(); i++) { // iterate over array to get inner JSON objects and extract values inside
				JSONObject record = content.getJSONObject(i); // each item of Array is a JSON object
				String ID = record.getString("ID");
				String UID = record.getString("UID");
				String DURATION = record.getString("DURATION");
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		
		//TarseelServices tsc = TarseelContext.getServices();
		//System.out.println(new JSONObject(new TestClass("t1", "t2", TESTENUM.T1, tsc)));
		/*System.out.println("dhfds"+null+"dfbdsaf");
		for (Charset string : Charset.availableCharsets().values()) {
			System.out.println(string.displayName()+":"+string.name()+":");
		}
		b.append("abcdefghijklmnopqrstuvwxyz");
		b.append("\n1234567890");
		b.append("\nabcdefghijklmnopqrstuvwxyz");
		b.append("\n1234567890");
		b.append("\nabcdefghijklmnopqrstuvwxyz");
		b.append("\n1234567890");
		System.out.println(b.toString());
		//b.setLength(10);
		if(b.length() > 90){
			b = new StringBuffer(b.substring(b.length()-90));
		}
		System.out.println("NEWSTR : "+b.toString());*/
		
		
	}
}
