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
import java.util.HashMap;
import java.util.List;

import mil.army.milirang.R;
import mil.army.milirang.schedule.vo.ScheduleVO;
import mil.army.milirang.user.vo.UserVO;

import static mil.army.milirang.schedule.ValuesFromFirebase.*;

public class ScheduleActivity extends AppCompatActivity {
    TextView t;
    Button b;
    Button b_list;
    Button my_set;
    CalendarPickerView calendar;
    ScheduleVO vo;
    FirebaseUser f_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        t = (TextView) findViewById(R.id.test);
        b = (Button) findViewById(R.id.summit_button);
        b_list = (Button) findViewById(R.id.to_mylist);
        my_set = (Button) findViewById(R.id.my_set_buttion);
        calendar = (CalendarPickerView) findViewById(R.id.calendar_view);
        f_user = FirebaseAuth.getInstance().getCurrentUser();

///////////////Calender Setting////////////////////////
        Calendar firstday = Calendar.getInstance();
        //firstday.set(Calendar.DAY_OF_MONTH, 1);
        Calendar lastday = Calendar.getInstance();
        lastday.add(Calendar.MONTH, 1);

        Date today = new Date();
        calendar.init(firstday.getTime(), lastday.getTime()).inMode(CalendarPickerView.SelectionMode.MULTIPLE).withHighlightedDate(today);

        List<Date> nodays = new ArrayList<Date>();
        nodays.add(Calendar.getInstance().getTime());

        calendar.setDateSelectableFilter(new CalendarPickerView.DateSelectableFilter() {
            @Override
            public boolean isDateSelectable(Date date) {
                String selected = new SimpleDateFormat("yyyy-MM-dd").format(date);
                for(String tmp : workdays.keySet())
                {
                    ArrayList<String> array_here = (ArrayList<String>) workdays.get(tmp);
                    if(array_here.contains(selected))
                    {
                        String name_found = ScheduleActivity.findNameInList(tmp, refuids);
                        if(name_found == null)
                        {
                            return false;
                        }
                        else
                            return true;
                    }
                }
                return true;
            }
        });

        CalendarCellDecorator deco = new CalDeco(calendar, nodays);
        List<CalendarCellDecorator> decorators = new ArrayList<CalendarCellDecorator>();
        decorators.add(deco);
        calendar.setDecorators(decorators);

        calendar.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                String selected = new SimpleDateFormat("yyyy-MM-dd").format(date);

                for(String tmp : workdays.keySet())
                {
                    ArrayList<String> array_here = (ArrayList<String>) workdays.get(tmp);
                    if(array_here.contains(selected))
                    {
                        String name_found = ScheduleActivity.findNameInList(tmp, refuids);
                        if(name_found != null)
                        {
                            t.setText(name_found);
                            return;
                        }
                    }
                }
                t.setText("Not Reserved!");
                //workdays에서 날짜를 찾아서 날짜를 가지고 있는 uid의 displayname을 출력 만약 다 찾아봤는데도 없으면 그냥 not reserved!
            }

            @Override
            public void onDateUnselected(Date date) {

            }
        });
////////////////////Calendar End/////////////////////////////////////////



////////////////Button Setting/////////////////////////

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List daylist = calendar.getSelectedDates();
                List<String> days = new ArrayList<String>();
                for(int i = 0; i < daylist.size(); i++) {
                    String day = new SimpleDateFormat("yyyy-MM-dd").format(daylist.get(i));
                    days.add(day);
                    Toast.makeText(ScheduleActivity.this, day, Toast.LENGTH_SHORT).show();
                    FirebaseDatabase.getInstance().getReference().child("schedules").child(f_user.getUid()).setValue(days); // 버튼을 누르면 선택한 날짜를 넘겨줌
                }
            }
        });

        b_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScheduleActivity.this, ScheduleScrollingActivity.class);
                ScheduleActivity.this.startActivity(intent);
            }
        });

        my_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List daylist = calendar.getSelectedDates();
                List<String> days = new ArrayList<String>();

                for(int i = 0; i < daylist.size(); i++) {
                    String day = new SimpleDateFormat("yyyy-MM-dd").format(daylist.get(i));
                    //if there is already value that matches with day
                    for(String tmp : workdays.keySet())
                    {
                        ArrayList<String> array_herdays = (ArrayList<String>) workdays.get(tmp);
                        if(array_herdays.contains(day) && !f_user.getUid().equals(tmp))
                        {
                            Toast.makeText(ScheduleActivity.this, day+"에 이미 "+findNameInList(tmp, refuids)+"가 등록했습니다.", Toast.LENGTH_SHORT).show();
                            days.clear();
                            return;
                        }
                    }
                    days.add(day);
                }
                Toast.makeText(ScheduleActivity.this, "내 당직으로 설정완료", Toast.LENGTH_SHORT).show();
                FirebaseDatabase.getInstance().getReference("workdays").child(f_user.getUid()).setValue(days);
            }
        });
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        Calendar firstday = Calendar.getInstance();
        Calendar lastday = Calendar.getInstance();
        lastday.add(Calendar.MONTH, 1);
        Date today = new Date();

        calendar.init(firstday.getTime(), lastday.getTime()).inMode(CalendarPickerView.SelectionMode.MULTIPLE).withHighlightedDate(today);
    }
    public static String findNameInList(String uid, List<UserVO> arr){
        for(UserVO a : arr)
        {
            if(a.getUid().equals(uid))
            {
                return a.getDisplayName();
            }
        }
        return null;
    }
}
