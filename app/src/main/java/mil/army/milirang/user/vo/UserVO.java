package mil.army.milirang.user.vo;

import java.io.Serializable;

public class UserVO implements Serializable {

    private String usr_name;
    private String usr_email;
    private String usr_milname;

    public UserVO() {}

    public UserVO(String usr_name, String usr_email, String usr_milname) {
        this.usr_name = usr_name;
        this.usr_email = usr_email;
        this.usr_milname = usr_milname;
    }

    public String getUsr_name() {
        return usr_name;
    }

    public void setUsr_name(String usr_name) {
        this.usr_name = usr_name;
    }

    public String getUsr_email() {
        return usr_email;
    }

    public void setUsr_email(String usr_email) {
        this.usr_email = usr_email;
    }

    public String getUsr_milname() {
        return usr_milname;
    }

    public void setUsr_milname(String usr_milname) {
        this.usr_milname = usr_milname;
    }
}
