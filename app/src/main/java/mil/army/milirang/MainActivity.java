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
import com.firebase.ui.auth.data.model.User;
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
import mil.army.milirang.report.vo.ReportReceiverVO;
import mil.army.milirang.schedule.ScheduleActivity;
import mil.army.milirang.report.ReportRecyclerViewAdapter;
import mil.army.milirang.report.vo.ReportVO;
import mil.army.milirang.user.ContactRecyclerViewAdapter;
import mil.army.milirang.user.vo.UserVO;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int RC_SIGN_IN = 186;

    private DatabaseReference mDatabase;
    RecyclerView mReportRecyclerView;
    ReportRecyclerViewAdapter mReportRecyclerViewAdapter;
    List<ReportVO> mReportList;

    RecyclerView mContactRecyclerView;
    ContactRecyclerViewAdapter mContactRecyclerViewAdapter;
    List<UserVO> mContactList;

    boolean reportViewPrepared = false;
    boolean scheduleViewPrepared = false;
    boolean contactViewPrepared = false;


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

        prepareReportView();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);




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

            ((TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_user_name)).setText(user.getDisplayName());
            loadReportList();
        }

    }

    private void prepareReportView() {

        Toolbar report_toolbar = (Toolbar) findViewById(R.id.report_toolbar);
        setSupportActionBar(report_toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle report_toggle = new ActionBarDrawerToggle(
                this, drawer, report_toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(report_toggle);
        report_toggle.syncState();


        mReportRecyclerView = findViewById(R.id.report_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mReportRecyclerView.setLayoutManager(linearLayoutManager);

        mReportList = new ArrayList<>();

        mReportRecyclerViewAdapter = new ReportRecyclerViewAdapter(this, mReportList);
        mReportRecyclerView.setAdapter(mReportRecyclerViewAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mReportRecyclerView.getContext(), linearLayoutManager.getOrientation());
        mReportRecyclerView.addItemDecoration(dividerItemDecoration);

        reportViewPrepared = true;
    }

    private void prepareScheduleView() {
        Toolbar schedule_toolbar = (Toolbar) findViewById(R.id.schedule_toolbar);
        setSupportActionBar(schedule_toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle schedule_toggle = new ActionBarDrawerToggle(
                this, drawer, schedule_toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(schedule_toggle);
        schedule_toggle.syncState();
        scheduleViewPrepared = true;
    }
    private void prepareContactView() {

        Toolbar contact_toolbar = (Toolbar) findViewById(R.id.contact_toolbar);
        setSupportActionBar(contact_toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle contact_toggle = new ActionBarDrawerToggle(
                this, drawer, contact_toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(contact_toggle);
        contact_toggle.syncState();


        mContactRecyclerView = findViewById(R.id.contact_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mContactRecyclerView.setLayoutManager(linearLayoutManager);

        mContactList = new ArrayList<>();

        mContactRecyclerViewAdapter = new ContactRecyclerViewAdapter(this, mContactList);
        mContactRecyclerView.setAdapter(mContactRecyclerViewAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContactRecyclerView.getContext(), linearLayoutManager.getOrientation());
        mContactRecyclerView.addItemDecoration(dividerItemDecoration);

        contactViewPrepared = true;
    }
    /**
     * Loads Reports from "report" table from firebase database.
     */
    private void loadReportList() {
        Query reportRef = mDatabase.child("report_receiver")
                .orderByChild("receiver_id")
                .equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());

        mReportList.clear();
        mReportRecyclerViewAdapter.notifyDataSetChanged();

        reportRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mReportList.clear();
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    ReportReceiverVO rpt = singleSnapshot.getValue(ReportReceiverVO.class);
                    mDatabase.child("report")
                            .child(rpt.getReport_id())
                            .addValueEventListener(new ValueEventListener() {

                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    ReportVO r = dataSnapshot.getValue(ReportVO.class);
                                    mReportList.add(r);
                                    mReportRecyclerViewAdapter.notifyDataSetChanged();
                                }
                                @Override  public void onCancelled(@NonNull DatabaseError databaseError) {}
                            });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("TAG", "ERROR!");
            }
        });
    }

    private void loadSentReportList() {
        mReportList.clear();
        mReportRecyclerViewAdapter.notifyDataSetChanged();

        mDatabase.child("report")
                .orderByChild("rpt_sender")
                .equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        mReportList.clear();
                        for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                            ReportVO r = singleSnapshot.getValue(ReportVO.class);
                            mReportList.add(r);
                        }
                        mReportRecyclerViewAdapter.notifyDataSetChanged();
                    }
                    @Override  public void onCancelled(@NonNull DatabaseError databaseError) {}
                });

    }

    /**
     * Loads Reports from "contact" table from firebase database.
     */
    private void loadContactList() {
        Query contactRef = mDatabase.child("users");

        mContactList.clear();
        mContactRecyclerViewAdapter.notifyDataSetChanged();

        contactRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mContactList.clear();
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    UserVO user = singleSnapshot.getValue(UserVO.class);
                    mContactList.add(user);
                }
                mContactRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("TAG", "ERROR!");
            }
        });
    }

    private void openReportView() {
        findViewById(R.id.report_view).setVisibility(View.VISIBLE);
        findViewById(R.id.schedule_view).setVisibility(View.INVISIBLE);
        findViewById(R.id.contact_view).setVisibility(View.INVISIBLE);
    }

    private void openScheduleView() {
        findViewById(R.id.report_view).setVisibility(View.INVISIBLE);
        findViewById(R.id.schedule_view).setVisibility(View.VISIBLE);
        findViewById(R.id.contact_view).setVisibility(View.INVISIBLE);
    }

    private void openContactView() {
        findViewById(R.id.report_view).setVisibility(View.INVISIBLE);
        findViewById(R.id.schedule_view).setVisibility(View.INVISIBLE);
        findViewById(R.id.contact_view).setVisibility(View.VISIBLE);
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
            this.setTitle("보고서 수신함");
            if(!reportViewPrepared) prepareReportView();
            openReportView();
            loadReportList();
        } else if (id == R.id.nav_report_sent) {
            this.setTitle("보고서 송신함");
            if(!reportViewPrepared) prepareReportView();
            openReportView();
            loadSentReportList();
        } else if (id == R.id.nav_schedule_event) {
            this.setTitle("이벤트");
            if(!scheduleViewPrepared) prepareScheduleView();
            openScheduleView();
        } else if (id == R.id.nav_schedule_work) {
            Intent intent = new Intent(MainActivity.this, ScheduleActivity.class);
            MainActivity.this.startActivity(intent);
        } else if (id == R.id.nav_contact_list) {
            this.setTitle("부대원 연락처");
            if(!contactViewPrepared) prepareContactView();
            openContactView();
            loadContactList();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
