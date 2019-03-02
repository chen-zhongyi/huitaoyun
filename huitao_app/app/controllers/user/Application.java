package controllers.user;

import annotations.ActionMethod;
import annotations.ParamField;
import enums.AppType;
import enums.ClientType;
import models.area.Area;
import org.apache.commons.lang.StringUtils;
import play.db.jpa.Transactional;
import vos.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Application extends ApiController {
    
    @Transactional(readOnly = true)
    public static void index() {
        renderHtml("mobile...");
    }
    
    @ActionMethod(name = "版本号详情", clazz = VersionVO.class)
    public static void version(@ParamField(name = "客户端类型") Integer clientType) {
        renderJSON(Result.succeed(new VersionVO(AppType.USER, ClientType.convert(clientType))));
    }
    
    @ActionMethod(name = "客户端下载")
    public static void download(@ParamField(name = "客户端类型") Integer clientType) {
        VersionVO versionVO = new VersionVO(AppType.USER, ClientType.convert(clientType));
        redirect(versionVO.downloadUrl);
    }
    
    @ActionMethod(name = "地区数据", clazz = AreaVO.class)
    public static void areaData(@ParamField(name = "地址code", required = false) String code) {
        AreaVO areaVO = null;
        if (StringUtils.isBlank(code)) {
            areaVO = AreaVO.tree();
        } else {
            Area area = Area.findByCode(code);
            areaVO = new AreaVO(area);
            areaVO.children(Area.fetchByParent(area));
        }
        renderJSON(Result.succeed(areaVO));
    }
    
    @ActionMethod(name = "七牛token", clazz = QiniuVO.class)
    public static void qiniuUptoken() {
        renderJSON(Result.succeed(new QiniuVO()));
    }
    
    
    public static String m(String content) {
        String reg = "(https|http|ftp)?(://)?[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(content);
        StringBuffer sb = new StringBuffer();
        
        while (matcher.find()) {
            String match = matcher.group(0);
            matcher.appendReplacement(sb, "<a>" + match + "</a>");
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

}
