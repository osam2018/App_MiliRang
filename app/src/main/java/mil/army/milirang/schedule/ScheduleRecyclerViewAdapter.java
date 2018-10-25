package mil.army.milirang.schedule;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.data.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mil.army.milirang.MainActivity;
import mil.army.milirang.R;
import mil.army.milirang.report.vo.ReportVO;
import mil.army.milirang.user.vo.UserVO;

public class ScheduleRecyclerViewAdapter extends RecyclerView.Adapter<ScheduleRecyclerViewAdapter.ViewHolder> {

    private Activity activity;
    private List<String> mdays;
    private List<String> selected;
    private HashMap<String, Object> uids;
    private int num;
    private List<UserVO> refuids;

    public ScheduleRecyclerViewAdapter(ScheduleScrollingActivity activity, List<String> mdays, List<String> selected, HashMap<String, Object> uids) {
        this.activity = activity;
        this.mdays = mdays;
        this.selected = selected;
        this.uids = uids;
        this.refuids = new ArrayList<UserVO>();

        FirebaseDatabase.getInstance().getReference().child("users")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot single : dataSnapshot.getChildren()) {
                            UserVO tmp = single.getValue(UserVO.class);
                            refuids.add(tmp);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    @NonNull
    @Override
    public ScheduleRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.schedule_item, viewGroup, false); // report_item => act...sche...

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ScheduleRecyclerViewAdapter.ViewHolder viewHolder, int i) {
        String data = mdays.get(i);
        viewHolder.title.setText(data);

        if(selected.contains(mdays.get(i))) {
            viewHolder.name.setText("내 당직");
        }
        else {
            for(String tmp : uids.keySet())
            {
                ArrayList<String> array_here = (ArrayList<String>) uids.get(tmp);
                if(array_here.contains(data))
                {
                    String name_temp = findNameInList(tmp, refuids);
                    if(name_temp == null)
                    {
                        name_temp = "No name in user DB";
                    }
                    viewHolder.name.setText(name_temp);
                }
                else
                    viewHolder.name.setText("Not Reserved!");
            }
        }
    }

    @Override
    public int getItemCount() {
        return mdays.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.schedule_title);
            name = (TextView) itemView.findViewById(R.id.schedule_person);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(activity, "click " +
                            mdays.get(getAdapterPosition()), Toast.LENGTH_SHORT).show();
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Toast.makeText(activity, "remove " +
                            mdays.get(getAdapterPosition()), Toast.LENGTH_SHORT).show();
                    removeItemView(getAdapterPosition());
                    return false;
                }
            });
        }
    }

    private void removeItemView(int position) {
        mdays.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mdays.size()); // 지워진 만큼 다시 채워넣기.
    }

    private String findNameInList(String uid, List<UserVO> arr){
        for(UserVO a : arr)
        {
            if(a.getUid().equals(uid))
            {
                return a.getDisplayName();
            }
        }

        return null;
    }
}
