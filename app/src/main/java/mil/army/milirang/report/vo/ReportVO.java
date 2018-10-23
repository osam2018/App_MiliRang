package mil.army.milirang.report.vo;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import mil.army.milirang.user.vo.UserVO;

@IgnoreExtraProperties
public class ReportVO implements Serializable {

    private String rpt_title;
    private String rpt_body;
    private String rpt_timestamp;
    private UserVO rpt_sender;
    private List<UserVO> rpt_receiver;

    public ReportVO() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
        this.setRpt_title("");
        this.setRpt_body("");
        this.setRpt_timestamp("");
        this.setRpt_sender(new UserVO());
        this.setRpt_receiver(new ArrayList<UserVO>());
    }

    public ReportVO(String rpt_title, String rpt_body, String rpt_timestamp, UserVO rpt_sender, List<UserVO> rpt_receiver) {
        this.setRpt_title(rpt_title);
        this.setRpt_body(rpt_body);
        this.setRpt_timestamp(rpt_timestamp);
        this.setRpt_sender(rpt_sender);
        this.setRpt_receiver(rpt_receiver);
    }

    public String getRpt_title() {
        return rpt_title;
    }

    public void setRpt_title(String rpt_title) {
        this.rpt_title = rpt_title;
    }

    public String getRpt_body() {
        return rpt_body;
    }

    public void setRpt_body(String rpt_body) {
        this.rpt_body = rpt_body;
    }

    public String getRpt_timestamp() {
        return rpt_timestamp;
    }

    public void setRpt_timestamp(String rpt_timestamp) {
        this.rpt_timestamp = rpt_timestamp;
    }

    public UserVO getRpt_sender() {
        return rpt_sender;
    }

    public void setRpt_sender(UserVO rpt_sender) {
        this.rpt_sender = rpt_sender;
    }

    public List<UserVO> getRpt_receiver() {
        return rpt_receiver;
    }

    public void setRpt_receiver(List<UserVO> rpt_receiver) {
        this.rpt_receiver = rpt_receiver;
    }
}