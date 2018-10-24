package mil.army.milirang;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mil.army.milirang.report.ReportCreateActivity;
import mil.army.milirang.schedule.ScheduleActivity;
import mil.army.milirang.report.ReportRecyclerViewAdapter;
import mil.army.milirang.report.vo.ReportVO;
import mil.army.milirang.user.vo.UserVO;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int RC_SIGN_IN = 186;

    private DatabaseReference mDatabase;
    RecyclerView mReportRecyclerView;
    ReportRecyclerViewAdapter mReportRecyclerViewAdapter;
    List<ReportVO> mReportList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        FloatingActionButton report_fab = (FloatingActionButton) findViewById(R.id.report_fab);
        report_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent = new Intent(MainActivity.this, ReportCreateActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

        FloatingActionButton schedule_fab = (FloatingActionButton) findViewById(R.id.schedule_fab);
        schedule_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent = new Intent(MainActivity.this, ScheduleActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

        openReportView();

        Toolbar report_toolbar = (Toolbar) findViewById(R.id.report_toolbar);
        Toolbar schedule_toolbar = (Toolbar) findViewById(R.id.schedule_toolbar);
        setSupportActionBar(report_toolbar);
        setSupportActionBar(schedule_toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle report_toggle = new ActionBarDrawerToggle(
                this, drawer, report_toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        ActionBarDrawerToggle schedule_toggle = new ActionBarDrawerToggle(
                this, drawer, schedule_toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(report_toggle);
        drawer.addDrawerListener(schedule_toggle);
        report_toggle.syncState();
        schedule_toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mReportRecyclerView = findViewById(R.id.report_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mReportRecyclerView.setLayoutManager(linearLayoutManager);

        mReportList = new ArrayList<>();

        mReportRecyclerViewAdapter = new ReportRecyclerViewAdapter(this, mReportList);
        mReportRecyclerView.setAdapter(mReportRecyclerViewAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mReportRecyclerView.getContext(), linearLayoutManager.getOrientation());
        mReportRecyclerView.addItemDecoration(dividerItemDecoration);


        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build()
        );

        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(),
                    RC_SIGN_IN
            );
        } else {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            Snackbar.make(findViewById(R.id.report_view), user.getDisplayName() + "님, 환영합니다!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            mDatabase.child("users").child(user.getUid()).setValue(user);
            loadReportList();
        }

    }

    /**
     * Loads Reports from "report" table from firebase database.
     */
    private void loadReportList() {
        Query reportRef = mDatabase.child("report")
                                .orderByChild("rpt_timestamp")
                                .equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid(), "rpt_receiver");


        reportRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mReportList.clear();
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    ReportVO rpt = singleSnapshot.getValue(ReportVO.class);
                    mReportList.add(rpt);
                }

                mReportRecyclerViewAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("TAG", "ERROR!");
            }
        });
    }

    private void openReportView() {
        findViewById(R.id.schedule_view).setVisibility(View.INVISIBLE);
        findViewById(R.id.report_view).setVisibility(View.VISIBLE);
    }

    private void openScheduleView() {
        findViewById(R.id.schedule_view).setVisibility(View.VISIBLE);
        findViewById(R.id.report_view).setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if(resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                mDatabase.child("users").child(user.getUid()).setValue(user);
                loadReportList();
            } else {

            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_report_inbox) {
            openReportView();
        } else if (id == R.id.nav_schedule_event) {
            openScheduleView();
        } else if (id == R.id.nav_schedule_work) {
            Intent intent = new Intent(MainActivity.this, ScheduleActivity.class);
            MainActivity.this.startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
