package mil.army.milirang.schedule;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.squareup.timessquare.CalendarPickerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mil.army.milirang.R;

public class ScheduleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        CalendarPickerView calendar = (CalendarPickerView) findViewById(R.id.calendar_view);
        TextView t = (TextView) findViewById(R.id.test);

        ArrayList<Date> info = new ArrayList<Date>();

        String day1 = "2018-10-25";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date date1 = format.parse(day1);
            info.add(date1);
        }catch (ParseException o)
        {
            Log.d("Wrong Format", "Wrong Format!");
        }



        Calendar nextMonth = Calendar.getInstance();
        nextMonth.add(Calendar.MONTH, 1);

        Date today = new Date();
        calendar.init(today, nextMonth.getTime()).inMode(CalendarPickerView.SelectionMode.RANGE).withHighlightedDate(today);

        calendar.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                TextView t = (TextView) findViewById(R.id.test);

                String selected = new SimpleDateFormat("yyyy-MM-dd").format(date);
                t.setText("당직자 : 상병 윤종식"); //나중에 서버에서 해당 날짜(포맷은 위와같음)로 저장된 키 : value를 찾아서 밑에 해당 휴가자나 당직자를 표시
            }

            @Override
            public void onDateUnselected(Date date) {

            }
        });

        t.setText(calendar.getSelectedDates().size());
//        CalendarCellDecorator deco = new CalendarCellDecorator();
  //      calendar.getDecorators().add(deco);


    }
}
