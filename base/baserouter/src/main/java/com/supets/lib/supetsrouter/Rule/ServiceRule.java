package com.supets.lib.supetsrouter.Rule;

import android.app.Service;

import com.supets.lib.supetsrouter.Rule.exception.ServiceNotRouteException;

/**
 * service路由规则<br />
 * Created by qibin on 2016/10/8.
 */

public class ServiceRule extends BaseIntentRule<Service> {

    /** service路由scheme*/
    public static final String SERVICE_SCHEME = "service://";

    @Override
    public void throwException(String pattern) {
        throw new ServiceNotRouteException(pattern);
    }
}
