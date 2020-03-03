package is.hi.HBV601G.mundusandroid.Activities;

import androidx.appcompat.app.AppCompatActivity;

import is.hi.HBV601G.mundusandroid.Entities.Account;
import is.hi.HBV601G.mundusandroid.Entities.Parent;
import is.hi.HBV601G.mundusandroid.Network.MundusAPI;
import is.hi.HBV601G.mundusandroid.Network.RetrofitSingleton;
import is.hi.HBV601G.mundusandroid.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SignupActivity extends AppCompatActivity {

    private EditText mEmail;
    private EditText mName;
    private EditText mPassword;
    private EditText mPin;

    private Button mButton;

    private Retrofit retrofit;
    private MundusAPI mundusAPI;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Http
        retrofit = RetrofitSingleton.getInstance().getRetrofit();
        mundusAPI = retrofit.create(MundusAPI.class);


        //Find items
        mEmail = findViewById(R.id.email_editText);
        mName = findViewById(R.id.name_editText);
        mPassword = findViewById(R.id.password_editText);
        mPin = findViewById(R.id.parentpin_editText);
        mButton = findViewById(R.id.signup_button);


        //Events
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });



    }


    private void createAccount(){
        String email = mEmail.getText().toString();
        String name = mName.getText().toString();
        String password = mPassword.getText().toString();
        String pin = mPin.getText().toString();


        Parent parent = new Parent(name, pin, null);
        Account account = new Account(name, email, password, parent);

        Call<ResponseBody> call = mundusAPI.signup(account);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(!response.isSuccessful()){
                    return;
                }

                //Back to signin
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });




    }

}
