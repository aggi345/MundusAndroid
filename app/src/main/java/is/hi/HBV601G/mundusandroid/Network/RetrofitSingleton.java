package is.hi.HBV601G.mundusandroid.Network;

import android.content.Context;
import android.content.SharedPreferences;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.net.CookieHandler;
import java.net.CookieManager;

import okhttp3.CookieJar;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitSingleton {

    private static RetrofitSingleton INSTANCE;

    private Retrofit retrofit;
    //private String BASE_URL = "http://192.168.1.71:8080/";
    private String BASE_URL = "https://mundus-android.herokuapp.com/";

    private RetrofitSingleton(Context context){

       // SimpleCookieJar simpleCookieJar = new SimpleCookieJar();
      //  CookieHandler cookieHandler = new CookieManager();
       // cookieJar = new JavaNetCookieJar(cookieHandler);
        ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context));

        SharedPrefsCookiePersistor sp = new SharedPrefsCookiePersistor(context);
        SetCookieCache sc = new SetCookieCache();





        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(new OkHttpClient().newBuilder().cookieJar(cookieJar).build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }


    public static RetrofitSingleton getInstance(Context context){
        if(INSTANCE == null){
            INSTANCE = new RetrofitSingleton(context);
        }

        return INSTANCE;
    }


    public Retrofit getRetrofit() {
        return retrofit;
    }

}
