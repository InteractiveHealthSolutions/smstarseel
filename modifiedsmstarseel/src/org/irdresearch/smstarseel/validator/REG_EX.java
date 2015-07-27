package org.irdresearch.smstarseel.validator;

public class REG_EX {
		
	public static final REG_EX ALPHA = new REG_EX("[a-zA-Z]+");
	public static final REG_EX ALPHA_NUMERIC = new REG_EX("[a-zA-Z0-9]+");
	public static final REG_EX CELL_NUMBER = new REG_EX("(\\+92|92|03)?[0-9]{9}");
	public static final REG_EX EMAIL = new REG_EX("((\\w+)|(\\w+\\.+\\w+)|(\\w+\\-+\\w+))\\@((\\w+)|(\\w+\\-\\w+))\\.\\w{2,4}");
	public static final REG_EX NAME_CHARACTERS = new REG_EX("[a-zA-Z\\.\\-/\\s]+");
	public static final REG_EX NO_SPECIAL_CHAR = new REG_EX("[a-zA-Z0-9\\|/\\(\\)_,\\-\\.\\s`]+");
	public static final REG_EX NUMERIC = new REG_EX("[0-9]+");
	public static final REG_EX PASSWORD = new REG_EX("[a-zA-Z0-9@!\\|~/\\$\\*\\(\\)_\\[\\]\\{\\};:\\.\\s]+");
	public static final REG_EX PHONE_NUMBER = new REG_EX("(\\d+([,\\-\\s]?)\\d+)+");
	public static final REG_EX PTCL_LANDLINE_NUMBER = new REG_EX("(\\+92|92|0)?213[0-9]{7}");
	public static final REG_EX PTCL_WIRELESS_NUMBER = new REG_EX("(\\+92|92|0)?213[0-9]{7}");
	public static final REG_EX WHITESPACE_WORD = new REG_EX("[\\w\\s]+");
	public static final REG_EX WHITESPACE_ALPHA = new REG_EX("[a-zA-Z\\s]+");
	public static final REG_EX WHITESPACE_NUMERIC = new REG_EX("[0-9\\s]+");
	public static final REG_EX WHITESPACE_ALPHA_NUMERIC = new REG_EX("[a-zA-Z0-9\\s]+");
	public static final REG_EX WORD = new REG_EX("\\w+");

		public String toString() {
		   return regexp;
		}
		private final String regexp;

		private REG_EX(String exp) {
			regexp=exp;
		}

}
