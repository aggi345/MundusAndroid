package is.hi.HBV601G.mundusandroid.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import is.hi.HBV601G.mundusandroid.Entities.Child;
import is.hi.HBV601G.mundusandroid.Network.MundusAPI;
import is.hi.HBV601G.mundusandroid.Network.RetrofitSingleton;
import is.hi.HBV601G.mundusandroid.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ChildMainMenuActivity extends AppCompatActivity {



    //Http
    private Retrofit retrofit;
    private MundusAPI mundusAPI;

    //View
    private TextView mName;
    private TextView mCoins;
    private TextView mLevel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_main_menu);


        //Http
        retrofit = RetrofitSingleton.getInstance().getRetrofit();
        mundusAPI = retrofit.create(MundusAPI.class);


        //Find
        mName = findViewById(R.id.personName_textView);
        mCoins = findViewById(R.id.coins_textView);
        mLevel = findViewById(R.id.level_textView);

        updateChildInfo();




    }


    private void updateChildInfo(){

        Call<Child> getChildCall = mundusAPI.getChild();


        getChildCall.enqueue(new Callback<Child>() {
            @Override
            public void onResponse(Call<Child> call, Response<Child> response) {
                if(!response.isSuccessful()){
                    System.out.println(response.code());
                    if(response.code() == 401){
                       moveToLogin();
                    }
                    return;
                }

                Child child = response.body();

                mName.setText(child.getName());
                mCoins.setText("Coins: " + child.getTotalCoins());
                System.out.println(child.getTotalCoins());
                mLevel.setText("Level: " + child.getLevel());

            }

            @Override
            public void onFailure(Call<Child> call, Throwable t) {
                //TODO vantar að gera þetta.
            }
        });




    }


    private void moveToLogin()
    {

    }




}
