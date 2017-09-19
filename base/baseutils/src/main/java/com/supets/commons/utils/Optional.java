package com.supets.commons.utils;

import android.support.annotation.StringRes;

import java.util.Collection;

/**
 * 避免null值处理
 */

public class Optional<T> {

    private T target;

    private Optional(T target) {
        this.target = target;
    }

    public static <T> Optional<T> of(T obj) {
        return new Optional<>(obj);
    }

    /**
     * 判斷是否引用存在
     *
     * @return
     */
    public boolean isPresent() {
        return target != null;
    }

    /**
     * 得到引用值
     *
     * @return
     */
    public T get() {
        if (target == null) {
            throw new NullPointerException();
        }
        return target;
    }

    /**
     * 得到引用值
     *
     * @param defaultVaule 默认值
     * @return
     */
    public T or(T defaultVaule) {
        return target == null ? defaultVaule : target;
    }

    /**
     * 设置字符串值
     *
     * @param target
     * @param defaultVaule
     * @return
     */
    public static String empty(String target, String defaultVaule) {
        return target == null || target.trim().length() == 0 ? defaultVaule : target;
    }

    public static String empty(String target, @StringRes int defaultVaule) {
        return target == null || target.trim().length() == 0 ? MiaTextUtils.getString(defaultVaule) : target;
    }

    public static boolean noNull(Object target) {
        return target != null;
    }

    public static boolean isNull(Object target) {
        return target == null;
    }

    public static boolean notNullOrEmpty(String target) {
        return target != null && target.trim().length() > 0;
    }

    public static boolean notNullOrEmpty(Collection<?> target) {
        return target != null && target.size() > 0;
    }

    public static boolean notNullOrEmpty(Collection<?> target, int page) {
        return target != null && target.size() > page;
    }

    public static String emptyToNull(String target) {
        return (target != null) && (target.trim().length() == 0) ? null : target;
    }

    public static String nullToEmpty(String target) {
        return (target == null) ? "" : target;
    }

    public static boolean equals(String a, String b) {
        return (a == null) ? (b == null) : a.equals(b);
    }

}
