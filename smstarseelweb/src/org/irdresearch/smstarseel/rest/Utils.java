package org.irdresearch.smstarseel.rest;

import java.lang.reflect.Field;
import java.util.List;

public class Utils {
	public static <T> void verifyRequiredProperties(List<String> properties, T entity) {
		if(properties != null)
		for (String p : properties) {
			Field[] aaa = entity.getClass().getDeclaredFields();
			for (Field field : aaa) {
				if(field.getName().equals(p)){
					field.setAccessible(true);
					try {
						if(field.get(entity) == null){
							throw new RuntimeException("A required field "+p+" was found empty");
						}
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
						throw new RuntimeException("A required field "+p+" was not found in resource class");
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
