package org.irdresearch.smstarseel.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataValidation {
	private static Matcher matcher;
	private static Pattern pattern ;//= Pattern.compile(regexpChars);

	public static boolean validate(REG_EX expression,String string){
		pattern=Pattern.compile(expression.toString(),Pattern.CASE_INSENSITIVE);
		matcher=pattern.matcher(string);
		return matcher.matches();
	}
	
	public static boolean validate(REG_EX expression,String string,int minln,int maxln){
		if(string.length()<minln ||string.length()>maxln){
			return false;
		}
		return validate(expression, string);
	}
	
	public static void main(String[] args) {
		Pattern p = Pattern.compile("\\w+|\\w+[/\\-\\.#\\s]+\\w+");
		Matcher m = p.matcher("sdm#sdsd"/*"http://hgh.yjuyt-yhg.hjh:9343/gg-fby-jhgj/hjhj/jyhjh/thgh-hjnh-hnhn/ghg.jhj"*/);
		boolean b = m.matches();
	
		System.out.println(b);
		
		//TODO .web not proper name .www must not be an email www.must not b email
		System.out.print("cell val:"+validate(REG_EX.CELL_NUMBER, null));
		//"http://((\\d{1,3}(\\.\\d{1,3}){3})|(\\w+|(\\w+[\\-\\.]\\w+)+))(:\\d{4})?(/\\w*|/((\\w+\\-\\w+)+|(\\w+/\\w+))+(/\\w+.\\w+)?)");
		
		Pattern p1 = Pattern.compile("http://" +
				"(" +
				"(\\d{1,3}(\\.\\d{1,3}){3}) |" +
				"( (\\w+ | (\\w+[\\-\\.]\\w+)+ ) ) " +
				")" +
				"(:\\d{4})?" +
				"");
	}
}
