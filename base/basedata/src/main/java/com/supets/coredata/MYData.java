package com.supets.coredata;

import android.support.annotation.Keep;
import android.support.annotation.UiThread;
import android.support.annotation.WorkerThread;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * 实体类的基类
 * 该类实现了序列化，主要用于gson转换需要。业务调度数据传输协议。
 */
@Keep
public class MYData implements Serializable {

    private static final long serialVersionUID = 1L;

    public String id;

    /**
     * 该方法主要目的是做数据缓存，数据同步，建议在非UI线程中回调
     */
    @WorkerThread
    @UiThread
    public void updatePoolData() {

    }

    /**
     * 重写此方法，可获得实体主键，默认都是id，取决于数据库的设计
     *
     * @return string
     */
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
