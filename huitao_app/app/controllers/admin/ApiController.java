package controllers.admin;

import controllers.BaseController;
import models.person.Person;
import play.mvc.Before;
import play.mvc.Util;
import utils.BaseUtils;

public class ApiController extends BaseController {

	@Before(priority = 100, unless = { "admin.Application.index", "admin.Application.version",
			"admin.Application.qiniuUptoken", "admin.Application.qiniuUptokenSimple", "admin.AdminController.login" })
	public static void access() {
		//accesstoken();
        if (getAdmin() == null) {
        	Application.index();
		}
	}

	private static final String ADMIN_SESSION_ID = "ADMIN_SESSION_ID";

	@Util
	protected static Person getAdmin() {
		String adminId = BaseUtils.getSession(ADMIN_SESSION_ID);
		if (adminId != null) {
			Person admin = Person.findByID(Long.parseLong(adminId));
			if (admin != null && admin.isAdmin()) {
				return admin;
			}
		}
		return null;
	}

	@Util
	protected  static void setAdmin(Person admin) {
		BaseUtils.setSession(ADMIN_SESSION_ID, admin.id + "");
	}
}