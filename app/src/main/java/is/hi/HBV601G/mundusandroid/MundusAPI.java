package is.hi.HBV601G.mundusandroid;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface MundusAPI {

    @FormUrlEncoded
    @POST("login")
    Call<Integer> login(@Field("email") String email, @Field("password") String password);

    @GET("login")
    Call<String> login2();

}
