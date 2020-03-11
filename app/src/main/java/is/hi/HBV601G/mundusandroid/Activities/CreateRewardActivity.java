package is.hi.HBV601G.mundusandroid.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import is.hi.HBV601G.mundusandroid.Entities.Reward;
import is.hi.HBV601G.mundusandroid.Network.MundusAPI;
import is.hi.HBV601G.mundusandroid.Network.RetrofitSingleton;
import is.hi.HBV601G.mundusandroid.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CreateRewardActivity extends AppCompatActivity {


    //View
    private EditText mRewardName;
    private EditText mRewardDescription;
    private EditText mPrice;
    private EditText mLevelRequired;

    private Button mCreateReward;


    //Http
    private Retrofit retrofit;
    private MundusAPI mundusAPI;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_reward);

        //Find
        mRewardName = findViewById(R.id.rewardName_editText);
        mRewardDescription = findViewById(R.id.rewardDescription_editText);
        mPrice = findViewById(R.id.price_editText);
        mLevelRequired = findViewById(R.id.levelRequired_editText);
        mCreateReward = findViewById(R.id.createReward_button);



        //Network
        retrofit = RetrofitSingleton.getInstance().getRetrofit();
        mundusAPI = retrofit.create(MundusAPI.class);


        mCreateReward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createReward();
            }
        });





    }



    private void createReward(){
        String rewardName = mRewardName.getText().toString();
        String rewardDescription = mRewardDescription.getText().toString();
        int price = Integer.valueOf("0" + mPrice.getText().toString());
        int LevelRequired = Integer.valueOf("0" + mLevelRequired.getText().toString());

        Reward reward = new Reward(rewardName, rewardDescription, price, LevelRequired);

        Call<ResponseBody> call = mundusAPI.createReward(reward);


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(!response.isSuccessful()){
                    //TODO Her tharf ad gera stoff
                    System.out.println("Her1");
                    return;
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //TODO Her tharf ad gera stoff
                System.out.println("Her2");
            }
        });



    }
}
