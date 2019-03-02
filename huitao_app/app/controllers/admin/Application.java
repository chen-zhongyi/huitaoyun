package controllers.admin;

import annotations.ActionMethod;
import annotations.ParamField;
import enums.Access;
import enums.AppType;
import enums.ClientType;
import models.area.Area;
import models.person.Person;
import org.apache.commons.lang.StringUtils;
import vos.*;
import vos.back.AccessVO;

import java.util.ArrayList;
import java.util.List;

public class Application extends ApiController {

    public static void index() {
        final Person admin = getAdmin();
        if (admin != null) {
            home();
        }
        render();
    }

    public static void home() {
        Person currAdmin = getAdmin();
        PersonVO admin = new PersonVO(currAdmin);
        AccessVO accessVO = new AccessVO();
        accessVO.name = "category";
        accessVO.code = 101;
        accessVO.intro = "目录管理";
        List<AccessVO> vos = new ArrayList<>();
        vos.add(accessVO);
        AccessVO accessVO1 = new AccessVO();
        accessVO1.name = "news";
        accessVO1.code = 102;
        accessVO1.intro = "新闻管理";
        render(admin, vos);
    }

    @ActionMethod(name = "版本号详情", clazz = VersionVO.class)
    public static void version(@ParamField(name = "客户端类型") Integer clientType) {
        renderJSON(Result.succeed(new VersionVO(AppType.ADMIN, ClientType.convert(clientType))));
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
    
    public static void qiniuUptokenSimple() {
        renderJSON(new QiniuVO());
    }
    
}
