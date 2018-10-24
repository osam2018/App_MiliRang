package mil.army.milirang.report;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import mil.army.milirang.MainActivity;
import mil.army.milirang.R;
import mil.army.milirang.report.vo.ReportVO;

class ReceiverRecyclerViewAdapter extends RecyclerView.Adapter<ReceiverRecyclerViewAdapter.ViewHolder> {


    private Activity activity;
    private List<String> receiver;
    private MainActivity ac;

    public ReceiverRecyclerViewAdapter(ReportDetailActivity activity, List<String> receiver) {
        this.activity = activity;
        this.receiver = receiver;
    }

    @NonNull
    @Override
    public ReceiverRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.report_detail_receiver, viewGroup, false);

        ReceiverRecyclerViewAdapter.ViewHolder viewHolder = new ReceiverRecyclerViewAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReceiverRecyclerViewAdapter.ViewHolder viewHolder, int i) {
        String data = receiver.get(i);

        // 데이터 결합
        viewHolder.title.setText(data);
    }

    @Override
    public int getItemCount() {
        return receiver.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView timestamp;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.report_detail_receiver);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });
        }
    }


}
