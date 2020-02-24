package is.hi.HBV601G.mundusandroid.Network;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface MundusAPI {

    @FormUrlEncoded
    @POST("login")
    Call<Integer> login(@Field("email") String email, @Field("password") String password);

    //@Headers("Cookie: JSESSIONID = E002569DE8BD39E03A8B1B522C320DDA")
    @GET("fakeTest")
    Call<String> login2();

}