package is.hi.HBV601G.mundusandroid.Activities.ParentActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import is.hi.HBV601G.mundusandroid.Activities.LoginActivity;
import is.hi.HBV601G.mundusandroid.Activities.PersonLoginActivity;
import is.hi.HBV601G.mundusandroid.InfoBar;
import is.hi.HBV601G.mundusandroid.Network.MundusAPI;
import is.hi.HBV601G.mundusandroid.Network.RetrofitSingleton;
import is.hi.HBV601G.mundusandroid.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ParentMainMenuActivity extends AppCompatActivity {



    //View
    private ImageButton mQuestLog;
    private ImageButton mMarketplace;
    private ImageButton mProfile;

    private Button mChangePerson;
    private Button mLogOut;

    private TextView mName;


    //Http
    private Retrofit retrofit;
    private MundusAPI mundusAPI;

    InfoBar infoBar;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_main_menu);

        //Http
        retrofit = RetrofitSingleton.getInstance(getApplicationContext()).getRetrofit();
        mundusAPI = retrofit.create(MundusAPI.class);


        //Find
        mQuestLog = findViewById(R.id.quest_imageButton);
        mMarketplace = findViewById(R.id.marketplace_imageButton);
        mProfile = findViewById(R.id.profile_imageButton);

        mChangePerson = findViewById(R.id.changePerson_button);
        mLogOut = findViewById(R.id.logOut_button);


        //Infobar
        infoBar = new InfoBar(this, "parent");



        //Handler
        mQuestLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToQuestLog();;
            }
        });

        mMarketplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToMarketPlace();
            }
        });

        mProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToProfile();
            }
        });

        mChangePerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeUser();
            }
        });

        mLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        infoBar.updateInfoBarView();
    }

    private void moveToQuestLog(){
        Intent intent = new Intent(ParentMainMenuActivity.this, QuestLogParentActivity.class);
        startActivity(intent);
    }

    private void moveToMarketPlace(){
        Intent intent = new Intent(ParentMainMenuActivity.this, MarketplaceParentActivity.class);
        startActivity(intent);
    }


    private void moveToStatistics(){
        Intent intent = new Intent(ParentMainMenuActivity.this, CreateRewardActivity.class);
        startActivity(intent);
    }

    private void moveToProfile(){
        Intent intent = new Intent(ParentMainMenuActivity.this, ProfileManagementActivity.class);
        startActivity(intent);
    }

    private void changeUser(){
        Call<ResponseBody> call = mundusAPI.logoutPerson();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(!response.isSuccessful()){
                    //TODO Her tharf ad gera stoff
                    return;
                }
                Intent intent = new Intent(ParentMainMenuActivity.this, PersonLoginActivity.class);
                startActivity(intent);
                finishAffinity();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //TODO Her tharf ad gera stoff
            }
        });
    }

    private void logOut(){
        Call<ResponseBody> call = mundusAPI.logoutAccount();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(!response.isSuccessful()){
                    //TODO Her tharf ad gera stoff
                    return;
                }
                Intent intent = new Intent(ParentMainMenuActivity.this, LoginActivity.class);
                startActivity(intent);
                finishAffinity();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //TODO Her tharf ad gera stoff
            }
        });

    }









}
