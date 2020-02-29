package is.hi.HBV601G.mundusandroid.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import is.hi.HBV601G.mundusandroid.Network.MundusAPI;
import is.hi.HBV601G.mundusandroid.Network.RetrofitSingleton;
import is.hi.HBV601G.mundusandroid.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PersonLoginActivity extends AppCompatActivity {

    //Http
    private Retrofit retrofit;
    private MundusAPI mundusAPI;


    //View
    private TextView mPersonName;
    private EditText mPin;
    private Button mLogin;
    private TextView mErrorMessage;

    private String personName;
    private long personId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_login);


        //Http
        retrofit = RetrofitSingleton.getInstance().getRetrofit();
        mundusAPI = retrofit.create(MundusAPI.class);

        //Find
        mPersonName = findViewById(R.id.personName_textView);
        mPin = findViewById(R.id.pin_editText);
        mLogin = findViewById(R.id.login_button);
        mErrorMessage = findViewById(R.id.message_textView);


        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticatePin();
            }
        });



        loadExtras();



    }


    private void authenticatePin(){
        Call<ResponseBody> authCall = mundusAPI.PinAuth(personId, mPin.getText().toString());

        authCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(!response.isSuccessful()){
                    System.out.println(response.code());
                    if(response.code() == 401){
                        mErrorMessage.setText("Wrong pin");
                    }
                    return;
                }

                mErrorMessage.setText("Worked");





            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //TODO Vantar að meðhöndla vandamál, verðum að gera eh hér.
            }
        });



    }

    private void loadExtras(){
        personId = getIntent().getLongExtra(PersonSelectActivity.getPERSONID(), -1);
        if(personId == -1){
            moveToSelectPerson();
            return;
        }

        personName = getIntent().getStringExtra(PersonSelectActivity.getPERSONNAME());

        mPersonName.setText(personName);
    }


    private void moveToSelectPerson(){
        Intent intent = new Intent(PersonLoginActivity.this, PersonSelectActivity.class);
        startActivity(intent);
    }


}
