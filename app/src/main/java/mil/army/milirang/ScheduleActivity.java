package mil.army.milirang;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.squareup.timessquare.CalendarPickerView;

import java.util.Calendar;
import java.util.Date;

public class ScheduleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        CalendarPickerView calendar = (CalendarPickerView) findViewById(R.id.calendar_view);
        TextView t = (TextView) findViewById(R.id.test);

        Calendar nextMonth = Calendar.getInstance();
        nextMonth.add(Calendar.MONTH, 1);

        Date today = new Date();
        calendar.init(today, nextMonth.getTime()).inMode(CalendarPickerView.SelectionMode.RANGE).withHighlightedDate(today);

        t.setText(calendar.getSelectedDates().size());
//        CalendarCellDecorator deco = new CalendarCellDecorator();
  //      calendar.getDecorators().add(deco);


    }
}
