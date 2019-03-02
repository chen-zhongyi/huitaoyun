package utils;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.processing.OperationManager;
import com.qiniu.processing.OperationStatus;
import com.qiniu.storage.Configuration;
import com.qiniu.util.Auth;
import com.qiniu.util.StringUtils;
import com.qiniu.util.UrlSafeBase64;
import play.Logger;

import java.util.ArrayList;
import java.util.List;

public class AudioUtils {
    
    public static String audioFormat(String fileName, String format) {
        return audioFormat(fileName, format, null, null, null);
    }
    
    private static String audioFormat(String fileName, String format, String bitRate, String audioQuality, String samplingRate) {
        List<String> ops = new ArrayList<>();
        ops.add("avthumb/" + format);
        //音频码率
        if (bitRate != null) {
            ops.add("ab/" + bitRate);
        }
        //音频质量
        if (audioQuality != null) {
            ops.add("aq/" + audioQuality);
        }
        //音频采样频率
        if (samplingRate != null) {
            ops.add("ar/" + samplingRate);
        }
        String accessKey = QiniuUtils.ACCESSKEY;
        //String accessKey = "DVRkIPodRYU1gxgR2Sg5umivdZxP4Ig9hhW0Nldb";
        String secretKey = QiniuUtils.SECRETKEY;
        //String secretKey = "3RMWucf8PZLqsEtwH2ry4A3J1O4pBmZbEiQJ9RCR";
        
        //待处理文件所在空间
        String bucket = QiniuUtils.BUCKET;
        //String bucket = "zhihan";
        //待处理文件名
        String key = fileName;
        
        Auth auth = Auth.create(accessKey, secretKey);
        
        //数据处理指令，支持多个指令
        String saveName = key.substring(0, key.indexOf(".")) + "." + format;
        String saveEntry = String.format("%s:" + saveName, bucket);
        String avthumbFop = String.format(StringUtils.join(ops, "/") + "|saveas/%s", UrlSafeBase64.encodeToString(saveEntry));
        //将多个数据处理指令拼接起来
        String persistentOpfs = StringUtils.join(new String[]{
                avthumbFop
        }, ";");
        
        //数据处理队列名称，必须
        String persistentPipeline = "zhihan";
        //数据处理完成结果通知地址
        //String persistentNotifyUrl = "http://api.example.com/qiniu/pfop/notify";
        
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());
        //...其他参数参考类注释
        
        //构建持久化数据处理对象
        OperationManager operationManager = new OperationManager(auth, cfg);
        try {
            
            String persistentId = operationManager.pfop(bucket, key, persistentOpfs, persistentPipeline, true);
            //可以根据该 persistentId 查询任务处理进度
            //System.out.println(persistentId);
            
            OperationStatus operationStatus = operationManager.prefop(persistentId);
            //解析 operationStatus 的结果
        } catch (QiniuException e) {
            Logger.error("[audio format error]:" + e.response.toString());
        }
        return QiniuUtils.DOMAIN + "/" + saveName;
        //return "https://oss.zhihanyun.com" + "/" + saveName;
    }
    
    public static void main(String[] args) {
        String name = "10015.wav";
        //System.out.println(name.substring(0, name.indexOf(".")));
        System.out.println(audioFormat("10015.wav", "mp3", null, null, null));
    }
}
