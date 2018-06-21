package com.yaoyaohao.health.surface.push;

import com.gexin.rp.sdk.http.IGtPush;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


/**
 * Created by guosh on 2018/6/15.
 */
public class IIGtPush extends IGtPush  {
    private String appId;
    private String appKey;
    private String masterSecret;
    private String url;
    private String host;

    public IIGtPush(String host,String appKey, String masterSecret,String appId) {
        super(host,appKey, masterSecret);
        this.appId=appId;
        this.appKey=appKey;
        this.masterSecret=masterSecret;
        this.host=host;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getMasterSecret() {
        return masterSecret;
    }

    public void setMasterSecret(String masterSecret) {
        this.masterSecret = masterSecret;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
