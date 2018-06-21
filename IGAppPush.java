package com.yaoyaohao.health.surface.push;

import java.util.Map;

/**
 * Created by guosh on 2018/6/15.
 */
public interface IGAppPush {
    /**
     * 推送给整个APP用户信息
     *
     * @param mapInfo key decrible
     *                templateType-1-强制打开软件 2-点击消息跳转软件
     *                title-消息主题
     *                text-消息内容
     *                transmissionContent--透传信息
     */

    public Map<String, String> pushToApp(Map<String, Object> mapInfo);


    /**
     * 对指定用户列表推送消息
     *
     * @param mapInfo key:decrible
     *                templateType-1-强制打开软件 2-点击消息跳转软件
     *                title-消息主题
     *                text-消息内容
     *                transmissionContent--透传信息
     *                cids--目标客户端（List<String>）
     */
    public Map<String, String> pushToList(Map<String, Object> mapInfo);
}
