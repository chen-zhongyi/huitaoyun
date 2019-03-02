package vos;

import annotations.DataField;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import models.person.Person;
import models.token.AccessToken;

public class PersonVO extends OneData {
    
    @DataField(name = "用户id")
    public Long personId;
    @DataField(name = "用户名")
    public String username;
    @DataField(name = "手机号")
    public String phone;
    @DataField(name = "用户类型")
    public Integer type;
    @DataField(name = "邮箱")
    public String email;
    @DataField(name = "编号")
    public String number;
    @DataField(name = "姓名")
    public String name;
    @DataField(name = "名字拼音")
    public String pinyin;
    @DataField(name = "头像")
    public String avatar;
    @DataField(name = "简介")
    public String intro;
    @DataField(name = "出生日期")
    public Long birthday;
    @DataField(name = "性别")
    public Integer sex;
    @DataField(name = "token", enable = false)
    public String accesstoken;
    @JsonInclude(Include.NON_NULL)
    @DataField(name = "密码", enable = false)
    public String password;
    @DataField(name = "首次登录时间", enable = false)
    public Long firstLoginTime;
    @DataField(name = "最后登录时间", enable = false)
    public Long lastLoginTime;
    @DataField(name = "登录次数", enable = false)
    public Integer loginAmount;

    @DataField(name = "创建时间", enable = false)
    public transient Long createTime;
    @DataField(name = "更新时间", enable = false)
    public transient Long updateTime;
    @DataField(name = "是否删除", enable = false)
    public transient Integer deleted;
    
    public PersonVO() {
        this.condition = " order by rank ";
    }
    
    public PersonVO(Person person) {
        super(person);
        this.personId = person.id;
        this.username = person.username;
        this.phone = person.phone;
        this.email = person.email;
        this.number = person.number;
        this.name = person.name;
        this.pinyin = person.pinyin;
        this.avatar = person.avatar;
        this.intro = person.intro;
        this.birthday = person.birthday;
        this.sex = person.sex.code();
        this.type = person.type.code();
        this.firstLoginTime = person.firstLoginTime;
        this.lastLoginTime = person.lastLoginTime;
        this.loginAmount = person.loginAmount;
    }
    
    public PersonVO(AccessToken accessToken) {
        this((Person) accessToken.person);
        Person person = (Person) accessToken.person;
    }
    
}
