package models.person;

import enums.PersonType;
import enums.Sex;
import models.token.BasePerson;
import org.apache.commons.lang.StringUtils;
import utils.CodeUtils;
import utils.PinYinUtils;
import vos.PersonVO;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Person extends BasePerson {

    public static Person add(PersonVO personVO) {
        Person person = new Person();
        person.username = personVO.username;
        person.email = personVO.email;
        person.phone = personVO.phone;
        person.edit(personVO);
        person.editPassword(CodeUtils.md5("123456"));
        return person;
    }
    
    public static Person regist(String phone, String password) {
        Person person = new Person();
        person.name = "管理员";
        person.pinyin = pinyin(person.name);
        person.phone = phone;
        person.save();
        person.editPassword(password);
        return person;
    }
    
    public static Person regist(String phone) {
        return regist(phone, CodeUtils.md5("123456"));
    }
    
    public static void initAdmin() {
        if (fetchAll().isEmpty()) {
            Person person = new Person();
            person.name = "管理员";
            person.pinyin = pinyin(person.name);
            person.username = "admin";
            person.type = PersonType.ADMIN;
            person.save();
            person.editPassword(CodeUtils.md5("123456"));
        }
    }
    
    public static void initPinYin() {
        List<Person> persons = Person.fetchAll();
        persons.stream().filter(p -> p.pinyin == null && p.name != null).forEach(p -> {
            p.pinyin = pinyin(p.name);
            p.save();
        });
    }
    
    public static String pinyin(String name) {
        if (name == null) return null;
        return PinYinUtils.getFirstSpell(name) + "," + PinYinUtils.getPinYin(name);
    }

    public void editType(PersonType type) {
        this.type = type;
        this.save();
    }
    
    public void editPhone(String phone) {
        this.phone = phone;
        this.save();
    }
    
    public void editEmail(String email) {
        this.email = email;
        this.save();
    }
    
    public void edit(PersonVO personVO) {
        this.name = personVO.name != null ? personVO.name : name;
        this.pinyin = pinyin(this.name);
        this.sex = personVO.sex != null ? Sex.convert(personVO.sex) : sex;
        this.avatar = personVO.avatar != null ? personVO.avatar : avatar;
        this.number = personVO.number != null ? personVO.number : number;
        this.email = personVO.email != null ? personVO.email : email;
        this.phone = personVO.phone != null ? personVO.phone : phone;
        this.intro = personVO.intro != null ? personVO.intro : intro;
        this.birthday = personVO.birthday != null ? personVO.birthday : birthday;
        this.save();
    }
    
    public static boolean isPhoneAvailable(String phone) {
        return Person.findByPhone(phone, PersonType.NORMAL) == null;
    }
    
    public static boolean isEmailAvailable(String email) {
        return Person.findByEmail(email) == null;
    }
    
    public boolean isAdmin() {
        return this.type != null && this.type == PersonType.ADMIN;
    }
    
    public void del() {
        this.logicDelete();
    }
    
    public static Person findByUsername(String username) {
        return Person.find(defaultSql("username = ?"), username).first();
    }
    
    public static Person findByPhone(String phone) {
        return Person.find(defaultSql("phone = ?"), phone).first();
    }
    
    public static Person findByPhone(String phone, PersonType type) {
        return Person.find(defaultSql("phone = ? and type = ?"), phone, type).first();
    }
    
    public static Person findByEmail(String email) {
        return Person.find(defaultSql("email = ?"), email).first();
    }
    
    public static Person findByNumber(String number) {
        return Person.find(defaultSql("number = ?"), number).first();
    }
    
    public static List<Person> fetchAll() {
        return Person.find(defaultSql()).fetch();
    }

    public static List<Person> fetch(PersonVO personVO) {
        Object[] data = data(personVO);
        List<String> hqls = (List<String>) data[0];
        List<Object> params = (List<Object>) data[1];
        return Person.find(defaultSql(StringUtils.join(hqls, " and ")) + personVO.condition, params.toArray())
                .fetch(personVO.page, personVO.size);
    }
    
    public static int count(PersonVO personVO) {
        Object[] data = data(personVO);
        List<String> hqls = (List<String>) data[0];
        List<Object> params = (List<Object>) data[1];
        return (int) Person.count(defaultSql(StringUtils.join(hqls, " and ")), params.toArray());
    }
    
    private static Object[] data(PersonVO personVO) {
        List<String> hqls = new ArrayList<>();
        List<Object> params = new ArrayList<>();
        if (StringUtils.isNotBlank(personVO.search)) {
            hqls.add("concat_ws(',',name,phone,email,pinyin) like ?");
            params.add("%" + personVO.search + "%");
        }
        return new Object[]{hqls, params};
    }
}
