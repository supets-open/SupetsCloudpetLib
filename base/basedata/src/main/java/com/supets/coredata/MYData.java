package com.supets.coredata;

import android.support.annotation.Keep;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

@Keep
public class MYData implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public String id;
    
    public void updatePoolData() {};
    
    public String getId() {
    	return this.id;
    }
    
    public void updateWith(MYData data) {
    	updateByClass(this.getClass(), data);
    }
    
    private void updateByClass(Class<?> clazz, MYData data) {
    	Field[] fields = clazz.getDeclaredFields();
    	if (fields == null) {
    		return;
    	}
    	for (Field field : fields) {
    		int modifiers = field.getModifiers();
    		if (Modifier.isPrivate(modifiers) || Modifier.isTransient(modifiers) || Modifier.isStatic(modifiers)) {
    			continue;
    		}
			try {
				field.setAccessible(true);
				Object value = field.get(data);
				if (value != null) {
					field.set(this, value);
				}
			} catch (Exception e) {
			}
    	}
    	
    	Class<?> superCls = clazz.getSuperclass();
    	if (superCls != null && superCls != Object.class) {
    		updateByClass(superCls, data);
    	}
    }
}
