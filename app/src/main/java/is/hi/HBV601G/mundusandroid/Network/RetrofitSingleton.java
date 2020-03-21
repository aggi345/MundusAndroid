package is.hi.HBV601G.mundusandroid.Network;

import java.net.CookieHandler;
import java.net.CookieManager;

import okhttp3.CookieJar;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitSingleton {

    private static final RetrofitSingleton INSTANCE = new RetrofitSingleton();

    private Retrofit retrofit;
    //private String BASE_URL = "http://192.168.1.71:8080/";
    private String BASE_URL = "https://mundus-android.herokuapp.com/";

    private RetrofitSingleton(){

       // SimpleCookieJar simpleCookieJar = new SimpleCookieJar();
        CookieHandler cookieHandler = new CookieManager();
        CookieJar cookieJar = new JavaNetCookieJar(cookieHandler);



        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(new OkHttpClient().newBuilder().cookieJar(cookieJar).build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

    public static RetrofitSingleton getInstance(){
        return INSTANCE;
    }


    public Retrofit getRetrofit() {
        return retrofit;
    }

}
