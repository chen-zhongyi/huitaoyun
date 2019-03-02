package utils;

import com.github.qcloudsms.SmsVoicePromptSender;
import com.github.qcloudsms.SmsVoicePromptSenderResult;
import play.Logger;

public class QCloudUtils {
    
    // 短信应用SDK AppID
    private static int appid = 1400074960; // 1400开头
    
    // 短信应用SDK AppKey
    private static String appkey = "eb377f8ab0fa16928e4484c07e0e4192";
    
    public static void sendVoice(String phone, String content) {
        try {
            String[] contents = content.split(",");
            String msg = "您好，{1}的{2}需要您审批，请尽快处理。{3}";
            msg = msg.replace("1", contents[0]).replace("2", contents[1]).replace("3", "");
            SmsVoicePromptSender vpsender = new SmsVoicePromptSender(appid, appkey);
            SmsVoicePromptSenderResult result = vpsender.send("86", phone, 2, 2, msg, "");
            Logger.info("qcloud:%s,%s,%s", phone, msg, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
