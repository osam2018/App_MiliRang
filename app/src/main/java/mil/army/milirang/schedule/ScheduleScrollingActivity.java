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

    public static ScheduleRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_scrolling);

        recyclerview = (RecyclerView) findViewById(R.id.schedule_list);
        adapter = new ScheduleRecyclerViewAdapter(this);
        layoutmanager = new LinearLayoutManager(this);

        recyclerview.setLayoutManager(layoutmanager);
        recyclerview.setAdapter(adapter);
        recyclerview.setHasFixedSize(true);
    }
}
