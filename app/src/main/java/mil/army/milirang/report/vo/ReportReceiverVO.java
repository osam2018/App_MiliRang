package mil.army.milirang.report.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ReportReceiverVO implements Serializable {

    private String report_id;
    private String receiver_id;

    public String getReport_id() {
        return report_id;
    }

    public void setReport_id(String report_id) {
        this.report_id = report_id;
    }

    public String getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(String receiver_id) {
        this.receiver_id = receiver_id;
    }
}
