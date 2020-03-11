package is.hi.HBV601G.mundusandroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import is.hi.HBV601G.mundusandroid.Entities.Quest;

public class QuestRecyclerViewAdapter extends RecyclerView.Adapter<QuestRecyclerViewAdapter.MyViewHolder> {

    Context mContext;
    List<Quest> mData;

    public QuestRecyclerViewAdapter(Context mContext, List<Quest> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull // Non null er hvegi í myndbandinu
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.item_quest, parent, false); // Parent í þessari línu tengist okkar parent ekki neitt
        MyViewHolder vHolder = new MyViewHolder(v);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tv_name.setText(mData.get(position).getName());
        holder.tv_xp.setText("XP: "+ mData.get(position).getXp()+"");
        holder.tv_coins.setText("Coins: " + mData.get(position).getCoins()+"");
        /*String assigne = mData.get(position).getAssignee().getName();
        if (assigne == null) {
            holder.tv_assignee.setText("N/A");
        }
        else {
            holder.tv_assignee.setText(assigne);
        }*/
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_name;
        private TextView tv_xp;
        private TextView tv_coins;
        private TextView tv_assignee;

        public MyViewHolder(@NonNull View itemView) { // NonNull var ekki í myndbaninu en kemur samt hérna
            super(itemView);

            tv_name = (TextView) itemView.findViewById(R.id.item_quest_name);
            tv_xp = (TextView) itemView.findViewById(R.id.item_quest_xp);
            tv_coins = (TextView) itemView.findViewById(R.id.item_quest_coins);
            tv_assignee = (TextView) itemView.findViewById(R.id.item_quest_assignee);
        }
    }

}
