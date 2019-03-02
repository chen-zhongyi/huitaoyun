package enums;

import annotations.EnumClass;
import interfaces.BaseEnum;

@EnumClass(name = "APP类型")
public enum AppType implements BaseEnum {
    USER(101, "用户端"), ADMIN(102, "管理端");
    private int code;
    private String intro;

    private AppType(int code, String intro) {
        this.code = code;
        this.intro = intro;
    }
    
    public static AppType convert(int code) {
        for (AppType sex : AppType.values()) {
            if (sex.code == code) {
                return sex;
            }
        }
        return null;
    }
    
    public static AppType convert(String intro) {
        for (AppType sex : AppType.values()) {
            if (sex.intro.equals(intro)) {
                return sex;
            }
        }
        return null;
    }
    
    public int code() {
        return this.code;
    }
    
    public String intro() {
        return this.intro;
    }
}