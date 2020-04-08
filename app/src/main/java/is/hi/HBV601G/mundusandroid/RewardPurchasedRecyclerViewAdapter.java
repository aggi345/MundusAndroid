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
import is.hi.HBV601G.mundusandroid.Entities.Quest;
import is.hi.HBV601G.mundusandroid.Entities.Reward;
import is.hi.HBV601G.mundusandroid.Network.MundusAPI;
import is.hi.HBV601G.mundusandroid.Network.RetrofitSingleton;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * An adapter for the rewards in a recyclerview
 */
public class RewardPurchasedRecyclerViewAdapter extends RecyclerView.Adapter<RewardPurchasedRecyclerViewAdapter.MyRewardPurchasedViewHolder> {

    Context mContext;
    List<ChildRewardPair> mData;

    private Button grantButton;

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

        holder.tv_rewardId.setText(""+mData.get(position).getReward().getId());
        holder.tv_rewardId.setVisibility(View.GONE); // Hide the ID
        holder.tv_childId.setText(""+mData.get(position).getChild().getId());
        holder.tv_childId.setVisibility(View.GONE); // Hide the ID
        //TODO: Setja binder á takkana

        grantButton = holder.itemView.findViewById(R.id.item_reward_purchased_grantButton);

        grantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long rewardId = Long.parseLong(holder.tv_rewardId.getText().toString());
                long childId = Long.parseLong(holder.tv_childId.getText().toString());
                System.out.println("Grant reward with Id: " + rewardId + " to child with id: " + childId);

                Retrofit retrofit = RetrofitSingleton.getInstance(mContext).getRetrofit();
                MundusAPI mundusAPI = retrofit.create(MundusAPI.class);
                Call<ResponseBody> call = mundusAPI.grantReward(rewardId, childId);

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(!response.isSuccessful()){
                            //TODO Her tharf ad gera stoff
                            System.out.println("Grant Reward failed");
                            return;
                        }
                        // TODO kannski þarf að gera eitthvað hérna

                        int position = holder.getAdapterPosition();

                        mData.remove(position);

                        RewardPurchasedRecyclerViewAdapter.this.notifyItemRemoved(position);
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        //TODO Her tharf ad gera stoff
                        System.out.println("Her2");
                    }
                });
                // TODO. Klara þetta
            }
        });

        // TODO laga svo hægt sé að endurnýta þetta fyrir purchased rewards
    }

    @Override
    public int getItemCount() {
        //return 0;
        return mData.size();
    }
    public void addItem(ChildRewardPair pair) {
        mData.add(pair);
        this.notifyDataSetChanged();
    }

    public static class MyRewardPurchasedViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_name;
        private TextView tv_description;
        private TextView tv_lvlreq;
        private TextView tv_price;
        private TextView tv_buyer;
        private Button bt_grant;
        private TextView tv_rewardId;
        private TextView tv_childId;

        public MyRewardPurchasedViewHolder(@NonNull View itemView) { // NonNull var ekki í myndbaninu en kemur samt hérna
            super(itemView);

            tv_name = (TextView) itemView.findViewById(R.id.item_reward_purchased_name);
            tv_description = (TextView) itemView.findViewById(R.id.item_reward_purchased_description);
            tv_lvlreq = (TextView) itemView.findViewById(R.id.item_reward_purchased_lvlreq);
            tv_price = (TextView) itemView.findViewById(R.id.item_reward_purchased_price);
            tv_buyer = (TextView) itemView.findViewById(R.id.item_reward_purchased_buyer);
            bt_grant = (Button) itemView.findViewById(R.id.item_reward_purchased_grantButton);

            tv_rewardId = (TextView) itemView.findViewById(R.id.item_reward_purchased_rewardId);
            tv_childId = (TextView) itemView.findViewById(R.id.item_reward_purchased_childId);
        }
    }

}
