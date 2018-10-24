package mil.army.milirang.report;

import android.app.Activity;
import android.content.Intent;
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

public class ReportRecyclerViewAdapter extends RecyclerView.Adapter<ReportRecyclerViewAdapter.ViewHolder> {

    private Activity activity;
    private List<ReportVO> report;
    private MainActivity ac;

    public ReportRecyclerViewAdapter(MainActivity activity, List<ReportVO> report) {
        this.activity = activity;
        this.report = report;
    }

    @NonNull
    @Override
    public ReportRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.report_item, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReportRecyclerViewAdapter.ViewHolder viewHolder, int i) {
        ReportVO data = report.get(i);

        // 데이터 결합
        viewHolder.title.setText(data.getRpt_title());
        viewHolder.timestamp.setText(data.getRpt_timestamp());
    }

    @Override
    public int getItemCount() {
        return report.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView timestamp;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.rpt_title);
            timestamp = (TextView) itemView.findViewById(R.id.rpt_timetsamp);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), ReportDetailActivity.class);
                    int i = 0;
                    intent.putExtra("report", report.get(getAdapterPosition()));
                    view.getContext().startActivity(intent);
                }
            });
        }
    }

    private void removeItemView(int position) {
        report.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, report.size()); // 지워진 만큼 다시 채워넣기.
    }
}
