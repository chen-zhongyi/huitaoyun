package enums;

import annotations.EnumClass;
import interfaces.BaseEnum;

@EnumClass(name = "短信类型", visible = false)
public enum SmsType implements BaseEnum {
    
    APPROVAL(101, "文本", "66906", "#name#,#title#,#content#");
    
    private int code;
    private String intro;
    private String sms;
    private String templet;
    
    
    private SmsType(int code, String intro, String sms, String templet) {
        this.code = code;
        this.intro = intro;
        this.sms = sms;
        this.templet = templet;
    }
    
    public static SmsType convert(int code) {
        for (SmsType type : SmsType.values()) {
            if (type.code == code) {
                return type;
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
    
    public String sms() {
        return this.sms;
    }
    
    public String templet() {
        return this.templet;
    }
}
