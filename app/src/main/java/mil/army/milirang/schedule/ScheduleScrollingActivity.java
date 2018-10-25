package mil.army.milirang.schedule;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mil.army.milirang.R;
import mil.army.milirang.report.ReportRecyclerViewAdapter;
import mil.army.milirang.report.vo.ReportVO;

import static mil.army.milirang.schedule.ValuesFromFirebase.*;

public class ScheduleScrollingActivity extends AppCompatActivity {

    RecyclerView recyclerview;
    RecyclerView.LayoutManager layoutmanager;
    List<String> arr;

    public static ScheduleRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_scrolling);

        recyclerview = (RecyclerView) findViewById(R.id.schedule_list);
        arr = new ArrayList<String>();
        adapter = new ScheduleRecyclerViewAdapter(this, arr);
        recyclerview.setHasFixedSize(true);

        layoutmanager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(layoutmanager);
        recyclerview.setAdapter(adapter);

        loadSchedulePersonList();
    }

    private void loadSchedulePersonList() {
        Query workdays = FirebaseDatabase.getInstance().getReference().child("workdays").orderByKey().equalTo(f_user.getUid());

        workdays.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arr.clear();
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    List<String> temp = (List<String>) singleSnapshot.getValue();
                    for (int i = 0; i < temp.size(); i++) {
                        String day = (String) temp.get(i);
                        arr.add(day);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("TAG", "ERROR!");
            }
        });
    }
}
