package mil.army.milirang.event;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.View;

import mil.army.milirang.R;

public class EventCalendarItemView extends ConstraintLayout {


    public EventCalendarItemView(Context context) {
        super(context);
        init();
    }

    public EventCalendarItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EventCalendarItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.event_calendar_item, this);
    }
}
