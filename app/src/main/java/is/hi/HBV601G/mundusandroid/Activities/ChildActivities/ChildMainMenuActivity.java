package is.hi.HBV601G.mundusandroid.Activities.ChildActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import is.hi.HBV601G.mundusandroid.Activities.LoginActivity;
import is.hi.HBV601G.mundusandroid.Entities.Child;
import is.hi.HBV601G.mundusandroid.InfoBar;
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



    private ImageButton mQuestLog;
    private ImageButton mMarketplace;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_main_menu);


        //Http
        retrofit = RetrofitSingleton.getInstance(getApplicationContext()).getRetrofit();
        mundusAPI = retrofit.create(MundusAPI.class);




        mQuestLog = findViewById(R.id.quest_imageButton);
        mMarketplace = findViewById(R.id.marketplace_imageButton);

        InfoBar infoBar = new InfoBar(this, "child");


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
    }



    private void moveToQuestLog(){
        Intent intent = new Intent(ChildMainMenuActivity.this, QuestLogChildActivity.class);
        startActivity(intent);
    }

    private void moveToMarketPlace(){
        Intent intent = new Intent(ChildMainMenuActivity.this, MarketplaceChildActivity.class);
        startActivity(intent);
    }




    private void moveToLogin()
    {
        Intent intent = new Intent(ChildMainMenuActivity.this, LoginActivity.class);
        startActivity(intent);
    }




}
