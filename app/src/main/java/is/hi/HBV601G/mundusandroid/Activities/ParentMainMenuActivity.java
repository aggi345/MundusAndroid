package is.hi.HBV601G.mundusandroid.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import is.hi.HBV601G.mundusandroid.Entities.Child;
import is.hi.HBV601G.mundusandroid.Entities.Parent;
import is.hi.HBV601G.mundusandroid.Network.MundusAPI;
import is.hi.HBV601G.mundusandroid.Network.RetrofitSingleton;
import is.hi.HBV601G.mundusandroid.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ParentMainMenuActivity extends AppCompatActivity {



    //View
    private ImageButton mQuestLog;
    private ImageButton mMarketplace;
    private ImageButton mStatistics;
    private ImageButton mProfile;

    private TextView mName;


    //Http
    private Retrofit retrofit;
    private MundusAPI mundusAPI;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_main_menu);

        //Http
        retrofit = RetrofitSingleton.getInstance().getRetrofit();
        mundusAPI = retrofit.create(MundusAPI.class);


        //Find
        mQuestLog = findViewById(R.id.quest_imageButton);
        mMarketplace = findViewById(R.id.marketplace_imageButton);
        mStatistics = findViewById(R.id.statistics_imageButton);
        mProfile = findViewById(R.id.profile_imageButton);

        mName = findViewById(R.id.personName_textView);


        updateParentInfo();


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

        mStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToStatistics();
            }
        });

        mProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToProfile();
            }
        });
    }


    private void updateParentInfo(){
        Call<Parent> getParentCall = mundusAPI.getSmallParent();

        getParentCall.enqueue(new Callback<Parent>() {
            @Override
            public void onResponse(Call<Parent> call, Response<Parent> response) {
                if(!response.isSuccessful()){
                    System.out.println(response.code());
                    if(response.code() == 401){
                        moveToLogin();
                    }
                    return;
                }

                Parent parent = response.body();

                mName.setText(parent.getName());
            }

            @Override
            public void onFailure(Call<Parent> call, Throwable t) {
                //TODO vantar að gera þetta.
            }
        });




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
        Intent intent = new Intent(ParentMainMenuActivity.this, StatisticsActivity.class);
        startActivity(intent);
    }

    private void moveToProfile(){
        Intent intent = new Intent(ParentMainMenuActivity.this, CreateQuestActivity.class);
        startActivity(intent);
    }

    private void moveToLogin()
    {

    }








}
