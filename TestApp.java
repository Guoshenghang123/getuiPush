import com.yaoyaohao.health.surface.enums.PushConsts;
import com.yaoyaohao.health.surface.push.impl.IGAppPushImpl;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by guosh on 2018/6/15.
 */
public  class TestApp {
    static ClassPathXmlApplicationContext context;
    static{
        context = new ClassPathXmlApplicationContext(new String[]{ "classpath:spring/spring-context.xml"});
        context.start();
    }
    @Test
    public void test(){
        IGAppPushImpl iGAppPushImpl= (IGAppPushImpl) context.getBean("iGAppPushImpl");
        Map<String, Object> mapInfo=new HashMap<>();
        mapInfo.put(PushConsts.KEY_TEMPLATETYPE,PushConsts.TRANSMISSION_TEMPLATE);
        mapInfo.put(PushConsts.KEY_TITLE,"药药好女厕所");
        mapInfo.put(PushConsts.KEY_TEXT,"12354745111");
        mapInfo.put(PushConsts.KEY_TRANSMISSIONCONTENT,"12354745111");
        iGAppPushImpl.pushToApp(mapInfo);
    }
}
