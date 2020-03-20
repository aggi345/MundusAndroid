package is.hi.HBV601G.mundusandroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import is.hi.HBV601G.mundusandroid.Entities.Reward;

/**
 * An adapter for the rewards in a recyclerview
 */
public class RewardRecyclerViewAdapter extends RecyclerView.Adapter<RewardRecyclerViewAdapter.MyRewardViewHolder> {

    Context mContext;
    List<Reward> mData;
    int mType;

    public RewardRecyclerViewAdapter(Context mContext, List<Reward> mData, int mType) {
        this.mContext = mContext;
        this.mData = mData;
        this.mType = mType;
    }

    @NonNull // Non null er hvergi í myndbandinu
    @Override
    public MyRewardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.item_reward, parent, false); // Parent í þessari línu tengist okkar parent ekki neitt
        MyRewardViewHolder vHolder = new MyRewardViewHolder(v);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyRewardViewHolder holder, int position) {

        holder.tv_name.setText(mData.get(position).getName());
        holder.tv_description.setText(mData.get(position).getDescription()+"");
        holder.tv_lvlreq.setText("Level: " + mData.get(position).getLevelRequired()+"");
        holder.tv_price.setText("Price: " + mData.get(position).getPrice());


        switch (mType) {
            case 0:
                holder.bt_grant.setVisibility(View.GONE);
                holder.bt_delete.setVisibility(View.GONE);
                break;
            case 1:
                holder.bt_grant.setVisibility(View.GONE);
                holder.bt_delete.setVisibility(View.GONE);
                holder.bt_buy.setVisibility(View.GONE);
                break;
            case 2:
                holder.bt_buy.setVisibility(View.GONE);
                holder.bt_grant.setVisibility(View.GONE);
                break;
            case 3:
                holder.bt_buy.setVisibility(View.GONE);
                holder.bt_delete.setVisibility(View.GONE);
                break;
        }
        //TODO: Setja binder á takkana


        // TODO laga svo hægt sé að endurnýta þetta fyrir purchased rewards
    }

    @Override
    public int getItemCount() {
        //return 0;
        return mData.size();
    }

    public static class MyRewardViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_name;
        private TextView tv_description;
        private TextView tv_lvlreq;
        private TextView tv_price;
        private Button bt_buy;
        private Button bt_delete;
        Button bt_grant;
        public MyRewardViewHolder(@NonNull View itemView) { // NonNull var ekki í myndbaninu en kemur samt hérna
            super(itemView);

            tv_name = (TextView) itemView.findViewById(R.id.item_reward_name);
            tv_description = (TextView) itemView.findViewById(R.id.item_reward_description);
            tv_lvlreq = (TextView) itemView.findViewById(R.id.item_reward_lvlreq);
            tv_price = (TextView) itemView.findViewById(R.id.item_reward_price);

            bt_buy = (Button) itemView.findViewById(R.id.item_reward_buyButton);
            bt_delete = (Button) itemView.findViewById(R.id.item_reward_deleteButton);
            bt_grant = (Button) itemView.findViewById(R.id.item_reward_grantButton);
        }
    }

}