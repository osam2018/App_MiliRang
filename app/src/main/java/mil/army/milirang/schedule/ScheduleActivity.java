package mil.army.milirang.schedule;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.timessquare.CalendarCellDecorator;
import com.squareup.timessquare.CalendarPickerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mil.army.milirang.R;

public class ScheduleActivity extends AppCompatActivity {
    TextView t;
    Button b;
    CalendarPickerView calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        t = (TextView) findViewById(R.id.test);
        b = (Button) findViewById(R.id.summit_button);
        calendar = (CalendarPickerView) findViewById(R.id.calendar_view);

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

        Calendar firstday = Calendar.getInstance();
        firstday.set(Calendar.DAY_OF_MONTH, 1);
        Calendar lastday = Calendar.getInstance();
        lastday.set(Calendar.DAY_OF_MONTH, 1);
        lastday.add(Calendar.DAY_OF_MONTH, 31);

        Date today = new Date();
        calendar.init(firstday.getTime(), lastday.getTime()).inMode(CalendarPickerView.SelectionMode.MULTIPLE).withHighlightedDate(today);

        List<Date> nodays = new ArrayList<Date>();
        nodays.add(Calendar.getInstance().getTime());

        CalendarCellDecorator deco = new CalDeco(calendar, nodays);
        final List<CalendarCellDecorator> decorators = new ArrayList<CalendarCellDecorator>();
        decorators.add(deco);
        calendar.setDecorators(decorators);

        calendar.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                String selected = new SimpleDateFormat("yyyy-MM-dd").format(date);
                t.setText(selected); //나중에 서버에서 해당 날짜(포맷은 위와같음)로 저장된 키 : value를 찾아서 밑에 해당 휴가자나 당직자를 표시
            }

            @Override
            public void onDateUnselected(Date date) {

            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List daylist = calendar.getSelectedDates();
                for(int i = 0; i < daylist.size(); i++) {
                    String day = new SimpleDateFormat("yyyy-MM-dd").format(daylist.get(i));
                    Toast.makeText(ScheduleActivity.this, day, Toast.LENGTH_SHORT).show();
                }
            }
        });

        t.setText(String.valueOf(calendar.getSelectedDates().size()));
//        CalendarCellDecorator deco = new CalendarCellDecorator();
  //      calendar.getDecorators().add(deco);


    }
}
