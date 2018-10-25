package mil.army.milirang.schedule;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mil.army.milirang.user.vo.UserVO;

public class ValuesFromFirebase {
    public static HashMap<String, Object> workdays;//workdays에 들어있는 당직 정보를 {ID : 날짜들} 방식으로 저장한다. //즉 ID로 무슨 날짜에 자신이 당직으로 되어있는지 알 수 있다.
    public static List<UserVO> refuids; //users에 있는 정보를 가져옴
    public static FirebaseUser f_user; //현재사용자
    public static List<String> schedules;


    public ValuesFromFirebase() {

        workdays = new HashMap<String, Object>();
        refuids = new ArrayList<UserVO>();
        f_user = FirebaseAuth.getInstance().getCurrentUser();
        schedules = new ArrayList<String>();

        FirebaseDatabase.getInstance().getReference().child("workdays").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                workdays.putAll((HashMap<String, Object>) dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot single : dataSnapshot.getChildren()) {
                    UserVO tmp = single.getValue(UserVO.class);
                    refuids.add(tmp);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseDatabase.getInstance().getReference().child("schedules").orderByKey().equalTo(f_user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                schedules.clear();
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    List<String> temp = (List<String>) singleSnapshot.getValue();
                    for (int i = 0; i < temp.size(); i++) {
                        String day = (String) temp.get(i);
                        schedules.add(day);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("TAG", "ERROR!");
            }
        });

    }
}
