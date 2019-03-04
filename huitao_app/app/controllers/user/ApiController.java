package controllers.user;

import controllers.BaseController;
import play.mvc.Before;

public class ApiController extends BaseController {
    
    @Before(priority = 100, unless = {
			"user.Application.index", "user.Application.version", "user.Application.download",
            "user.Application.configData", "user.Application.areaData", "user.Application.qiniuUptoken", "user.Application.push",
            "user.PersonController.captcha", "user.PersonController.login", "user.PersonController.forgetPassword",
            "user.CategoryController.list", "user.NewsController.list", "user.NewsController.info"
    })
    public static void access() {
        accesstoken();
    }
}
