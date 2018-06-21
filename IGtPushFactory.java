package com.yaoyaohao.health.surface.push;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

/**
 * Created by guosh on 2018/6/14.
 */
public class IGtPushFactory implements FactoryBean<IIGtPush>, InitializingBean {
    private String appId;
    private String appKey;
    private String masterSecret;
    private String url;
    private IIGtPush iiGtPush;



    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setMasterSecret(String masterSecret) {
        this.masterSecret = masterSecret;
    }

    public String getMasterSecret() {
        return masterSecret;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public IIGtPush getObject() throws Exception {
        return iiGtPush;
    }

    @Override
    public Class<? extends  IIGtPush> getObjectType() {
        return this.iiGtPush != null ?  this.iiGtPush.getClass() : IIGtPush.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        iiGtPush=new IIGtPush(url, appKey, masterSecret,appId);
    }
}
