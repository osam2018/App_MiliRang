package mil.army.milirang.report;

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
import java.util.List;

import mil.army.milirang.R;
import mil.army.milirang.report.vo.ReportReceiverVO;
import mil.army.milirang.report.vo.ReportVO;
import mil.army.milirang.user.vo.UserVO;

public class ReportDetailActivity extends AppCompatActivity {

    DatabaseReference mDatabase;
    RecyclerView mReceiverRecyclerView;
    ReceiverRecyclerViewAdapter mReceiverRecyclerViewAdapter;
    List<String> mReceiverList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_report_detail);

        mDatabase = FirebaseDatabase.getInstance().getReference();


        ReportVO report = (ReportVO) this.getIntent().getExtras().get("report");

        TextView titleview = findViewById(R.id.report_detail_title);
        TextView bodyview = findViewById(R.id.report_detail_body);
        final TextView senderview = findViewById(R.id.report_detail_sender);
        TextView timestampviewview = findViewById(R.id.report_detail_timestamp);

        titleview.setText(report.getRpt_title());
        bodyview.setText(report.getRpt_body());
        senderview.setText(report.getRpt_sender());
        timestampviewview.setText(report.getRpt_timestamp());

        mDatabase.child("report_receiver")
                .orderByChild("report_id")
                .equalTo(report.getRpt_id())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                            ReportReceiverVO rvo = singleSnapshot.getValue(ReportReceiverVO.class);
                            if(rvo.getReceiver_id().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                String key = singleSnapshot.getKey();
                                mDatabase.child("report_receiver").child(key).child("received").setValue(true);
                                break;
                            }
                        }
                    }
                    @Override public void onCancelled(@NonNull DatabaseError databaseError) {}
                });

        mDatabase.child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
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

        mReceiverRecyclerView = findViewById(R.id.report_detail_receivers);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mReceiverRecyclerView.setLayoutManager(linearLayoutManager);

        mReceiverList = new ArrayList<>();

        mReceiverRecyclerViewAdapter = new ReceiverRecyclerViewAdapter(this, mReceiverList);
        mReceiverRecyclerView.setAdapter(mReceiverRecyclerViewAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mReceiverRecyclerView.getContext(), linearLayoutManager.getOrientation());
        mReceiverRecyclerView.addItemDecoration(dividerItemDecoration);


        mDatabase.child("report_receiver")
                .orderByChild("report_id")
                .equalTo(report.getRpt_id())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        mReceiverList.clear();
                        mReceiverRecyclerViewAdapter.notifyDataSetChanged();
                        for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                            String uid = singleSnapshot.getValue(ReportReceiverVO.class).getReceiver_id();
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
