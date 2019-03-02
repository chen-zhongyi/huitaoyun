package utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import enums.CaptchaType;
import enums.SmsType;
import org.apache.commons.lang.StringUtils;
import play.Logger;
import play.libs.WS;
import play.libs.WS.HttpResponse;
import results.sso.OrganizesResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SMSUtils {
    
    private static final String APPHOST = "http://v.juhe.cn/sms/send";
    private static final String APPKEY = "f75b91e41da77c0cb85426b3367f5061";
    
    public static void send(CaptchaType type, String captcha, String phone) {
        HttpResponse response = WS.url(APPHOST).setParameter("tpl_id", type.sms())
                .setParameter("mobile", phone).setParameter("tpl_value", "#code#=" + captcha)
                .setParameter("key", APPKEY).get();
        Logger.info("[sms response] %s", response.getString());
    }
    
    public static void send(SmsType type, String phone, String content)
    {
        String[] templets = StringUtils.split(type.templet(), ",");
        String[] contents = StringUtils.split(content, ",");
        List<String> list = new ArrayList<>();
        for (int i = 0; i < templets.length; i++) {
            list.add(templets[i] + "=" + contents[i]);
        }
        String tpl_value = StringUtils.join(list, "&");
        WS.url(APPHOST).setParameter("tpl_id", type.sms())
                .setParameter("mobile", phone).setParameter("tpl_value", tpl_value)
                .setParameter("key", APPKEY).get();
    }
    
    public static void main(String[] args) {
        String s = "{\"status\":\"succ\",\"code\":20000,\"message\":\"请求成功\",\"data\":{\"page\":1,\"size\":111,\"totalPage\":1,\"totalSize\":111,\"array\":[{\"createTime\":1523799445337,\"updateTime\":1523799445339,\"deleted\":0,\"organizeId\":2,\"name\":\"雾途测试1\",\"id\":2}]}}";
        
        System.out.println(s);
        try {
            ObjectMapper mapper=new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    
            OrganizesResult result = mapper.readValue(s, OrganizesResult.class);
            System.out.println(result.data.array);
            result.data.array.forEach(o -> System.out.println(o.name));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
    }
}
