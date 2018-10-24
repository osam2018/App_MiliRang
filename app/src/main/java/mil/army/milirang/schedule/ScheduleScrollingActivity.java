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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mil.army.milirang.R;
import mil.army.milirang.report.ReportRecyclerViewAdapter;
import mil.army.milirang.report.vo.ReportVO;

public class ScheduleScrollingActivity extends AppCompatActivity {

    RecyclerView recyclerview;
    RecyclerView.LayoutManager layoutmanager;
    ScheduleRecyclerViewAdapter adapter;
    List<String> list;
    DatabaseReference ref;
    FirebaseUser f_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_scrolling);

        ref = FirebaseDatabase.getInstance().getReference().child("schedules");
        recyclerview = (RecyclerView) findViewById(R.id.schedule_list);
        f_user = FirebaseAuth.getInstance().getCurrentUser();
        list = new ArrayList<String>();
        loadScheduleList();
        Toast.makeText(ScheduleScrollingActivity.this, list.get(0), Toast.LENGTH_SHORT);

        recyclerview.setHasFixedSize(true);

        layoutmanager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(layoutmanager);

        adapter = new ScheduleRecyclerViewAdapter(this, list);
        recyclerview.setAdapter(adapter);
    }


    /**
     * Loads Reports from "schedule" table from firebase database.
     */
    private void loadScheduleList() {
        DatabaseReference schedules = ref.child(f_user.getUid());

        schedules.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                int i = 0;
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    HashMap<String, String> temp = (HashMap<String, String>) singleSnapshot.getValue();
                    String day = temp.get(String.valueOf(i));
                    list.add(day);
                    i++;
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("TAG", "ERROR!");
            }
        });
    }
}
