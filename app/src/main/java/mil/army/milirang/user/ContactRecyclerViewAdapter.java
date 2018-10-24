package mil.army.milirang.user;

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
import mil.army.milirang.user.vo.UserVO;

public class ContactRecyclerViewAdapter extends RecyclerView.Adapter<ContactRecyclerViewAdapter.ViewHolder> {

    private Activity activity;
    private List<UserVO> contact;
    private MainActivity ac;

    public ContactRecyclerViewAdapter(MainActivity activity, List<UserVO> contact) {
        this.activity = activity;
        this.contact = contact;
    }

    @NonNull
    @Override
    public ContactRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.contact_item, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactRecyclerViewAdapter.ViewHolder viewHolder, int i) {
        UserVO data = contact.get(i);

        // 데이터 결합
        viewHolder.title.setText(data.getDisplayName());
        viewHolder.phoneNum.setText(data.getPhoneNum());
    }

    @Override
    public int getItemCount() {
        return contact.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView phoneNum;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.cnt_title);
            phoneNum = (TextView) itemView.findViewById(R.id.cnt_phoneNum);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(activity, "click " +
                            contact.get(getAdapterPosition()).getDisplayName(), Toast.LENGTH_SHORT).show();
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Toast.makeText(activity, "remove " +
                            contact.get(getAdapterPosition()).getDisplayName(), Toast.LENGTH_SHORT).show();
                    removeItemView(getAdapterPosition());
                    return false;
                }
            });
        }
    }

    private void removeItemView(int position) {
        contact.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, contact.size()); // 지워진 만큼 다시 채워넣기.
    }
}
