package is.hi.HBV601G.mundusandroid;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import is.hi.HBV601G.mundusandroid.Activities.ParentActivities.FragmentAvailableRewardsParent;
import is.hi.HBV601G.mundusandroid.Entities.ChildRewardPair;
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
public class RewardRecyclerViewAdapter extends RecyclerView.Adapter<RewardRecyclerViewAdapter.MyRewardViewHolder> {

    Context mContext;
    List<Reward> mData;
    private List<ChildRewardPair> pairRewards;
    int mType;
    private Button deleteButton;
    private Button buyButton;
    private Button grantButton;

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
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull MyRewardViewHolder holder, int position) {

        holder.tv_name.setText(mData.get(position).getName());
        holder.tv_description.setText(mData.get(position).getDescription()+"");
        holder.tv_lvlreq.setText("Level: " + mData.get(position).getLevelRequired()+"");
        holder.tv_price.setText("Price: " + mData.get(position).getPrice());

        holder.tv_rewardId.setText(""+mData.get(position).getId());
        holder.tv_rewardId.setVisibility(View.GONE); // Hide the ID
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

        deleteButton = holder.itemView.findViewById(R.id.item_reward_deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long rewardId = Long.parseLong(holder.tv_rewardId.getText().toString());
                System.out.println("Delete reward with Id: " + rewardId);

                Retrofit retrofit = RetrofitSingleton.getInstance().getRetrofit();
                MundusAPI mundusAPI = retrofit.create(MundusAPI.class);
                Call<ResponseBody> call = mundusAPI.deleteReward(rewardId);

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(!response.isSuccessful()){
                            //TODO Her tharf ad gera stoff
                            System.out.println("Her1");
                            return;
                        }
                        int position = holder.getAdapterPosition();

                        mData.remove(position);
                        RewardRecyclerViewAdapter.this.notifyItemRemoved(position);

                        /*int count = RewardRecyclerViewAdapter.this.getItemCount();
                        RewardRecyclerViewAdapter.this.notifyItemRangeRemoved(0, count-1);

                         */
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

        buyButton = holder.itemView.findViewById(R.id.item_reward_buyButton);

        buyButton.setOnClickListener(new View.OnClickListener() {
             @Override
            public void onClick(View v) {
                 long rewardId = Long.parseLong(holder.tv_rewardId.getText().toString());
                 System.out.println("Buy reward with Id: " + rewardId);

                 Retrofit retrofit = RetrofitSingleton.getInstance().getRetrofit();
                 MundusAPI mundusAPI = retrofit.create(MundusAPI.class);
                 Call<Boolean> call = mundusAPI.purchaseReward(rewardId);

                 call.enqueue(new Callback<Boolean>() {
                     @Override
                     public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                         if(!response.isSuccessful()){
                             //TODO Her tharf ad gera stoff
                             System.out.println("Buy resposne not successful");
                             return;
                         }
                            // Setja dialog eða toast sem segir you broke bitch! eða e-h þannig
                         boolean status = response.body();
                         System.out.println(status);

                         if (status == true) {
                             Toast.makeText(mContext,
                                     "Reward purchases", Toast.LENGTH_SHORT).show();
                         }
                         else {
                             Toast.makeText(mContext,
                                     "You broke bitch!", Toast.LENGTH_SHORT).show();
                         }
                            // TODO uppfæra listann í My rewards jafnóðum
                     }
                     @Override
                     public void onFailure(Call<Boolean> call, Throwable t) {
                         //TODO Her tharf ad gera stoff
                         System.out.println("Buy on failure");
                     }
                 });
                 // TODO. Klara þetta
             }
        });

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
        private Button bt_grant;
        private TextView tv_rewardId;
        private TextView tv_childId;
        private TextView tv_buyer;
        public MyRewardViewHolder(@NonNull View itemView) { // NonNull var ekki í myndbaninu en kemur samt hérna
            super(itemView);

            tv_name = (TextView) itemView.findViewById(R.id.item_reward_name);
            tv_description = (TextView) itemView.findViewById(R.id.item_reward_description);
            tv_lvlreq = (TextView) itemView.findViewById(R.id.item_reward_lvlreq);
            tv_price = (TextView) itemView.findViewById(R.id.item_reward_price);

            bt_buy = (Button) itemView.findViewById(R.id.item_reward_buyButton);
            bt_delete = (Button) itemView.findViewById(R.id.item_reward_deleteButton);
            bt_grant = (Button) itemView.findViewById(R.id.item_reward_grantButton);
            tv_rewardId = (TextView) itemView.findViewById(R.id.item_reward_rewardId);
        }
    }

}
