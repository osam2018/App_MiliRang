package mil.army.milirang.schedule;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import mil.army.milirang.MainActivity;
import mil.army.milirang.R;
import mil.army.milirang.report.vo.ReportVO;

public class ScheduleRecyclerViewAdapter extends RecyclerView.Adapter<ScheduleRecyclerViewAdapter.ViewHolder> {

    private Activity activity;
    private List<String> mdays;
    private List<String> names;

    public ScheduleRecyclerViewAdapter(ScheduleScrollingActivity activity, List<String> mdays, List<String> names) {
        this.activity = activity;
        this.mdays = mdays;
        this.names = names;
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
    public void onBindViewHolder(@NonNull ScheduleRecyclerViewAdapter.ViewHolder viewHolder, int i) {
        String data = mdays.get(i);
        viewHolder.title.setText(data);

        if(i < names.size() && mdays.indexOf(names.get(i)) != -1) {
            String name = names.get(i);
            viewHolder.name.setText("내 당직");
        }
        else
        {
            viewHolder.name.setText("Not Mine!");
        }
        // 데이터 결합
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
}
