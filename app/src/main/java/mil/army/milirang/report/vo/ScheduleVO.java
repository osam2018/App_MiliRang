package mil.army.milirang.report.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@IgnoreExtraProperties
public class ScheduleVO implements Serializable
{
    private String sch_user;
    private List<String> mydays;

    public ScheduleVO(String sch_user, List<String> mydays) {
        this.setSch_user(sch_user);
        this.setDays(mydays);
    }

    public String getSch_user() {  return this.sch_user;  }

    public void setSch_user(String sch_user) {
        this.sch_user = sch_user;
    }

    public List<String> getDays() { return this.mydays;  }

    public void setDays(List<String> mydays) { this.mydays = mydays;   }
}
