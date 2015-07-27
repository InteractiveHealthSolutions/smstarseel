package org.irdresearch.smstarseel.validator;

import org.irdresearch.smstarseel.constant.TarseelGlobals.ErrorMessages;
import org.irdresearch.smstarseel.global.SmsTarseelGlobal;


public class UserValidator {

	public static ErrorMessages validateUsername(String username){
		if(username.trim().equals("")){
			return ErrorMessages.EMPTY_USER_CREDENTIALS;
		}
		if(username.trim().length() < SmsTarseelGlobal.USERNAME_MIN_LENGTH){
			return ErrorMessages.USERNAME_MIN_LENGTH_ERROR;
		}
		if(!username.trim().matches("[\\w~\\s@\\-\\.;]+")){
			return ErrorMessages.INVALID_USERNAME_CHARACTERS;
		}
		
		return ErrorMessages.NO_ERROR;
	}
	
	public static ErrorMessages validatePassword(String password){
		if(password.trim().equals("")){
			return ErrorMessages.EMPTY_USER_CREDENTIALS;
		}
		if(password.trim().length() < SmsTarseelGlobal.PASSWORD_MIN_LENGTH){
			return ErrorMessages.PASSWORD_MIN_LENGTH_ERROR;
		}
		if(!password.trim().matches("[\\w~\\s@\\-\\.;]+")){
			return ErrorMessages.INVALID_PASSWORD_CHARACTERS;
		}
		
		return ErrorMessages.NO_ERROR;
	}
}
