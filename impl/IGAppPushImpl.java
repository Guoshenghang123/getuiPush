package com.yaoyaohao.health.surface.push.impl;

import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.ITemplate;
import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.base.impl.ListMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.template.NotificationTemplate;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.gexin.rp.sdk.template.style.Style0;
import com.yaoyaohao.health.surface.enums.PushConsts;
import com.yaoyaohao.health.surface.push.IGAppPush;
import com.yaoyaohao.health.surface.push.IIGtPush;
import com.yaoyaohao.health.surface.push.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yaoyaohao.health.surface.enums.PushConsts.KEY_TEMPLATETYPE;
import static com.yaoyaohao.health.surface.enums.PushConsts.TRANSMISSION_TEMPLATE;

/**
 * Created by guosh on 2018/6/15.
 */
@Component
@Service("iGAppPushImpl")
public class IGAppPushImpl implements IGAppPush{

    private static Logger logger = LoggerFactory.getLogger(IGAppPushImpl.class);

    @Autowired
    private IIGtPush iiGtPush;


    /**
     * 推送给整个APP用户信息
     *
     * @param mapInfo key decrible
     *                templateType-1-强制打开软件 2-点击消息跳转软件
     *                title-消息主题
     *                text-消息内容
     *                transmissionContent--透传信息
     */

    @Override
    public Map<String, String> pushToApp(Map<String, Object> mapInfo) {
        Map<String, String> result = new HashMap<>();
        result.put(PushConsts.RESULT_CODE, PushConsts.SUCCESS);
        //参数校验
        if (!verificationParam(mapInfo)) {
            result.put(PushConsts.RESULT_CODE, PushConsts.PARAM_ERRO);
            return result;
        }

        ITemplate template = getTemplate(mapInfo);
        // 定义"AppMessage"类型消息对象，设置消息内容模板、发送的目标App列表、是否支持离线发送、以及离线消息有效期(单位毫秒)
        AppMessage message = getAppMessag(template, iiGtPush.getAppId());
        IPushResult ret = iiGtPush.pushMessageToApp(message);
        //结果处理
        operatePushResult(ret, mapInfo);
        return result;
    }

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
    @Override
    public Map<String, String> pushToList(Map<String, Object> mapInfo) {
        Map<String, String> result = new HashMap<>();
        result.put(PushConsts.RESULT_CODE, "OK");
        //参数校验
        if (!verificationParam(mapInfo)) {
            result.put(PushConsts.RESULT_CODE, PushConsts.PARAM_ERRO);
            return result;
        }
        ITemplate template = getNotificationTemplate(mapInfo);
        // 定义"AppMessage"类型消息对象，设置消息内容模板、发送的目标App列表、是否支持离线发送、以及离线消息有效期(单位毫秒)
        ListMessage message = getListMessage(template);
        // 配置推送目标
        List targets = getTargetList(mapInfo);
        // taskId用于在推送时去查找对应的message
        String taskId = iiGtPush.getContentId(message);
        IPushResult ret = iiGtPush.pushMessageToList(taskId, targets);
        //结果处理
        operatePushResult(ret, mapInfo);
        return result;
    }


    private boolean verificationParam(Map<String, Object> mapInfo) {
        if (null == mapInfo.get(PushConsts.KEY_TEMPLATETYPE)) {
            mapInfo.put(PushConsts.KEY_TEMPLATETYPE, PushConsts.TRANSMISSION_TEMPLATE);
        }
        return true;
    }

    /**
     * 日志记录，异常重调
     *
     * @param ret
     * @param mapInfo
     */
    private void operatePushResult(IPushResult ret, Map<String, Object> mapInfo) {
        logger.info("个推调用接口返回：" + ret.getResponse().toString());
    }

    private List getTargetList(Map<String, Object> mapInfo) {
        List<Target> targets = new ArrayList<>();
        List<String> cids = (List) mapInfo.get(PushConsts.KEY_CIDS);
        for (String cid : cids) {
            Target target = new Target();
            target.setAppId(iiGtPush.getAppId());
            target.setClientId(cid);
            targets.add(target);
        }
        return targets;
    }

    private ListMessage getListMessage(ITemplate template) {
        ListMessage message = new ListMessage();
        message.setData(template);
        // 设置消息离线，并设置离线时间
        message.setOffline(true);
        // 离线有效时间，单位为毫秒，可选
        message.setOfflineExpireTime(24 * 1000 * 3600);
        return message;
    }


    private AppMessage getAppMessag(ITemplate template, String appId) {
        AppMessage message = new AppMessage();
        message.setData(template);
        List<String> appIds = new ArrayList<String>();
        appIds.add(appId);
        message.setAppIdList(appIds);
        message.setOffline(true);
        message.setOfflineExpireTime(1000 * 600);
        return message;
    }

    private ITemplate getTemplate(Map<String, Object> mapInfo) {
        ITemplate iTemplate;
        String templateType = mapInfo.get(KEY_TEMPLATETYPE).toString();
        if (TRANSMISSION_TEMPLATE.equals(templateType)) {
            TransmissionTemplate template = getTransmissionTemplate(mapInfo);
            iTemplate = template;
        } else {
            NotificationTemplate template = getNotificationTemplate(mapInfo);
            iTemplate = template;
        }
        return iTemplate;
    }

    private NotificationTemplate getNotificationTemplate(Map<String, Object> mapInfo) {
        NotificationTemplate template = new NotificationTemplate();
        template.setAppId(iiGtPush.getAppId());
        template.setAppkey(iiGtPush.getAppKey());
        // 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
        template.setTransmissionType(1);
        template.setTransmissionContent(mapInfo.get(PushConsts.KEY_TRANSMISSIONCONTENT).toString());
        Style0 style = new Style0();
        // 设置通知栏标题与内容
        style.setTitle(mapInfo.get(PushConsts.KEY_TITLE).toString());
        style.setText(mapInfo.get(PushConsts.KEY_TEXT).toString());
        // 设置通知是否响铃，震动，或者可清除
        style.setRing(true);
        style.setVibrate(true);
        style.setClearable(true);
        template.setStyle(style);
        return template;
    }


    private TransmissionTemplate getTransmissionTemplate(Map<String, Object> mapInfo) {
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(iiGtPush.getAppId());
        template.setAppkey(iiGtPush.getAppKey());
        // 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
        template.setTransmissionType(1);
        template.setTransmissionContent(mapInfo.get(PushConsts.KEY_TRANSMISSIONCONTENT).toString());
        return template;
    }

}
