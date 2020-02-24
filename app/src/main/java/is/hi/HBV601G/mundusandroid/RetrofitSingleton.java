package is.hi.HBV601G.mundusandroid;

import android.app.Activity;
import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitSingleton {

    private static final RetrofitSingleton INSTANCE = new RetrofitSingleton();

    private Retrofit retrofit;
    private String BASE_URL = "http://192.168.1.55:8080/";

    private RetrofitSingleton(){

        SimpleCookieJar simpleCookieJar = new SimpleCookieJar();



        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(new OkHttpClient().newBuilder().cookieJar(simpleCookieJar).build())
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
