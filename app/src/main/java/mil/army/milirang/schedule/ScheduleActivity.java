package mil.army.milirang.schedule;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.squareup.timessquare.CalendarCellDecorator;
import com.squareup.timessquare.CalendarPickerView;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mil.army.milirang.R;
import mil.army.milirang.schedule.vo.ScheduleVO;

public class ScheduleActivity extends AppCompatActivity {
    TextView t;
    Button b;
    Button b_list;
    CalendarPickerView calendar;
    ScheduleVO vo;
    DatabaseReference ref;
    FirebaseUser f_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        t = (TextView) findViewById(R.id.test);
        b = (Button) findViewById(R.id.summit_button);
        b_list = (Button) findViewById(R.id.to_mylist);
        calendar = (CalendarPickerView) findViewById(R.id.calendar_view);
        f_user = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference().child("schedules");

        ArrayList<Date> info = new ArrayList<Date>();

        String day1 = "2018-10-25";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        try { //임시로 String을 Date로 바꾸는거 실험용
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
                List<String> days = new ArrayList<String>();
                for(int i = 0; i < daylist.size(); i++) {
                    String day = new SimpleDateFormat("yyyy-MM-dd").format(daylist.get(i));
                    days.add(day);
                    Toast.makeText(ScheduleActivity.this, day, Toast.LENGTH_SHORT).show();
                ref.child(f_user.getUid()).setValue(days); // 버튼을 누르면 선택한 날짜를 넘겨줌
                }
            }
        });

        b_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<String> templist = new ArrayList<String>();
                templist.add("2018-10-28");
                FirebaseDatabase.getInstance().getReference("messeges").child(f_user.getUid()).setValue(templist); // 버튼을 누르면 창이넘어가면서

                Intent intent = new Intent(ScheduleActivity.this, ScheduleScrollingActivity.class);
                ScheduleActivity.this.startActivity(intent);
            }
        });



        t.setText(String.valueOf(calendar.getSelectedDates().size()));
//        CalendarCellDecorator deco = new CalendarCellDecorator();
  //      calendar.getDecorators().add(deco);


    }
}
