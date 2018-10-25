package mil.army.milirang.event;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import mil.army.milirang.R;
import mil.army.milirang.event.vo.EventReceiverVO;
import mil.army.milirang.event.vo.EventVO;
import mil.army.milirang.report.vo.ReportVO;
import mil.army.milirang.user.vo.UserVO;

public class EventCreateActivity extends AppCompatActivity {

    DatabaseReference mDatabase;
    List<UserVO> mUserList;

    Calendar start_date;
    Calendar end_date;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_create);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mUserList = new ArrayList<>();

        mDatabase.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUserList.clear();
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    UserVO user = singleSnapshot.getValue(UserVO.class);
                    mUserList.add(user);
                }
                MultiAutoCompleteTextView auto = findViewById(R.id.event_create_receiver);

                ArrayAdapter<UserVO> users = new ArrayAdapter<>(EventCreateActivity.this, R.layout.autocomplete_tag, (UserVO[]) mUserList.toArray(new UserVO[0]));
                auto.setAdapter(users);
                auto.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        final EditText startdate = findViewById(R.id.event_create_start_date);
        final Calendar startcal = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener startdatelistener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                startcal.set(Calendar.YEAR, year);
                startcal.set(Calendar.MONTH, month);
                startcal.set(Calendar.DATE, dayOfMonth);
                String myFormat="yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREAN);
                start_date = startcal;
                startdate.setText(sdf.format(startcal.getTime()));
            }
        };

        final EditText enddate = findViewById(R.id.event_create_end_date);
        final Calendar endcal = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener enddatelistener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                endcal.set(Calendar.YEAR, year);
                endcal.set(Calendar.MONTH, month);
                endcal.set(Calendar.DATE, dayOfMonth);
                String myFormat="yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREAN);
                end_date = endcal;
                enddate.setText(sdf.format(endcal.getTime()));
            }
        };

        startdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(EventCreateActivity.this, startdatelistener, startcal.get(Calendar.YEAR),startcal.get(Calendar.MONTH),startcal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        enddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(EventCreateActivity.this, enddatelistener, endcal.get(Calendar.YEAR),endcal.get(Calendar.MONTH),endcal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



        Button submitBtn = findViewById(R.id.event_create_submit);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventVO event = new EventVO();
                event.setEvent_title(((TextView) findViewById(R.id.event_create_title)).getText().toString());
                event.setEvent_detail(((TextView) findViewById(R.id.event_create_body)).getText().toString());
                event.setEvent_from(start_date.getTimeInMillis());
                event.setEvent_to(end_date.getTimeInMillis());
                DatabaseReference newobj = mDatabase.child("event")
                        .child(String.valueOf(start_date.get(Calendar.YEAR)))
                        .child(String.valueOf(start_date.get(Calendar.MONTH) + 1))
                        .push();
                String newkey = newobj.getKey();
                newobj.setValue(event);

                MultiAutoCompleteTextView receiver_input = findViewById(R.id.event_create_receiver);
                String[] rcv_names = receiver_input.getText().toString().split(",");

                for(String rcv_name : rcv_names) {
                    if(rcv_name.trim().length() == 0) continue;
                    EventReceiverVO rcvo = new EventReceiverVO();
                    for(UserVO user : mUserList) {
                        if(user.getDisplayName().equals(rcv_name.trim())) {
                            rcvo.setReceiver_id(user.getUid());
                            rcvo.setEvent_id(newkey);
                            mDatabase.child("event_receiver")
                                    .child(String.valueOf(start_date.get(Calendar.YEAR)))
                                    .child(String.valueOf(start_date.get(Calendar.MONTH) + 1))
                                    .push().setValue(rcvo);
                            break;
                        }
                    }
                }
                EventCreateActivity.this.finish();

            }
        });

    }
}
