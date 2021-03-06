package mil.army.milirang.user.vo;

import java.io.Serializable;

public class UserVO implements Serializable {

    private String email;
    private String displayName;
    private String uid;
    private String milname;
    private String phoneNum;

    public UserVO() {}

    public String toString() {
        return this.getDisplayName();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUid() {       return uid;    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMilname() {
        return milname;
    }

    public void setMilname(String milname) {
        this.milname = milname;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

}
