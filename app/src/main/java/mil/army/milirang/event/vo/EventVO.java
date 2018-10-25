package mil.army.milirang.event.vo;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class EventVO implements Serializable {

    private String event_id;
    private String event_title;
    private long event_from;
    private long event_to;
    private String event_detail;
    private String event_location;

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public String getEvent_title() {
        return event_title;
    }

    public void setEvent_title(String event_title) {
        this.event_title = event_title;
    }

    public long getEvent_from() {
        return event_from;
    }

    public void setEvent_from(long event_from) {
        this.event_from = event_from;
    }

    public long getEvent_to() {
        return event_to;
    }

    public void setEvent_to(long event_to) {
        this.event_to = event_to;
    }

    public String getEvent_detail() {
        return event_detail;
    }

    public void setEvent_detail(String event_detail) {
        this.event_detail = event_detail;
    }

    public String getEvent_location() {
        return event_location;
    }

    public void setEvent_location(String event_location) {
        this.event_location = event_location;
    }
}
