package controllers.user;

import annotations.ActionMethod;
import annotations.ParamField;
import binders.PasswordBinder;
import enums.CaptchaType;
import enums.PersonType;
import models.person.Person;
import models.token.AccessToken;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import play.Logger;
import play.Play;
import play.data.binding.As;
import utils.CacheUtils;
import utils.SMSUtils;
import vos.PersonVO;
import vos.Result;
import vos.Result.StatusCode;

public class PersonController extends ApiController {
    
    @ActionMethod(name = "验证码获取")
    public static void captcha(@ParamField(name = "验证码类型") Integer type,
                               @ParamField(name = "手机号") String phone) {
        CaptchaType captchaType = CaptchaType.convert(type);
        if (captchaType == null) {
            renderJSON(Result.failed());
        }
        String captcha = RandomStringUtils.randomNumeric(4);
        if (!Person.isPhoneLegal(phone)) {
            renderJSON(Result.failed(StatusCode.PERSON_PHONE_UNVALID));
        }
        Person person = Person.findByPhone(phone, PersonType.NORMAL);
        if (captchaType == CaptchaType.REGIST && person != null) {
            renderJSON(Result.failed(StatusCode.PERSON_PHONE_EXIST));
        }
        if (captchaType == CaptchaType.LOGIN && person == null) {
            renderJSON(Result.failed(StatusCode.PERSON_ACCOUNT_NOTEXIST));
        }
        if (captchaType == CaptchaType.PASSWORD && person == null) {
            renderJSON(Result.failed(StatusCode.PERSON_ACCOUNT_NOTEXIST));
        }
        if (captchaType == CaptchaType.PHONE && person != null) {
            renderJSON(Result.failed(StatusCode.PERSON_PHONE_EXIST));
        }
        if (Play.id.equals("p"))
            SMSUtils.send(captchaType, captcha, phone);
        else
            captcha = "4545";
        Logger.info("[captcha] %s,%s,%s", type, phone, captcha);
        CacheUtils.set(captchaType.key(phone), captcha, "10mn");
        if (Play.id.equals("p"))
            renderJSON(Result.succeed());
        else
            renderJSON(Result.succeed(captcha));
    }
    
    @ActionMethod(name = "用户登录", clazz = PersonVO.class)
    public static void login(@ParamField(name = "手机号") String phone,
                             @ParamField(name = "密码", required = false) @As(binder = PasswordBinder.class) String password,
                             @ParamField(name = "验证码", required = false) String captcha) {
        Person person = Person.findByPhone(phone, PersonType.NORMAL);
        if (person == null) {
            renderJSON(Result.failed(StatusCode.PERSON_ACCOUNT_NOTEXIST));
        }
        if (StringUtils.isNotBlank(captcha)) {
            if (!CaptchaType.LOGIN.validate(phone, captcha)) {
                renderJSON(Result.failed(StatusCode.PERSON_CAPTCHA_ERROR));
            }
        } else {
            if (!person.isPasswordRight(password)) {
                renderJSON(Result.failed(StatusCode.PERSON_PASSWORD_ERROR));
            }
        }
        AccessToken accessToken = AccessToken.add(person);
        renderJSON(Result.succeed(new PersonVO(accessToken)));
    }
    
    @ActionMethod(name = "忘记密码", clazz = PersonVO.class)
    public static void forgetPassword(@ParamField(name = "用户名") String phone,
                                      @ParamField(name = "密码") @As(binder = PasswordBinder.class) String password,
                                      @ParamField(name = "验证码") String captcha) {
        if (!CaptchaType.PASSWORD.validate(phone, captcha)) {
            renderJSON(Result.failed(StatusCode.PERSON_CAPTCHA_ERROR));
        }
        Person person = Person.findByPhone(phone, PersonType.NORMAL);
        if (person == null) {
            renderJSON(Result.failed(StatusCode.PERSON_ACCOUNT_NOTEXIST));
        }
        if (StringUtils.isNotBlank(password)) {
            if (!Person.isPasswordLegal(password)) {
                renderJSON(Result.failed(StatusCode.PERSON_PASSWORD_UNVALID));
            }
            person.editPassword(password);
        }
        AccessToken accessToken = AccessToken.add(person);
        renderJSON(Result.succeed(new PersonVO(accessToken)));
    }
    
    @ActionMethod(name = "绑定手机")
    public static void bindPhone(@ParamField(name = "手机号") String phone, @ParamField(name = "验证码") String captcha) {
        if (!CaptchaType.PHONE.validate(phone, captcha)) {
            renderJSON(Result.failed(StatusCode.PERSON_CAPTCHA_ERROR));
        }
        if (!Person.isPhoneAvailable(phone)) {
            renderJSON(Result.failed(StatusCode.PERSON_PHONE_EXIST));
        }
        Person person = getPersonByToken();
        person.editPhone(phone);
        renderJSON(Result.succeed());
    }
    
    @ActionMethod(name = "绑定邮箱")
    public static void bindEmail(@ParamField(name = "邮箱") String email, @ParamField(name = "验证码") String captcha) {
        if (!CaptchaType.EMAIL.validate(email, captcha)) {
            renderJSON(Result.failed(StatusCode.PERSON_CAPTCHA_ERROR));
        }
        if (!Person.isEmailAvailable(email)) {
            renderJSON(Result.failed(StatusCode.PERSON_PHONE_EXIST));
        }
        Person person = getPersonByToken();
        person.editEmail(email);
        renderJSON(Result.succeed());
    }
    
    @ActionMethod(name = "用户详情", clazz = PersonVO.class)
    public static void info() {
        Person person = getPersonByToken();
        AccessToken accessToken = getAccessTokenByToken();
        renderJSON(Result.succeed(new PersonVO(accessToken)));
    }
    
    @ActionMethod(name = "信息编辑", param = "-name,-avatar,-sex,-birthday", clazz = PersonVO.class)
    public static void edit(PersonVO vo) {
        Person person = getPersonByToken();
        person.edit(vo);
        renderJSON(Result.succeed(new PersonVO(person)));
    }
    
    @ActionMethod(name = "验证密码")
    public static void validatePassword(@ParamField(name = "密码") @As(binder = PasswordBinder.class) String password) {
        Person person = getPersonByToken();
        if (!person.isPasswordRight(password)) {
            renderJSON(Result.failed(StatusCode.PERSON_PASSWORD_ERROR));
        }
        renderJSON(Result.succeed());
    }
    
    @ActionMethod(name = "重置密码")
    public static void resetPassword(@ParamField(name = "密码") @As(binder = PasswordBinder.class) String password) {
        Person person = getPersonByToken();
        if (!Person.isPasswordLegal(password)) {
            renderJSON(Result.failed(StatusCode.PERSON_PASSWORD_UNVALID));
        }
        person.editPassword(password);
        renderJSON(Result.succeed());
    }
    
    @ActionMethod(name = "用户登出")
    public static void logout() {
        renderJSON(Result.succeed());
    }

}