

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TestClass {

	public enum TESTENUM{
		T1,T2,T3;
	}
	private String test1;
	
	private String test2;

	private TESTENUM tenum;

	private Object object;
	
	public TestClass () {
		
	}
	
	public TestClass (String t1, String t2, TESTENUM tenum, Object obj) {
		setTest1(t1);
		test2 = t2;
		this.setTenum(tenum);
		object = obj;
	}

	public String getTest1 () {
		return test1;
	}

	public void setTest1 (String test1) {
		this.test1 = test1;
	}

	public String getTest2 () {
		return test2;
	}

	public void setTest2 (String test2) {
		this.test2 = test2;
	}

	public TESTENUM getTenum () {
		return tenum;
	}

	public void setTenum (TESTENUM tenum) {
		this.tenum = tenum;
	}

	public Object getObject () {
		return object;
	}

	public void setObject (Object object) {
		this.object = object;
	}
}
