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

import static mil.army.milirang.schedule.ValuesFromFirebase.*;

public class ScheduleRecyclerViewAdapter extends RecyclerView.Adapter<ScheduleRecyclerViewAdapter.ViewHolder> {

    private Activity activity;
    private List<String> mdays ;// 내 계정에 할당된 당직날짜들
    String removeday;

    public ScheduleRecyclerViewAdapter(ScheduleScrollingActivity activity, List<String> mdays) {
        this.activity = activity;
        this.mdays = (ArrayList<String>) workdays.get(f_user.getUid());//mdays;
    }

    @NonNull
    @Override
    public ScheduleRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.schedule_item, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ScheduleRecyclerViewAdapter.ViewHolder viewHolder, int i) {
        String data = schedules.get(i);
        viewHolder.title.setText(data);
        ArrayList<String> tmp_mday = (ArrayList<String>) workdays.get(f_user.getUid());

        if(tmp_mday != null && tmp_mday.contains(schedules.get(i))) {
            viewHolder.name.setText("내 당직");
        }
        else {
            for(String tmp : workdays.keySet())
            {
                ArrayList<String> array_here = (ArrayList<String>) workdays.get(tmp);
                if(array_here.contains(data))
                {
                    String name_temp = ScheduleActivity.findNameInList(tmp, refuids);
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
        return schedules.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView title;
        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.schedule_title);
            name = (TextView) itemView.findViewById(R.id.schedule_person);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(activity, "click "+schedules.get(getAdapterPosition()), Toast.LENGTH_SHORT).show();
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    removeday = title.getText().toString();
                    //HashMap<String, Object> removemap = (HashMap<String, Object>) workdays;

                    if (mdays.indexOf(removeday) != -1) {
                        List<String> removearray = (ArrayList<String>) workdays.get(f_user.getUid());
                        if (removearray != null && removearray.contains(removeday)) {
                            removearray.remove(removeday);
                            FirebaseDatabase.getInstance().getReference().child("workdays").child(f_user.getUid()).getRef().setValue(removearray);
                            Toast.makeText(activity, "remove " + removeday + " from your workdays", Toast.LENGTH_SHORT).show();
                        }
                    }
                    ScheduleScrollingActivity.adapter.notifyDataSetChanged();
                    return false;
                }
            });
        }
    }

    public ArrayList<String> AlwaysNotNullArrayListFromWorkdays()
    {
        if(workdays.get(f_user.getUid()) != null)
            return (ArrayList<String>) workdays.get(f_user.getUid());
        else
            return new ArrayList<String>();

    }


    private void removeItemView(int position) {
        mdays.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mdays.size()); // 지워진 만큼 다시 채워넣기.
    }
}
