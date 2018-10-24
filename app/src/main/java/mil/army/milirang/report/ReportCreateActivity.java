package mil.army.milirang.report;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.MultiAutoCompleteTextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import mil.army.milirang.R;
import mil.army.milirang.report.vo.ReportReceiverVO;
import mil.army.milirang.report.vo.ReportVO;
import mil.army.milirang.user.vo.UserVO;

public class ReportCreateActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private List<UserVO> mUserList;

    private String addZero(int i) {
        if(i < 10) return "0" + i;
        else return "" + i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_create);

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
                MultiAutoCompleteTextView auto = findViewById(R.id.rpt_create_receiver);

                ArrayAdapter<UserVO> users = new ArrayAdapter<>(ReportCreateActivity.this, R.layout.autocomplete_tag, (UserVO[]) mUserList.toArray(new UserVO[0]));
                auto.setAdapter(users);
                auto.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Button submitButton = (Button) findViewById(R.id.rpt_create_submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                Calendar c = Calendar.getInstance();
                String rpt_title = ((EditText) findViewById(R.id.rpt_create_title)).getText().toString();
                String rpt_body = ((EditText) findViewById(R.id.rpt_create_body)).getText().toString();
                String rpt_timestamp = c.get(Calendar.YEAR) + "-" +
                                        addZero(c.get(Calendar.MONTH) + 1) + "-" +
                                        addZero(c.get(Calendar.DATE) + 1) + " " +
                                        addZero(c.get(Calendar.HOUR)) + ":" +
                                        addZero(c.get(Calendar.MINUTE)) + ":" +
                                        addZero(c.get(Calendar.SECOND));
                ReportVO rvo = new ReportVO(rpt_title, rpt_body, rpt_timestamp, uid, Arrays.asList(""));

                String newkey = mDatabase.child("report").push().getKey();
                mDatabase.child("report").child(newkey).setValue(rvo);

                DatabaseReference report_receiver = mDatabase.child("report_receiver");

                MultiAutoCompleteTextView receiver_input = findViewById(R.id.rpt_create_receiver);
                String[] rcv_names = receiver_input.getText().toString().split(",");

                for(String rcv_name : rcv_names) {
                    if(rcv_name.trim().length() == 0) continue;
                    ReportReceiverVO rcvo = new ReportReceiverVO();
                    for(UserVO user : mUserList) {
                        if(user.getDisplayName().equals(rcv_name.trim())) {
                            rcvo.setReceiver_id(user.getUid());
                            rcvo.setReport_id(newkey);
                            report_receiver.push().setValue(rcvo);
                            break;
                        }
                    }
                }


                ReportCreateActivity.this.finish();
            }
        });
    }



}
