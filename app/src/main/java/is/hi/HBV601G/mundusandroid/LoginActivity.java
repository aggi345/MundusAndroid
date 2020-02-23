package is.hi.HBV601G.mundusandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private Retrofit retrofit;

    private TextView textView;
    private TextView textView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textView = findViewById(R.id.textView2);
        textView2 = findViewById(R.id.textView3);


         retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.71:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

         MundusAPI mundusAPI = retrofit.create(MundusAPI.class);

         Call<Integer> call = mundusAPI.login("k@k.is", "k");

         call.enqueue(new Callback<Integer>() {
             @Override
             public void onResponse(Call<Integer> call, Response<Integer> response) {
                 if(!response.isSuccessful()){
                     return;
                 }

                 Integer r = response.body();
                 textView.setText(r.toString());

             }

             @Override
             public void onFailure(Call<Integer> call, Throwable t) {
                 textView.setText(t.getMessage());
             }
         });


        Call<String> loginCall = mundusAPI.login2();

        loginCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(!response.isSuccessful()){
                    return;
                }

                String r = response.body();
                textView2.setText(r.toString());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                textView2.setText(t.getMessage());
            }
        });





    }






}
