package is.hi.HBV601G.mundusandroid;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

import is.hi.HBV601G.mundusandroid.Entities.Child;
import is.hi.HBV601G.mundusandroid.Entities.ChildRewardPair;
import is.hi.HBV601G.mundusandroid.Entities.Reward;

/**
 * An adapter for the rewards in a recyclerview
 */
public class RewardPurchasedRecyclerViewAdapter extends RecyclerView.Adapter<RewardPurchasedRecyclerViewAdapter.MyRewardPurchasedViewHolder> {

    Context mContext;
    List<ChildRewardPair> mData;

    public RewardPurchasedRecyclerViewAdapter(Context mContext, List<ChildRewardPair> mData) {
        this.mContext = mContext;
        this.mData = mData;

    }

    @NonNull // Non null er hvergi í myndbandinu
    @Override
    public MyRewardPurchasedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.item_reward_purchased, parent, false); // Parent í þessari línu tengist okkar parent ekki neitt
        MyRewardPurchasedViewHolder vHolder = new MyRewardPurchasedViewHolder(v);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyRewardPurchasedViewHolder holder, int position) {
        //Reward reward = mData.get(position).second;
        //Child child = mData.get(position).first;
        holder.tv_name.setText(mData.get(position).getReward().getName());
        holder.tv_description.setText(mData.get(position).getReward().getDescription()+"");
        holder.tv_lvlreq.setText("Level: " + mData.get(position).getReward().getLevelRequired()+"");
        holder.tv_price.setText("Price: " + mData.get(position).getReward().getPrice());

        holder.tv_buyer.setText("Buyer: " + mData.get(position).getChild().getName());
        //TODO: Setja binder á takkana


        // TODO laga svo hægt sé að endurnýta þetta fyrir purchased rewards
    }

    @Override
    public int getItemCount() {
        //return 0;
        return mData.size();
    }

    public static class MyRewardPurchasedViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_name;
        private TextView tv_description;
        private TextView tv_lvlreq;
        private TextView tv_price;
        private TextView tv_buyer;
        private Button bt_grant;

        public MyRewardPurchasedViewHolder(@NonNull View itemView) { // NonNull var ekki í myndbaninu en kemur samt hérna
            super(itemView);

            tv_name = (TextView) itemView.findViewById(R.id.item_reward_purchased_name);
            tv_description = (TextView) itemView.findViewById(R.id.item_reward_purchased_description);
            tv_lvlreq = (TextView) itemView.findViewById(R.id.item_reward_purchased_lvlreq);
            tv_price = (TextView) itemView.findViewById(R.id.item_reward_purchased_price);
            tv_buyer = (TextView) itemView.findViewById(R.id.item_reward_purchased_buyer);
            bt_grant = (Button) itemView.findViewById(R.id.item_reward_purchased_grantButton);
        }
    }

}
