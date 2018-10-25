package mil.army.milirang.event.vo;

import java.io.Serializable;

public class EventReceiverVO implements Serializable {

    private String event_id;
    private String receiver_id;

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public String getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(String receiver_id) {
        this.receiver_id = receiver_id;
    }
}
