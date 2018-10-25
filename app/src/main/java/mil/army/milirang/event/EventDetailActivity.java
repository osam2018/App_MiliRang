package mil.army.milirang.event;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import mil.army.milirang.R;
import mil.army.milirang.event.vo.EventReceiverVO;
import mil.army.milirang.event.vo.EventVO;
import mil.army.milirang.report.ReceiverRecyclerViewAdapter;
import mil.army.milirang.report.vo.ReportReceiverVO;
import mil.army.milirang.report.vo.ReportVO;
import mil.army.milirang.user.vo.UserVO;

public class EventDetailActivity extends AppCompatActivity {

    DatabaseReference mDatabase;
    RecyclerView mReceiverRecyclerView;
    ReceiverRecyclerViewAdapter mReceiverRecyclerViewAdapter;
    List<String> mReceiverList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_report_detail);

        mDatabase = FirebaseDatabase.getInstance().getReference();


        EventVO event = (EventVO) this.getIntent().getExtras().get("event");

        TextView titleview = findViewById(R.id.report_detail_title);
        TextView bodyview = findViewById(R.id.report_detail_body);
        final TextView senderview = findViewById(R.id.report_detail_sender);
        TextView timestampviewview = findViewById(R.id.report_detail_timestamp);

        titleview.setText(event.getEvent_title());
        bodyview.setText(event.getEvent_detail());
        senderview.setText(event.getEvent_owner());

        Calendar from = Calendar.getInstance();
        Calendar to = Calendar.getInstance();
        from.setTimeInMillis(event.getEvent_from());
        to.setTimeInMillis(event.getEvent_to());

        String dateText = "";
        dateText += from.get(Calendar.YEAR) + "/";
        dateText += from.get(Calendar.MONTH) + "/";
        dateText += from.get(Calendar.DAY_OF_MONTH) + " - ";

        dateText += to.get(Calendar.YEAR) + "/";
        dateText += to.get(Calendar.MONTH) + "/";
        dateText += to.get(Calendar.DAY_OF_MONTH);

        timestampviewview.setText(dateText);

        if(event.getEvent_owner() != null) {
            mDatabase.child("users")
                    .child(event.getEvent_owner())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            UserVO user = dataSnapshot.getValue(UserVO.class);
                            senderview.setText(user.getDisplayName());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }

        mReceiverRecyclerView = findViewById(R.id.report_detail_receivers);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mReceiverRecyclerView.setLayoutManager(linearLayoutManager);

        mReceiverList = new ArrayList<>();

        mReceiverRecyclerViewAdapter = new ReceiverRecyclerViewAdapter(this, mReceiverList);
        mReceiverRecyclerView.setAdapter(mReceiverRecyclerViewAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mReceiverRecyclerView.getContext(), linearLayoutManager.getOrientation());
        mReceiverRecyclerView.addItemDecoration(dividerItemDecoration);


        mDatabase.child("event_receiver")
                .orderByChild("event_id")
                .equalTo(event.getEvent_id())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        mReceiverList.clear();
                        mReceiverRecyclerViewAdapter.notifyDataSetChanged();
                        for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                            String uid = singleSnapshot.getValue(EventReceiverVO.class).getReceiver_id();
                            mDatabase.child("users")
                                    .child(uid)
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            UserVO user = dataSnapshot.getValue(UserVO.class);
                                            mReceiverList.add(user.getDisplayName());
                                            mReceiverRecyclerViewAdapter.notifyDataSetChanged();
                                        }
                                        @Override public void onCancelled(@NonNull DatabaseError databaseError) {}
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



    }
}
